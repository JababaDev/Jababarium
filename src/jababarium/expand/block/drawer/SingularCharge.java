package jababarium.expand.block.drawer;

import arc.func.Floatf;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.entities.part.DrawPart;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import jababarium.util.graphic.DrawFunc;

public class SingularCharge extends DrawPart {
    protected static final Rand rand = new Rand();
    protected static final Vec2 tr = new Vec2(), tr2 = new Vec2();

    public Interp curve = Interp.pow3;
    public PartProgress progress;

    public Floatf<PartParams> chargeY = t -> -20f;
    public Floatf<PartParams> shootY  = t ->  50f;

    public Color colorCore  = Color.valueOf("ffffff");
    public Color colorMid   = Color.valueOf("d0e8ff");
    public Color colorEdge  = Color.valueOf("8ab4cc");
    public Color colorDark  = Color.valueOf("1a2a33");

    public float coreRad    = 9f;
    public float muzzleRad  = 14f;
    public float hexSize    = 28f;

    @Override
    public void draw(PartParams params) {
        float x = params.x, y = params.y, rotation = params.rotation;
        float fin = progress.getClamp(params);

        if (fin < 0.01f) return;

        Draw.z(Layer.effect - 1f);

        tr2.trns(rotation, chargeY.get(params));
        tr.trns(rotation, shootY.get(params));

        float coreFin   = Mathf.curve(fin, 0.08f, 0.9f);
        float muzzleFin = Mathf.curve(fin, 0.45f, 1f);
        float hexFin    = Mathf.curve(fin, 0.05f, 0.75f);

        if (hexFin > 0f) {
            float hexR = hexSize * (1f - hexFin * 0.35f);
            float hexRot = Time.time * 0.6f;

            Draw.color(colorEdge, colorMid, hexFin);
            Lines.stroke(2.5f * hexFin);

            for (int i = 0; i < 6; i++) {
                float a1 = hexRot + i * 60f;
                float a2 = hexRot + (i + 1) * 60f;
                Lines.line(
                        x + Angles.trnsx(a1, hexR),
                        y + Angles.trnsy(a1, hexR),
                        x + Angles.trnsx(a2, hexR),
                        y + Angles.trnsy(a2, hexR)
                );
            }

            float hexR2 = hexR * 0.6f;
            Draw.color(colorMid, colorCore, hexFin);
            Lines.stroke(1.5f * hexFin);
            for (int i = 0; i < 6; i++) {
                float a1 = -hexRot * 1.4f + i * 60f + 30f;
                float a2 = -hexRot * 1.4f + (i + 1) * 60f + 30f;
                Lines.line(
                        x + Angles.trnsx(a1, hexR2),
                        y + Angles.trnsy(a1, hexR2),
                        x + Angles.trnsx(a2, hexR2),
                        y + Angles.trnsy(a2, hexR2)
                );
            }

            float fin9 = Mathf.curve(fin, 0.88f, 1f);
            if (fin9 > 0f) {
                Draw.color(colorCore, fin9);
                for (int i = 0; i < 6; i++) {
                    float a = hexRot + i * 60f;
                    float vx = x + Angles.trnsx(a, hexR);
                    float vy = y + Angles.trnsy(a, hexR);
                    Fill.square(vx, vy, 2.8f * fin9, a + 45f);
                }

                Draw.color(colorMid, fin9 * 0.7f);
                for (int i = 0; i < 6; i++) {
                    float a = -hexRot * 1.4f + i * 60f + 30f;
                    float vx = x + Angles.trnsx(a, hexR2);
                    float vy = y + Angles.trnsy(a, hexR2);
                    Fill.square(vx, vy, 1.8f * fin9, a + 45f);
                }
            }

            float collapseAlpha = Mathf.curve(fin, 0.6f, 1f);
            if (collapseAlpha > 0f) {
                Draw.color(colorEdge, collapseAlpha * 0.5f);
                Lines.stroke(1f * collapseAlpha);
                for (int i = 0; i < 6; i++) {
                    float a = hexRot + i * 60f;
                    Lines.line(
                            x + Angles.trnsx(a, hexR),
                            y + Angles.trnsy(a, hexR),
                            x, y
                    );
                }
            }
        }

        if (coreFin > 0f) {
            float cx = x + tr2.x, cy = y + tr2.y;

            Drawf.light(cx, cy, coreRad * 6f * coreFin, colorMid, 0.5f * coreFin);

            Draw.color(colorEdge, colorMid, coreFin);
            for (int i = 0; i < 4; i++) {
                float spikeAngle = rotation + i * 90f + Time.time * 1.2f;
                float spikeLen   = coreRad * 2.2f * coreFin;
                Drawf.tri(cx, cy, coreRad * 0.25f * coreFin, spikeLen, spikeAngle);
            }

            Draw.color(colorMid, coreFin * 0.6f);
            for (int i = 0; i < 4; i++) {
                float spikeAngle = rotation + 45f + i * 90f - Time.time * 0.9f;
                Drawf.tri(cx, cy, coreRad * 0.12f * coreFin, coreRad * 1.4f * coreFin, spikeAngle);
            }

            Draw.color(colorMid, colorCore, coreFin);
            Lines.stroke(coreFin * 1.8f);
            DrawFunc.circlePercentFlip(cx, cy, coreFin * (coreRad + 3f), Time.time * 1.3f, 20f);

            Draw.color(colorEdge, colorMid, coreFin);
            Fill.circle(cx, cy, coreFin * coreRad * 0.85f);

            Draw.color(colorCore, coreFin);
            Fill.circle(cx, cy, coreFin * coreRad * 0.45f);
        }

        if (muzzleFin > 0f) {
            float mx = x + tr.x, my = y + tr.y;

            Draw.color(colorMid, colorCore, muzzleFin);
            Lines.stroke(muzzleFin * 3f);
            DrawFunc.circlePercent(mx, my,
                    muzzleRad * (1f + muzzleFin * 0.4f),
                    muzzleFin, rotation - muzzleFin * 360f);

            for (int i = 0; i < 4; i++) {
                float lineAngle = rotation + i * 45f + Time.time * 0.8f;
                float lineLen   = muzzleRad * (1.8f + Mathf.absin(Time.time + i * 20f, 8f, 0.6f)) * muzzleFin;
                Draw.color(colorEdge, colorMid, muzzleFin);
                Lines.stroke(muzzleFin * 1.5f);
                Lines.line(
                        mx + Angles.trnsx(lineAngle, 2f),
                        my + Angles.trnsy(lineAngle, 2f),
                        mx + Angles.trnsx(lineAngle, lineLen),
                        my + Angles.trnsy(lineAngle, lineLen)
                );
            }

            float fin9 = Mathf.curve(fin, 0.9f, 1f);
            Draw.color(colorCore, fin9);
            for (int i = 0; i < 4; i++) {
                float sqAngle = Time.time * 2.2f + i * 90f;
                Tmp.v1.trns(sqAngle, muzzleRad * (1f + muzzleFin * 0.4f) + Lines.getStroke() * 2f)
                        .add(mx, my);
                Fill.square(Tmp.v1.x, Tmp.v1.y, 2.5f * fin9, sqAngle + 45f);
            }

            Draw.color(colorMid, fin9 * 0.7f);
            for (int i = 0; i < 4; i++) {
                float sqAngle = -Time.time * 1.5f + i * 90f + 45f;
                Tmp.v1.trns(sqAngle, muzzleRad * (1f + muzzleFin * 0.4f) + Lines.getStroke() * 3.5f)
                        .add(mx, my);
                Fill.square(Tmp.v1.x, Tmp.v1.y, 1.8f * fin9, sqAngle + 45f);
            }

            Draw.color(colorEdge, colorMid, muzzleFin);
            Fill.circle(mx, my, muzzleFin * muzzleRad * 0.9f);
            Draw.color(colorDark, muzzleFin);
            Fill.circle(mx, my, muzzleFin * muzzleRad * 0.55f);
            Draw.color(colorMid, colorCore, muzzleFin);
            Fill.circle(mx, my, muzzleFin * muzzleRad * 0.28f);

            Drawf.light(mx, my, muzzleRad * 7f * muzzleFin, colorMid, 0.65f * muzzleFin);
        }
    }
}