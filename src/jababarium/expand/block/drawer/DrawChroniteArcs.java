package jababarium.expand.block.drawer;

import arc.graphics.Blending;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import jababarium.content.JBColor;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.draw.DrawBlock;

public class DrawChroniteArcs extends DrawBlock {

    @Override
    public void draw(Building build){
        if(!build.enabled || build.efficiency <= 0f) return;

        float x = build.x;
        float y = build.y;

        float time = Time.time;
        float warmup = build.warmup();

        float rot1 = time * 0.4f;
        float rot2 = -time * 0.25f;

        float glow = 0.6f + Mathf.absin(time, 6f, 0.2f);

        Draw.z(Layer.light);

        Drawf.light(
                x,
                y,
                42f * warmup,
                JBColor.chroniteGlow,
                0.75f * glow * warmup
        );

        Draw.z(Layer.blockOver);

        Draw.color(JBColor.chroniteBase, JBColor.chroniteLight, 0.15f);
        Lines.stroke(1.4f * warmup);
        Lines.arc(x, y, 12f, 0.35f, rot1);

        Draw.color(JBColor.chroniteDark, JBColor.chroniteMid, 0.12f);
        Lines.stroke(1.1f * warmup);
        Lines.arc(x, y, 18f, 0.25f, rot2);

        for(int i = 0; i < 6; i++){
            float angle = time * 0.8f + i * 60f;
            float radius = 10f + (i % 3) * 6f;

            float px = x + Angles.trnsx(angle, radius);
            float py = y + Angles.trnsy(angle, radius);

            Draw.color(JBColor.chroniteLight, JBColor.chroniteMid, 0.25f);
            Fill.circle(px, py, 1.05f * warmup);
        }

        Draw.color(JBColor.chroniteBase);
        Fill.circle(x, y, 2.6f);

        Draw.blend(Blending.additive);
        Draw.color(JBColor.chroniteGlow, 0.85f * glow * warmup);
        Fill.circle(x, y, 4.4f * warmup);
        Draw.blend();

        Draw.reset();
    }
}

