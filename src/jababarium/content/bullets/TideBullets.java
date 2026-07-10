package jababarium.content.bullets;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import jababarium.expand.bullets.AccelBulletType;
import jababarium.expand.bullets.AdaptedLightningBulletType;
import jababarium.expand.bullets.LightningLinkerBulletType;
import jababarium.util.feature.PosLightning;
import jababarium.util.func.JBFunc;
import jababarium.util.graphic.DrawFunc;
import jababarium.util.graphic.EffectWrapper;
import jababarium.util.graphic.JBInterp;
import jababarium.util.graphic.OptionalMultiEffect;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.Units;
import mindustry.game.Team;
import arc.math.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.entities.effect.*;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Fill.circle;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static arc.math.Mathf.rand;

import jababarium.content.*;
import static jababarium.content.JBBullets.*;

public class TideBullets {

    public static void load() {
        tidebreakerLaser = new ShrapnelBulletType() {
            {
                length = 1320;
                damage = 1300f;
                status = StatusEffects.slow;
                statusDuration = 60f;
                width = 11f;
                fromColor = Pal.techBlue;
                hitColor = lightColor = lightningColor = toColor = Pal.techBlue;
                shootEffect = JBFx.lightningHitSmall(toColor);
                smokeEffect = new OptionalMultiEffect(new Effect(lifetime + 2f, b -> {
                    Draw.color(fromColor, toColor, b.fin());
                    circle(b.x, b.y, (width / 2f) * b.fout());
                    DrawFunc.tri(b.x, b.y, width / 1.75f * b.fout(Interp.circleIn), 30f, b.rotation + 60);
                    DrawFunc.tri(b.x, b.y, width / 1.75f * b.fout(Interp.circleIn), 30f, b.rotation - 60);
                }), JBFx.hitSpark(toColor, 35f, 6, 24f, 1.75f, 8f));
            }
        };

        condensedBolt = new BasicBulletType() {
            {
                damage = 2280f;
                speed = 10.5f;
                lifetime = 65f;
                width = 10f;
                height = 58f;

                frontColor = Color.white;
                backColor = Pal.techBlue;
                hitColor = Pal.techBlue;

                trailLength = 45;
                trailWidth = 6.5f;
                trailColor = Pal.techBlue;
                trailInterval = 1f;
                trailRotation = false;

                lightColor = Pal.techBlue;
                lightRadius = 80f;
                lightOpacity = 0.85f;

                splashDamage = 120f;
                splashDamageRadius = 65f;

                status = StatusEffects.electrified;
                statusDuration = 60f * 4f;

                hitShake = 4f;
                hitSound = Sounds.explosionCleroi;

                shootEffect = new Effect(30f, e -> {
                    color(Pal.techBlue, Color.white, e.fout() * 0.45f);
                    stroke(e.fout() * 3.5f);
                    Lines.circle(e.x, e.y, e.finpow() * 32f);

                    color(Pal.techBlue);
                    randLenVectors(e.id, 8, e.finpow() * 35f, e.rotation + 180f, 50f, (x, y) -> {
                        circle(e.x + x, e.y + y, e.fout() * 3.2f);
                        Drawf.light(e.x + x, e.y + y, e.fout() * 8f, Pal.techBlue, 0.6f);
                    });
                });

                smokeEffect = Fx.none;

                trailEffect = new Effect(25f, e -> {
                    color(Pal.techBlue, Color.white, e.fin() * 0.3f);
                    stroke(e.fout(Interp.pow2Out) * 1.8f);
                    Lines.circle(e.x, e.y, e.fout() * 9f + 2f);
                    Drawf.light(e.x, e.y, e.fout() * 18f, Pal.techBlue, 0.5f);
                    Draw.reset();
                });

                
                hitEffect = new Effect(100f, 220f, e -> {
                    float rad = 90f;

                    
                    e.scaled(40f, i -> {
                        Draw.color(Color.white, Pal.techBlue, i.fin() * 0.6f);
                        Lines.stroke(14f * i.fout(Interp.pow2Out));
                        Lines.circle(e.x, e.y, i.fin(Interp.circleOut) * rad * 1.4f);
                    });

                    
                    e.scaled(65f, i -> {
                        color(Pal.techBlue);
                        Lines.stroke(8f * i.fout());
                        Lines.circle(e.x, e.y, i.fin(Interp.circleOut) * rad);

                        
                        Draw.alpha(i.fout(Interp.pow3Out) * 0.35f);
                        circle(e.x, e.y, i.fout(Interp.pow2Out) * rad * 0.55f);
                    });

                    
                    color(Pal.techBlue, Color.white, 0.25f);
                    stroke(e.fout(Interp.pow4Out) * 3.5f);
                    Lines.circle(e.x, e.y, e.fin(Interp.pow3Out) * rad * 1.85f);

                    
                    e.scaled(70f, i -> {
                        Angles.randLenVectors(i.id, 12, rad / 5f,
                                rad * (1f + i.fout(Interp.circleOut)) / 2f, (x, y) -> {

                                    float angle = Mathf.angle(x, y);
                                    float w = i.foutpowdown() * Mathf.random(rad / 8f, rad / 5f) / 2f * i.fout();
                                    float len = Mathf.random(rad * 0.6f, rad * 1.2f) * i.fout(Interp.circleOut);

                                    Draw.color(Pal.techBlue, Color.white, 0.35f);
                                    DrawFunc.tri(i.x + x, i.y + y, w, rad / 3.5f * i.fout(Interp.pow2In), angle - 180f);
                                    DrawFunc.tri(i.x + x, i.y + y, w, len, angle);

                                    Draw.color(Color.white);
                                    DrawFunc.tri(i.x + x, i.y + y, w * 0.4f, len * 0.6f * i.fout(), angle);
                                });
                    });

                    
                    e.scaled(25f, i -> {
                        Draw.color(Color.white, Pal.techBlue, i.fin());
                        Draw.alpha(i.fout(Interp.pow2Out));
                        circle(e.x, e.y, i.fout(Interp.pow3Out) * rad * 0.5f);
                    });

                    Drawf.light(e.x, e.y, rad * e.fout() * 4.5f, Pal.techBlue, 0.9f);
                });

                despawnEffect = new Effect(45f, e -> {
                    for (int i = 0; i < 3; i++) {
                        float delay = i * 0.2f;
                        float p = Mathf.curve(e.fin(), delay, 1f);
                        if (p <= 0f)
                            continue;
                        float fo = 1f - p;

                        color(Pal.techBlue, Color.white, fo * 0.4f);
                        stroke(fo * (2.5f - i * 0.5f));
                        Lines.circle(e.x, e.y, p * (20f + i * 18f));
                    }

                    color(Pal.techBlue);
                    Draw.alpha(e.fout(Interp.pow3Out) * 0.5f);
                    circle(e.x, e.y, e.fout(Interp.pow3Out) * 14f);
                    Drawf.light(e.x, e.y, e.fout() * 50f, Pal.techBlue, 0.55f);
                    Draw.reset();
                });
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                float pulse = Mathf.absin(b.time, 3f, 1f);
                float flow = (b.time * 0.06f) % 1f;
                float angle = b.rotation();

                Lines.stroke(1.4f + pulse * 0.2f);

                for (int i = 0; i < 4; i++) {
                    float phase = ((flow + i * 0.25f) % 1f);
                    
                    float off = (phase - 0.5f) * height * 0.8f;

                    float cx = b.x + Angles.trnsx(angle, off);
                    float cy = b.y + Angles.trnsy(angle, off);

                    float fade = 1f - Math.abs(phase - 0.5f) * 2f;

                    Draw.color(Pal.techBlue);
                    Draw.alpha((0.28f + pulse * 0.08f) * fade);

                    
                    Draw.color(Pal.techBlue);
                    Draw.alpha(0.22f + pulse * 0.07f);
                    circle(b.x, b.y, width * 1.05f + pulse);

                    
                    Draw.color(Color.white);
                    Draw.alpha(0.92f);
                    circle(b.x, b.y, width * 0.32f);

                    Drawf.light(b.x, b.y, lightRadius * (0.75f + pulse * 0.1f), Pal.techBlue, lightOpacity);

                    Draw.reset();
                }
            }

            @Override
            public void despawned(Bullet b) {
                super.despawned(b);
                Sounds.explosion.at(b.x, b.y, 0.85f);
            }
        };

        tidebreakerStd = new AccelBulletType(2.85f, 920f) {
            {
                frontColor = JBColor.lightSkyFront;
                backColor = lightningColor = hitColor = lightColor = JBColor.lightSkyBack;
                trailColor = JBColor.lightSkyMiddle;
                lifetime = 156f;
                knockback = 2f;
                ammoMultiplier = 8f;
                accelerateBegin = 0.1f;
                accelerateEnd = 0.85f;

                statusDuration = 30f;

                despawnSound = hitSound = Sounds.explosionDull;
                hitSoundVolume /= 4f;

                velocityBegin = 8f;
                velocityIncrease = -5f;

                homingDelay = 20f;
                homingPower = 0.05f;
                homingRange = 120f;

                despawnHit = pierceBuilding = true;
                hitShake = despawnShake = 5f;
                lightning = 1;
                lightningCone = 360;
                lightningLengthRand = 12;
                lightningLength = 4;
                width = 10f;
                height = 35f;
                pierceCap = 8;
                shrinkX = shrinkY = 0;

                lightningDamage = damage * 0.85f;

                hitEffect = JBFx.hitSparkLarge;
                despawnEffect = JBFx.square45_6_45;
                shootEffect = JBFx.shootCircleSmall(backColor);
                smokeEffect = JBFx.hugeSmokeGray;
                trailEffect = JBFx.trailToGray;

                trailLength = 15;
                trailWidth = 2f;
                drawSize = 300f;
            }
        };

        basicSkyFrag = new BasicBulletType(6.8f, 150) {
            {
                speed = 6f;
                trailLength = 12;
                trailWidth = 2f;
                lifetime = 60;
                despawnEffect = JBFx.square45_4_45;
                hitEffect = new Effect(45f, e -> {
                    Fx.rand.setSeed(e.id);
                    Draw.color(JBColor.lightSkyFront, JBColor.lightSkyBack, e.fin());
                    Lines.stroke(1.75f * e.fout());
                    Lines.spikes(e.x, e.y, Fx.rand.random(14, 28) * e.finpow(),
                            Fx.rand.random(1, 5) * e.fout() + Fx.rand.random(5, 8) * e.fin(JBInterp.parabola4Reversed),
                            4, 45);
                    Lines.square(e.x, e.y, Fx.rand.random(4, 14) * e.fin(Interp.pow3Out), 45);
                });
                knockback = 4f;
                width = 15f;
                height = 37f;
                lightningDamage = damage * 0.65f;
                backColor = lightColor = lightningColor = trailColor = hitColor = JBColor.lightSkyBack;
                frontColor = JBColor.lightSkyFront;
                lightning = 2;
                lightningLength = lightningLengthRand = 3;
                smokeEffect = Fx.shootBigSmoke2;
                trailChance = 0.2f;
                trailEffect = JBFx.skyTrail;
                drag = 0.015f;
                hitShake = 2f;
                hitSound = Sounds.explosion;
            }
        };

        tideLightning = new LightningLinkerBulletType(5.5f, 1950) {
            {
                rangeOverride = 480;
                lightning = 0; 
                randomLightningChance = 0f;

                trailWidth = 8f;
                trailLength = 40;

                backColor = trailColor = lightColor = lightningColor = JBColor.lightSkyBack;
                frontColor = Color.white;
                randomGenerateRange = 280f;
                randomLightningNum = 5;
                linkRange = 280f;

                scaleLife = true;
                hittable = false;

                size /= 2f;
                drag = 0.0065f;
                fragLifeMin = 0.125f;
                fragLifeMax = 0.45f;
                fragVelocityMax = 0.75f;
                fragVelocityMin = 0.25f;
                fragBullets = 13;
                fragBullet = JBBullets.basicSkyFrag;
                drawSize = 40;
                splashDamageRadius = 240;
                splashDamage = 850;
                status = StatusEffects.shocked;
                lifetime = 300;
                despawnEffect = new OptionalMultiEffect(JBFx.circleOut(JBColor.lightSkyFront, 120f),
                        JBFx.hitSpark(JBColor.lightSkyFront, 50f, 40, 200f, 7f, 25f));
                hitEffect = new Effect(50, e -> {
                    color(JBColor.lightSkyBack);
                    circle(e.x, e.y, e.fout() * 44);
                    stroke(e.fout() * 3.2f);
                    circle(e.x, e.y, e.fin() * 80);
                    stroke(e.fout() * 2.5f);
                    circle(e.x, e.y, e.fin() * 50);
                    Angles.randLenVectors(e.id, 30, 18 + 80 * e.fin(), (x, y) -> {
                        stroke(e.fout() * 3.2f);
                        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 14 + 5);
                    });
                    color(Color.white);
                    circle(e.x, e.y, e.fout() * 30);
                });
                shootEffect = new Effect(30f, e -> {
                    color(JBColor.lightSkyBack);
                    circle(e.x, e.y, e.fout() * 32);
                    color(Color.white);
                    circle(e.x, e.y, e.fout() * 20);
                });
            }
        };

