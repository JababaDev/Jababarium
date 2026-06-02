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
import jababarium.content.JBColor;
import jababarium.util.graphic.DrawFunc;

public class CollapseCharge extends DrawPart {
    protected static final Rand rand = new Rand();
    protected static final Vec2 tr = new Vec2(), tr2 = new Vec2();

    
    public PartProgress progress = PartProgress.smoothReload;
    public Interp curve = Interp.pow3;

    
    public Floatf<PartParams> chargeY = t -> -20f;
    
    public Floatf<PartParams> shootY  = t ->  40f;

    
    public Color colorOuter = JBColor.thurmixRed;
    public Color colorInner = JBColor.thurmixRedLight;
    public Color colorDark  = JBColor.thurmixRedDark;

    
    public float coreRadius    = 10f;   
    public float muzzleRadius  = 15f;   
    public float spikeCount    = 6;     
    public float ringRadiusScl = 1.6f;  

    @Override
    public void draw(PartParams params) {
        float x = params.x, y = params.y, rotation = params.rotation;
        float fin = progress.getClamp(params);

        if (fin < 0.01f) return;

        Draw.z(Layer.effect - 1f);

        
        tr2.trns(rotation, chargeY.get(params));   
        tr.trns(rotation, shootY.get(params));     

        
        float lineAlpha = Mathf.curve(fin, 0.15f, 0.6f);
        Draw.color(colorOuter, lineAlpha * 0.7f);
        Lines.stroke(Mathf.lerp(1f, 3.5f, fin) * lineAlpha);
        Lines.line(x + tr2.x, y + tr2.y, x + tr.x, y + tr.y);

        
        Draw.color(colorInner, lineAlpha * 0.4f);
        Lines.stroke(Mathf.lerp(0.5f, 1.5f, fin) * lineAlpha);
        Lines.line(x + tr2.x, y + tr2.y, x + tr.x, y + tr.y);

        
        float coreFin = Mathf.curve(fin, 0.1f, 0.9f);

        
        Draw.color(colorOuter);
        Lines.stroke(coreFin * 2.5f);
        Lines.circle(x + tr2.x, y + tr2.y, coreRadius * ringRadiusScl * coreFin);

        
        for (int i = 0; i < 3; i++) {
            float arcRot = Time.time * (1.2f + i * 0.3f) + i * 120f;
            Draw.color(colorOuter, coreFin * (0.6f + i * 0.15f));
            Lines.stroke(coreFin * 2f);
            Lines.arc(x + tr2.x, y + tr2.y,
                    coreRadius * (0.9f + i * 0.18f) * coreFin,
                    0.22f,
                    arcRot);
        }

        
        float spikeFin = Mathf.curve(fin, 0.3f, 1f);
        Draw.color(colorOuter);
        for (int i = 0; i < (int) spikeCount; i++) {
            float spikeAngle = rotation + i * (360f / spikeCount) + Time.time * 0.8f;
            float spikeLen   = coreRadius * 1.8f * spikeFin;
            float spikeWidth = coreRadius * 0.22f * spikeFin;
            Drawf.tri(x + tr2.x, y + tr2.y, spikeWidth, spikeLen, spikeAngle);
        }

        
        Draw.color(colorOuter);
        Fill.circle(x + tr2.x, y + tr2.y, coreFin * coreRadius * 0.85f);
        Draw.color(colorInner);
        Fill.circle(x + tr2.x, y + tr2.y, coreFin * coreRadius * 0.5f);
        Draw.color(Color.white);
        Fill.circle(x + tr2.x, y + tr2.y, coreFin * coreRadius * 0.22f);

        
        Draw.color(colorOuter);
        Lines.stroke(coreFin * 1.8f);
        DrawFunc.circlePercentFlip(x + tr2.x, y + tr2.y,
                coreFin * (coreRadius + 4f), Time.time * 1.1f, 18f);

        
        float muzzleFin = Mathf.curve(fin, 0.55f, 1f);

        
        Draw.color(colorOuter);
        for (int i = 0; i < 4; i++) {
            float rayAngle = rotation + i * 90f + Time.time * 1.5f;
            float rayLen   = muzzleRadius * (2.5f + Mathf.absin(Time.time + i * 25f, 8f, 0.8f)) * muzzleFin;
            float rayWidth = muzzleRadius * 0.18f * muzzleFin;
            Drawf.tri(x + tr.x, y + tr.y, rayWidth, rayLen, rayAngle);
            
            Drawf.tri(x + tr.x, y + tr.y, rayWidth * 0.6f, rayLen * 0.3f, rayAngle + 180f);
        }

        
        Draw.color(colorOuter, muzzleFin * 0.6f);
        for (int i = 0; i < 4; i++) {
            float rayAngle = rotation + 45f + i * 90f - Time.time * 0.9f;
            float rayLen   = muzzleRadius * (1.6f + Mathf.absin(Time.time + i * 15f, 10f, 0.5f)) * muzzleFin;
            Drawf.tri(x + tr.x, y + tr.y, muzzleRadius * 0.1f * muzzleFin, rayLen, rayAngle);
        }

        
        Draw.color(colorOuter);
        Lines.stroke(muzzleFin * 3f);
        DrawFunc.circlePercent(x + tr.x, y + tr.y,
                muzzleRadius * ringRadiusScl * muzzleFin,
                muzzleFin, rotation - muzzleFin * 360f);

        
        float fin9 = Mathf.curve(fin, 0.88f, 1f);
        for (int i = 0; i < 4; i++) {
            float rot = Time.time * 2f + i * 90f;
            Tmp.v1.trns(rot, muzzleRadius * ringRadiusScl * muzzleFin + Lines.getStroke() * 2f).add(x + tr.x, y + tr.y);
            Draw.color(colorOuter, fin9);
            Fill.square(Tmp.v1.x, Tmp.v1.y, 2.5f * fin9, rot + 45f);
        }
        
        for (int i = 0; i < 4; i++) {
            float rot = -Time.time * 1.3f + i * 90f;
            Tmp.v1.trns(rot, muzzleRadius * ringRadiusScl * muzzleFin + Lines.getStroke() * 3.5f).add(x + tr.x, y + tr.y);
            Draw.color(colorInner, fin9 * 0.8f);
            Fill.square(Tmp.v1.x, Tmp.v1.y, 1.8f * fin9, rot + 45f);
        }

        
        Draw.color(colorOuter);
        Fill.circle(x + tr.x, y + tr.y, muzzleFin * muzzleRadius);
        Draw.color(colorDark);
        Fill.circle(x + tr.x, y + tr.y, muzzleFin * muzzleRadius * 0.65f);
        Draw.color(colorInner);
        Fill.circle(x + tr.x, y + tr.y, muzzleFin * muzzleRadius * 0.35f);
        Draw.color(Color.white);
        Fill.circle(x + tr.x, y + tr.y, muzzleFin * muzzleRadius * 0.15f);

        
        Drawf.light(x + tr2.x, y + tr2.y, coreRadius * 5f * coreFin, colorOuter, 0.55f * coreFin);
        Drawf.light(x + tr.x,  y + tr.y,  muzzleRadius * 6f * muzzleFin, colorOuter, 0.7f * muzzleFin);
    }
}