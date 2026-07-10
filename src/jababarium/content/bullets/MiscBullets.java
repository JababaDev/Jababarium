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

public class MiscBullets {

    public static void load() {
        burst = new ArtilleryBulletType(16f, 2500) {
            {
                lifetime = 80f;
                width = 14f;
                height = 24f;
                shrinkY = 0.3f;
                backColor = Color.valueOf("#5CE65C");
                frontColor = Color.white;
                lightColor = backColor;
                trailWidth = 4.5f;
                trailLength = 25;
                trailColor = backColor;
                hitSound = JBSounds.shootGauss1;
                shootEffect = Fx.shootBigColor;
                hitEffect = Fx.massiveExplosion;
                despawnEffect = Fx.scatheExplosion;

                fragBullets = 12;
                fragBullet = new BasicBulletType(5f, 40) {
                    {
                        width = 7f;
                        height = 12f;
                        lifetime = 25f; 
                        backColor = Color.valueOf("#5CE65C");
                        frontColor = Color.white;
                        lightning = 2;
                        lightningColor = backColor;
                    }
                };
            }
        };

        singularityPoint = new PointBulletType() {
            {
                shootEffect = Fx.instShoot;
                hitEffect = Fx.instHit;
                smokeEffect = Fx.smokeCloud;
                trailEffect = Fx.instTrail;

                
                trailColor = Color.valueOf("bf92f9");
                lightColor = Color.valueOf("bf92f9");

                hitSound = JBSounds.shootGauss3;
                damage = 3050f;
                speed = 500f;
                hitShake = 8f;

                fragOnHit = true;
                fragBullets = 1;

                fragBullet = new BasicBulletType(0f, 0) {
                    {
                        lifetime = 15f;
                        splashDamageRadius = 400f;
                        splashDamage = 800f;

                        hitEffect = JBFx.singularityCollapse;
                        despawnEffect = Fx.none;

                        collidesAir = true;
                        collidesGround = true;
                    }

                    @Override
                    public void update(Bullet b) {
                        if (b.time < 1f) {
                            Units.nearbyEnemies(b.team, b.x, b.y, splashDamageRadius, unit -> {
                                float angle = arc.math.Angles.angle(unit.x, unit.y, b.x, b.y);
                                float dst = arc.math.Mathf.dst(unit.x, unit.y, b.x, b.y);

                                float strength = dst * 0.12f;

                                strength = arc.math.Mathf.clamp(strength, 0f, 15f);

                                unit.vel.set(0, 0);

                                unit.vel.add(
                                        arc.math.Mathf.cosDeg(angle) * strength,
                                        arc.math.Mathf.sinDeg(angle) * strength);
                            });

                            JBFx.singularityCollapse.at(b.x, b.y);
                        }
                        super.update(b);
                    }
                };
            }
        };

        entropyBolt = new BasicBulletType(8f, 150) {
            {
                lifetime = 40f;
                width = 12f;
                height = 12f;
                backColor = Color.valueOf("4fdfff");
                frontColor = Color.white;
                hitEffect = Fx.hitLancer;
                hitSound = JBSounds.shootGauss3;

                lightning = 3;
                lightningLength = 3;
                lightningColor = backColor;

                collidesAir = true;
                collidesGround = true;
                pierce = false;
            }

            @Override
            public void hit(Bullet b, float x, float y) {
                super.hit(b, x, y);

                if (b.damage > 15f) {
                    mindustry.gen.Unit target = mindustry.entities.Units.closestEnemy(b.team, x, y, 180f,
                            u -> u.dst(x, y) > 10f);

                    if (target != null) {

                        Bullet next = this.create(b.owner, b.team, x, y,
                                arc.math.Angles.angle(x, y, target.x, target.y));
                        next.damage = b.damage * 0.85f;

                        Fx.chainLightning.at(x, y, 0, Color.valueOf("4fdfff"), target);
                    }
                }
            }
        };

        transgression = new ContinuousFlameBulletType(300) {
            {
                shake = 3;
                hitColor = flareColor = lightColor = lightningColor = JBColor.lightSkyBack;
                colors = new Color[] { JBColor.lightSkyBack.cpy().mul(0.75f, 0.85f, 1f, 0.65f),
                        JBColor.lightSkyBack.cpy().mul(1f, 1f, 1f, 0.65f),
                        JBColor.lightSkyBack.cpy().lerp(JBColor.green, 0.5f), JBColor.green };
                width = 6;
                length = 380f;
                oscScl = 0.9f;
                oscMag *= 2f;
                lifetime = 35f;
                lightning = 4;
                lightningLength = 2;
                lightningLengthRand = 18;
                flareLength = 75;
                flareWidth = 6;
                hitEffect = JBFx.shootCircleSmall(JBColor.lightSkyBack);
                shootEffect = JBFx.lightningHitLarge(JBColor.lightSkyBack);
                lightningDamage = damage / 6f;
                despawnHit = false;
                pierceArmor = true;
                status = StatusEffects.burning;
            }

            @Override
            public void update(Bullet b) {
                super.update(b);

                if (Mathf.chanceDelta(0.11))
                    for (int i = 0; i < lightning; i++) {
                        Lightning.create(b, lightningColor, lightningDamage < 0 ? damage : lightningDamage, b.x, b.y,
                                b.rotation() + Mathf.range(lightningCone / 2) + lightningAngle,
                                lightningLength + Mathf.random(lightningLengthRand));
                    }
            }

            @Override
            public void hit(Bullet b, float x, float y) {
                hitEffect.at(x, y, b.rotation(), hitColor);
                hitSound.at(x, y, hitSoundPitch, hitSoundVolume);

                Effect.shake(hitShake, hitShake, b);

                Lightning.create(b, lightningColor, lightningDamage < 0 ? damage : lightningDamage, x, y,
                        b.rotation() + Mathf.range(lightningCone / 2) + lightningAngle,
                        lightningLength + Mathf.random(lightningLengthRand));
            }
        };

        chronosField = new BasicBulletType(0f, 0f) {
            final Color fieldColor = Color.valueOf("a066ff");
            final Color fieldColorLight = Color.valueOf("ead9ff");
            final Color fieldColorMid = Color.valueOf("c099ff");
            {
                lifetime = 60f * 5f;
                collides = false;
                collidesTiles = false;
                collidesAir = false;
                absorbable = false;
                pierce = true;
                hitEffect = JBFx.circleOut(Color.valueOf("a066ff"), 240f);
                despawnEffect = Fx.none;
            }

            @Override
            public void update(Bullet b) {
                super.update(b);

                if (b.timer.get(1, 5f)) {
                    float remaining = Math.max(0f, b.lifetime - b.time);
                    float applyDuration = Math.min(60f, remaining);
                    Units.nearbyEnemies(b.team, b.x, b.y, 240f, unit -> {
                        unit.apply(JBStatus.chronosStop, applyDuration);
                    });
                }
                if (b.timer.get(2, 12f)) {
                    JBFx.circleOut(Color.valueOf("a066ff"), 240f).at(b.x, b.y);
                }
                if (b.timer.get(3, 8f)) {
                    float ang = Mathf.random(360f);
                    float dist = Mathf.random(60f, 220f);
                    JBFx.squareRand(fieldColor, 2f, 4.5f).at(
                        b.x + Angles.trnsx(ang, dist),
                        b.y + Angles.trnsy(ang, dist),
                        ang
                    );
                }
            }

            @Override
            public void draw(Bullet b) {
                float rad = 240f;
                float remaining = Math.max(0f, b.lifetime - b.time) / b.lifetime;
                float pulse = 0.5f + 0.5f * Mathf.sinDeg(Time.time * 1.4f);

                
                Draw.color(fieldColor, 0.07f * remaining * pulse);
                Fill.circle(b.x, b.y, rad);

                
                Draw.color(fieldColor, fieldColorLight, pulse);
                Draw.alpha(0.55f * remaining);
                Lines.stroke(2.5f * remaining);
                Lines.circle(b.x, b.y, rad);

                
                Draw.color(fieldColorMid, 0.5f * remaining);
                Lines.stroke(1.5f * remaining);
                for (int i = 0; i < 4; i++) {
                    Lines.arc(b.x, b.y, rad * 0.68f, 0.13f, Time.time * 0.9f + i * 90f);
                }

                
                Draw.color(fieldColorLight, 0.35f * remaining * pulse);
                Lines.stroke(1.2f * remaining);
                Lines.circle(b.x, b.y, rad * 0.35f);

                Drawf.light(b.x, b.y, rad * 0.45f, fieldColor, 0.28f * remaining);
                Draw.reset();
            }
        };

        chronosShell = new ArtilleryBulletType(6f, 0f) {
            final Color shellColor = Color.valueOf("a066ff");
            {
                lifetime = 200f;
                width = 20f;
                height = 26f;
                shrinkY = 0.3f;
                backColor = Color.valueOf("a066ff");
                frontColor = Color.valueOf("f0e6ff");
                trailWidth = 5f;
                trailLength = 45;
                trailColor = backColor;

                hitEffect = JBFx.circleOut(Color.valueOf("a066ff"), 180f);
                despawnHit = true;
                hitSound = JBSounds.largeBeam;
            }

            @Override
            public void update(Bullet b) {
                super.update(b);
                if (b.timer.get(2, 5f)) {
                    JBFx.circleOut(shellColor, 22f).at(b.x, b.y, b.rotation());
                }
                if (b.timer.get(3, 9f)) {
                    float ang = b.rotation() + Mathf.range(100f);
                    float dist = Mathf.random(5f, 16f);
                    JBFx.squareRand(shellColor, 1.5f, 3.5f).at(
                        b.x + Angles.trnsx(ang, dist),
                        b.y + Angles.trnsy(ang, dist),
                        ang
                    );
                }
            }

            @Override
            public void hit(Bullet b, float x, float y) {
                super.hit(b, x, y);
                b.data = Boolean.TRUE;
                chronosField.create(b.owner, b.team, x, y, 0f);
            }

            @Override
            public void despawned(Bullet b) {
                if (b.data == null) {
                    chronosField.create(b.owner, b.team, b.x, b.y, 0f);
                }
            }
        };

        apexMicro = new BasicBulletType(7f, 800) {
            {
                lifetime = 45f;
                width = 8f;
                height = 10f;
                homingPower = 0.12f;
                homingRange = 220f;

                frontColor = Color.white;
                backColor = Color.valueOf("7bff9a");
                trailColor = backColor;
                trailWidth = 2.8f;
                trailLength = 10;

                hitEffect = Fx.hitBulletBig;
                despawnHit = true;
                hitSound = JBSounds.shootGauss1;
            }
        };

        apexShard = new BasicBulletType(5f, 1520) {
            {
                lifetime = 55f;
                width = 12f;
                height = 18f;
                shrinkY = 0.2f;

                frontColor = Color.white;
                backColor = Color.valueOf("4dff7a");
                trailColor = backColor;
                trailWidth = 3.6f;
                trailLength = 20;

                hitEffect = Fx.hitBulletColor;
                despawnHit = true;
                hitSound = JBSounds.blast;

                fragBullets = 12;
                fragBullet = apexMicro;
                fragVelocityMin = 0.8f;
                fragVelocityMax = 1.6f;
                fragLifeMin = 0.6f;
                fragLifeMax = 1.1f;
            }
        };

        apexShell = new ArtilleryBulletType(5.2f, 7000f) {
            {
                lifetime = 180f;
                width = 30f;
                height = 40f;
                shrinkY = 0.3f;

                splashDamage = 1400f;
                splashDamageRadius = 160f;

                frontColor = Color.white;
                backColor = Color.valueOf("69ff6a");
                trailColor = backColor;
                trailWidth = 7f;
                trailLength = 45;

                hitEffect = Fx.massiveExplosion;
                despawnEffect = Fx.scatheExplosion;
                hitSound = JBSounds.blastShockwave;
                hitShake = 12f;

                fragBullets = 100;
                fragBullet = apexShard;
                fragVelocityMin = 0.7f;
                fragVelocityMax = 1.4f;
                fragLifeMin = 0.6f;
                fragLifeMax = 1.2f;
            }
        };

        abbys = new ArtilleryBulletType(3f, 25000) {
            BulletType damageField;

            {
                hitEffect = despawnEffect = Fx.none;
                width = 32f;
                height = 38f;
                shrinkY = 0f;
                shrinkX = 0f;
                lifetime = 180f;

                shootSound = JBSounds.hugeShoot;

                collidesTiles = false;
                collidesAir = false;
                collides = false;
                absorbable = false;

                pierce = true;

                splashDamage = 0f;
                splashDamageRadius = 0f;

                frontColor = Color.valueOf("#9b9b9b");
                backColor = Color.valueOf("#f8f8f8");
                trailColor = Color.valueOf("#cdcccc");

                lightning = 12;
                lightningLength = 35;
                lightningDamage = 600f;
                lightningColor = Color.valueOf("#fef4f4");

                trailWidth = 8f;
                trailLength = 45;
                trailInterval = 3f;
                trailRotation = true;

                fragBullets = 0;

                fragBullet = new BasicBulletType(8f, 250) {
                    {
                        width = 14f;
                        height = 18f;
                        lifetime = 55f;
                        frontColor = Color.valueOf("fff4f4");
                        backColor = Color.valueOf("fff4f4");

                        lightning = 4;
                        lightningLength = 14;
                        lightningDamage = 120;
                        lightningColor = Color.valueOf("fff4f4");

                        collidesAir = true;
                        collidesGround = true;
                        pierce = true;
                        pierceCap = 3;

                        hitEffect = new Effect(30f, e -> {
                            JBFx.hitSparkHuge.at(e.x, e.y, e.color);
                            Draw.blend(Blending.additive);
                            Draw.color(Color.valueOf("fff4f4"));
                            circle(e.x, e.y, e.fout() * 10f);
                            Lines.stroke(2.5f * e.fout());
                            Lines.circle(e.x, e.y, e.fin() * 24f);
                            Draw.blend();
                        });
                        despawnHit = true;
                    }
                };

                trailEffect = new Effect(40f, e -> {
                    Draw.blend(Blending.additive);
                    Draw.color(Color.valueOf("1a1a1a"), Color.valueOf("#fbdede"), e.fin());
                    circle(e.x, e.y, e.fout() * 5f);
                    for (int i = 0; i < 2; i++) {
                        Drawf.light(e.x, e.y, e.fout() * 30f, Color.valueOf("#f2e4e4"), 0.7f);
                    }
                    Draw.blend();
                });

                
                despawnEffect = new Effect(700f, 1400f, e -> {
                    Draw.blend(Blending.additive);

                    
                    if (e.time < 60f) {
                        float phase1 = e.time / 60f;
                        float invP1 = 1f - phase1;

                        Draw.color(Color.white, Color.valueOf("#fff4f4"), phase1);

                        
                        Fill.circle(e.x, e.y, invP1 * 120f);

                        
                        for (int i = 0; i < 5; i++) {
                            float ringPhase = Mathf.clamp((phase1 - i * 0.07f) * 1.4f);
                            if (ringPhase <= 0f) continue;
                            Lines.stroke(6f * (1f - ringPhase) + 1.5f);
                            Draw.color(Color.valueOf("#fff8f8"), Color.valueOf("#ffdddd"), ringPhase);
                            Lines.circle(e.x, e.y, ringPhase * 300f);
                        }

                        
                        for (int i = 0; i < 30; i++) {
                            float angle = i * 12f + e.time * 6f;
                            float dst = phase1 * Mathf.randomSeed(e.id + i, 60f, 140f);
                            Draw.color(Color.valueOf("#fef4f4"), Color.valueOf("#ffcccc"), phase1);
                            circle(e.x + Angles.trnsx(angle, dst), e.y + Angles.trnsy(angle, dst),
                                    invP1 * Mathf.randomSeed(e.id + i * 2, 2f, 5f));
                        }

                        Drawf.light(e.x, e.y, invP1 * 600f, Color.valueOf("#fff4f4"), 0.9f);

                        
                    } else if (e.time < 300f) {
                        float phase2 = (e.time - 60f) / 240f;
                        float invP2 = 1f - phase2;

                        if (Math.abs(e.time - 65f) < 2f) {
                            JBFx.hitSparkHuge.at(e.x, e.y, Color.valueOf("#fff4f4"));
                        }

                        
                        for (int i = 0; i < 8; i++) {
                            float angle = i * 45f;
                            float len = phase2 * 720f;
                            float beamWidth = 22f * invP2;

                            
                            Lines.stroke(beamWidth);
                            Draw.color(Color.valueOf("#fffbfb"), Color.valueOf("#f0e8e8"), phase2);
                            Lines.lineAngle(e.x, e.y, angle, len);

                            
                            Lines.stroke(beamWidth * 0.55f);
                            Draw.color(Color.valueOf("#fff4f4"));
                            float branchStart = len * 0.55f;
                            Lines.lineAngle(e.x + Angles.trnsx(angle, branchStart),
                                    e.y + Angles.trnsy(angle, branchStart), angle + 30, len * 0.45f);
                            Lines.lineAngle(e.x + Angles.trnsx(angle, branchStart),
                                    e.y + Angles.trnsy(angle, branchStart), angle - 30, len * 0.45f);

                            
                            Lines.stroke(beamWidth * 0.3f);
                            float branch2Start = branchStart + len * 0.25f;
                            Lines.lineAngle(e.x + Angles.trnsx(angle, branch2Start),
                                    e.y + Angles.trnsy(angle, branch2Start), angle + 50, len * 0.2f);
                            Lines.lineAngle(e.x + Angles.trnsx(angle, branch2Start),
                                    e.y + Angles.trnsy(angle, branch2Start), angle - 50, len * 0.2f);
                        }

                        
                        for (int i = 0; i < 8; i++) {
                            float angle = i * 45f + 22.5f;
                            float len = phase2 * 450f;
                            Lines.stroke(12f * invP2);
                            Draw.color(Color.valueOf("#fff4f4"), Color.valueOf("#ffe8e8"), phase2);
                            Lines.lineAngle(e.x, e.y, angle, len);

                            Lines.stroke(6f * invP2);
                            float branchStart = len * 0.5f;
                            Lines.lineAngle(e.x + Angles.trnsx(angle, branchStart),
                                    e.y + Angles.trnsy(angle, branchStart), angle + 35, len * 0.4f);
                            Lines.lineAngle(e.x + Angles.trnsx(angle, branchStart),
                                    e.y + Angles.trnsy(angle, branchStart), angle - 35, len * 0.4f);
                        }

                        
                        Draw.color(Color.valueOf("fff4f4"));
                        circle(e.x, e.y, 130f + Mathf.absin(e.time * 2.5f, 5f, 45f));
                        circle(e.x, e.y, 88f + Mathf.absin(e.time * 2.5f, 5f, 30f));
                        circle(e.x, e.y, 44f + Mathf.absin(e.time * 2.5f, 5f, 15f));

                        
                        for (int i = 0; i < 24; i++) {
                            if (Mathf.randomSeed(e.id + i + (long)(e.time / 3f)) > 0.55f) {
                                float angle = Mathf.randomSeed(e.id + i * 2, 360f);
                                float dist = Mathf.randomSeed(e.id + i * 3, 180f, 420f);
                                Lines.stroke(4f);
                                Draw.color(Color.valueOf("fff4f4"), Color.valueOf("fff4f4"), Mathf.random());
                                Lines.line(e.x, e.y, e.x + Angles.trnsx(angle, dist), e.y + Angles.trnsy(angle, dist));
                            }
                        }

                        
                        for (int i = 0; i < 16; i++) {
                            float spiralAngle = (e.time * 2.5f + i * 22.5f) % 360f;
                            float spiralDist = 70f + (e.time - 60f) * 0.95f;
                            Draw.color(Color.valueOf("fff4f4"));
                            circle(e.x + Angles.trnsx(spiralAngle, spiralDist),
                                    e.y + Angles.trnsy(spiralAngle, spiralDist), 5f);
                        }

                        Drawf.light(e.x, e.y, 800f * invP2, Color.valueOf("#fff4f4"), 0.85f);

                        
                    } else if (e.time < 500f) {
                        float phase3 = (e.time - 300f) / 200f;
                        float invP3 = 1f - phase3;

                        
                        for (int ring = 0; ring < 10; ring++) {
                            float ringPhase = Mathf.clamp((phase3 - ring * 0.06f) * 1.8f);
                            if (ringPhase <= 0f) continue;
                            Lines.stroke(20f * invP3);
                            Draw.color(Color.valueOf("fff4f4"), Color.valueOf("fff4f4"), ringPhase);
                            Lines.circle(e.x, e.y, ringPhase * 1100f);
                            Lines.stroke(10f * invP3);
                            Lines.circle(e.x, e.y, ringPhase * 1100f + 12f);
                        }

                        
                        for (int i = 0; i < 100; i++) {
                            float angle = i * 3.6f + Mathf.randomSeed(e.id + i, 360f);
                            float dst = phase3 * Mathf.randomSeed(e.id + i * 2, 560f, 1000f);
                            float px = e.x + Angles.trnsx(angle, dst);
                            float py = e.y + Angles.trnsy(angle, dst);
                            Lines.stroke(4f * invP3);
                            Draw.color(Color.valueOf("fff4f4"), Color.valueOf("fff4f4"), phase3);
                            Lines.lineAngle(px, py, angle + 180, invP3 * 150f);
                            Draw.color(Color.valueOf("fff4f4"));
                            circle(px, py, invP3 * 8f);
                        }

                        
                        for (int i = 0; i < 24; i++) {
                            float angle = i * 15f;
                            float blastPhase = Mathf.clamp((phase3 - i * 0.01f) * 1.3f);
                            if (blastPhase > 0) {
                                Lines.stroke(14f * (1f - blastPhase));
                                Draw.color(Color.valueOf("fff4f4"), Color.valueOf("fff4f4"), blastPhase);
                                Lines.lineAngle(e.x, e.y, angle, blastPhase * 880f);
                                Drawf.light(e.x + Angles.trnsx(angle, blastPhase * 880f),
                                        e.y + Angles.trnsy(angle, blastPhase * 880f),
                                        (1f - blastPhase) * 120f, Color.valueOf("fff4f4"), 0.7f);
                            }
                        }

                        
                        for (int i = 0; i < 20; i++) {
                            float cloudAngle = Mathf.randomSeed(e.id + i, 360f);
                            float cloudDist = phase3 * Mathf.randomSeed(e.id + i * 5, 350f, 700f);
                            Draw.color(Color.valueOf("fff4f4"), invP3 * 0.7f);
                            circle(e.x + Angles.trnsx(cloudAngle, cloudDist),
                                    e.y + Angles.trnsy(cloudAngle, cloudDist),
                                    invP3 * Mathf.randomSeed(e.id + i * 7, 50f, 100f));
                        }

                        
                        Draw.color(Color.valueOf("fff4f4"), 0.55f * invP3);
                        circle(e.x, e.y, invP3 * 560f);
                        Draw.color(Color.valueOf("fff4f4"), 0.75f * invP3);
                        circle(e.x, e.y, invP3 * 420f);
                        Draw.color(Color.valueOf("fff4f4"));
                        circle(e.x, e.y, invP3 * 140f + Mathf.absin(e.time * 3f, 6f, 25f));
                        Drawf.light(e.x, e.y, invP3 * 1200f, Color.valueOf("fff4f4"), 0.85f);

                    }

                    Draw.blend();
                });

                hitSound = JBSounds.hugeBlast;

                shootEffect = new Effect(30f, e -> {
                    Draw.blend(Blending.additive);
                    Draw.color(Color.valueOf("fff4f4"));
                    for (int i = 0; i < 8; i++) {
                        Drawf.tri(e.x, e.y, 10f * e.fout(), 80f, i * 45 + e.rotation);
                    }
                    Draw.blend();
                });

                smokeEffect = new Effect(50f, e -> {
                    Draw.blend(Blending.additive);
                    Draw.color(Color.valueOf("fff4f4"), Color.valueOf("fff4f4"), e.fin());
                    Angles.randLenVectors(e.id, 18, 90f * e.finpow(), (x, y) -> {
                        circle(e.x + x, e.y + y, e.fout() * 7f);
                    });
                    Draw.blend();
                });

                
                damageField = new BasicBulletType(0f, 1) {
                    {
                        lifetime = 420f;  

                        despawnEffect = Fx.none;
                        hitEffect = Fx.none;
                        smokeEffect = Fx.none;
                        shootEffect = Fx.none;

                        hittable = false;
                        absorbable = false;
                        collides = false;
                        drag = 0f;
                    }

                    @Override
                    public void update(Bullet b) {
                        
                        if (b.time < 60f) {
                            if (b.timer.get(0, 8f)) {
                                Damage.damage(b.team, b.x, b.y, 200f, 1800f);
                                Damage.status(b.team, b.x, b.y, 200f, StatusEffects.slow, 90f, true, true);

                                if (!Vars.headless) {
                                    
                                    for (int i = 0; i < 6; i++) {
                                        float angle = Mathf.random(360f);
                                        float len = Mathf.random(300f, 600f);
                                        Vec2 target = new Vec2().trns(angle, len).add(b.x, b.y);
                                        PosLightning.createEffect(b, target, Color.valueOf("#fef4f4"), 2, PosLightning.WIDTH);
                                    }
                                }
                            }

                            
                        } else if (b.time < 300f) {
                            if (b.timer.get(0, 4f)) {
                                float progress = (b.time - 60f) / 240f;
                                float radius = 200f + progress * 600f;

                                Damage.damage(b.team, b.x, b.y, radius, 5500f);

                                if (Mathf.chance(0.35)) {
                                    JBFx.hitSparkHuge.at(b.x + Mathf.range(radius / 2), b.y + Mathf.range(radius / 2),
                                            Color.valueOf("#fef4f4"));
                                }

                                
                                if (!Vars.headless && b.timer.get(1, 12f)) {
                                    PosLightning.createRandomRange(b.team, b, radius * 1.8f, Color.valueOf("#fef4f4"),
                                            false, 0f, 0, PosLightning.WIDTH, 2, 3, PosLightning.none);
                                }
                            }

                            
                        } else if (b.time < 350f && Math.abs(b.time - 300f) < 3f) {
                            Damage.damage(b.team, b.x, b.y, 1100f, 85000f);
                            Damage.status(b.team, b.x, b.y, 1100f, StatusEffects.melting, 900f, true, true);
                            Damage.status(b.team, b.x, b.y, 800f, StatusEffects.slow, 600f, true, true);

                            if (fragBullet != null) {
                                for (int i = 0; i < 48; i++) {
                                    fragBullet.create(b.owner, b.team, b.x, b.y, i * (360f / 48f));
                                }
                            }

                            
                            if (!Vars.headless) {
                                for (int i = 0; i < 24; i++) {
                                    float angle = i * 15f;
                                    float len = Mathf.random(800f, 1600f);
                                    Vec2 target = new Vec2().trns(angle, len).add(b.x, b.y);
                                    PosLightning.createEffect(b, target, Color.valueOf("#fef4f4"), 3, PosLightning.WIDTH * 1.5f);
                                }
                            }

                            JBSounds.hugeBlast.at(b.x, b.y);

                            
                        } else if (b.time >= 350f) {
                            if (b.timer.get(0, 12f)) {
                                float progress = (b.time - 350f) / 70f;
                                Damage.damage(b.team, b.x, b.y, 900f + progress * 200f, 2500f);
                            }
                        }
                    }
                };

            }

            @Override
            public void update(Bullet b) {
                super.update(b);
                if (b.timer.get(0, 8f)) {
                    JBFx.hitSparkHuge.at(b.x, b.y, lightningColor);
                    if (Mathf.chance(0.6)) {
                        Lightning.create(b.team, lightningColor, 14f, b.x, b.y, Mathf.random(360f), 10);
                    }
                }
            }

            @Override
            public void despawned(Bullet b) {
                super.despawned(b);

                despawnEffect.at(b.x, b.y, 0f, backColor);
                damageField.create(b.owner, b.team, b.x, b.y, 0f);

                if (fragBullet != null) {
                    for (int i = 0; i < 24; i++) {
                        fragBullet.create(b.owner, b.team, b.x, b.y, Mathf.random(360f));
                    }
                }
            }
        };

        tinyShell = new BasicBulletType(7f, 100) {
            {
                lifetime = 45f;
                width = 8f;
                height = 10f;
                homingPower = 0.12f;
                homingRange = 220f;

                frontColor = Color.white;
                backColor = Color.valueOf("#bc2312");
                trailColor = backColor;
                trailWidth = 2.8f;
                trailLength = 10;

                hitEffect = Fx.hitBulletBig;
                despawnHit = true;
                hitSound = JBSounds.shootGauss1;
            }
        };

        laserBurn = new LaserBulletType() {
            {
                
                damage = 2800f; 
                length = 320f; 
                width = 7.5f; 

                
                colors = new Color[] {
                        Color.valueOf("ff000033"), 
                        Color.valueOf("ff3333bb"), 
                        Color.valueOf("ff8888ee"), 
                        Color.valueOf("ffffffff") 
                };

                
                lifetime = 22f; 

                
                status = StatusEffects.burning;
                statusDuration = 480f; 

                
                shootEffect = JBFx.hitSpark(Color.valueOf("ff2222"), 35f, 14, 45f, 2.0f, 11f);
                hitEffect = JBFx.hitSpark(Color.valueOf("ff5555"), 40f, 18, 55f, 2.2f, 13f);
                hitColor = Color.valueOf("ff2222");
                smokeEffect = JBFx.hugeSmoke; 

                
                knockback = 0f;
                pierce = true;
                pierceBuilding = false;
                bulletInterval = 0f;

                
                lightRadius = 55f;
                lightColor = Color.valueOf("ff333388");
                lightOpacity = 0.75f;
            }
        };

        thunderShot = new BasicBulletType(13f, 650f) {
            {
                
                width = 10f;
                height = 18f;

                
                backColor = Color.valueOf("ff3333"); 
                frontColor = Color.valueOf("ffffff"); 

                
                lifetime = 35f;

                
                hitEffect = JBFx.lightningHitLarge(Color.valueOf("ff4444"));
                despawnEffect = JBFx.lightningHitSmall;
                hitColor = Color.valueOf("ff6666");

                
                lightningDamage = 305f; 
                lightningLength = 20; 
                lightningColor = Color.valueOf("ff9999"); 

                
                trailLength = 12;
                trailColor = Color.valueOf("ff333388");
                trailWidth = 4.5f;
                trailEffect = JBFx.lightningSpark;

                
                shootEffect = JBFx.hitSpark(Color.valueOf("ff2222"), 20f, 8, 22f, 1.6f, 8f);
                smokeEffect = JBFx.hugeSmokeGray;

                
                inaccuracy = 2.5f;
                drag = 0.005f;
                knockback = 2.5f;

                
                lightRadius = 30f;
                lightColor = Color.valueOf("ff333366");
                lightOpacity = 0.65f;
            }
        };

        pulseWave = new BasicBulletType(4.5f, 20f) {
            {
                
                width = 44f;
                height = 44f;
                backColor = Color.valueOf("ff2222"); 
                frontColor = Color.valueOf("ffffff"); 

                
                lifetime = 30f;

                
                splashDamage = 6800f; 
                splashDamageRadius = 280f; 

                
                knockback = 14f;

                
                hitEffect = new MultiEffect(
                        JBFx.crossBlast(Color.valueOf("ff2222"), 140f), 
                        JBFx.circleOut(Color.valueOf("ffffff"), 130f), 
                        JBFx.hitSparkHuge 
                );
                despawnEffect = new MultiEffect(
                        JBFx.blast(Color.valueOf("ff1111"), 130f),
                        JBFx.circleOut(Color.valueOf("ffaaaa"), 140f));
                hitColor = Color.valueOf("ff2222");

                
                despawnHit = true;

                
                trailLength = 14;
                trailColor = Color.valueOf("ff222299");
                trailWidth = 7f;

                
                shootEffect = JBFx.crossBlast(Color.valueOf("ff2222"), 50f);
                smokeEffect = JBFx.hugeSmokeGray;

                
                lightRadius = 55f;
                lightColor = Color.valueOf("ff111188");
                lightOpacity = 0.8f;

                drag = 0.015f;
                pierce = false;

                
                status = StatusEffects.slow;
                statusDuration = 180f; 
            }
        };

        missileStrike = new BasicBulletType(8, 1100f, JBBullets.STRIKE) {
            {
                trailColor = lightningColor = backColor = lightColor = JBColor.lightSkyBack;
                frontColor = JBColor.lightSkyFront;
                lightning = 2;
                lightningCone = 360;
                lightningLengthRand = lightningLength = 8;
                homingPower = 0;
                scaleLife = true;
                collides = false;

                trailLength = 15;
                trailWidth = 3.5f;

                splashDamage = lightningDamage = damage;
                splashDamageRadius = 48f;
                lifetime = 95f;

                width = 22f;
                height = 35f;

                trailEffect = JBFx.trailToGray;
                trailParam = 3f;
                trailChance = 0.35f;

                hitShake = 7f;
                hitSound = Sounds.explosion;
                hitEffect = JBFx.hitSpark(backColor, 75f, 24, 95f, 2.8f, 16);

                smokeEffect = new OptionalMultiEffect(JBFx.hugeSmokeGray, JBFx.circleSplash(backColor, 60f, 8, 60f, 6));
                shootEffect = JBFx.hitSpark(backColor, 30f, 15, 35f, 1.7f, 8);

                despawnEffect = JBFx.blast(backColor, 60);

                
                fragBullets = 5;
                fragLifeMax = 0.6f;
                fragLifeMin = 0.2f;
                fragVelocityMax = 0.35f;
                fragVelocityMin = 0.074f;
            }
        };

        nemesisBullet = new LightningLinkerBulletType() {
            {
                effectLightningChance = 0.15f;
                damage = 1200;
                backColor = trailColor = lightColor = lightningColor = hitColor = Color.valueOf("#75FFB0");
                size = 10f;
                frontColor = JBColor.thurmixRedLight;
                range = 1200f;
                spreadEffect = Fx.none;

                trailWidth = 8f;
                trailLength = 20;

                speed = 8f;

                linkRange = 280f;

                maxHit = 12;
                drag = 0.0065f;
                hitSound = Sounds.explosion;
                splashDamageRadius = 90f;
                splashDamage = lightningDamage = damage / 3f;
                lifetime = 230f;
                despawnEffect = JBFx.lightningHitLarge(hitColor);
                hitEffect = JBFx.sharpBlast(hitColor, frontColor, 35, splashDamageRadius * 1.25f);
                shootEffect = JBFx.hitSpark(backColor, 45f, 12, 60, 3, 8);
                smokeEffect = JBFx.hugeSmoke;
            }
        };

        maelstrom = new LightningLinkerBulletType() {
            {
                effectLightningChance = 0.15f;
                damage = 300;
                backColor = trailColor = lightColor = lightningColor = hitColor = Color.valueOf("#CC27F5");
                size = 10f;
                frontColor = JBColor.thurmixRedLight;
                range = 120f;
                spreadEffect = Fx.none;

                trailWidth = 8f;
                trailLength = 20;

                speed = 6f;

                linkRange = 280f;

                maxHit = 12;
                drag = 0.0065f;
                hitSound = Sounds.explosion;
                splashDamageRadius = 90f;
                splashDamage = lightningDamage = damage / 3f;
                lifetime = 90f;
                despawnEffect = JBFx.lightningHitLarge(hitColor);
                hitEffect = JBFx.sharpBlast(hitColor, frontColor, 35, splashDamageRadius * 1.25f);
                shootEffect = JBFx.hitSpark(backColor, 45f, 12, 60, 3, 8);
                smokeEffect = JBFx.hugeSmoke;
            }
        };

    }
}