        rift = new BasicBulletType() {
            {
                recoil = 0.095f;
                lifetime = 30f;
                speed = 25f;
                damage = 600f;
                shieldDamageMultiplier = 0.2f;
                trailLength = 200;
                trailWidth = 2F;
                
                keepVelocity = false;

                
                

                trailColor = hitColor = backColor = lightColor = lightningColor = JBColor.thurmixRed;
                frontColor = JBColor.thurmixRedLight;
                width = 10f;
                height = 40f;

                hitSound = Sounds.explosionAfflict;
                despawnShake = hitShake = 18f;

                pierceArmor = true;

                
                

                lightning = 3;
                lightningLength = 6;
                lightningLengthRand = 18;
                lightningDamage = 200;

                smokeEffect = JBFx.square(hitColor, 80f, 8, 48f, 6f);
                shootEffect = JBFx.instShoot(backColor, frontColor);
                despawnEffect = JBFx.lightningHitLarge;
                hitEffect = new MultiEffect(JBFx.hitSpark(backColor, 75f, 24, 90f, 2f, 12f), JBFx.square45_6_45,
                         JBFx.sharpBlast(backColor, frontColor, 120f,
                                40f));
                despawnHit = true;
            }
        };

        arcBolt = new BasicBulletType(75f, 8000) {
            {
                width = 15f;
                height = 100f;
                lifetime = 10f;

                pierce = true;
                pierceCap = -1;

                frontColor = Color.white;
                backColor = Color.red;

                trailColor = JBColor.thurmixRed;
                trailWidth = 7f;
                trailLength = 60;

                lightning = 5;
                lightningDamage = 150;
                lightningLength = 20;

                hitEffect = Fx.instBomb;
                hitSound = JBSounds.blast;
                
                shootEffect = Fx.shootBigColor;
                hitEffect = Fx.massiveExplosion;
                despawnEffect = Fx.scatheExplosion;
            }
        };

