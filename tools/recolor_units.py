"""Recolor unit sprites of each upgrade branch into the palette of the branch's top unit.

Every sprite is pixel art built from a small set of colors: a grey "metal" ramp plus one or
two saturated accent ramps. For each unit, the union of colors across all of its sprite files is
split into roles (metal / primary accent / secondary accent), each role is sorted by luminance,
and every color is replaced by the target ramp sampled at the same relative rank.

Usage:
    python tools/recolor_units.py            # write recolored sprites in place
    python tools/recolor_units.py --preview out.png   # only render a before/after sheet
"""
import colorsys
import glob
import os
import sys
from collections import Counter

from PIL import Image

ROOT = os.path.join(os.path.dirname(__file__), "..", "assets", "sprites", "units")

BRANCHES = {
    "nemesis": ["scout", "fray", "omniq", "vortex", "destroyer", "decimator", "revenant"],
    "oblivion": ["zanuka", "blip", "geran", "spectre", "inferno", "vindicator"],
    "tidebreaker": ["undertow", "ripjaw", "brinneclaw", "maelstromis", "quantar", "leviathan"],
    "ocelexis": ["pelagis", "aquarail", "vector", "glacial", "rift", "phantom"],
    "broodmother": ["tiny", "ariel", "widow", "empress", "theridion", "octoclasm", "oraxia"],
}

METAL_CHROMA = 32  # below this chroma (max - min channel) a color counts as metal
HUE_TOL = 0.04    # hue distance (0..1) for grouping accent colors into one ramp


def lum(c):
    r, g, b = c
    return 0.299 * r + 0.587 * g + 0.114 * b


def hsv(c):
    return colorsys.rgb_to_hsv(*(v / 255 for v in c))


def hue_dist(a, b):
    d = abs(a - b)
    return min(d, 1 - d)


def files_of(unit):
    return sorted(glob.glob(os.path.join(ROOT, unit, "*.png")))


def unit_colors(unit, body_only=False):
    count = Counter()
    for f in [os.path.join(ROOT, unit, unit + ".png")] if body_only else files_of(unit):
        im = Image.open(f).convert("RGBA")
        for (r, g, b, a), n in Counter(im.get_flattened_data()).items():
            if a > 0:
                count[(r, g, b)] += n
    return count


def split_roles(count):
    """-> (metal, [accent groups sorted by coverage desc]); each list holds colors."""
    metal, accents = [], []
    for c in count:
        if max(c) - min(c) < METAL_CHROMA:
            metal.append(c)
        else:
            accents.append(c)
    groups = []  # [hue, colors]
    for c in sorted(accents, key=lambda c: -count[c]):
        h = hsv(c)[0]
        for g in groups:
            if hue_dist(g[0], h) < HUE_TOL:
                g[1].append(c)
                break
        else:
            groups.append([h, [c]])
    groups = [g[1] for g in groups]
    groups.sort(key=lambda g: -sum(count[c] for c in g))
    # a saturated ramp that dominates the sprite is body paint: its large areas become metal,
    # its small highlights stay an accent
    total = sum(count.values())
    for g in [g for g in groups if sum(count[c] for c in g) > 0.25 * total]:
        body = [c for c in g if count[c] > 0.08 * total]
        metal.extend(body)
        g[:] = [c for c in g if c not in body]
    groups = [g for g in groups if g]
    return metal, groups


def mean_hue(group):
    return sum(hsv(c)[0] for c in group) / len(group)


def sample(ramp, t):
    """Piecewise-linear sample of a luminance-sorted ramp at t in [0, 1]."""
    if len(ramp) == 1:
        return ramp[0]
    x = t * (len(ramp) - 1)
    i = min(int(x), len(ramp) - 2)
    f = x - i
    a, b = ramp[i], ramp[i + 1]
    return tuple(round(a[k] + (b[k] - a[k]) * f) for k in range(3))


def map_ramp(src, dst, mapping):
    src = sorted(src, key=lum)
    dst = sorted(dst, key=lum)
    for i, c in enumerate(src):
        t = i / (len(src) - 1) if len(src) > 1 else 1.0
        mapping[c] = sample(dst, t)


def build_mapping(unit, target):
    s_metal, s_acc = split_roles(unit_colors(unit))
    t_metal, t_acc = split_roles(unit_colors(target, body_only=True))
    mapping = {}
    if s_metal:
        map_ramp(s_metal, t_metal, mapping)
    for i, g in enumerate(s_acc):
        # the main accent always takes the target's main accent, the rest pick the closest hue
        dst = t_acc[0] if i == 0 else min(t_acc, key=lambda t: hue_dist(mean_hue(t), mean_hue(g)))
        map_ramp(g, dst, mapping)
    for c in list(mapping):
        if c in ((255, 255, 255), (0, 0, 0)):
            mapping[c] = c
    return mapping


def recolor(im, mapping):
    im = im.convert("RGBA")
    px = im.load()
    for y in range(im.height):
        for x in range(im.width):
            r, g, b, a = px[x, y]
            if a and (r, g, b) in mapping:
                px[x, y] = (*mapping[(r, g, b)], a)
    return im


def preview(out):
    rows = []
    for target, units in BRANCHES.items():
        for u in units:
            body = Image.open(os.path.join(ROOT, u, u + ".png")).convert("RGBA")
            rows.append((u, body, recolor(body.copy(), build_mapping(u, target)),
                         Image.open(os.path.join(ROOT, target, target + ".png")).convert("RGBA")))
    cell = 128
    sheet = Image.new("RGBA", (cell * 3, cell * len(rows)), (40, 40, 48, 255))
    for i, row in enumerate(rows):
        for j, im in enumerate(row[1:]):
            im = im.copy()
            im.thumbnail((cell - 4, cell - 4), Image.NEAREST)
            sheet.alpha_composite(im, (j * cell + (cell - im.width) // 2, i * cell + (cell - im.height) // 2))
    sheet.save(out)


def main():
    if len(sys.argv) > 2 and sys.argv[1] == "--preview":
        preview(sys.argv[2])
        return
    for target, units in BRANCHES.items():
        for u in units:
            mapping = build_mapping(u, target)
            for f in files_of(u):
                recolor(Image.open(f), mapping).save(f)
            print(u, "->", target, len(mapping), "colors")


if __name__ == "__main__":
    main()
