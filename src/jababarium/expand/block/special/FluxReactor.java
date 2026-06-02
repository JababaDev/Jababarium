package jababarium.expand.block.special;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.util.Time;
import arc.util.Tmp;
import jababarium.content.JBSounds;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.meta.BlockFlag;

import static mindustry.Vars.tilesize;

public class FluxReactor extends PowerGenerator {

    private float warmupSpeed = 0.0025f;
    private float cooldownSpeed = 0.02f;

    private float meltdownThreshold = 0.75f;
    private float explodeThreshold = 0.9f;

    private float explosionRadius = 220f;
    private float explosionDamage = 48000f;

    private float baseProduction = 18000f;
    private Color fluxColor = Color.valueOf("3aff6f");

    private Effect meltdownEffect = Fx.impactReactorExplosion;
    private Effect workEffect = Fx.greenCloud;

    private Sound workSound = JBSounds.fluxReactorWorking;
    private Sound meltdownSound = JBSounds.fluxReactorExplosion;

    public Liquid coolant;

    public FluxReactor(String name) {
        super(name);

        hasPower = true;
        outputsPower = true;

        hasLiquids = true;
        liquidCapacity = 60f;

        baseExplosiveness = 2500f;

        flags = EnumSet.of(BlockFlag.reactor, BlockFlag.generator);
    }

    @Override
    public void setBars(){
        super.setBars();

        addBar("flux",
                (FluxReactorBuild b) ->
                        new Bar(
                                () -> Core.bundle.get("bar.jababarium-flux"),
                                () -> fluxColor,
                                () -> b.warmup
                        )
        );

        addBar("meltdown",
                (FluxReactorBuild b) ->
                        new Bar(
                                () -> Core.bundle.get("bar.jababarium-meltdown"),
                                () -> Pal.redderDust,
                                () -> b.meltdown
                        )
        );
    }

    public class FluxReactorBuild extends GeneratorBuild {

        private float warmup;
        private float meltdown;
        private boolean exploding = false;
        private float consumeTimer = 0f;
        private float consumeInterval = 60f;
        private int soundTimer = 600;

        @Override
        public void updateTile(){
            super.updateTile();
            consumeTimer += Time.delta;
            soundTimer += Time.delta;

            if (consumeTimer >= consumeInterval){
                if(canConsume()){
                    consume();
                    consumeTimer = 0;
                }
            }
            if (soundTimer >= 600 && efficiency >= 1){
                workSound.at(this);
            }

            boolean hasCoolant = coolant != null && liquids.get(coolant) > 0.1f;

            if(!exploding){
                if(hasCoolant && efficiency > 0){
                    warmup = Mathf.lerpDelta(warmup, 1f, warmupSpeed);
                    meltdown = Mathf.lerpDelta(meltdown, 0f, cooldownSpeed * 2f);

                    if(coolant != null){
                        liquids.remove(coolant, 0.2f * Time.delta);
                    }
                } else {
                    warmup = Mathf.lerpDelta(warmup, 0f, cooldownSpeed);

                    if(warmup > 0.01f){
                        meltdown += Time.delta / 180f;
                    }
                }

                productionEfficiency = Mathf.pow(warmup, 3f);

                if(warmup > meltdownThreshold){
                    meltdown += Time.delta / 90f;

                    if(Mathf.chanceDelta(0.35f)){
                        workEffect.at(x + Mathf.range(size * tilesize),
                                y + Mathf.range(size * tilesize), fluxColor);
                    }
                }

                if(meltdown > explodeThreshold){
                    exploding = true;
                    startExplosion();
                }
            }

        }

        public void startExplosion(){
            Sounds.loopPulse.at(this);

            final float fx = x;
            final float fy = y;
            final float fsize = size;

            for(int i = 0; i < 25; i++){
                final int index = i;
                Time.run(i * 3f, () -> {

                    new Effect(30f, 200f, e -> {
                        Draw.color(fluxColor, Color.white, e.fin());
                        Lines.stroke(2f * e.fout());
                        Lines.circle(e.x, e.y, e.finpow() * 60f);
                        Draw.alpha(e.fout() * 0.7f);
                        Fill.circle(e.x, e.y, e.fout() * 15f);
                    }).at(fx + Mathf.range(fsize * tilesize * 1.5f),
                            fy + Mathf.range(fsize * tilesize * 1.5f));

                    if(Mathf.chance(0.4f)){
                        Fx.explosion.at(fx + Mathf.range(fsize * tilesize),
                                fy + Mathf.range(fsize * tilesize));
                    }

                    Effect.shake(2f + index * 0.3f, 2f, fx, fy);
                });
            }

            Time.run(75f, () -> {
                Sounds.explosion.at(fx, fy, 0.8f);
                Effect.shake(15f, 10f, fx, fy);

                new Effect(20f, 300f, e -> {
                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    Draw.blend(Blending.additive);
                    Fill.circle(e.x, e.y, 150f * e.fout());
                    Draw.blend();
                }).at(fx, fy);
            });

            Time.run(80f, () -> {
                if(!isValid()) return;
                kill();
            });
        }

        @Override
        public float getPowerProduction(){
            return baseProduction * productionEfficiency;
        }

        @Override
        public void draw(){
            Draw.rect(region, x, y);

            if(warmup > 0.01f){
                float pulse = 1f + Mathf.absin(Time.time, 6f, 0.2f) * warmup;
                float danger = Mathf.clamp(meltdown / meltdownThreshold);

                Draw.z(Layer.effect);
                Draw.color(fluxColor, Pal.redderDust, danger * 0.5f);
                Draw.blend(Blending.additive);

                Fill.circle(x, y, size * tilesize * 0.45f * pulse);

                Lines.stroke(3f * warmup);
                Lines.circle(x, y, size * tilesize * 0.9f * pulse);

                Draw.blend();
                Draw.reset();
            }

            Drawf.light(x, y, size * tilesize * 6f * warmup, fluxColor, 0.95f);
        }