        theridionBolt = new BasicBulletType(6f, 0f) {
            {
                sprite = "circle-bullet";
                width = 18f;
                height = 18f;
                shrinkY = 0f;
                shrinkX = 0f;

                Color lightningWhite = Color.valueOf("#ffffff");
                Color lightningPurple = Color.valueOf("#f99292");
                Color deepPurple = Color.valueOf("#b06b6b");
                Color darkPurple = Color.valueOf("#845c5c");

                frontColor = lightningWhite;
                backColor = lightningPurple;
                trailColor = deepPurple;

                lifetime = 120f; 

                
                damage = 0f;
                splashDamage = 0f;

                trailLength = 20;
                trailWidth = 4f;
                trailInterval = 2f;

                
                trailEffect = new ParticleEffect() {
                    {
                        particles = 4;
                        length = 20f;
                        lifetime = 40f;
                        sizeFrom = 6f;
                        sizeTo = 0f;
                        colorFrom = lightningPurple;
                        colorTo = deepPurple.cpy().a(0f);
                        cone = 30f;
                    }
                };

                
                shootEffect = new MultiEffect(
                        new ParticleEffect() {
                            {
                                particles = 20;
                                length = 50f;
                                lifetime = 30f;
                                sizeFrom = 8f;
                                sizeTo = 0f;
                                colorFrom = lightningWhite;
                                colorTo = lightningPurple.cpy().a(0f);
                                cone = 25f;
                                lightOpacity = 0.8f;
                            }
                        },
                        new ParticleEffect() {
                            {
                                particles = 15;
                                length = 40f;
                                lifetime = 35f;
                                sizeFrom = 6f;
                                sizeTo = 0f;
                                colorFrom = lightningPurple;
                                colorTo = deepPurple.cpy().a(0f);
                                cone = 25f;
                            }
                        });

                hitSound = Sounds.explosionNavanax;
            }

            @Override
            public void hit(Bullet b, float x, float y) {
                super.hit(b, x, y);

                
                createLightningNova(x, y, b.team);
            }

            @Override
            public void despawned(Bullet b) {
                super.despawned(b);

                
                if (b.time >= b.lifetime - 1f) {
                    createLightningNova(b.x, b.y, b.team);
                }
            }

            
            void createLightningNova(float x, float y, mindustry.game.Team team) {

                
                int mainBolts = 24; 
                int boltLength = 16; 
                float boltDamage = 80f; 
                float novaRadius = 280f; 

                
                new MultiEffect(
                        
                        new ParticleEffect() {
                            {
                                particles = 1;
                                length = 0f;
                                lifetime = 30f;
                                sizeFrom = 40f;
                                sizeTo = 0f;
                                colorFrom = Color.valueOf("ffffff");
                                colorTo = Color.valueOf("ffffff").cpy().a(0f);
                                region = "circle";
                            }
                        },
                        
                        new ParticleEffect() {
                            {
                                particles = 50;
                                length = 80f;
                                lifetime = 40f;
                                sizeFrom = 10f;
                                sizeTo = 0f;
                                colorFrom = Color.valueOf("#f99292");
                                colorTo = Color.valueOf("#b06b6b").cpy().a(0f);
                                cone = 360f;
                                lightOpacity = 0.9f;
                            }
                        },
                        
                        new ParticleEffect() {
                            {
                                particles = 80;
                                length = 120f;
                                lifetime = 50f;
                                sizeFrom = 8f;
                                sizeTo = 0f;
                                colorFrom = Color.valueOf("ffffff");
                                colorTo = Color.valueOf("#f99292").cpy().a(0f);
                                cone = 360f;
                            }
                        }).at(x, y);

                
                Sounds.explosionNavanax.at(x, y, 1f);

                
                for (int i = 0; i < mainBolts; i++) {
                    float angle = (360f / mainBolts) * i;

                    
                    Lightning.create(
                            team,
                            Color.valueOf("#f99292"),
                            boltDamage,
                            x, y,
                            angle,
                            boltLength);

                    
                    if (i % 2 == 0) {
                        float midAngle = angle + (360f / mainBolts) / 2f;
                        Lightning.create(
                                team,
                                Color.valueOf("#b06b6b"),
                                boltDamage * 0.6f,
                                x, y,
                                midAngle,
                                boltLength - 4);
                    }
                }

                
                for (int i = 0; i < 12; i++) {
                    Time.run(Mathf.random(5f, 15f), () -> {
                        float randomAngle = Mathf.random(360f);
                        Lightning.create(
                                team,
                                Color.valueOf("#f99292").cpy().a(0.6f),
                                boltDamage * 0.4f,
                                x, y,
                                randomAngle,
                                boltLength - 6);
                    });
                }

                
                Damage.damage(
                        team,
                        x, y,
                        100f, 
                        200f 
                );

                
                for (int ring = 0; ring < 3; ring++) {
                    final int currentRing = ring;
                    Time.run(ring * 8f, () -> {
                        float ringRadius = 60f + currentRing * 40f;

                        new ParticleEffect() {
                            {
                                particles = 30;
                                length = ringRadius;
                                lifetime = 35f;
                                sizeFrom = 6f;
                                sizeTo = 0f;
                                colorFrom = Color.valueOf("#f99292").cpy().a(0.7f);
                                colorTo = Color.valueOf("#b06b6b").cpy().a(0f);
                                cone = 360f;
                            }
                        }.at(x, y);

                        Sounds.shootLancer.at(x, y, 1f, 0.8f);
                    });
                }

                
                Effect.shake(8f, 8f, x, y);
            }
        };

