package jababarium.expand.units.abilities;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;

public class RotatingCoreAbility extends Ability {

    public Color color;
    public Color darkColor;
    public Color frontColor;

    public float size    = 28f;
    public float offsetX = 0f;
    public float offsetY = 0f;

    public float blade1Speed =  2.2f;
    public float blade2Speed = -2.8f;

    public float bladeLength  = 3.6f;
    public float bladeWidth   = 0.65f;
    public float bladeTailLen = 0.45f;

    public float ringPulseSpeed = 55f;

    public float lightRadius  = 3.8f;
    public float lightOpacity = 0.75f;

    protected float angle1 = 0f;
    protected float angle2 = 90f;

    public RotatingCoreAbility() {
        color = Color.valueOf("ff9988");
        buildColors();
    }

    public RotatingCoreAbility(Color color, float size, float offsetX, float offsetY) {
        this.color   = color;
        this.size    = size;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        buildColors();
    }

    void buildColors() {
        darkColor  = color.cpy().mul(0.22f, 0.22f, 0.22f, 1f);
        frontColor = color.cpy().lerp(Color.white, 0.55f);
    }

    @Override
    public void init(UnitType type) {
        buildColors();
    }

    @Override
    public void update(Unit unit) {
        angle1 += blade1Speed * Time.delta;
        angle2 += blade2Speed * Time.delta;
    }

    @Override
    public void draw(Unit unit) {
        float rad = unit.rotation * Mathf.degRad;
        float cx  = unit.x + offsetX * Mathf.cos(rad) - offsetY * Mathf.sin(rad);
        float cy  = unit.y + offsetX * Mathf.sin(rad) + offsetY * Mathf.cos(rad);

        float s = size;

        float bLen1  = s * bladeLength;
        float bW1    = s * bladeWidth;
        float bTail1 = bLen1 * bladeTailLen;

        float bLen2  = bLen1 * 0.70f;
        float bW2    = bW1   * 0.68f;
        float bTail2 = bLen2 * bladeTailLen;

        Draw.z(Layer.effect);

        float cs = s * 1.3f;

        
        Draw.color(darkColor);
        Draw.alpha(1f);
        Fill.circle(cx, cy, cs * 0.78f);

        
        drawBlade(cx, cy, angle1,        bLen1, bW1, bTail1);
        drawBlade(cx, cy, angle1 + 180f, bLen1, bW1, bTail1);

        drawBlade(cx, cy, angle2,        bLen2, bW2, bTail2);
        drawBlade(cx, cy, angle2 + 180f, bLen2, bW2, bTail2);

        
        Draw.color(color);
        Draw.alpha(1f);
        Fill.circle(cx, cy, cs * 0.62f);

        Draw.color(frontColor);
        Draw.alpha(0.95f);
        Fill.circle(cx, cy, cs * 0.46f);

        Draw.color(frontColor);
        Draw.alpha(0.92f);
        Drawf.tri(cx, cy, cs * 1.04f, cs * 0.57f, 90f);
        Drawf.tri(cx, cy, cs * 1.04f, cs * 0.57f, 270f);

        Draw.color(Color.white);
        Draw.alpha(0.70f);
        Drawf.tri(cx, cy, cs * 0.55f, cs * 0.12f, 0f);
        Drawf.tri(cx, cy, cs * 0.55f, cs * 0.12f, 180f);

        Draw.color(Color.white);
        Draw.alpha(0.40f);
        Drawf.tri(cx, cy, cs * 0.40f, cs * 0.07f, 0f);
        Drawf.tri(cx, cy, cs * 0.40f, cs * 0.07f, 180f);

        Draw.color(frontColor);
        Draw.alpha(1f);
        Fill.circle(cx, cy, cs * 0.32f);

        Draw.color(Color.white);
        Draw.alpha(0.92f);
        Fill.circle(cx, cy, cs * 0.20f);

        
        float ringAlpha = (Mathf.sin(Time.time / ringPulseSpeed) + 1f) * 0.5f;
        float ringR = cs * 1.08f;

        Draw.color(darkColor);
        Draw.alpha(ringAlpha * 0.9f);
        Lines.stroke(cs * 0.11f);
        Lines.circle(cx, cy, ringR);

        Draw.color(color);
        Draw.alpha(ringAlpha);
        Lines.stroke(cs * 0.065f);
        Lines.circle(cx, cy, ringR);

        Draw.color(frontColor);
        Draw.alpha(ringAlpha * 0.65f);
        Lines.stroke(cs * 0.028f);
        Lines.circle(cx, cy, ringR);

        
        drawBladeTip(cx, cy, angle1,        bLen1, s);
        drawBladeTip(cx, cy, angle1 + 180f, bLen1, s);
        drawBladeTip(cx, cy, angle2,        bLen2, s * 0.78f);
        drawBladeTip(cx, cy, angle2 + 180f, bLen2, s * 0.78f);

        
        Drawf.light(cx, cy, s * lightRadius, color, lightOpacity);

        Draw.reset();
    }

    void drawBlade(float cx, float cy, float angle,
                   float len, float w, float tail) {
        float baseOffset = len * 0.08f;
        float bx = cx + Angles.trnsx(angle, baseOffset);
        float by = cy + Angles.trnsy(angle, baseOffset);

        Draw.color(darkColor);
        Draw.alpha(0.88f);
        Drawf.tri(bx, by, w * 1.5f, len * 1.05f, angle);
        Drawf.tri(bx, by, w * 1.0f, tail * 1.1f,  angle + 180f);

        Draw.color(color);
        Draw.alpha(0.92f);
        Drawf.tri(bx, by, w,         len,  angle);
        Drawf.tri(bx, by, w * 0.65f, tail, angle + 180f);

        Draw.color(frontColor);
        Draw.alpha(0.68f);
        Drawf.tri(bx, by, w * 0.38f, len * 0.82f, angle);

        Draw.color(Color.white);
        Draw.alpha(0.42f);
        Drawf.tri(bx, by, w * 0.18f, len * 0.65f, angle);
    }

    void drawBladeTip(float cx, float cy, float angle, float len, float s) {
        float tipX = cx + Angles.trnsx(angle, len * 0.85f);
        float tipY = cy + Angles.trnsy(angle, len * 0.85f);

        Draw.color(color);
        Draw.alpha(0.55f);
        Fill.circle(tipX, tipY, s * 0.14f);

        Draw.color(Color.white);
        Draw.alpha(0.35f);
        Fill.circle(tipX, tipY, s * 0.07f);

        Drawf.light(tipX, tipY, s * 0.9f, color, 0.65f);
    }

    public void write(Writes write) {
        write.f(angle1);
        write.f(angle2);
    }

    public void read(Reads read) {
        angle1 = read.f();
        angle2 = read.f();
    }
}

