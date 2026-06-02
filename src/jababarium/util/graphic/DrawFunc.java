package jababarium.util.graphic;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.GlyphLayout;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;
import arc.util.Tmp;
import arc.util.pooling.Pools;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;

import arc.math.geom.Position;
import arc.math.geom.Vec2;

import arc.util.Time;

import jababarium.Jababarium;

public class DrawFunc {
    public static final Color bottomColor = Pal.gray;
    public static final int[] oneArr = { 1 };
    public static final float sinScl = 1f;
    private static final Vec2 vec1 = new Vec2(),
            vec2 = new Vec2();
    
    
    public static float NOR_DISTANCE = 600f;

    public static void posSquare(Color color, float stroke, float size, float x1, float y1, float x2, float y2) {

    }

    public static void posSquareLinkArr(Color color, float stroke, float size, boolean drawBottom, boolean linkLine,
            Position... pos) {
        if (pos.length < 2 || (!linkLine && pos[0] == null))
            return;

        for (int c : drawBottom ? Mathf.signs : oneArr) {
            for (int i = 1; i < pos.length; i++) {
                if (pos[i] == null)
                    continue;
                Position p1 = pos[i - 1], p2 = pos[i];
                Lines.stroke(stroke + 1 - c, c == 1 ? color : bottomColor);
                if (linkLine) {
                    if (p1 == null)
                        continue;
                    Lines.line(p2.getX(), p2.getY(), p1.getX(), p1.getY());
                } else {
                    Lines.line(p2.getX(), p2.getY(), pos[0].getX(), pos[0].getY());
                }
                Draw.reset();
            }

            for (Position p : pos) {
                if (p == null)
                    continue;
                Draw.color(c == 1 ? color : bottomColor);
                Fill.square(p.getX(), p.getY(), size + 1 - c / 1.5f, 45);
                Draw.reset();
            }
        }
    }

    public static void posSquareLink(Color color, float stroke, float size, boolean drawBottom, float x, float y,
            float x2, float y2) {
        posSquareLink(color, stroke, size, drawBottom, vec1.set(x, y), vec2.set(x2, y2));
    }

    public static void posSquareLink(Color color, float stroke, float size, boolean drawBottom, Position from,
            Position to) {
        posSquareLinkArr(color, stroke, size, drawBottom, false, from, to);
    }

    public static void drawConnected(float x, float y, float size, Color color) {
        Draw.reset();
        float sin = Mathf.absin(Time.time * sinScl, 8f, 1.25f);

        for (int i = 0; i < 4; i++) {
            float length = size / 2f + 3 + sin;
            Tmp.v1.trns(i * 90, -length);
            Draw.color(Pal.gray);
            Draw.rect(Jababarium.name("linked-arrow-back"), x + Tmp.v1.x, y + Tmp.v1.y, i * 90);
            Draw.color(color);
            Draw.rect(Jababarium.name("linked-arrow"), x + Tmp.v1.x, y + Tmp.v1.y, i * 90);
        }
        Draw.reset();
    }

    public static void overlayText(Font font, String text, float x, float y, float offset, float offsetScl, float size,
            Color color, boolean underline, boolean align) {
        GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);
        font.getData().setScale(size / Scl.scl(1.0f));
        layout.setText(font, text);
        font.setColor(color);

        float dy = offset + 3.0F;
        font.draw(text, x, y + layout.height / (align ? 2 : 1) + (dy + 1.0F) * offsetScl, 1);
        --dy;

        if (underline) {
            Lines.stroke(2.0F, Color.darkGray);
            Lines.line(x - layout.width / 2.0F - 2.0F, dy + y, x + layout.width / 2.0F + 1.5F, dy + y);
            Lines.stroke(1.0F, color);
            Lines.line(x - layout.width / 2.0F - 2.0F, dy + y, x + layout.width / 2.0F + 1.5F, dy + y);
            Draw.color();
        }