        @Override
        public void onDestroyed(){
            super.onDestroyed();
            workSound.stop();

            final float fx = x;
            final float fy = y;

            new Effect(40f, 600f, e -> {
                Draw.color(Color.white);
                Draw.alpha(e.fout() * 0.9f);
                Draw.blend(Blending.additive);
                Fill.circle(e.x, e.y, 250f * e.fout(Interp.pow3Out));
                Draw.blend();
            }).at(fx, fy);

            Sounds.explosion.at(fx, fy, 0.9f);
            Effect.shake(40f, 30f, fx, fy);

            new Effect(60f, 500f, e -> {
                Draw.color(fluxColor);
                Draw.alpha((1f - e.fin()) * 0.8f);

                Fill.circle(e.x, e.y, 180f * (1f - e.fin(Interp.pow2In)));

                for(int i = 0; i < 12; i++){
                    float angle = i * 30f + e.rotation * 3f;
                    float len = 300f * e.fout(Interp.pow2Out);
                    Drawf.tri(e.x, e.y, 25f * e.fout(), len, angle);
                }

                Draw.color(Color.white);
                Draw.alpha((1f - e.fin()) * 0.6f);
                Fill.circle(e.x, e.y, 100f * (1f - e.fin(Interp.pow3In)));
            }).at(fx, fy);

            for(int i = 0; i < 6; i++){
                final int index = i;
                Time.run(i * 15f, () -> {
                    new Effect(50f, 700f, e -> {
                        Draw.color(fluxColor, Color.white, e.fin());

                        Lines.stroke(12f * e.fout());
                        Lines.circle(e.x, e.y, e.fin(Interp.pow3Out) * 450f);

                        Lines.stroke(8f * e.fout());
                        Lines.circle(e.x, e.y, e.fin(Interp.pow2Out) * 400f);

                        Draw.alpha(e.fout() * 0.3f);
                        Fill.circle(e.x, e.y, e.fout(Interp.pow4Out) * 300f);
                    }).at(fx, fy);

                    Effect.shake(35f - index * 5f, 20f, fx, fy);

                    if(index % 2 == 0){
                        Sounds.explosion.at(fx, fy, 1f - index * 0.15f);
                    }
                });
            }

            for(int i = 0; i < 12; i++){
                Time.run(Mathf.random(120f), () -> {
                    float angle = Mathf.random(360f);
                    float dist = Mathf.random(50f, explosionRadius * 0.8f);

                    Tmp.v1.trns(angle, dist);

                    Fx.dynamicExplosion.at(fx + Tmp.v1.x, fy + Tmp.v1.y, explosionRadius / 6f);

                    new Effect(25f, 200f, e -> {
                        Draw.color(fluxColor);
                        Draw.alpha(e.fout() * 0.7f);
                        Fill.circle(e.x, e.y, 40f * e.fout());

                        Lines.stroke(3f * e.fout());
                        Lines.circle(e.x, e.y, e.fin() * 60f);
                    }).at(fx + Tmp.v1.x, fy + Tmp.v1.y);
                });
            }

            for(int i = 0; i < 8; i++){
                Time.run(Mathf.random(100f), () -> {
                    float angle = Mathf.random(360f);
                    float dist = Mathf.random(100f, explosionRadius * 1.2f);

                    Tmp.v1.trns(angle, dist).add(fx, fy);
                    Fx.chainLightning.at(fx, fy, 15f, fluxColor, Tmp.v1);
                });
            }

            for(int i = 0; i < 12; i++){
                float angle = i * 30f + Mathf.random(15f);
                new Effect(150f, 400f, e -> {
                    Draw.color(fluxColor, Color.gray, e.fin());
                    Draw.alpha(e.fout());

                    float dst = e.rotation * 150f;
                    Tmp.v1.trns(e.data instanceof Float ? (Float)e.data : 0f, dst * e.fin(Interp.pow2Out));

                    Fill.circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 8f * e.fout());

                    Lines.stroke(2f * e.fout());
                    Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, Tmp.v1.angle(), 15f * e.fout());
                }).at(fx, fy, 2f + Mathf.random(1f), angle);
            }

            for(int i = 0; i < 5; i++){
                Time.run(i * 20f, () -> {
                    new Effect(15f, 500f, e -> {
                        Draw.color(fluxColor, Color.white, e.fout());
                        Draw.alpha(e.fout() * 0.6f);
                        Draw.blend(Blending.additive);
                        Fill.circle(e.x, e.y, 200f * e.fout(Interp.pow2Out));
                        Draw.blend();
                    }).at(fx, fy);
                });
            }

            meltdownEffect.at(fx, fy, explosionRadius / 2f, fluxColor);
            Fx.impactReactorExplosion.at(fx, fy, explosionRadius / 3f);

            meltdownSound.at(fx, fy, 0.9f);

            Damage.damage(fx, fy, explosionRadius * 8.5f, explosionDamage);

            Damage.damage(fx, fy, explosionRadius * 10f, explosionDamage * 0.3f);

            Units.nearby(Tmp.r1.setCenter(fx, fy).setSize(explosionRadius * 4f), unit -> {
                float dst = unit.dst(fx, fy);
                float falloff = 1f - Mathf.clamp(dst / (explosionRadius * 2f));

                if(falloff > 0){
                    float force = falloff * 100f;
                    unit.impulse(Tmp.v3.set(unit).sub(fx, fy).nor().scl(force * unit.mass()));
                    unit.apply(StatusEffects.shocked, 240f * falloff);
                }
            });
        }
    }
}