        LightningRed = new LightningLinkerBulletType(4f, 220) {
            private final Effect RshootEffect = new Effect(24.0F, e -> {
                e.scaled(10.0F, (b) -> {
                    Draw.color(e.color);
                    Lines.stroke(b.fout() * 3.0F + 0.2F);
                    Lines.circle(b.x, b.y, b.fin() * 70.0F);
                });
                Draw.color(e.color);

                for (int i : Mathf.signs) {
                    DrawFunc.tri(e.x, e.y, 8.0F * e.fout(), 85.0F, e.rotation + 90.0F * i);
                }

                Draw.color(Color.black);

                for (int i : Mathf.signs) {
                    DrawFunc.tri(e.x, e.y, 3F * e.fout(), 38.0F, e.rotation + 90.0F * i);
                }
            });
            private final Effect RsmokeEffect = JBFx.hitSparkLarge;

            {
                lifetime = 160;
                keepVelocity = false;

                lightningDamage = damage = splashDamage = 580;
                splashDamageRadius = 50f;

                homingDelay = 20f;
                homingRange = 300f;
                homingPower = 0.025f;

                smokeEffect = shootEffect = Fx.none;

                effectLingtning = 0;

                maxHit = 6;
                hitShake = despawnShake = 5f;
                hitSound = despawnSound = Sounds.explosionDull;

                size = 7.2f;
                trailWidth = 3f;
                trailLength = 16;

                linkRange = 80f;

                scaleLife = false;
                despawnHit = false;

                collidesAir = collidesGround = true;

                despawnEffect = hitEffect = new OptionalMultiEffect(JBFx.lightningHitLarge, JBFx.hitSparkHuge);

                trailEffect = slopeEffect = JBFx.trailFromWhite;
                spreadEffect = new Effect(32f, e -> {
                    randLenVectors(e.id, 2, 6 + 45 * e.fin(), (x, y) -> {
                        color(e.color);
                        Fill.circle(e.x + x, e.y + y, e.fout() * size / 2f);
                        color(Color.black);
                        Fill.circle(e.x + x, e.y + y, e.fout() * (size / 3f - 1f));
                    });
                }).layer(Layer.effect + 0.00001f);
            }

            public Color getColor(Bullet b) {
                return JBColor.thurmixRed;
            }

            @Override
            public void update(Bullet b) {
                updateTrail(b);
                updateHoming(b);
                updateWeaving(b);
                updateBulletInterval(b);

                Effect.shake(hitShake, hitShake, b);
                if (b.timer(5, hitSpacing)) {
                    slopeEffect.at(b.x + Mathf.range(size / 4f), b.y + Mathf.range(size / 4f), Mathf.random(2f, 4f),
                            JBColor.thurmixRed);
                    spreadEffect.at(b.x, b.y, JBColor.thurmixRed);
                    PosLightning.createRange(b, collidesAir, collidesGround, b, b.team, linkRange, maxHit,
                            JBColor.thurmixRed, Mathf.chanceDelta(randomLightningChance), lightningDamage,
                            lightningLength, PosLightning.WIDTH, boltNum, p -> {
                                liHitEffect.at(p.getX(), p.getY(), JBColor.thurmixRed);
                            });
                }

                if (Mathf.chanceDelta(0.1)) {
                    slopeEffect.at(b.x + Mathf.range(size / 4f), b.y + Mathf.range(size / 4f), Mathf.random(2f, 4f),
                            JBColor.thurmixRed);
                    spreadEffect.at(b.x, b.y, JBColor.thurmixRed);
                }

                if (randomGenerateRange > 0f && Mathf.chance(Time.delta * randomGenerateChance)
                        && b.lifetime - b.time > PosLightning.lifetime)
                    PosLightning.createRandomRange(b, b.team, b, randomGenerateRange, backColor,
                            Mathf.chanceDelta(randomLightningChance), 0, 0, boltWidth, boltNum, randomLightningNum,
                            hitPos -> {
                                randomGenerateSound.at(hitPos, Mathf.random(0.9f, 1.1f));
                                Damage.damage(b.team, hitPos.getX(), hitPos.getY(), splashDamageRadius / 8,
                                        splashDamage * b.damageMultiplier() / 8, collidesAir, collidesGround);
                                JBFx.lightningHitLarge.at(hitPos.getX(), hitPos.getY(), JBColor.thurmixRed);

                                hitModifier.get(hitPos);
                            });

                if (Mathf.chanceDelta(effectLightningChance) && b.lifetime - b.time > Fx.chainLightning.lifetime) {
                    for (int i = 0; i < effectLingtning; i++) {
                        Vec2 v = randVec.rnd(effectLightningLength + Mathf.random(effectLightningLengthRand)).add(b)
                                .add(Tmp.v1.set(b.vel).scl(Fx.chainLightning.lifetime / 2));
                        Fx.chainLightning.at(b.x, b.y, 12f, JBColor.thurmixRed, v.cpy());
                        JBFx.lightningHitSmall.at(v.x, v.y, 20f, JBColor.thurmixRed);
                    }
                }
            }

            @Override
            public void init(Bullet b) {
                super.init(b);

                b.lifetime *= Mathf.randomSeed(b.id, 0.875f, 1.125f);

                RsmokeEffect.at(b.x, b.y, JBColor.thurmixRed);
                RshootEffect.at(b.x, b.y, b.rotation(), JBColor.thurmixRed);
            }

            @Override
            public void drawTrail(Bullet b) {
                if (trailLength > 0 && b.trail != null) {
                    float z = Draw.z();
                    Draw.z(z - 0.0001f);
                    b.trail.draw(getColor(b), trailWidth);
                    Draw.z(z);
                }
            }

            @Override
            public void draw(Bullet b) {
                drawTrail(b);

                Draw.color(Tmp.c1);
                Fill.circle(b.x, b.y, size);

                float[] param = {
                        9f, 28f, 1f,
                        9f, 22f, -1.25f,
                        12f, 16f, -0.45f,
                };

                for (int i = 0; i < param.length / 3; i++) {
                    for (int j : Mathf.signs) {
                        Drawf.tri(b.x, b.y, param[i * 3] * b.fout(), param[i * 3 + 1] * b.fout(),
                                b.rotation() + 90.0F * j + param[i * 3 + 2] * Time.time);
                    }
                }

                Draw.color(Color.black);

                Fill.circle(b.x, b.y, size / 6.125f + size / 3 * Mathf.curve(b.fout(), 0.1f, 0.35f));

                Drawf.light(b.x, b.y, size * 6.85f, JBColor.thurmixRed, 0.7f);
            }

            @Override
            public void despawned(Bullet b) {
                PosLightning.createRandomRange(b, b.team, b, randomGenerateRange, JBColor.thurmixRed,
                        Mathf.chanceDelta(randomLightningChance), 0, 0, boltWidth, boltNum, randomLightningNum,
                        hitPos -> {
                            Damage.damage(b.team, hitPos.getX(), hitPos.getY(), splashDamageRadius,
                                    splashDamage * b.damageMultiplier(), collidesAir, collidesGround);
                            JBFx.lightningHitLarge.at(hitPos.getX(), hitPos.getY(), JBColor.thurmixRed);
                            liHitEffect.at(hitPos);
                            for (int j = 0; j < lightning; j++) {
                                Lightning.create(b, JBColor.thurmixRed,
                                        lightningDamage < 0.0F ? damage : lightningDamage, b.x, b.y,
                                        b.rotation() + Mathf.range(lightningCone / 2.0F) + lightningAngle,
                                        lightningLength + Mathf.random(lightningLengthRand));
                            }
                            hitSound.at(hitPos, Mathf.random(0.9f, 1.1f));

                            hitModifier.get(hitPos);
                        });

                if (despawnHit) {
                    hit(b);
                } else {
                    createUnits(b, b.x, b.y);
                }

                if (!fragOnHit) {
                    createFrags(b, b.x, b.y);
                }

                despawnEffect.at(b.x, b.y, b.rotation(), JBColor.thurmixRed);
                despawnSound.at(b);

                Effect.shake(despawnShake, despawnShake, b);
            }

            @Override
            public void hit(Bullet b, float x, float y) {
                hitEffect.at(x, y, b.rotation(), JBColor.thurmixRed);
                hitSound.at(x, y, hitSoundPitch, hitSoundVolume);

                Effect.shake(hitShake, hitShake, b);

                if (fragOnHit) {
                    createFrags(b, x, y);
                }
                createPuddles(b, x, y);
                createIncend(b, x, y);
                createUnits(b, x, y);

                if (suppressionRange > 0) {
                    Damage.applySuppression(b.team, b.x, b.y, suppressionRange, suppressionDuration, 0f,
                            suppressionEffectChance, new Vec2(b.x, b.y));
                }

                createSplashDamage(b, x, y);

                for (int i = 0; i < lightning; i++) {
                    Lightning.create(b, JBColor.thurmixRed, lightningDamage < 0 ? damage : lightningDamage, b.x, b.y,
                            b.rotation() + Mathf.range(lightningCone / 2) + lightningAngle,
                            lightningLength + Mathf.random(lightningLengthRand));
                }
            }

            @Override
            public void removed(Bullet b) {
                if (trailLength > 0 && b.trail != null && b.trail.size() > 0) {
                    Fx.trailFade.at(b.x, b.y, trailWidth, JBColor.thurmixRed, b.trail.copy());
                }
            }
        };

