package jababarium.expand.units.abilities;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Drawf;

public class AuraCircleAbility extends Ability {

    public float radius;
    public Color color;
    public float lineWidth;
    public float pulseSpeed;
    public float pulseMagnitude;
    public int glowLayers = 3;
    public float glowAlpha = 0.3f;

    public AuraCircleAbility(float radius, Color color, float lineWidth){
        this.radius = radius;
        this.color = color;
        this.lineWidth = lineWidth;
        this.pulseSpeed = 4f;
        this.pulseMagnitude = 0.15f;
    }

    public AuraCircleAbility(float radius, Color color){
        this(radius, color, 2f);
    }

    @Override
    public void draw(Unit unit){
        float x = unit.x;
        float y = unit.y;

        Draw.z(Layer.effect + 0.01f);

        for(int i = glowLayers; i > 0; i--){
            float glowRadius = radius + (i * lineWidth * 2);
            float alpha = glowAlpha * (1f - (i / (float)glowLayers));

            Draw.color(color);
            Draw.alpha(alpha);
            Lines.stroke(lineWidth * 1.5f);
            Lines.circle(x, y, glowRadius);
        }

        Draw.color(color);
        Draw.alpha(0.9f);
        Lines.stroke(lineWidth);
        Lines.circle(x, y, radius);

        Drawf.light(x, y, (radius) * 1.5f, color, 0.7f);
        Draw.reset();
    }
}