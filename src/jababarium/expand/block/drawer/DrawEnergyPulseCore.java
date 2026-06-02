package jababarium.expand.block.drawer;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.draw.DrawBlock;

public class DrawEnergyPulseCore extends DrawBlock {

    public Color baseColor = Color.valueOf("4CFF6A");
    public float scale = 0.6f;

    @Override
    public void draw(Building build){
        if(build.warmup() <= 0f) return;

        float prevZ = Draw.z();
        Draw.z(Layer.effect + 1f);

        float x = build.x;
        float y = build.y;
        float time = Time.time;
        float warm = build.warmup();

        float cycle = 80f;
        float fin = (time % cycle) / cycle;
        float fout = 1f - fin;

        Drawf.light(
                x, y,
                fout * 140f * scale,
                baseColor,
                0.8f
        );

        Draw.color(baseColor, Color.white, fin * 0.35f);
        Fill.circle(
                x,
                y,
                (6f + Mathf.absin(time, 6f, 2.5f)) * scale * warm
        );

        Draw.color(baseColor);
        Lines.stroke(4f * fout * scale);
        Lines.circle(
                x, y,
                (18f + fin * 90f) * scale * warm
        );

        Draw.color(baseColor, Color.white, 0.2f);
        Lines.stroke(2f * fout * scale);
        Lines.circle(
                x, y,
                (10f + fin * 120f) * scale * warm
        );

        Fx.rand.setSeed(build.id + (long)(time * 3));
        Angles.randLenVectors(
                build.id,
                12,
                (25f + 80f * fin) * scale,
                (dx, dy) -> {
                    float ang = Mathf.angle(dx, dy);
                    Draw.color(baseColor, Color.white, fout);
                    Lines.stroke(1.5f * fout * scale);
                    Lines.lineAngle(
                            x + dx,
                            y + dy,
                            ang,
                            (6f + Fx.rand.random(8f)) * fout * scale
                    );
                }
        );

        Draw.color(baseColor);
        Angles.randLenVectors(
                build.id + 42,
                6,
                (8f + 50f * fin) * scale,
                (dx, dy) -> {
                    Fill.circle(
                            x + dx,
                            y + dy,
                            fout * 3f * scale
                    );
                }
        );
        Draw.reset();
        Draw.z(prevZ);
    }
}