        chargedCannonBolt = new BasicBulletType(28f, 10500) {
            {
                width = 35f;
                height = 95f;
                lifetime = 45f;
                sprite = "missile-large";

                
                backColor = JBColor.thurmixRed;
                frontColor = Color.white;
                lightColor = JBColor.thurmixRedLight;
                lightRadius = 180f;
                lightOpacity = 0.9f;

                
                trailWidth = 10f;
                trailLength = 65;
                trailColor = backColor;
                trailEffect = JBFx.hitSparkLarge;
                trailChance = 0.3f;

                pierce = true;
                pierceCap = 40;
                pierceBuilding = true;

                splashDamage = 9500f;
                splashDamageRadius = 180f;

                
                lightning = 10;
                lightningLength = 30;
                lightningLengthRand = 20;
                lightningDamage = 2000f;
                lightningColor = backColor;

                hitSound = JBSounds.blastShockwave;

                shootEffect = new MultiEffect(
                        JBFx.crossBlast(Color.valueOf("ff1a1a"), 320f),
                        new Effect(60f, e -> {
                            Draw.color(Color.valueOf("ff1a1a"), Color.white, e.fin());
                            Lines.stroke(5f * e.fout());
                            Lines.circle(e.x, e.y, 180f * e.fout());
                            Angles.randLenVectors(e.id, 25, 90f * e.fout(), (x, y) -> {
                                Fill.circle(e.x + x, e.y + y, 7f * e.fout());
                            });
                            Drawf.light(e.x, e.y, e.fout() * 150f, Color.valueOf("ff1a1a"), 0.8f);
                        }));

                hitEffect = new MultiEffect(
                        JBFx.blast(lightColor, 200f),
                        JBFx.hitSparkHuge,
                        JBFx.lightningHitLarge);

                despawnEffect = new MultiEffect(
                        JBFx.crossBlast(lightColor, 240f, 45f),
                        Fx.scatheExplosion,
                        JBFx.blast(lightColor, 280f));

                knockback = 50f;
                hitShake = 30f;
                status = StatusEffects.melting;
                statusDuration = 450f;
            }

            @Override
            public void update(Bullet b) {
                super.update(b);

                if (Mathf.chanceDelta(0.6f)) {
                    JBFx.lightningSpark.at(b.x + Mathf.range(12f), b.y + Mathf.range(12f), b.rotation(), backColor);
                }
                if (Mathf.chanceDelta(0.25f)) {
                    JBFx.lightningHitSmall.at(b.x, b.y, b.rotation(), backColor);
                }
            }
        };

