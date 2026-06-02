package jababarium.expand.block.special;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import jababarium.content.JBSounds;
import mindustry.content.Fx;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.graphics.Trail;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

import static mindustry.Vars.*;

public class UnitPrinter extends Block {

    public ObjectMap<UnitType, UnitType> upgrades = new ObjectMap<>();
    public ItemStack[] recipe = {};
    public float buildTime = 500 * 60f;
    public float fadeTime = 500f;
    public float spawnDuration = 500f;
    public Color hologramColor = Color.valueOf("4488ff");
    public DrawBlock drawer = new DrawDefault();

    public UnitPrinter(String name) {
        super(name);

        update = true;
        solid = true;
        configurable = true;
        sync = true;
        hasItems = true;
        itemCapacity = 200;

        config(Integer.class, (UnitPrinterBuild b, Integer unitId) -> {
            if (unitId == null) return;
            Unit u = Groups.unit.getByID(unitId);
            if (u != null && b.canAccept(u)) {
                b.beginUpgrade(u);
            }
        });
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("progress", (UnitPrinterBuild e) -> new Bar(
                () -> Core.bundle.get("bar.progress"),
                () -> Pal.accent,
                () -> e.buildProgress
        ));
    }

    public boolean acceptsUnitType(UnitType type) {
        return type != null && upgrades.containsKey(type);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    public class UnitPrinterBuild extends Building {

        public UnitType inputUnitType = null;
        public float buildProgress = 0f;
        public boolean building = false;

        public boolean fadingOut = false;
        public float fadeProgress = 0f;
        public Unit fadingUnit = null;

        public boolean spawning = false;
        public float spawnProgress = 0f;
        public Unit spawnedUnit = null;
        public Trail spawnTrail = new Trail(60);

        private Trail fadeTrail = new Trail(60);
        private float fadeX, fadeY;
        private float fadeRotation;
        private UnitType fadeType;

        private final Vec2 spawnPos = new Vec2();

        public boolean canAccept(Unit unit) {
            if (unit == null || !unit.isValid()) return false;
            if (unit.team != team) return false;
            if (building || fadingOut || spawning || inputUnitType != null) return false;
            if (!acceptsUnitType(unit.type)) return false;
            return hasResources();
        }

        public boolean hasResources() {
            if (items == null) return false;
            for (ItemStack stack : recipe) {
                if (items.get(stack.item) < stack.amount) return false;
            }
            return true;
        }

        public void beginUpgrade(Unit unit) {
            for (ItemStack stack : recipe) {
                items.remove(stack.item, stack.amount);
            }

            fadeRotation = unit.rotation;
            fadeType = unit.type;

            inputUnitType = unit.type;
            fadingUnit = unit;
            fadingOut = true;
            fadeProgress = 0f;

            fadeX = unit.x;
            fadeY = unit.y;
            fadeTrail = new Trail(60);

            spawnPos.set(x, y);
            JBSounds.antimatter.at(this);
        }

        private void finishBuild() {
            if (inputUnitType == null) { reset(); return; }

            UnitType resultType = upgrades.get(inputUnitType);
            if (resultType != null) {

                
                if (resultType.naval) {
                    Vec2 safePos = findNavalSpawn();
                    spawnPos.set(safePos != null ? safePos.x : x, safePos != null ? safePos.y : y);
                } else {
                    spawnPos.set(x, y);
                }

                spawnedUnit = resultType.create(team);

                
                if (spawnedUnit.isAdded()) {
                    spawnedUnit.remove();
                }

                spawnedUnit.set(spawnPos.x, spawnPos.y);
                spawnedUnit.rotation(90f);
                spawnedUnit.vel.setZero();

                building = false;
                buildProgress = 0f;
                spawning = true;
                spawnProgress = 0f;
                spawnTrail = new Trail(60);

                JBSounds.antimatter.at(this);
            } else {
                reset();
            }
        }

        private Vec2 findNavalSpawn() {
            int searchRadius = 15;
            int tx = tileX(), ty = tileY();

            for (int r = 1; r <= searchRadius; r++) {
                for (int dx = -r; dx <= r; dx++) {
                    for (int dy = -r; dy <= r; dy++) {

                        int cx = tx + dx, cy = ty + dy;
                        var tile = world.tile(cx, cy);
                        if (tile == null) continue;

                        if ((tile.floor().isDeep() || tile.floor().isLiquid)
                                && (tile.build == null || !tile.solid())) {
                            return new Vec2(tile.worldx(), tile.worldy());
                        }
                    }
                }
            }
            return null;
        }

        private void finishSpawn() {
            if (spawnedUnit != null) {
                spawnedUnit.set(spawnPos.x, spawnPos.y);
                spawnedUnit.vel.setZero();
                spawnedUnit.rotation(90f);

                if (!spawnedUnit.isAdded()) {
                    spawnedUnit.add();
                }

                Fx.spawn.at(spawnPos.x, spawnPos.y);
            }
            reset();
        }

        private void reset() {
            inputUnitType = null;
            buildProgress = 0f;
            building = false;
            fadingOut = false;
            fadeProgress = 0f;
            fadingUnit = null;
            spawning = false;
            spawnProgress = 0f;
            spawnedUnit = null;
        }

        @Override
        public void updateTile() {

            if (fadingOut) {
                fadeProgress += Time.delta / fadeTime;

                if (fadeProgress >= 0.50f && fadingUnit != null) {
                    Fx.unitDespawn.at(fadeX, fadeY, 0f, fadingUnit.type.outlineColor);
                    fadingUnit.remove();
                    fadingUnit = null;
                }

                if (fadeProgress >= 1f) {
                    fadingOut = false;
                    building = true;
                    buildProgress = 0f;
                    fadeProgress = 0f;
                }
                return;
            }

            if (building && efficiency > 0) {
                buildProgress += (1f / buildTime) * edelta();
                if (buildProgress >= 1f) {
                    finishBuild();
                }
                return;
            }

            if (spawning) {
                spawnProgress += Time.delta / spawnDuration;

                
                if (spawnedUnit != null && !spawnedUnit.isAdded()) {
                    spawnedUnit.set(spawnPos.x, spawnPos.y);
                    spawnedUnit.vel.setZero();
                }

                if (spawnProgress >= 1f) {
                    finishSpawn();
                }
                return;
            }
        }

        @Override
        public void draw() {
            drawer.draw(this);

            if (fadingOut) {
                drawFadingUnit();
            }

            if (building && inputUnitType != null) {
                UnitType result = upgrades.get(inputUnitType);
                if (result != null) drawHologram(result);
            }

            if (spawning && spawnedUnit != null) {
                drawSpawningUnit();
            }

            if (building || fadingOut || spawning) {
                drawProgressArc();
            }
        }

        private void drawFadingUnit() {
            float fin = fadeProgress;
            float fslope = Mathf.slope(fin);
            float alpha = 1f - fin;

            Draw.z(Layer.effect);

            fadeTrail.draw(team.color, 4f * fslope);

            Drawf.light(
                    fadeX,
                    fadeY,
                    fadeType.hitSize * 3f * fslope,
                    team.color,
                    0.8f * fslope
            );

            Draw.z(Layer.flyingUnit);

            float height = Mathf.curve(fslope * fslope, 0f, 0.3f) * 1.1f;
            float width  = Mathf.curve(fslope * fslope, 0.35f, 0.75f) * 1.1f;

            Draw.z(Layer.effect);
            Draw.color(team.color.cpy().mul(1.15f), Pal.gray,
                    new Rand(id).random(-0.25f, 0.25f) / 4f);

            Fill.rect(
                    fadeX,
                    fadeY,
                    Draw.scl * fadeType.shadowRegion.height * width + 1f,
                    Draw.scl * fadeType.shadowRegion.width  * height,
                    fadeRotation
            );

            Draw.reset();

            Draw.mixcol(team.color, fslope * 0.35f);
            Draw.alpha(alpha);

            Draw.mixcol();
            Draw.alpha(1f);
            Draw.reset();
        }

        private void drawSpawningUnit() {
            float fin = spawnProgress;
            float fslope = Mathf.slope(fin);

            float sx = spawnPos.x, sy = spawnPos.y;  

            Draw.z(Layer.effect);
            if (!headless) spawnTrail.update(sx, sy);
            spawnTrail.draw(team.color, 4f * fslope);

            Drawf.light(sx, sy,
                    spawnedUnit.hitSize * 3f * fslope,
                    team.color,
                    0.8f * fslope
            );

            Draw.z(Layer.effect);

            float height = Mathf.curve(fslope * fslope, 0f, 0.3f) * 1.1f;
            float width  = Mathf.curve(fslope * fslope, 0.35f, 0.75f) * 1.1f;

            Draw.color(team.color.cpy().mul(1.15f), Pal.gray,
                    new Rand(spawnedUnit.id).random(-0.25f, 0.25f) / 4f);

            Fill.rect(
                    sx, sy,
                    Draw.scl * spawnedUnit.type.shadowRegion.height * width + 1f,
                    Draw.scl * spawnedUnit.type.shadowRegion.width  * height,
                    90f
            );

            Draw.reset();

            if (spawnProgress >= 0.5f) {
                float unitAlpha = (spawnProgress - 0.5f) / 0.5f;

                Draw.z(Layer.flyingUnit);
                Draw.mixcol(team.color, fslope * 0.35f);
                Draw.alpha(unitAlpha);

                
                spawnedUnit.type.draw(spawnedUnit);

                Draw.mixcol();
                Draw.alpha(1f);
                Draw.reset();
            }
        }

        private void drawHologram(UnitType type) {
            Draw.z(Layer.effect + 0.05f);

            clipSize = 1012f;

            float pulse  = Mathf.absin(Time.time, 5f, 0.08f);
            float alpha  = 0.55f + pulse;
            Color hColor = hologramColor.cpy().lerp(team.color, 0.2f);
            float scanRange = Math.max(type.hitSize, 8f) * 1.3f;
            float cx = spawnPos.x, cy = spawnPos.y;

            float fullHeight = scanRange * 2f;
            float builtHeight = fullHeight * buildProgress;
            float clipBottom  = cy - scanRange;
            float clipTop     = clipBottom + builtHeight;

            Draw.mixcol(hColor, 0.7f);
            Draw.alpha(alpha);

            if (type.outlineRegion.found()) {
                float rw = type.outlineRegion.width  * Draw.scl;
                float rh = type.outlineRegion.height * Draw.scl;

                float totalV = type.outlineRegion.v2 - type.outlineRegion.v;
                float v1     = type.outlineRegion.v2 - totalV * buildProgress;

                float drawH = rh * buildProgress;

                arc.graphics.g2d.TextureRegion clipped = new arc.graphics.g2d.TextureRegion(type.outlineRegion);
                clipped.v  = v1;
                clipped.v2 = type.outlineRegion.v2;

                Draw.rect(clipped, cx, cy - rh/2f + drawH/2f, rw, drawH, 0f);
            }

            if (type.region.found()) {
                float rw = type.region.width  * Draw.scl;
                float rh = type.region.height * Draw.scl;

                float totalV = type.region.v2 - type.region.v;
                float v1     = type.region.v2 - totalV * buildProgress;

                float drawH = rh * buildProgress;

                arc.graphics.g2d.TextureRegion clipped = new arc.graphics.g2d.TextureRegion(type.region);
                clipped.v  = v1;
                clipped.v2 = type.region.v2;

                Draw.rect(clipped, cx, cy - rh/2f + drawH/2f, rw, drawH, 0f);
            }

            Draw.mixcol();
            Draw.alpha(1f);

            Draw.color(Color.white);
            Draw.alpha(0.8f * Mathf.absin(Time.time, 3f, 1f) + 0.2f);
            Lines.stroke(1.8f);
            Lines.line(cx - scanRange, clipTop, cx + scanRange, clipTop);

            Draw.color(hColor);
            Draw.alpha(0.85f);
            Lines.stroke(1.5f);
            float c = scanRange * 0.4f;
            Lines.line(cx - scanRange, cy + scanRange, cx - scanRange + c, cy + scanRange);
            Lines.line(cx - scanRange, cy + scanRange, cx - scanRange,     cy + scanRange - c);
            Lines.line(cx + scanRange, cy + scanRange, cx + scanRange - c, cy + scanRange);
            Lines.line(cx + scanRange, cy + scanRange, cx + scanRange,     cy + scanRange - c);
            Lines.line(cx - scanRange, cy - scanRange, cx - scanRange + c, cy - scanRange);
            Lines.line(cx - scanRange, cy - scanRange, cx - scanRange,     cy - scanRange + c);
            Lines.line(cx + scanRange, cy - scanRange, cx + scanRange - c, cy - scanRange);
            Lines.line(cx + scanRange, cy - scanRange, cx + scanRange,     cy - scanRange + c);

            Drawf.light(cx, cy, scanRange * 4f * buildProgress, hColor, alpha * 0.4f);

            Draw.reset();
        }

        private void drawProgressArc() {
            Draw.z(Layer.effect);
            float progress = fadingOut ? fadeProgress : (spawning ? spawnProgress : buildProgress);

            Draw.color(Color.black);
            Draw.alpha(0.45f);
            Lines.stroke(3f);
            Lines.circle(x, y, size * tilesize * 0.72f);

            Draw.color(team.color);
            Draw.alpha(0.95f);
            Lines.stroke(3f);
            Lines.arc(x, y, size * tilesize * 0.72f, progress, 90f);

            float tipAngle = 90f + progress * 360f;
            Tmp.v1.trns(tipAngle, size * tilesize * 0.72f);
            Fill.circle(x + Tmp.v1.x, y + Tmp.v1.y,
                    3f + Mathf.absin(Time.time, 3f, 1.5f));

            Drawf.light(x, y,
                    size * tilesize * 2f * progress, team.color, 0.3f * progress);

            Draw.reset();
        }

        @Override
        public void drawConfigure() {
            super.drawConfigure();

            Groups.unit.each(u -> u.team == team && acceptsUnitType(u.type), u -> {
                Draw.color(canAccept(u) ? team.color : Pal.gray);
                Drawf.square(u.x, u.y, u.hitSize / 1.4f + 1f, 45f);
            });

            control.input.selectedUnits.each(u -> {
                if (acceptsUnitType(u.type)) {
                    Draw.color(Color.white);
                    Drawf.square(u.x, u.y, u.hitSize / 1.4f + 1f, 45f + Time.time);
                }
            });

            Draw.reset();
        }

        @Override
        public void buildConfiguration(Table table) {
            table.table(p -> {
                p.setBackground(Tex.pane);
                p.table(Styles.black, t -> {

                    t.label(() -> {
                        if (fadingOut)
                            return "[accent]• [white]Preparing...";
                        if (building && inputUnitType != null) {
                            UnitType result = upgrades.get(inputUnitType);
                            String to = result != null ? result.localizedName : "?";
                            return "[accent]⚙ [white]" + inputUnitType.localizedName
                                    + " -> " + to
                                    + " [lightgray](" + (int)(buildProgress * 100) + "%)";
                        }
                        if (spawning)
                            return "[accent]✓ [white]Spawning... [lightgray](" + (int)(spawnProgress * 100) + "%)";
                        if (!hasResources())
                            return "[scarlet]✖ [white]Missing resources";
                        return "[lightgray]<-> [white]Select unit to upgrade";
                    }).pad(6f).growX().left().row();

                    t.image().color(Pal.accent).height(1f).growX().pad(2f).row();

                    t.table(r -> {
                        r.add("[lightgray]<> Resources: ").padRight(4f);
                        for (ItemStack stack : recipe) {
                            final ItemStack s = stack;
                            r.image(s.item.uiIcon).size(20f).padLeft(4f);
                            r.label(() -> {
                                int has = items == null ? 0 : items.get(s.item);
                                return (has >= s.amount ? "[white]" : "[scarlet]")
                                        + has + "[lightgray]/" + s.amount;
                            }).padRight(2f);
                        }
                    }).pad(4f).growX().left().row();

                    t.image().color(Pal.accent).height(1f).growX().pad(2f).row();

                    t.table(u -> {
                        u.add("[lightgray]* Upgrades:").pad(4f).left().row();
                        upgrades.each((from, to) -> {
                            u.table(row -> {
                                row.image(from.uiIcon).size(22f).padRight(4f);
                                row.add("[accent]->").padRight(4f);
                                row.image(to.uiIcon).size(22f);
                            }).left().pad(2f).row();
                        });
                    }).pad(4f).growX().left().row();

                    t.image().color(Pal.accent).height(1f).growX().pad(2f).row();

                    t.button("|| Start Upgrade", Styles.cleart, () -> {
                        Seq<Unit> selected = control.input.selectedUnits;
                        Unit target = selected.find(u -> canAccept(u));
                        if (target != null) configure(target.id);
                    }).update(b -> {
                        boolean busy = building || fadingOut || spawning;
                        boolean anyValid = !control.input.selectedUnits.isEmpty()
                                && control.input.selectedUnits.contains(u -> canAccept(u));
                        b.setDisabled(busy || !anyValid || !hasResources());

                        if (fadingOut) b.setText("• Preparing...");
                        else if (spawning) b.setText("✓ Spawning...");
                        else if (building) b.setText("⚙ Working...");
                        else if (!hasResources()) b.setText("✖ No resources");
                        else if (!anyValid) b.setText("<-> Select valid unit");
                        else b.setText("|| Start Upgrade");
                    }).size(220f, 44f).pad(4f);

                }).minWidth(250f).pad(0f);
            }).fill();
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (item == null || items == null) return false;
            for (ItemStack stack : recipe) {
                if (stack.item == item && items.get(item) < stack.amount) return true;
            }
            return false;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.bool(building);
            write.f(buildProgress);
            write.bool(fadingOut);
            write.f(fadeProgress);
            write.bool(spawning);
            write.f(spawnProgress);
            write.str(inputUnitType != null ? inputUnitType.name : "");
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            building      = read.bool();
            buildProgress = read.f();
            fadingOut     = read.bool();
            fadeProgress  = read.f();
            spawning      = read.bool();
            spawnProgress = read.f();
            String typeName = read.str();

            if (typeName != null && !typeName.isEmpty()) {
                inputUnitType = content.unit(typeName);
                if (inputUnitType == null) { reset(); return; }
            }

            if (fadingOut || spawning) {
                reset();
            }
        }
    }
}