        font.setUseIntegerPositions(ints);
        font.setColor(Color.white);
        font.getData().setScale(1.0F);
        Draw.reset();
        Pools.free(layout);
    }

    public static void overlayText(String text, float x, float y, float offset, Color color, boolean underline) {
        overlayText(Fonts.outline, text, x, y, offset, 1, 0.25f, color, underline, false);
    }

    public static void tri(float x, float y, float width, float length, float angle) {
        float wx = Angles.trnsx(angle + 90, width), wy = Angles.trnsy(angle + 90, width);
        Fill.tri(x + wx, y + wy, x - wx, y - wy, Angles.trnsx(angle, length) + x, Angles.trnsy(angle, length) + y);
    }

    public static void circlePercent(float x, float y, float rad, float percent, float angle) {
        float p = Mathf.clamp(percent);

        int sides = Lines.circleVertices(rad);

        float space = 360.0F / (float) sides;
        float len = 2 * rad * Mathf.sinDeg(space / 2);
        float hstep = Lines.getStroke() / 2.0F / Mathf.cosDeg(space / 2.0F);
        float r1 = rad - hstep;
        float r2 = rad + hstep;

        int i;

        for (i = 0; i < sides * p - 1; ++i) {
            float a = space * (float) i + angle;
            float cos = Mathf.cosDeg(a);
            float sin = Mathf.sinDeg(a);
            float cos2 = Mathf.cosDeg(a + space);
            float sin2 = Mathf.sinDeg(a + space);
            Fill.quad(x + r1 * cos, y + r1 * sin, x + r1 * cos2, y + r1 * sin2, x + r2 * cos2, y + r2 * sin2, x + r2 * cos, y + r2 * sin);
        }

        float a = space * i + angle;
        float cos = Mathf.cosDeg(a);
        float sin = Mathf.sinDeg(a);
        float cos2 = Mathf.cosDeg(a + space);
        float sin2 = Mathf.sinDeg(a + space);
        float f = sides * p - i;
        vec1.trns(a, 0, len * (f - 1));
        Fill.quad(x + r1 * cos, y + r1 * sin, x + r1 * cos2 + vec1.x, y + r1 * sin2 + vec1.y, x + r2 * cos2 + vec1.x, y + r2 * sin2 + vec1.y, x + r2 * cos, y + r2 * sin);
    }

    public static void circlePercentFlip(float x, float y, float rad, float in, float scl) {
        boolean monoIncr = in % (scl * 4) < scl * 2;
        float f = Mathf.cos(in % (scl * 3f), scl, 1.1f);
        circlePercent(x, y, rad, f > 0 ? f : -f, in + -90 * Mathf.sign(f));
    }

    public static float cameraDstScl(float x, float y, float norDst) {
        vec1.set(Core.camera.position);
        float dst = Mathf.dst(x, y, vec1.x, vec1.y);
        return 1 - Mathf.clamp(dst / norDst);
    }
    public static float rotator_90(float in , float margin) {
        return 90 * JBInterp.pow10.apply(Mathf.curve(in, margin, 1 - margin));
    }
    public static float rotator_90() {
        return 90 * Interp.pow5.apply(Mathf.curve(cycle_100(), 0.15f, 0.85f));
    }
    public static float cycle_100() {
        return Time.time % 100 / 100;
    }

    public static float cycle(float phaseOffset, float T) {
        return (Time.time + phaseOffset) % T / T;
    }

    public static float cycle(float in, float phaseOffset, float T) {
        return (in + phaseOffset) % T / T;
    }

    public static void arrow(float x, float y, float width, float length, float backLength, float angle) {
        float wx = Angles.trnsx(angle + 90, width), wy = Angles.trnsy(angle + 90, width);
        float ox = Angles.trnsx(angle, backLength), oy = Angles.trnsy(angle, backLength);
        float cx = Angles.trnsx(angle, length) + x, cy = Angles.trnsy(angle, length) + y;
        Fill.tri(x + ox, y + oy, x - wx, y - wy, cx, cy);
        Fill.tri(x + wx, y + wy, x + ox, y + oy, cx, cy);
    }
}