        tideLaser = new LaserBulletType() {
            {
                damage = 1600f;
                hitColor = JBColor.thurmixRed;
                colors = new Color[] { hitColor.cpy().mul(1f, 1f, 1f, 0.45f), hitColor, JBColor.thurmixRedLight,
                        Color.white };
                length = 600f;
                width = 14f;
                lifetime = PosLightning.lifetime + 5f;
                ammoMultiplier = 4;
                lengthFalloff = 0.8f;
                sideLength = 40f;
                sideWidth = 0.5f;
                sideAngle = 30f;
                largeHit = true;
                hitEffect = JBFx.instHit(hitColor, 2, 36f);
                shootEffect = JBFx.square(hitColor, 15f, 2, 8f, 2f);
            }
        };

        tideBall = new AccelBulletType(3.85f, 240f, "mine-bullet") {
            {
                frontColor = Color.white;
                backColor = lightningColor = trailColor = hitColor = lightColor = JBColor.thurmixRed;
                lifetime = 165f;

                spin = 3f;

                statusDuration = 300f;

                accelerateBegin = 0.15f;
                accelerateEnd = 0.95f;

                despawnSound = hitSound = Sounds.explosionTitan;

                velocityBegin = 8f;
                velocityIncrease = -7.5f;

                collides = false;
                scaleLife = scaledSplashDamage = true;
                despawnHit = true;
                hitShake = despawnShake = 18f;
                lightning = 4;
                lightningCone = 360;
                lightningLengthRand = 12;
                lightningLength = 10;
                width = height = 30;
                shrinkX = shrinkY = 0;

                splashDamageRadius = 120f;
                splashDamage = 800f;

                lightningDamage = damage * 0.85f;

                hitEffect = JBFx.hitSparkLarge;
                despawnEffect = JBFx.square45_6_45;
                trailEffect = JBFx.trailToGray;

                trailLength = 15;
                trailWidth = 5f;
                drawSize = 300f;

                shootEffect = JBFx.instShoot(backColor, frontColor);
                smokeEffect = JBFx.lightningHitLarge;

                hitEffect = new Effect(90, e -> {
                    Draw.color(backColor, frontColor, e.fout() * 0.7f);
                    Fill.circle(e.x, e.y, e.fout() * height / 1.25f);
                    Lines.stroke(e.fout() * 3f);
                    Lines.circle(e.x, e.y, e.fin() * 80);
                    Lines.stroke(e.fout() * 2f);
                    Lines.circle(e.x, e.y, e.fin() * 50);
                    Angles.randLenVectors(e.id, 35, 18 + 100 * e.fin(),
                            (x, y) -> lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 12 + 4));

                    Draw.color(frontColor);
                    Fill.circle(e.x, e.y, e.fout() * height / 1.75f);
                });
                despawnEffect = new OptionalMultiEffect(JBFx.hitSparkHuge, JBFx.instHit(backColor, 3, 120f));

                fragBullets = 3;
                fragBullet = new LaserBulletType() {
                    {
                        length = 460f;
                        damage = 4060f;
                        width = 45f;

                        statusDuration = 120f;

                        lifetime = 65f;

                        splashDamage = 800;
                        splashDamageRadius = 120;
                        hitShake = 18f;

                        lightningSpacing = 35f;
                        lightningLength = 8;
                        lightningDelay = 1.1f;
                        lightningLengthRand = 15;
                        lightningDamage = 450;
                        lightningAngleRand = 40f;
                        scaledSplashDamage = largeHit = true;

                        lightningColor = trailColor = hitColor = lightColor = JBColor.thurmixRedLight;

                        despawnHit = false;
                        hitEffect = new Effect(90, 500, e -> {
                            Draw.color(backColor, frontColor, e.fout() * 0.7f);
                            Fill.circle(e.x, e.y, e.fout() * height / 1.55f);
                            Lines.stroke(e.fout() * 3f);
                            Lines.circle(e.x, e.y, e.fin(Interp.pow3Out) * 80);
                            Angles.randLenVectors(e.id, 18, 18 + 100 * e.fin(),
                                    (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 7f));

                            Draw.color(frontColor);
                            Fill.circle(e.x, e.y, e.fout() * height / 2f);
                        });

                        sideAngle = 15f;
                        sideWidth = 0f;
                        sideLength = 0f;
                        colors = new Color[] { hitColor.cpy().a(0.2f), hitColor, Color.white };
                    }

                    @Override
                    public void despawned(Bullet b) {
                        
                    }

                    @Override
                    public void init(Bullet b) {
                        Vec2 p = new Vec2()
                                .set(JBFunc.collideBuildOnLength(b.team, b.x, b.y, length, b.rotation(), bu -> true));

                        float resultLength = b.dst(p), rot = b.rotation();

                        b.fdata = resultLength;
                        laserEffect.at(b.x, b.y, rot, resultLength * 0.75f);

                        if (lightningSpacing > 0) {
                            int idx = 0;
                            for (float i = 0; i <= resultLength; i += lightningSpacing) {
                                float cx = b.x + Angles.trnsx(rot, i),
                                        cy = b.y + Angles.trnsy(rot, i);

                                int f = idx++;

                                for (int s : Mathf.signs) {
                                    Time.run(f * lightningDelay, () -> {
                                        if (b.isAdded() && b.type == this) {
                                            Lightning.create(b, lightningColor,
                                                    lightningDamage < 0 ? damage : lightningDamage,
                                                    cx, cy, rot + 90 * s + Mathf.range(lightningAngleRand),
                                                    lightningLength + Mathf.random(lightningLengthRand));
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void draw(Bullet b) {
                        float realLength = b.fdata;

                        float f = Mathf.curve(b.fin(), 0f, 0.2f);
                        float baseLen = realLength * f;
                        float cwidth = width;
                        float compound = 1f;

                        Tmp.v1.trns(b.rotation(), baseLen);

                        for (Color color : colors) {
                            Draw.color(color);
                            Lines.stroke((cwidth *= lengthFalloff) * b.fout());
                            Lines.lineAngle(b.x, b.y, b.rotation(), baseLen, false);

                            Fill.circle(Tmp.v1.x + b.x, Tmp.v1.y + b.y, Lines.getStroke() * 2.2f);
                            Fill.circle(b.x, b.y, 1f * cwidth * b.fout());
                            compound *= lengthFalloff;
                        }
                        Draw.reset();
                        Drawf.light(b.x, b.y, b.x + Tmp.v1.x, b.y + Tmp.v1.y, width * 1.4f * b.fout(), colors[0], 0.6f);
                    }
                };
            }
        };

        tideLightningRed = new BasicBulletType(18f, 480) {
            {
                lifetime = 60f;
                width = 6f;
                height = 28f;
                keepVelocity = false;
                pierceArmor = true;
                pierceCap = 2;
                pierceBuilding = true;

                drag = 0.018f;
                knockback = 8f;
                hitShake = despawnShake = 6f;

                trailLength = 18;
                trailWidth = 3.5f;
                trailChance = 1f;

                backColor = trailColor = hitColor = lightColor = lightningColor = JBColor.thurmixRed;
                frontColor = JBColor.thurmixRedLight;

                trailEffect = new Effect(28f, e -> {
                    rand.setSeed(e.id);
                    Draw.color(JBColor.thurmixRed, JBColor.thurmixRedLight, e.fout() * 0.4f);
                    Draw.alpha(e.fout() * 0.85f);
                    Fill.square(e.x, e.y, rand.random(1.5f, 4f) * e.fout(), e.rotation + 45f);
                    Drawf.light(e.x, e.y, e.fout() * 14f, JBColor.thurmixRed, 0.6f);
                });

                shootEffect = new Effect(35f, e -> {
                    Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.5f);

                    Lines.stroke(e.fout() * 2.5f);
                    Lines.circle(e.x, e.y, e.fin(Interp.pow3Out) * 28f);

                    Angles.randLenVectors(e.id, 5, 6f + 28f * e.finpow(), e.rotation, 28f, (x, y) -> {
                        Fill.square(e.x + x, e.y + y, e.fout() * 3.5f, 45f);
                        Drawf.light(e.x + x, e.y + y, e.fout() * 8f, JBColor.thurmixRed, 0.6f);
                    });
                });

                hitEffect = new MultiEffect(
                        new Effect(55f, e -> {
                            Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.35f);

                            
                            Lines.stroke(3.5f * e.fout());
                            Lines.circle(e.x, e.y, e.fin(Interp.pow4Out) * 65f);

                            Lines.stroke(1.8f * e.fout());
                            Lines.circle(e.x, e.y, e.fin(Interp.pow2Out) * 42f);

                            
                            Angles.randLenVectors(e.id, 10, 8f + 55f * e.finpow(), (x, y) -> {
                                float ang = Mathf.angle(x, y);
                                Fill.square(e.x + x, e.y + y, e.fout() * 4.5f, ang + 45f);
                                Drawf.light(e.x + x, e.y + y, e.fout() * 10f, JBColor.thurmixRed, 0.5f);
                            });

                            
                            Draw.color(Color.white, JBColor.thurmixRedLight, e.fin());
                            Fill.circle(e.x, e.y, e.fout(Interp.pow5Out) * 14f);

                            Drawf.light(e.x, e.y, e.fout() * 90f, JBColor.thurmixRed, 0.8f);
                        }),
                        JBFx.sharpBlast(JBColor.thurmixRed, JBColor.thurmixRedLight, 65f, 32f),
                        JBFx.hitSpark(JBColor.thurmixRed, 50f, 16, 60f, 1.8f, 9f));

                despawnEffect = new MultiEffect(
                        new Effect(40f, e -> {
                            Draw.color(JBColor.thurmixRed, JBColor.thurmixRedLight, e.fin());
                            Lines.stroke(2f * e.fout());
                            for (int i = 0; i < 4; i++) {
                                float angle = i * 90f + e.fin() * 35f;
                                Lines.lineAngle(e.x, e.y, angle, e.finpow() * 30f);
                            }
                            Fill.circle(e.x, e.y, e.fout() * 6f);
                            Drawf.light(e.x, e.y, e.fout() * 40f, JBColor.thurmixRed, 0.6f);
                        }),
                        JBFx.square45_4_45);

                smokeEffect = new Effect(45f, e -> {
                    Draw.color(JBColor.thurmixRedDark, Color.darkGray, e.fin());
                    Angles.randLenVectors(e.id, 5, 3f + 18f * e.finpow(),
                            (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 3.5f));
                });

                hitSound = Sounds.explosionAfflict;
                hitSoundVolume = 0.75f;
                despawnHit = true;
            }
        };

        collapseShell = new BasicBulletType(20f, 2800) {
            {
                lifetime = 100f;
                width = 18f;
                height = 52f;
                keepVelocity = false;

                drag = 0.022f;
                knockback = 28f;
                hitShake = despawnShake = 35f;

                splashDamage = 1800f;
                splashDamageRadius = 180f;

                trailLength = 25;
                trailWidth = 7f;

                backColor = trailColor = hitColor = lightColor = lightningColor = JBColor.thurmixRed;
                frontColor = JBColor.thurmixRedLight;

                trailEffect = new Effect(35f, e -> {
                    Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.3f);
                    Fill.circle(e.x, e.y, e.fout() * 9f);
                    Drawf.light(e.x, e.y, e.fout() * 30f, JBColor.thurmixRed, 0.8f);
                });

                Effect explosion = new Effect(110f, 600f, e -> {
                    
                    Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.55f);
                    Lines.stroke(6f * e.fout(Interp.pow5Out));
                    Lines.circle(e.x, e.y, e.fin(Interp.pow3Out) * 260f);

                    
                    e.scaled(70f, s -> {
                        Draw.color(JBColor.thurmixRed);
                        Lines.stroke(4f * s.fout());
                        Lines.circle(e.x, e.y, s.fin(Interp.pow2Out) * 180f);
                    });

                    
                    e.scaled(45f, s -> {
                        Draw.color(JBColor.thurmixRedLight);
                        Lines.stroke(2.5f * s.fout());
                        Lines.circle(e.x, e.y, s.fin(Interp.pow2Out) * 110f);
                    });

                    
                    e.scaled(50f, s -> {
                        Draw.color(Color.white, JBColor.thurmixRedLight, s.fin());
                        Fill.circle(e.x, e.y, s.fout(Interp.pow3Out) * 95f);

                        Draw.color(JBColor.thurmixRed, JBColor.thurmixRedDark, s.fin());
                        Fill.circle(e.x, e.y, s.fout(Interp.pow2Out) * 70f);
                    });

                    
                    e.scaled(20f, s -> {
                        Draw.color(Color.white);
                        Fill.circle(e.x, e.y, s.fout(Interp.pow5Out) * 50f);
                    });

                    
                    Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.4f);
                    Angles.randLenVectors(e.id, 18, 25f + 200f * e.finpow(), (x, y) -> {
                        float ang = Mathf.angle(x, y);
                        Lines.stroke(e.fout() * 4f);
                        Lines.lineAngle(e.x + x, e.y + y, ang, e.fslope() * 28f + 8f);
                        Drawf.light(e.x + x, e.y + y, e.fout() * 22f, JBColor.thurmixRed, 0.6f);
                    });

                    
                    Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, e.fin());
                    Angles.randLenVectors(e.id + 3, 12, 15f + 130f * e.finpow(), (x, y) -> {
                        Fill.square(e.x + x, e.y + y, e.fout() * 5.5f, 45f);
                        Drawf.light(e.x + x, e.y + y, e.fout() * 14f, JBColor.thurmixRedLight, 0.5f);
                    });

                    
                    Draw.color(Color.gray, Color.darkGray, e.fin());
                    Draw.alpha(0.65f * e.fout());
                    Angles.randLenVectors(e.id + 1, 12, 14f + 110f * e.finpow(),
                            (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 22f));

                    
                    Drawf.light(e.x, e.y, e.fout() * 380f, JBColor.thurmixRed, 0.98f);
                });

                hitEffect = new MultiEffect(
                        explosion,
                        JBFx.hitSpark(JBColor.thurmixRed, 90f, 32, 170f, 3.5f, 22f),
                        JBFx.sharpBlast(JBColor.thurmixRed, JBColor.thurmixRedLight, 110f, 85f),
                        JBFx.circleOut(JBColor.thurmixRed, 200f));

                despawnEffect = hitEffect;

                shootEffect = JBFx.instShoot(JBColor.thurmixRed, JBColor.thurmixRedLight);
                smokeEffect = Fx.shootBigSmoke2;
                hitSound = Sounds.explosionAfflict;
                hitSoundVolume = 2f;
                hitShake = 40f;

                lightning = 4;
                lightningLength = 10;
                lightningLengthRand = 22;
                lightningDamage = 350f;
                lightningCone = 360f;

                pierceArmor = true;
                despawnHit = true;
            }
        };

    }
}
