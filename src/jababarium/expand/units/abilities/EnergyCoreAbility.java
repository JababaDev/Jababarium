package jababarium.expand.units.abilities;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Drawf;

public class EnergyCoreAbility extends Ability {

    public float offsetX, offsetY;
    public float radius;
    public Color color;

    public EnergyCoreAbility(float offsetX, float offsetY, float radius, Color color){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void draw(Unit unit){
        float x = unit.x + Angles.trnsx(unit.rotation, offsetY) + Angles.trnsx(unit.rotation + 90f, offsetX);
        float y = unit.y + Angles.trnsy(unit.rotation, offsetY) + Angles.trnsy(unit.rotation + 90f, offsetX);

        float pulse = Mathf.absin(Time.time, 6f, radius * 0.25f);

        Draw.z(Layer.effect + 0.01f);

        Draw.color(color);
        Fill.circle(x, y, radius + pulse);

        Drawf.light(x, y, (radius + pulse) * 3f, color, 0.8f);

        Draw.reset();
    }
}
