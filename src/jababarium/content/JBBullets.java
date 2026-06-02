package jababarium.content;

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

public class JBBullets {

    public static String STRIKE = "missile";

    public static String MINE_BULLET = "mine-bullet";

    public static BulletType burst, singularityPoint, entropyBolt, transgression, chronosShell, chronosField, apexShell,
            apexShard, apexMicro, abbys, Orb, laserBeam, plasma, plasmaBeam, lightSupport, crimson, crimsonLance,
            crimsonLanceHeavy,
            voidPlasma, guidedVoidMissile, voidCollapse, lightSupport2, crimsonChain, crimsonBeam, laserBeam2,
            crimsonNova, absoluteZero, guidedZeroMissile, azureStream, frostCascade, frostShotgun, crimsonVortex,
            infernoCore,
            guidedCrimsonLance, absoluteInferno, nemesisBullet, verdantCollapse, verdantBeamBurst, tinyShell,
            empressVenom, empressWebCluster, empressFangBarrage, laserBurn, thunderShot, pulseWave,
            photosynthesisBullet,
            verdantLightningWeb, verdantBeam, crossSpinLaser, verdantApex, maelstrom, quantarBullet, ionPulseBullet,
            plasmaBoltBullet,
            hyperBeamBullet, voidLanceBullet, missileStrike, tidebreakerLaser, condensedBolt, tidebreakerStd,
            basicSkyFrag, tideLightning, LightningRed,
            rift, arcBolt, theridionBolt, chargedCannonBolt, tideLaser, tideBall, deathBeam, tideLightningRed,
            collapseShell, ancientBall, oraxiaBullet,
            oraxiaLaser, broodmotherDeathBeam, supernovaLaser, supernovaCore,
            supernovaArtillery, repeater, gammaReaper;

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

        Orb = new BasicBulletType(7f, 160) {
            {
                width = 20f;
                height = 20f;
                sprite = "circle-bullet";
                lifetime = 35f;

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("a98cff");
                frontColor = Color.valueOf("6ec6ff");
                lightColor = Color.valueOf("a98cff");
                lightOpacity = 0.8f;

                trailLength = 10;
                trailWidth = 3f;
                trailColor = Color.valueOf("a98cff");
                trailInterval = 2f;
                trailEffect = new Effect(30f, e -> {
                    Draw.color(Color.valueOf("a98cff"));
                    Draw.alpha(e.fout() * 0.6f);
                    circle(e.x, e.y, e.fout() * 3f);
                });

                trailChance = 0.3f;

                hitEffect = new Effect(35f, e -> {
                    Draw.color(Color.valueOf("a98cff"), Color.valueOf("6ec6ff"), e.fin());

                    Lines.stroke(5f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 40f);

                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 25f);

                    Draw.alpha(e.fout());
                    circle(e.x, e.y, 15f * e.fout());

                    for (int i = 0; i < 6; i++) {
                        float angle = i * 60f;
                        Tmp.v1.trns(angle, e.finpow() * 30f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 6f);
                    }
                });

                despawnHit = true;
                shootEffect = Fx.shootBig;
                smokeEffect = new Effect(20f, e -> {
                    Draw.color(Color.valueOf("6ec6ff"));
                    Draw.alpha(e.fout() * 0.5f);
                    circle(e.x, e.y, e.fout() * 4f);
                });

                lightRadius = 45f;

                status = StatusEffects.shocked;
                statusDuration = 60f;
            }
        };

        laserBeam = new RailBulletType() {
            {
                lifetime = 1f;
                damage = 650f;
                length = 220f;
                pierce = true;
                pierceBuilding = true;
                pierceDamageFactor = 1f;
                pointEffectSpace = 10f;

                collidesTiles = true;
                collidesAir = true;
                collidesGround = true;

                hitEffect = new Effect(25f, e -> {
                    Draw.color(Color.valueOf("ff2020"), Color.valueOf("ffffff"), e.fin());

                    float refractAngle = e.rotation + 180f + Mathf.range(40f, 60f);
                    float refractLength = 30f * e.finpow();

                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(3f * e.fout());
                    Lines.lineAngle(e.x, e.y, refractAngle, refractLength);

                    Draw.color(Color.valueOf("ff6060"));
                    Draw.alpha(e.fout() * 0.8f);
                    Lines.stroke(2f * e.fout());
                    Lines.lineAngle(e.x, e.y, refractAngle, refractLength);

                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    Lines.stroke(1f * e.fout());
                    Lines.lineAngle(e.x, e.y, refractAngle, refractLength);

                    Tmp.v1.trns(refractAngle, refractLength);
                    Draw.color(Color.valueOf("ff2020"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 3f * e.fout());

                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 1.5f * e.fout());

                    Draw.color(Color.valueOf("ffffff"));
                    Draw.alpha(e.fout());
                    circle(e.x, e.y, 4f * e.fout());

                    Draw.color(Color.valueOf("ff2020"));
                    Draw.alpha(e.fout() * 0.6f);
                    circle(e.x, e.y, 6f * e.fout());

                    Lines.stroke(1f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 12f);
                });

                despawnEffect = Fx.none;

                shootEffect = new Effect(25f, 200f, e -> {
                    Draw.color(Color.valueOf("ff2020"));
                    Draw.alpha(e.fout() * 0.7f);

                    Lines.stroke(4f * e.fout());
                    Lines.lineAngle(e.x, e.y, e.rotation, 200f, false);

                    Draw.color(Color.valueOf("ff6060"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.lineAngle(e.x, e.y, e.rotation, 200f, false);

                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    Lines.stroke(1f * e.fout());
                    Lines.lineAngle(e.x, e.y, e.rotation, 200f, false);

                    for (int i = 0; i < 8; i++) {
                        float dst = Mathf.random(200f);
                        Tmp.v1.trns(e.rotation, dst);

                        Draw.color(Color.valueOf("ff2020"), Color.valueOf("ffffff"), Mathf.random());
                        Draw.alpha(e.fout() * 0.6f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, Mathf.random(1f, 2.5f) * e.fout());
                    }
                });

                smokeEffect = Fx.none;

                lightColor = Color.valueOf("ff4444");
                lightOpacity = 0.7f;
                lightRadius = 60f;

                status = StatusEffects.melting;
                statusDuration = 60f;

                pierce = true;
                pierceCap = 2;
                pierceBuilding = true;

                hitSize = 4f;
            }
        };

        plasma = new BasicBulletType(6f, 850) {
            {
                lifetime = 55f;

                width = 18f;
                height = 28f;
                sprite = "circle-bullet";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("4a9eff");
                frontColor = Color.valueOf("d0f4ff");
                lightColor = Color.valueOf("6eb5ff");
                lightOpacity = 0.9f;
                lightRadius = 65f;

                trailLength = 25;
                trailWidth = 4.5f;
                trailColor = Color.valueOf("4a9eff");
                trailInterval = 1.2f;

                trailEffect = new Effect(35f, e -> {
                    Draw.color(Color.valueOf("4a9eff"), Color.valueOf("d0f4ff"), e.fin());
                    Draw.alpha(e.fout() * 0.85f);

                    circle(e.x, e.y, e.fout() * 5f);

                    Lines.stroke(1.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fout() * 7f);

                    for (int i = 0; i < 4; i++) {
                        float angle = e.rotation + 90f * i + e.fin() * 45f;
                        Tmp.v1.trns(angle, e.fout() * 4f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 1.5f);
                    }
                });

                trailChance = 0.6f;

                hitEffect = new Effect(50f, e -> {
                    Draw.color(Color.valueOf("4a9eff"), Color.valueOf("d0f4ff"), e.fin());

                    Draw.alpha(e.fout() * 0.9f);
                    circle(e.x, e.y, 22f * e.fout());

                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    circle(e.x, e.y, 12f * e.fout());

                    Draw.color(Color.valueOf("4a9eff"));
                    Lines.stroke(6f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 60f);

                    Lines.stroke(4f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 40f);

                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 80f);

                    for (int i = 0; i < 12; i++) {
                        float angle = i * 30f + e.fin() * 120f;
                        float length = e.finpow() * 45f;

                        Draw.color(Color.valueOf("4a9eff"));
                        Draw.alpha(e.fout() * 0.7f);
                        Lines.stroke(3f * e.fout());
                        Lines.lineAngle(e.x, e.y, angle, length);

                        Draw.color(Color.valueOf("d0f4ff"));
                        Lines.stroke(1.5f * e.fout());
                        Lines.lineAngle(e.x, e.y, angle, length);

                        Tmp.v1.trns(angle, length);
                        Draw.color(Color.valueOf("4a9eff"));
                        Draw.alpha(e.fout());
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 5f);

                        Draw.color(Color.white);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 2.5f);
                    }

                    for (int i = 0; i < 8; i++) {
                        float angle = i * 45f;
                        float dst = e.fin() * 50f;
                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("6eb5ff"), Color.valueOf("d0f4ff"), e.fin());
                        Draw.alpha(e.fout() * 0.8f);

                        float size = e.fout() * 4f;
                        Fill.poly(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 4, size, 45f);
                    }

                    Draw.color(Color.white);
                    Draw.alpha(Mathf.curve(e.fin(), 0f, 0.2f) * e.fout());
                    circle(e.x, e.y, 28f * Mathf.curve(e.fin(), 0f, 0.15f));
                });

                despawnHit = true;

                shootEffect = new Effect(30f, e -> {
                    Draw.color(Color.valueOf("4a9eff"), Color.valueOf("d0f4ff"), e.fin());

                    for (int i = 0; i < 6; i++) {
                        float angle = e.rotation + i * 60f + e.fin() * 360f;
                        Tmp.v1.trns(angle, e.finpow() * 25f);

                        Draw.alpha(e.fout() * 0.7f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 3.5f);

                        Lines.stroke(2f * e.fout());
                        Lines.lineAngle(e.x, e.y, angle, e.finpow() * 18f);
                    }

                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    circle(e.x, e.y, e.fout() * 12f);
                });

                smokeEffect = new Effect(35f, e -> {
                    Draw.color(Color.valueOf("4a9eff"), Color.valueOf("d0f4ff"), e.fin());
                    Draw.alpha(e.fout() * 0.6f);

                    circle(e.x, e.y, e.fout() * 8f);

                    Lines.stroke(1.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 12f);
                });

                splashDamageRadius = 50f;
                splashDamage = damage * 0.7f;

                knockback = 8f;
                hitShake = 5f;

                status = StatusEffects.freezing;
                statusDuration = 120f;

                pierce = true;
                pierceCap = 2;
                pierceBuilding = true;

                homingPower = 0.12f;
                homingRange = 120f;

                hitSound = JBSounds.blast;
            }
        };

        plasmaBeam = new ContinuousFlameBulletType(340) {
            {
                shake = 3;
                hitColor = flareColor = lightColor = lightningColor = Color.valueOf("4a9eff");

                colors = new Color[] {
                        Color.valueOf("2a5f9f").a(0.65f),
                        Color.valueOf("4a9eff").a(0.75f),
                        Color.valueOf("6eb5ff").a(0.85f),
                        Color.valueOf("d0f4ff")
                };

                width = 6;
                length = 380f;
                oscScl = 0.9f;
                oscMag *= 2f;

                lifetime = 400f;

                lightning = 4;
                lightningLength = 2;
                lightningLengthRand = 18;
                flareLength = 75;
                flareWidth = 6;

                hitEffect = JBFx.shootCircleSmall(Color.valueOf("4a9eff"));
                shootEffect = JBFx.lightningHitLarge(Color.valueOf("4a9eff"));

                lightningDamage = damage / 6f;
                despawnHit = false;
                pierceArmor = true;

                status = StatusEffects.freezing;
                statusDuration = 90f;
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

        lightSupport = new BasicBulletType(8f, 120) {
            {
                lifetime = 65f;

                width = 8f;
                height = 12f;
                sprite = "bullet";

                shrinkY = 0.2f;
                shrinkX = 0f;

                backColor = Color.valueOf("5fa3e0");
                frontColor = Color.valueOf("c8e3ff");
                lightColor = Color.valueOf("7db5ed");
                lightOpacity = 0.7f;
                lightRadius = 35f;

                trailLength = 12;
                trailWidth = 2f;
                trailColor = Color.valueOf("5fa3e0");
                trailInterval = 2f;

                trailEffect = new Effect(20f, e -> {
                    Draw.color(Color.valueOf("5fa3e0"), Color.valueOf("c8e3ff"), e.fin());
                    Draw.alpha(e.fout() * 0.6f);

                    circle(e.x, e.y, e.fout() * 2.5f);
                });

                trailChance = 0.4f;

                hitEffect = new Effect(25f, e -> {
                    Draw.color(Color.valueOf("5fa3e0"), Color.valueOf("c8e3ff"), e.fin());

                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, 10f * e.fout());

                    Draw.color(Color.white);
                    Draw.alpha(e.fout() * 0.5f);
                    circle(e.x, e.y, 6f * e.fout());

                    Draw.color(Color.valueOf("5fa3e0"));
                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 20f);

                    for (int i = 0; i < 4; i++) {
                        float angle = i * 90f;
                        Lines.stroke(1.5f * e.fout());
                        Lines.lineAngle(e.x, e.y, angle, e.finpow() * 12f);
                    }

                    for (int i = 0; i < 4; i++) {
                        float angle = i * 90f + 45f;
                        Tmp.v1.trns(angle, e.fin() * 10f);

                        Draw.alpha(e.fout() * 0.5f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 2f);
                    }
                });

                despawnHit = true;

                shootEffect = new Effect(15f, e -> {
                    Draw.color(Color.valueOf("5fa3e0"), Color.valueOf("c8e3ff"), e.fin());
                    Draw.alpha(e.fout() * 0.6f);

                    circle(e.x, e.y, e.fout() * 5f);

                    for (int i = 0; i < 2; i++) {
                        float angle = e.rotation + Mathf.range(10f);
                        Lines.stroke(1f * e.fout());
                        Lines.lineAngle(e.x, e.y, angle, e.finpow() * 8f);
                    }
                });

                smokeEffect = new Effect(12f, e -> {
                    Draw.color(Color.valueOf("7db5ed"));
                    Draw.alpha(e.fout() * 0.4f);
                    circle(e.x, e.y, e.fout() * 3f);
                });

                knockback = 1.5f;
                hitShake = 1f;

                splashDamageRadius = 15f;
                splashDamage = damage * 0.4f;

                hitSound = Sounds.explosion;
            }
        };

        crimson = new BasicBulletType(5.5f, 780) {
            {
                lifetime = 55f;

                width = 24f;
                height = 24f;
                sprite = "circle-bullet";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("ff2222");
                frontColor = Color.valueOf("ffeeee");
                lightColor = Color.valueOf("ff3333");
                lightOpacity = 1f;
                lightRadius = 85f;

                trailLength = 25;
                trailWidth = 7f;
                trailColor = Color.valueOf("ff2222");
                trailInterval = 1f;

                trailEffect = new Effect(45f, e -> {

                    Draw.color(Color.valueOf("ff2222"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.fout() * 10f);

                    Draw.color(Color.valueOf("ff6644"));
                    circle(e.x, e.y, e.fout() * 7f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 4f);

                    Draw.color(Color.valueOf("ff4444"));
                    Draw.alpha(e.fout() * 0.8f);
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fout() * 12f);

                    Lines.stroke(1.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 8f);

                    for (int i = 0; i < 3; i++) {
                        float angle = i * 120f + e.fin() * 360f;
                        Lines.stroke(1.5f * e.fout());
                        Lines.arc(e.x, e.y, e.fout() * 9f, 0.3f, angle);
                    }
                });

                trailChance = 0.8f;

                hitEffect = new Effect(70f, e -> {

                    Draw.color(Color.valueOf("cc0000"));
                    Draw.alpha(e.fout() * 0.4f);
                    circle(e.x, e.y, e.fin() * 80f);

                    Lines.stroke(10f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 70f);

                    Draw.color(Color.valueOf("ff4444"));
                    Lines.stroke(6f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 85f);

                    Draw.color(Color.valueOf("cc0000"));
                    Draw.alpha(e.fout() * 0.7f);
                    Lines.stroke(4f * e.fout());
                    for (int i = 0; i < 6; i++) {
                        float angle = i * 60f;
                        float angle2 = (i + 1) * 60f;
                        float rad = e.finpow() * 60f;

                        Tmp.v1.trns(angle, rad);
                        Tmp.v2.trns(angle2, rad);

                        Lines.line(
                                e.x + Tmp.v1.x, e.y + Tmp.v1.y,
                                e.x + Tmp.v2.x, e.y + Tmp.v2.y);
                    }

                    for (int i = 0; i < 6; i++) {
                        float angle = i * 60f;
                        float distance = e.finpow() * 70f;

                        Tmp.v1.trns(angle, distance);

                        Draw.color(Color.valueOf("ff4444"));
                        Draw.alpha(e.fout());
                        Draw.rect("block-4", e.x + Tmp.v1.x, e.y + Tmp.v1.y,
                                8f * e.fout(), 16f * e.fout(), angle + 90f);

                        Draw.color(Color.valueOf("cc0000"));
                        Draw.alpha(e.fout() * 0.6f);
                        Lines.stroke(3f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y,
                                angle + 180f, e.fin() * 20f);
                    }

                    Draw.color(Color.valueOf("880000"));
                    Draw.alpha(e.fout() * 0.8f);
                    circle(e.x, e.y, 25f * e.fout());

                    Draw.color(Color.valueOf("440000"));
                    circle(e.x, e.y, 15f * e.fout());

                    Draw.color(Color.valueOf("ff6666"));
                    Draw.alpha(e.fout() * 0.5f);
                    Lines.stroke(8f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 95f);

                    for (int i = 0; i < 24; i++) {
                        float angle = Mathf.random(360f);
                        float dst = Mathf.random(30f, e.fin() * 75f);
                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("ff4444"), Color.valueOf("cc0000"), Mathf.random());
                        Draw.alpha(e.fout() * Mathf.random(0.4f, 0.8f));
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y,
                                Mathf.random(2f, 5f) * e.fout());
                    }

                    if (e.fin() < 0.3f) {
                        Draw.color(Color.white);
                        Draw.alpha((1f - e.fin() / 0.3f) * 0.9f);
                        circle(e.x, e.y, 40f * (1f - e.fin() / 0.3f));
                    }
                });

                despawnHit = true;

                shootEffect = new Effect(30f, e -> {

                    Draw.color(Color.valueOf("ff2222"), Color.valueOf("ff6644"), e.fin());

                    for (int i = 0; i < 12; i++) {
                        float angle = e.rotation + i * 30f + e.fin() * 720f;
                        float distance = (1f - e.fin()) * 35f;

                        Tmp.v1.trns(angle, distance);

                        Draw.alpha(e.fin() * 0.9f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fin() * 5f);

                        Draw.color(Color.valueOf("ffaa77"));
                        Lines.stroke(2f * e.fin());
                        Lines.lineAngle(e.x, e.y, angle, distance * 1.2f);
                    }

                    Draw.color(Color.valueOf("ff4444"));
                    Draw.alpha(e.fin() * 0.9f);
                    circle(e.x, e.y, e.fin() * 18f);

                    Draw.color(Color.valueOf("ffaa77"));
                    circle(e.x, e.y, e.fin() * 12f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fin() * 7f);

                    Lines.stroke(3f * e.fin());
                    Lines.circle(e.x, e.y, e.finpow() * 25f);
                });

                smokeEffect = new Effect(50f, e -> {
                    Draw.color(Color.valueOf("ff4444"));
                    Draw.alpha(e.fout() * 0.7f);

                    circle(e.x, e.y, e.fout() * 12f);

                    Draw.color(Color.valueOf("ff2222"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 18f);

                    for (int i = 0; i < 4; i++) {
                        float angle = Mathf.random(360f);
                        Tmp.v1.trns(angle, e.fin() * 10f);

                        Draw.alpha(e.fout() * 0.6f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 2f);
                    }
                });

                splashDamageRadius = 75f;
                splashDamage = damage * 0.85f;

                fragBullets = 6;
                fragBullet = new BasicBulletType(4f, 80) {
                    {
                        width = 8f;
                        height = 12f;
                        lifetime = 25f;

                        backColor = Color.valueOf("ff2222");
                        frontColor = Color.valueOf("ffeeee");
                        lightColor = Color.valueOf("ff4444");
                        lightRadius = 35f;

                        trailLength = 8;
                        trailWidth = 1.5f;
                        trailColor = Color.valueOf("ff2222");

                        splashDamageRadius = 20f;
                        splashDamage = 40f;

                        hitEffect = new Effect(20f, e -> {
                            Draw.color(Color.valueOf("ff2222"));
                            Draw.alpha(e.fout());
                            circle(e.x, e.y, e.fout() * 8f);

                            Draw.color(Color.white);
                            circle(e.x, e.y, e.fout() * 4f);

                            Lines.stroke(2f * e.fout());
                            Lines.circle(e.x, e.y, e.fin() * 15f);
                        });
                    }
                };
                fragVelocityMin = 0.8f;
                fragVelocityMax = 1.2f;
                fragLifeMin = 0.8f;
                fragLifeMax = 1.2f;

                knockback = 12f;
                hitShake = 8f;

                status = StatusEffects.burning;
                statusDuration = 240f;

            }
        };

        crimsonLance = new BasicBulletType(9f, 145) {
            {
                lifetime = 45f;

                width = 10f;
                height = 28f;
                sprite = "missile-large";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("ff2222");
                frontColor = Color.valueOf("ffffff");
                lightColor = Color.valueOf("ff3333");
                lightOpacity = 1f;
                lightRadius = 65f;

                trailLength = 18;
                trailWidth = 4.5f;
                trailColor = Color.valueOf("ff3333");
                trailInterval = 1.2f;

                trailEffect = new Effect(35f, e -> {
                    Draw.color(Color.valueOf("ff3333"));
                    Draw.alpha(e.fout() * 0.8f);
                    circle(e.x, e.y, e.fout() * 6f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 3.5f);

                    Draw.color(Color.valueOf("ff4444"));
                    Draw.alpha(e.fout() * 0.7f);
                    Lines.stroke(1.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fout() * 8f);
                });

                trailChance = 0.6f;

                hitEffect = new Effect(50f, 140f, e -> {
                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 15f);

                    Draw.color(Color.valueOf("ff3333"), Color.white, e.fin() * 0.5f);

                    e.scaled(22f, s -> {
                        Lines.stroke(4f * s.fout());
                        Lines.circle(e.x, e.y, 5f + s.fin(Interp.pow3Out) * 45f);
                    });

                    e.scaled(35f, s -> {
                        Lines.stroke(2.5f * s.fout());
                        Lines.circle(e.x, e.y, 8f + s.fin(Interp.pow2Out) * 65f);
                    });

                    Draw.color(Color.white, Color.valueOf("ff4444"), e.fin() + 0.25f);
                    Lines.stroke(2.2f * e.fout());

                    Angles.randLenVectors(e.id, 16, 10f + 50f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, angle, e.fout() * (10f + Mathf.random(10f)));
                    });

                    Draw.color(Color.valueOf("ff6666"), Color.white, e.fout() * 0.7f);
                    Lines.stroke(1.2f * e.fout());

                    Angles.randLenVectors(e.id + 1, 12, 6f + 35f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 14f);
                    });

                    Angles.randLenVectors(e.id + 2, 10, 4f + 28f * e.finpow(), (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("ff3333"), e.fin() * 0.8f);
                        circle(e.x + x, e.y + y, e.fout() * 4f);
                    });

                    Draw.color(Color.valueOf("ff2222"));
                    Angles.randLenVectors(e.id + 3, 8, 20f + 45f * e.fin(), (x, y) -> {
                        Lines.stroke(1.5f * e.fout());
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 16f);
                    });

                    Drawf.light(e.x, e.y, e.fout() * 85f, Color.valueOf("ff3333"), 0.85f);

                    for (int i = 0; i < 6; i++) {
                        float angle = i * 60f + e.fin() * 30f;
                        float dst = e.finpow() * 55f;

                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("ff4444"), Color.white, e.fout() * 0.6f);
                        Lines.stroke(1.8f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle + 90f, e.fout() * 12f);
                    }
                });

                despawnHit = true;

                shootEffect = new Effect(25f, e -> {
                    Draw.color(Color.white, Color.valueOf("ff3333"), e.fin() * 0.7f);

                    Lines.stroke(e.fout() * 3f);
                    Angles.randLenVectors(e.id, 10, 30f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 8f + 3f);
                    });

                    circle(e.x, e.y, e.fout() * 8f);

                    Draw.color(Color.valueOf("ff4444"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 18f);

                    Drawf.light(e.x, e.y, e.fout() * 45f, Color.valueOf("ff3333"), 0.7f);
                });

                smokeEffect = new Effect(30f, e -> {
                    Draw.color(Color.valueOf("ff3333"));
                    Draw.alpha(e.fout() * 0.6f);

                    circle(e.x, e.y, e.fout() * 5f);

                    Lines.stroke(1.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 12f);
                });

                pierce = true;
                pierceCap = 2;
                pierceBuilding = false;

                splashDamageRadius = 25f;
                splashDamage = damage * 0.6f;

                knockback = 2f;
                hitShake = 2.5f;

                status = StatusEffects.burning;
                statusDuration = 120f;

                homingPower = 0.08f;
                homingRange = 80f;
            }
        };

        crimsonLanceHeavy = new BasicBulletType(7.5f, 780) {
            {
                lifetime = 55f;

                width = 14f;
                height = 36f;
                sprite = "missile-large";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("ff1111");
                frontColor = Color.valueOf("ffffff");
                lightColor = Color.valueOf("ff2222");
                lightOpacity = 1f;
                lightRadius = 90f;

                trailLength = 24;
                trailWidth = 6f;
                trailColor = Color.valueOf("ff2222");
                trailInterval = 1f;

                trailEffect = new Effect(45f, e -> {
                    Draw.color(Color.valueOf("ff2222"));
                    Draw.alpha(e.fout() * 0.8f);
                    circle(e.x, e.y, e.fout() * 9f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 5f);

                    Draw.color(Color.valueOf("ff4444"));
                    Draw.alpha(e.fout() * 0.8f);
                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, e.fout() * 11f);

                    Lines.stroke(1.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 8f);

                    for (int i = 0; i < 3; i++) {
                        float angle = i * 120f + e.fin() * 360f;
                        Lines.stroke(1.5f * e.fout());
                        Lines.arc(e.x, e.y, e.fout() * 10f, 0.3f, angle);
                    }
                });

                trailChance = 0.8f;

                hitEffect = new Effect(65f, 180f, e -> {
                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 22f);

                    Draw.color(Color.valueOf("ff2222"), Color.white, e.fin() * 0.4f);

                    e.scaled(28f, s -> {
                        Lines.stroke(5f * s.fout());
                        Lines.circle(e.x, e.y, 6f + s.fin(Interp.pow3Out) * 60f);
                    });

                    e.scaled(45f, s -> {
                        Lines.stroke(3.5f * s.fout());
                        Lines.circle(e.x, e.y, 10f + s.fin(Interp.pow2Out) * 85f);
                    });

                    e.scaled(55f, s -> {
                        Lines.stroke(2f * s.fout());
                        Lines.circle(e.x, e.y, 12f + s.fin() * 100f);
                    });

                    Draw.color(Color.white, Color.valueOf("ff3333"), e.fin() + 0.2f);
                    Lines.stroke(3f * e.fout());

                    Angles.randLenVectors(e.id, 24, 15f + 70f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, angle, e.fout() * (14f + Mathf.random(14f)));
                    });

                    Draw.color(Color.valueOf("ff5555"), Color.white, e.fout() * 0.6f);
                    Lines.stroke(1.8f * e.fout());

                    Angles.randLenVectors(e.id + 1, 18, 8f + 50f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 20f);
                    });

                    Angles.randLenVectors(e.id + 2, 14, 6f + 40f * e.finpow(), (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("ff2222"), e.fin() * 0.7f);
                        circle(e.x + x, e.y + y, e.fout() * 5.5f);
                    });

                    for (int i = 0; i < 8; i++) {
                        float angle = i * 45f + e.fin() * 25f;
                        float dst = e.finpow() * 75f;

                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("ff3333"), Color.white, e.fout() * 0.5f);
                        Lines.stroke(2.5f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle + 90f, e.fout() * 18f);
                    }

                    Draw.color(Color.valueOf("ff4444"));
                    Lines.stroke(2f * e.fout());
                    for (int i = 0; i < 6; i++) {
                        float angle1 = i * 60f;
                        float angle2 = (i + 1) * 60f;
                        float rad = e.finpow() * 70f;

                        Tmp.v1.trns(angle1, rad);
                        Tmp.v2.trns(angle2, rad);

                        Lines.line(
                                e.x + Tmp.v1.x, e.y + Tmp.v1.y,
                                e.x + Tmp.v2.x, e.y + Tmp.v2.y);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 120f, Color.valueOf("ff2222"), 0.9f);
                });

                despawnHit = true;

                shootEffect = new Effect(32f, e -> {
                    Draw.color(Color.white, Color.valueOf("ff2222"), e.fin() * 0.6f);

                    Lines.stroke(e.fout() * 4f);
                    Angles.randLenVectors(e.id, 14, 40f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 12f + 4f);
                    });

                    circle(e.x, e.y, e.fout() * 11f);

                    Draw.color(Color.valueOf("ff3333"));
                    Lines.stroke(3.5f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 25f);

                    Drawf.light(e.x, e.y, e.fout() * 60f, Color.valueOf("ff2222"), 0.8f);
                });

                smokeEffect = new Effect(40f, e -> {
                    Draw.color(Color.valueOf("ff2222"));
                    Draw.alpha(e.fout() * 0.7f);

                    circle(e.x, e.y, e.fout() * 8f);

                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 16f);
                });

                pierce = true;
                pierceCap = 3;
                pierceBuilding = true;

                splashDamageRadius = 45f;
                splashDamage = damage * 0.75f;

                knockback = 4f;
                hitShake = 4f;

                status = StatusEffects.burning;
                statusDuration = 180f;

                homingPower = 0.12f;
                homingRange = 120f;
            }
        };

        voidPlasma = new BasicBulletType(11f, 1020) {
            {
                lifetime = 65f;

                width = 18f;
                height = 42f;
                sprite = "missile-large";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("8b4fc4");
                frontColor = Color.valueOf("f3e5ff");
                lightColor = Color.valueOf("b47dff");
                lightOpacity = 1f;
                lightRadius = 110f;

                trailLength = 32;
                trailWidth = 8f;
                trailColor = Color.valueOf("9f5ed4");
                trailInterval = 0.8f;

                trailEffect = new Effect(60f, e -> {

                    Draw.color(Color.valueOf("8b4fc4"));
                    Draw.alpha(e.fout() * 0.9f);
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 4f, 3f)) * e.fout());

                    Draw.color(Color.valueOf("d896ff"));
                    circle(e.x, e.y, (8f + Mathf.absin(e.time, 4f, 2f)) * e.fout());

                    Draw.color(Color.white);
                    circle(e.x, e.y, (5f + Mathf.absin(e.time, 4f, 1.5f)) * e.fout());

                    Draw.color(Color.valueOf("b47dff"));
                    Draw.alpha(e.fout() * 0.85f);

                    for (int i = 0; i < 3; i++) {
                        float rot = e.time * (1.5f + i * 0.5f);
                        Lines.stroke(2.5f * e.fout());
                        Lines.arc(e.x, e.y, (10f + i * 3f) * e.fout(), 0.4f, rot * 60f);
                    }

                    Angles.randLenVectors(e.id, 6, (8f + e.fin() * 4f) * e.fout(), (x, y) -> {
                        Draw.color(Color.valueOf("d896ff"), Color.white, Mathf.random(0.3f, 0.8f));
                        circle(e.x + x, e.y + y, e.fout() * 2.5f);
                    });

                    for (int i = 0; i < 4; i++) {
                        float angle = e.fin() * 720f + i * 90f;
                        float rad = e.fout() * 14f;

                        Tmp.v1.trns(angle, rad);

                        Draw.color(Color.valueOf("f3e5ff"));
                        Draw.alpha(e.fout() * 0.7f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 2f);
                    }
                });

                trailChance = 0.85f;

                hitEffect = new Effect(90f, 250f, e -> {

                    Draw.color(Color.white);
                    circle(e.x, e.y, (28f + Mathf.absin(e.time, 3f, 4f)) * e.fout());

                    Draw.color(Color.valueOf("8b4fc4"), Color.valueOf("f3e5ff"), e.fin() * 0.6f);

                    e.scaled(30f, s -> {
                        Lines.stroke(7f * s.fout());
                        Lines.circle(e.x, e.y, 8f + s.fin(Interp.pow3Out) * 90f);

                        Lines.stroke(4f * s.fout());
                        Lines.circle(e.x, e.y, 12f + s.fin(Interp.pow2Out) * 70f);
                    });

                    e.scaled(50f, s -> {
                        Lines.stroke(5f * s.fout());
                        Lines.circle(e.x, e.y, 15f + s.fin(Interp.pow2Out) * 120f);
                    });

                    e.scaled(70f, s -> {
                        Lines.stroke(3f * s.fout());
                        Lines.circle(e.x, e.y, 20f + s.fin() * 140f);
                    });

                    Draw.color(Color.white, Color.valueOf("b47dff"), e.fin() + 0.3f);
                    Lines.stroke(3.5f * e.fout());

                    for (int i = 0; i < 12; i++) {
                        float angle = i * 30f + e.fin() * 45f;
                        float len = e.finpow() * (90f + Mathf.random(20f));

                        Tmp.v1.trns(angle, len);
                        Lines.lineAngle(e.x, e.y, angle, len * e.fout());
                    }

                    Draw.color(Color.valueOf("d896ff"), Color.white, e.fout() * 0.7f);
                    Lines.stroke(2.5f * e.fout());

                    Angles.randLenVectors(e.id, 24, 20f + 80f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, angle, e.fout() * (15f + Mathf.random(18f)));

                        Tmp.v1.set(x, y);
                        Draw.color(Color.white, Color.valueOf("f3e5ff"), e.fin());
                        circle(e.x + x, e.y + y, e.fout() * 3.5f);
                    });

                    Draw.color(Color.valueOf("b47dff"), Color.white, e.fout() * 0.6f);
                    Lines.stroke(1.8f * e.fout());

                    Angles.randLenVectors(e.id + 1, 20, 12f + 65f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 25f);
                    });

                    Angles.randLenVectors(e.id + 2, 18, 8f + 55f * e.finpow(), (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("8b4fc4"), e.fin() * 0.8f);
                        circle(e.x + x, e.y + y, e.fout() * 7f);

                        Draw.color(Color.valueOf("b47dff"));
                        Draw.alpha(e.fout() * 0.5f);
                        circle(e.x + x, e.y + y, e.fout() * 10f);
                    });

                    Draw.color(Color.valueOf("8b4fc4"), Color.valueOf("d896ff"), e.fout());
                    Lines.stroke(3f * e.fout());
                    for (int i = 0; i < 8; i++) {
                        float angle1 = i * 45f;
                        float angle2 = (i + 1) * 45f;
                        float rad = e.finpow() * 95f;

                        Tmp.v1.trns(angle1, rad);
                        Tmp.v2.trns(angle2, rad);

                        Lines.line(
                                e.x + Tmp.v1.x, e.y + Tmp.v1.y,
                                e.x + Tmp.v2.x, e.y + Tmp.v2.y);
                    }

                    for (int i = 0; i < 12; i++) {
                        float angle = i * 30f + e.fin() * 40f;
                        float dst = e.finpow() * 100f;

                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("b47dff"), Color.white, e.fout() * 0.6f);
                        Lines.stroke(3f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle + 90f, e.fout() * 25f);

                        Draw.color(Color.white);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 4f);
                    }

                    Draw.color(Color.valueOf("d896ff"));
                    Lines.stroke(2.5f * e.fout());
                    for (int i = 0; i < 4; i++) {
                        float rot = e.fin() * 360f * (i % 2 == 0 ? 1 : -1);
                        float radius = (40f + i * 20f) * e.fin();

                        Lines.arc(e.x, e.y, radius, 0.5f, rot);
                    }

                    Draw.color(Color.valueOf("5a2f8f"));
                    Draw.alpha(e.fout() * 0.9f);
                    circle(e.x, e.y, (20f + Mathf.absin(e.time, 3f, 5f)) * e.fout());

                    Draw.color(Color.valueOf("8b4fc4"));
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 3f, 3f)) * e.fout());

                    Drawf.light(e.x, e.y, e.fout() * 180f, Color.valueOf("b47dff"), 0.95f);

                    Angles.randLenVectors(e.id + 3, 30, 30f + 90f * e.fin(), (x, y) -> {
                        float angle = Mathf.angle(x, y);

                        Draw.color(Color.valueOf("f3e5ff"), Color.valueOf("8b4fc4"), Mathf.random());
                        Draw.alpha(e.fout() * 0.7f);

                        for (int i = 0; i < 3; i++) {
                            float offset = i * 3f;
                            Tmp.v1.trns(angle, offset);
                            circle(e.x + x + Tmp.v1.x, e.y + y + Tmp.v1.y,
                                    e.fout() * (2f - i * 0.5f));
                        }
                    });
                });

                despawnHit = true;

                shootEffect = new Effect(40f, e -> {

                    Draw.color(Color.valueOf("5a2f8f"));
                    Draw.alpha(e.fin() * 0.8f);
                    circle(e.x, e.y, e.fin() * 20f);

                    Draw.color(Color.valueOf("8b4fc4"), Color.white, e.fin() * 0.7f);

                    for (int i = 0; i < 4; i++) {
                        float rot = e.time * (2f + i * 0.8f) * (i % 2 == 0 ? 1 : -1);
                        Lines.stroke((3f - i * 0.5f) * e.fin());
                        Lines.arc(e.x, e.y, (12f + i * 6f) * e.fin(), 0.6f, rot * 60f);
                    }

                    Lines.stroke(e.fout() * 4f);
                    Angles.randLenVectors(e.id, 16, 45f * e.finpow(), (x, y) -> {
                        Draw.color(Color.valueOf("d896ff"), Color.white, e.fout() * 0.8f);
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 14f + 5f);
                    });

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 12f);

                    Draw.color(Color.valueOf("f3e5ff"));
                    circle(e.x, e.y, e.fout() * 8f);

                    Draw.color(Color.valueOf("b47dff"));
                    Lines.stroke(4f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 30f);

                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin(Interp.pow2Out) * 40f);

                    for (int i = 0; i < 12; i++) {
                        float angle = i * 30f;
                        float distance = (1f - e.fin()) * 50f;

                        Tmp.v1.trns(angle, distance);

                        Draw.color(Color.valueOf("d896ff"), Color.white, e.fin() * 0.7f);
                        Draw.alpha(e.fin() * 0.9f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fin() * 4f);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 70f, Color.valueOf("b47dff"), 0.8f);
                });

                smokeEffect = new Effect(50f, e -> {
                    Draw.color(Color.valueOf("8b4fc4"));
                    Draw.alpha(e.fout() * 0.8f);

                    circle(e.x, e.y, e.fout() * 10f);

                    Draw.color(Color.valueOf("d896ff"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 20f);

                    Angles.randLenVectors(e.id, 6, e.fin() * 15f, (x, y) -> {
                        Draw.color(Color.valueOf("f3e5ff"), Color.valueOf("8b4fc4"), Mathf.random());
                        Draw.alpha(e.fout() * 0.7f);
                        circle(e.x + x, e.y + y, e.fout() * 3f);
                    });
                });

                pierce = true;
                pierceCap = 4;
                pierceBuilding = true;

                splashDamageRadius = 70f;
                splashDamage = damage * 0.85f;

                knockback = 8f;
                hitShake = 6f;

                status = StatusEffects.shocked;
                statusDuration = 180f;

                homingPower = 0.15f;
                homingRange = 160f;

                fragOnHit = false;
                fragBullets = 0;
            }
        };

        guidedVoidMissile = new BasicBulletType(7.5f, 580) {
            {
                lifetime = 150f;

                width = 12f;
                height = 32f;
                sprite = "missile-large";

                shrinkY = 0.1f;
                shrinkX = 0f;

                backColor = Color.valueOf("8b4fc4");
                frontColor = Color.valueOf("e8d5ff");
                lightColor = Color.valueOf("b47dff");
                lightOpacity = 0.9f;
                lightRadius = 75f;

                trailLength = 22;
                trailWidth = 4.5f;
                trailColor = Color.valueOf("9f5ed4");
                trailInterval = 1.5f;

                trailEffect = new Effect(45f, e -> {

                    Draw.color(Color.valueOf("8b4fc4"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.fout() * 7f);

                    Draw.color(Color.valueOf("b47dff"));
                    circle(e.x, e.y, e.fout() * 4.5f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 2f);

                    Draw.color(Color.valueOf("9f5ed4"));
                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(1.8f * e.fout());
                    Lines.circle(e.x, e.y, e.fout() * 9f);

                    Angles.randLenVectors(e.id, 4, e.fin() * 6f, (x, y) -> {
                        Draw.color(Color.valueOf("e8d5ff"), Color.valueOf("8b4fc4"), Mathf.random());
                        circle(e.x + x, e.y + y, e.fout() * 1.8f);
                    });
                });

                trailChance = 0.7f;

                hitEffect = new Effect(60f, 150f, e -> {

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 18f);

                    Draw.color(Color.valueOf("8b4fc4"), Color.valueOf("e8d5ff"), e.fin() * 0.6f);

                    e.scaled(25f, s -> {
                        Lines.stroke(5f * s.fout());
                        Lines.circle(e.x, e.y, 6f + s.fin(Interp.pow3Out) * 60f);
                    });

                    e.scaled(40f, s -> {
                        Lines.stroke(3f * s.fout());
                        Lines.circle(e.x, e.y, 10f + s.fin(Interp.pow2Out) * 80f);
                    });

                    Draw.color(Color.valueOf("b47dff"), Color.white, e.fout() * 0.7f);
                    Lines.stroke(2.5f * e.fout());

                    Angles.randLenVectors(e.id, 18, 15f + 60f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, angle, e.fout() * (12f + Mathf.random(10f)));
                    });

                    Angles.randLenVectors(e.id + 1, 14, 8f + 45f * e.finpow(), (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("8b4fc4"), e.fin() * 0.8f);
                        circle(e.x + x, e.y + y, e.fout() * 5f);
                    });

                    for (int i = 0; i < 6; i++) {
                        float angle = i * 60f + e.fin() * 30f;
                        float dst = e.finpow() * 65f;

                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("b47dff"), Color.white, e.fout() * 0.6f);
                        Lines.stroke(2.2f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle + 90f, e.fout() * 16f);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 100f, Color.valueOf("b47dff"), 0.85f);
                });

                despawnHit = true;

                shootEffect = new Effect(30f, e -> {
                    Draw.color(Color.white, Color.valueOf("8b4fc4"), e.fin() * 0.7f);

                    Lines.stroke(e.fout() * 3.5f);
                    Angles.randLenVectors(e.id, 12, 35f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 10f + 4f);
                    });

                    circle(e.x, e.y, e.fout() * 9f);

                    Draw.color(Color.valueOf("b47dff"));
                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 22f);

                    Drawf.light(e.x, e.y, e.fout() * 50f, Color.valueOf("b47dff"), 0.7f);
                });

                smokeEffect = new Effect(40f, e -> {
                    Draw.color(Color.valueOf("8b4fc4"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.fout() * 6f);

                    Draw.color(Color.valueOf("9f5ed4"));
                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 14f);
                });

                pierce = false;
                pierceBuilding = false;

                splashDamageRadius = 50f;
                splashDamage = damage * 0.75f;

                knockback = 5f;
                hitShake = 4f;

                status = StatusEffects.shocked;
                statusDuration = 150f;

                homingPower = 0.12f;
                homingRange = 285f;
                homingDelay = 5f;

                trailRotation = true;
                despawnHit = true;

                weaveMag = 2f;
                weaveScale = 4f;
            }
        };

        voidCollapse = new BasicBulletType(5f, 720) {
            {
                lifetime = 125f;

                width = 28f;
                height = 28f;
                sprite = "circle-bullet";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("2a1540");
                frontColor = Color.valueOf("e8d5ff");
                lightColor = Color.valueOf("9955ee");
                lightOpacity = 1f;
                lightRadius = 130f;

                trailLength = 25;
                trailWidth = 7f;
                trailColor = Color.valueOf("4a2866");
                trailInterval = 1.8f;

                trailEffect = new Effect(65f, e -> {
                    Draw.color(Color.valueOf("2a1540"));
                    Draw.alpha(e.fout() * 0.9f);
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 2.5f, 3f)) * e.fout());

                    Draw.color(Color.valueOf("9955ee"));
                    circle(e.x, e.y, (8f + Mathf.absin(e.time, 2.5f, 2f)) * e.fout());

                    Draw.color(Color.white);
                    circle(e.x, e.y, (4f + Mathf.absin(e.time, 2.5f, 1f)) * e.fout());

                    Draw.color(Color.valueOf("aa77ff"));
                    Draw.alpha(e.fout() * 0.8f);

                    float timeVal = e.time;
                    float foutVal = e.fout();
                    float xVal = e.x;
                    float yVal = e.y;

                    for (int i = 0; i < 4; i++) {
                        final int fi = i;
                        float rot = timeVal * (1.5f + fi * 0.5f) * (fi % 2 == 0 ? 1 : -1);
                        Lines.stroke((2.5f - fi * 0.4f) * foutVal);

                        for (int j = 0; j < 20; j++) {
                            float angle1 = j * 18f + rot;
                            float angle2 = (j + 1) * 18f + rot;

                            float rad = (10f + fi * 3f) * foutVal;
                            float distortion = Mathf.sin(angle1 * 4f + timeVal * 2.5f) * 2f;

                            Tmp.v1.trns(angle1, rad + distortion);
                            Tmp.v2.trns(angle2, rad + distortion);

                            Lines.line(
                                    xVal + Tmp.v1.x, yVal + Tmp.v1.y,
                                    xVal + Tmp.v2.x, yVal + Tmp.v2.y);
                        }
                    }
                });

                trailChance = 0.85f;

                hitEffect = new Effect(130f, 350f, e -> {

                    Draw.color(Color.valueOf("0d0520"));
                    circle(e.x, e.y, (55f + Mathf.absin(e.time, 1.8f, 12f)) * e.fout());

                    Draw.color(Color.valueOf("2a1540"));
                    circle(e.x, e.y, (42f + Mathf.absin(e.time, 1.8f, 9f)) * e.fout());

                    Draw.color(Color.valueOf("9955ee"));
                    circle(e.x, e.y, (28f + Mathf.absin(e.time, 1.8f, 6f)) * e.fout());

                    Draw.color(Color.white);
                    circle(e.x, e.y, (14f + Mathf.absin(e.time, 1.8f, 3f)) * e.fout());

                    Draw.color(Color.valueOf("9955ee"), Color.valueOf("e8d5ff"), e.fin() * 0.5f);

                    float exVal = e.x;
                    float eyVal = e.y;

                    for (int s = 0; s < 6; s++) {
                        final int fs = s;
                        e.scaled(30f + fs * 18f, spiral -> {
                            Lines.stroke((6f - fs) * spiral.fout());

                            for (int i = 0; i < 32; i++) {
                                float angle = i * 11.25f + spiral.fin() * 450f * (fs % 2 == 0 ? 1 : -1);
                                float len = spiral.finpow() * (80f + fs * 25f);

                                Tmp.v1.trns(angle, len);
                                Lines.lineAngle(exVal, eyVal, angle, len * spiral.fout());
                            }
                        });
                    }

                    Angles.randLenVectors(e.id, 35, 20f + 120f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);

                        Draw.color(Color.valueOf("e8d5ff"), Color.valueOf("2a1540"), e.fin() * 0.6f);
                        Draw.alpha(e.fout() * 0.9f);

                        for (int i = 0; i < 6; i++) {
                            float off = i * 60f;
                            Tmp.v1.trns(angle + off, e.fout() * 6f);
                            circle(e.x + x + Tmp.v1.x, e.y + y + Tmp.v1.y, e.fout() * 5f);
                        }
                    });

                    for (int i = 0; i < 16; i++) {
                        float angle = i * 22.5f + e.fin() * 80f;
                        float dst = e.finpow() * 140f;

                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("2a1540"));
                        Draw.alpha(e.fout() * 0.8f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 14f);

                        Draw.color(Color.valueOf("9955ee"));
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 9f);

                        Draw.color(Color.white);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 4f);

                        Draw.color(Color.valueOf("aa77ff"));
                        Draw.alpha(e.fout() * 0.6f);
                        Lines.stroke(3f * e.fout());
                        Lines.line(e.x, e.y, e.x + Tmp.v1.x, e.y + Tmp.v1.y);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 260f, Color.valueOf("9955ee"), 1f);
                });

                despawnHit = true;

                shootEffect = new Effect(55f, e -> {
                    Draw.color(Color.valueOf("0d0520"));
                    Draw.alpha(e.fin() * 0.95f);
                    circle(e.x, e.y, e.fin() * 35f);

                    Draw.color(Color.valueOf("2a1540"), Color.white, e.fin() * 0.4f);

                    for (int i = 0; i < 7; i++) {
                        final int fi = i;
                        float delay = fi * 0.12f;
                        if (e.fin() > delay) {
                            float progress = (e.fin() - delay) / (1f - delay);

                            Lines.stroke((5f - fi * 0.7f) * Mathf.curve(progress, 0f, 0.4f));
                            Lines.circle(e.x, e.y, (14f + fi * 8f) * progress);
                        }
                    }

                    float finVal = e.fin();
                    float xVal = e.x;
                    float yVal = e.y;
                    Angles.randLenVectors(e.id, 28, 65f * (1f - finVal), (x, y) -> {
                        Draw.color(Color.valueOf("e8d5ff"), Color.valueOf("9955ee"), finVal);
                        Draw.alpha(finVal * 0.95f);
                        circle(xVal + x * (1f - finVal), yVal + y * (1f - finVal), finVal * 5f);
                    });

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 20f);

                    Drawf.light(e.x, e.y, e.fout() * 100f, Color.valueOf("9955ee"), 0.9f);
                });

                smokeEffect = new Effect(70f, e -> {
                    Draw.color(Color.valueOf("2a1540"));
                    Draw.alpha(e.fout() * 0.85f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float timeVal = e.time;
                    float foutVal = e.fout();

                    for (int i = 0; i < 4; i++) {
                        final int fi = i;
                        float angle = timeVal * (1.2f + fi * 0.4f) * (fi % 2 == 0 ? 1 : -1);
                        Tmp.v1.trns(angle * 60f, (5f + fi * 2.5f) * foutVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, foutVal * (11f - fi * 2f));
                    }
                });

                pierce = true;
                pierceCap = 5;
                pierceBuilding = true;

                splashDamageRadius = 85f;
                splashDamage = damage * 0.85f;

                knockback = 10f;
                hitShake = 8f;

                status = StatusEffects.slow;
                statusDuration = 240f;

                homingPower = 0.08f;
                homingRange = 120f;

                despawnHit = true;
            }
        };

        lightSupport2 = new BasicBulletType(8f, 120) {
            {
                lifetime = 85f;

                width = 8f;
                height = 12f;
                sprite = "bullet";

                shrinkY = 0.2f;
                shrinkX = 0f;

                backColor = Color.valueOf("7a4cff");
                frontColor = Color.valueOf("e3ccff");
                lightColor = Color.valueOf("b07cff");
                lightOpacity = 0.7f;
                lightRadius = 35f;

                trailLength = 12;
                trailWidth = 2f;
                trailColor = Color.valueOf("7a4cff");
                trailInterval = 2f;

                trailEffect = new Effect(20f, e -> {
                    Draw.color(Color.valueOf("7a4cff"), Color.valueOf("e3ccff"), e.fin());
                    Draw.alpha(e.fout() * 0.6f);

                    circle(e.x, e.y, e.fout() * 2.5f);
                });

                trailChance = 0.4f;

                hitEffect = new Effect(25f, e -> {
                    Draw.color(Color.valueOf("7a4cff"), Color.valueOf("e3ccff"), e.fin());

                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, 10f * e.fout());

                    Draw.color(Color.white);
                    Draw.alpha(e.fout() * 0.5f);
                    circle(e.x, e.y, 6f * e.fout());

                    Draw.color(Color.valueOf("7a4cff"));
                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 20f);

                    for (int i = 0; i < 4; i++) {
                        float angle = i * 90f;
                        Lines.stroke(1.5f * e.fout());
                        Lines.lineAngle(e.x, e.y, angle, e.finpow() * 12f);
                    }

                    for (int i = 0; i < 4; i++) {
                        float angle = i * 90f + 45f;
                        Tmp.v1.trns(angle, e.fin() * 10f);

                        Draw.alpha(e.fout() * 0.5f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 2f);
                    }
                });

                despawnHit = true;

                shootEffect = new Effect(15f, e -> {
                    Draw.color(Color.valueOf("7a4cff"), Color.valueOf("e3ccff"), e.fin());
                    Draw.alpha(e.fout() * 0.6f);

                    circle(e.x, e.y, e.fout() * 5f);

                    for (int i = 0; i < 2; i++) {
                        float angle = e.rotation + Mathf.range(10f);
                        Lines.stroke(1f * e.fout());
                        Lines.lineAngle(e.x, e.y, angle, e.finpow() * 8f);
                    }
                });

                smokeEffect = new Effect(12f, e -> {
                    Draw.color(Color.valueOf("b07cff"));
                    Draw.alpha(e.fout() * 0.4f);
                    circle(e.x, e.y, e.fout() * 3f);
                });

                knockback = 1.5f;
                hitShake = 1f;

                splashDamageRadius = 15f;
                splashDamage = damage * 0.4f;

                hitSound = Sounds.explosion;
            }
        };

        crimsonChain = new BasicBulletType(8f, 750) {
            {
                lifetime = 85f;

                width = 16f;
                height = 38f;
                sprite = "missile-large";

                shrinkY = 0.05f;
                shrinkX = 0f;

                backColor = Color.valueOf("ff1a1a");
                frontColor = Color.valueOf("ffdddd");
                lightColor = Color.valueOf("ff3333");
                lightOpacity = 1f;
                lightRadius = 120f;

                trailLength = 26;
                trailWidth = 7f;
                trailColor = Color.valueOf("ff2a2a");
                trailInterval = 1.2f;

                trailEffect = new Effect(50f, e -> {

                    Draw.color(Color.valueOf("ff1a1a"));
                    Draw.alpha(e.fout() * 0.9f);
                    circle(e.x, e.y, (10f + Mathf.absin(e.time, 2f, 2.5f)) * e.fout());

                    Draw.color(Color.valueOf("ff4444"));
                    circle(e.x, e.y, (7f + Mathf.absin(e.time, 2f, 1.8f)) * e.fout());

                    Draw.color(Color.valueOf("ffcccc"));
                    circle(e.x, e.y, (4f + Mathf.absin(e.time, 2f, 1f)) * e.fout());

                    float xVal = e.x;
                    float yVal = e.y;
                    float foutVal = e.fout();
                    float finVal = e.fin();

                    Angles.randLenVectors(e.id, 6, 2f + finVal * 12f, (x, y) -> {
                        Draw.color(Color.valueOf("ff6644"), Color.valueOf("ff1a1a"), Mathf.random(0.3f, 0.9f));
                        circle(xVal + x, yVal + y, foutVal * 2.5f);
                    });

                    Draw.color(Color.valueOf("993333"));
                    Draw.alpha(e.fout() * 0.5f);
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 2f, 3f)) * e.fout());
                });

                trailChance = 0.8f;

                hitEffect = new Effect(80f, 220f, e -> {

                    Draw.color(Color.valueOf("ffcc44"));
                    circle(e.x, e.y, (35f + Mathf.absin(e.time, 2f, 7f)) * e.fout());

                    Draw.color(Color.valueOf("ff6622"));
                    circle(e.x, e.y, (28f + Mathf.absin(e.time, 2f, 5f)) * e.fout());

                    Draw.color(Color.valueOf("ff2222"));
                    circle(e.x, e.y, (20f + Mathf.absin(e.time, 2f, 4f)) * e.fout());

                    Draw.color(Color.white);
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 2f, 2f)) * e.fout());

                    Draw.color(Color.valueOf("ff4444"), Color.valueOf("ffaa66"), e.fin() * 0.7f);

                    float exVal = e.x;
                    float eyVal = e.y;

                    for (int s = 0; s < 4; s++) {
                        final int fs = s;
                        e.scaled(20f + fs * 12f, wave -> {
                            Lines.stroke((6f - fs * 1.2f) * wave.fout());
                            Lines.circle(exVal, eyVal, 10f + wave.fin(Interp.pow3Out) * (70f + fs * 15f));
                        });
                    }

                    Draw.color(Color.white, Color.valueOf("ff6622"), e.fin() + 0.4f);
                    Lines.stroke(4f * e.fout());

                    for (int i = 0; i < 16; i++) {
                        final int fi = i;
                        float angle = fi * 22.5f + e.fin() * 60f;
                        float len = e.finpow() * (85f + Mathf.random(25f));

                        Tmp.v1.trns(angle, len);
                        Lines.lineAngle(e.x, e.y, angle, len * e.fout());

                        Draw.color(Color.valueOf("ffaa33"));
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 6f);
                    }

                    Draw.color(Color.valueOf("ff6622"), Color.white, e.fout() * 0.8f);
                    Lines.stroke(3f * e.fout());

                    float foutVal = e.fout();
                    float finpowVal = e.finpow();

                    Angles.randLenVectors(e.id, 28, 18f + 75f * finpowVal, (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(exVal + x, eyVal + y, angle, foutVal * (14f + Mathf.random(12f)));
                    });

                    float finVal = e.fin();

                    Angles.randLenVectors(e.id + 1, 20, 10f + 65f * finpowVal, (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("ff2222"), finVal * 0.75f);
                        circle(exVal + x, eyVal + y, foutVal * 8f);

                        Draw.color(Color.valueOf("ff8833"));
                        Draw.alpha(foutVal * 0.6f);
                        circle(exVal + x, eyVal + y, foutVal * 12f);
                    });

                    for (int i = 0; i < 12; i++) {
                        final int fi = i;
                        float angle = fi * 30f + e.fin() * 120f;
                        float dst = e.finpow() * 95f;

                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("ff4444"));
                        Draw.alpha(e.fout() * 0.8f);

                        for (int j = 0; j < 4; j++) {
                            final int fj = j;
                            float spiralAngle = angle + fj * 90f + e.fin() * 360f;
                            float spiralDst = e.fout() * (8f + fj * 2f);

                            Tmp.v2.trns(spiralAngle, spiralDst);
                            circle(e.x + Tmp.v1.x + Tmp.v2.x, e.y + Tmp.v1.y + Tmp.v2.y, e.fout() * 4f);
                        }
                    }

                    if (e.fin() > 0.3f && e.fin() < 0.7f) {
                        float chainProgress = (e.fin() - 0.3f) / 0.4f;

                        for (int i = 0; i < 8; i++) {
                            final int fi = i;
                            float chainAngle = fi * 45f;
                            float chainDst = 60f + chainProgress * 40f;

                            Tmp.v1.trns(chainAngle, chainDst);

                            Draw.color(Color.valueOf("ff6622"));
                            Draw.alpha((1f - chainProgress) * 0.9f);
                            circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, (1f - chainProgress) * 15f);

                            Draw.color(Color.white);
                            circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, (1f - chainProgress) * 8f);
                        }
                    }

                    Draw.color(Color.valueOf("774444"));
                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(5f * e.fout());
                    for (int i = 0; i < 6; i++) {
                        final int fi = i;
                        float angle1 = fi * 60f + e.fin() * 45f;
                        float angle2 = (fi + 1) * 60f + e.fin() * 45f;
                        float rad = e.finpow() * 85f;

                        Tmp.v1.trns(angle1, rad);
                        Tmp.v2.trns(angle2, rad);

                        Lines.line(
                                e.x + Tmp.v1.x, e.y + Tmp.v1.y,
                                e.x + Tmp.v2.x, e.y + Tmp.v2.y);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 180f, Color.valueOf("ff4444"), 0.95f);
                });

                despawnHit = true;

                shootEffect = new Effect(35f, e -> {

                    Draw.color(Color.valueOf("ff2222"));
                    Draw.alpha(e.fin() * 0.9f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float finVal = e.fin();

                    for (int i = 0; i < 3; i++) {
                        final int fi = i;
                        float rot = e.time * (2f + fi * 0.8f);
                        Tmp.v1.trns(rot * 60f, (8f + fi * 4f) * finVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, finVal * (12f - fi * 2f));
                    }

                    Draw.color(Color.white, Color.valueOf("ff6622"), e.fin() * 0.6f);

                    Lines.stroke(e.fout() * 4f);
                    Angles.randLenVectors(e.id, 16, 40f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(xVal + x, yVal + y, Mathf.angle(x, y), e.fslope() * 12f + 5f);
                    });

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 16f);

                    Draw.color(Color.valueOf("ffaa33"));
                    circle(e.x, e.y, e.fout() * 12f);

                    Draw.color(Color.valueOf("ff4444"));
                    Lines.stroke(4f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 28f);

                    Drawf.light(e.x, e.y, e.fout() * 70f, Color.valueOf("ff4444"), 0.8f);
                });

                smokeEffect = new Effect(55f, e -> {
                    Draw.color(Color.valueOf("994444"));
                    Draw.alpha(e.fout() * 0.8f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float timeVal = e.time;
                    float foutVal = e.fout();

                    for (int i = 0; i < 3; i++) {
                        final int fi = i;
                        float angle = timeVal * (1.5f + fi * 0.5f);
                        Tmp.v1.trns(angle * 50f, (6f + fi * 3f) * foutVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, foutVal * (10f - fi * 2f));
                    }

                    Draw.color(Color.valueOf("ff6622"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 20f);
                });

                pierce = true;
                pierceCap = 4;
                pierceBuilding = true;

                splashDamageRadius = 75f;
                splashDamage = damage * 0.85f;

                status = StatusEffects.burning;
                statusDuration = 240f;

                knockback = 6f;
                hitShake = 5.5f;

                homingPower = 0.1f;
                homingRange = 140f;

                despawnHit = true;

                fragOnHit = true;
                fragBullets = 16;
                fragBullet = new BasicBulletType(5f, 65) {
                    {
                        width = 8f;
                        height = 12f;
                        lifetime = 22f;

                        backColor = Color.valueOf("ff1a1a");
                        frontColor = Color.valueOf("ffdddd");
                        lightColor = Color.valueOf("ff3333");
                        lightRadius = 30f;

                        trailLength = 6;
                        trailWidth = 2f;
                        trailColor = Color.valueOf("ff2a2a");

                        splashDamageRadius = 18f;
                        splashDamage = 35f;

                        status = StatusEffects.burning;
                        statusDuration = 90f;

                        hitEffect = new Effect(20f, e -> {
                            Draw.color(Color.valueOf("ff3333"));
                            circle(e.x, e.y, e.fout() * 8f);

                            Draw.color(Color.valueOf("ffcccc"));
                            circle(e.x, e.y, e.fout() * 4f);

                            Lines.stroke(2f * e.fout());
                            Lines.circle(e.x, e.y, e.fin() * 18f);
                        });
                    }
                };
                fragVelocityMin = 0.9f;
                fragVelocityMax = 1.5f;
            }
        };

        crimsonBeam = new ContinuousFlameBulletType(550) {
            {
                shake = 5;
                hitColor = lightColor = lightningColor = Color.valueOf("ff1a1a");

                colors = new Color[] {
                        Color.valueOf("aa0000").a(0.55f),
                        Color.valueOf("ff1a1a").a(0.7f),
                        Color.valueOf("ff3333").a(0.8f),
                        Color.valueOf("ff6666").a(0.9f),
                        Color.valueOf("ffcccc")
                };

                width = 9;
                length = 500f;
                oscScl = 1.2f;
                oscMag *= 2.5f;

                lifetime = 700f;

                lightning = 6;
                lightningLength = 3;
                lightningLengthRand = 22;
                flareLength = 95;
                flareWidth = 9;

                hitEffect = new Effect(15f, e -> {
                    Draw.color(Color.valueOf("ff3333"), Color.valueOf("ffcccc"), e.fin());
                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 12f);

                    Drawf.light(e.x, e.y, e.fout() * 25f, Color.valueOf("ff1a1a"), 0.7f);
                });

                shootEffect = JBFx.lightningHitLarge(Color.valueOf("ff1a1a"));

                lightningDamage = damage / 5f;
                despawnHit = false;
                pierceArmor = true;

                status = StatusEffects.burning;
                statusDuration = 180f;
            }

            @Override
            public void update(Bullet b) {
                super.update(b);

                if (Mathf.chanceDelta(0.15))
                    for (int i = 0; i < lightning; i++) {
                        Lightning.create(b, lightningColor, lightningDamage < 0 ? damage : lightningDamage, b.x, b.y,
                                b.rotation() + Mathf.range(lightningCone / 2) + lightningAngle,
                                lightningLength + Mathf.random(lightningLengthRand));
                    }

                if (Mathf.chanceDelta(0.25)) {
                    float len = Mathf.random(length);
                    Tmp.v1.trns(b.rotation(), len);

                    Effect flareEffect = new Effect(20f, e -> {
                        Draw.color(Color.valueOf("ff3333"), Color.valueOf("ffcccc"), e.fin());
                        Draw.alpha(e.fout() * 0.8f);
                        circle(e.x, e.y, e.fout() * 8f);

                        Draw.color(Color.valueOf("ff1a1a"));
                        Draw.alpha(e.fout() * 0.6f);
                        circle(e.x, e.y, e.fout() * 12f);
                    });

                    flareEffect.at(b.x + Tmp.v1.x, b.y + Tmp.v1.y);
                }
            }

            @Override
            public void hit(Bullet b, float x, float y) {
                hitEffect.at(x, y, b.rotation(), hitColor);
                hitSound.at(x, y, hitSoundPitch, hitSoundVolume);

                Effect.shake(hitShake, hitShake, b);

                for (int i = 0; i < 2; i++) {
                    Lightning.create(b, lightningColor, lightningDamage < 0 ? damage : lightningDamage, x, y,
                            b.rotation() + Mathf.range(lightningCone / 2) + lightningAngle,
                            lightningLength + Mathf.random(lightningLengthRand));
                }

                Effect impactFlare = new Effect(35f, e -> {
                    Draw.color(Color.valueOf("ff1a1a"), Color.valueOf("ffcccc"), e.fin() * 0.6f);

                    Lines.stroke(4f * e.fout());
                    Lines.circle(x, y, e.fin(Interp.pow2Out) * 35f);

                    circle(x, y, e.fout() * 15f);

                    Draw.color(Color.valueOf("ff6666"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(x, y, e.fin(Interp.pow3Out) * 50f);

                    Drawf.light(x, y, e.fout() * 60f, Color.valueOf("ff1a1a"), 0.7f);
                });

                impactFlare.at(x, y);
            }
        };

        laserBeam2 = new RailBulletType() {
            {
                lifetime = 1f;
                damage = 220f;
                length = 650f;
                pierce = true;
                pierceBuilding = true;
                pierceDamageFactor = 1f;
                pointEffectSpace = 10f;

                collidesTiles = true;
                collidesAir = true;
                collidesGround = true;

                hitEffect = new Effect(25f, e -> {
                    Draw.color(Color.valueOf("ff2020"), Color.valueOf("ffffff"), e.fin());

                    float refractAngle = e.rotation + 180f + Mathf.range(40f, 60f);
                    float refractLength = 30f * e.finpow();

                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(3f * e.fout());
                    Lines.lineAngle(e.x, e.y, refractAngle, refractLength);

                    Draw.color(Color.valueOf("ff6060"));
                    Draw.alpha(e.fout() * 0.8f);
                    Lines.stroke(2f * e.fout());
                    Lines.lineAngle(e.x, e.y, refractAngle, refractLength);

                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    Lines.stroke(1f * e.fout());
                    Lines.lineAngle(e.x, e.y, refractAngle, refractLength);

                    Tmp.v1.trns(refractAngle, refractLength);
                    Draw.color(Color.valueOf("ff2020"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 3f * e.fout());

                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 1.5f * e.fout());

                    Draw.color(Color.valueOf("ffffff"));
                    Draw.alpha(e.fout());
                    circle(e.x, e.y, 4f * e.fout());

                    Draw.color(Color.valueOf("ff2020"));
                    Draw.alpha(e.fout() * 0.6f);
                    circle(e.x, e.y, 6f * e.fout());

                    Lines.stroke(1f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 12f);
                });

                despawnEffect = Fx.none;

                shootEffect = new Effect(25f, 630f, e -> {
                    Draw.color(Color.valueOf("ff2020"));
                    Draw.alpha(e.fout() * 0.7f);

                    Lines.stroke(4f * e.fout());
                    Lines.lineAngle(e.x, e.y, e.rotation, 630f, false);

                    Draw.color(Color.valueOf("ff6060"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.lineAngle(e.x, e.y, e.rotation, 630f, false);

                    Draw.color(Color.white);
                    Draw.alpha(e.fout());
                    Lines.stroke(1f * e.fout());
                    Lines.lineAngle(e.x, e.y, e.rotation, 630f, false);

                    for (int i = 0; i < 8; i++) {
                        float dst = Mathf.random(630f);
                        Tmp.v1.trns(e.rotation, dst);

                        Draw.color(Color.valueOf("ff2020"), Color.valueOf("ffffff"), Mathf.random());
                        Draw.alpha(e.fout() * 0.6f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, Mathf.random(1f, 2.5f) * e.fout());
                    }
                });

                smokeEffect = Fx.none;

                lightColor = Color.valueOf("ff4444");
                lightOpacity = 0.7f;
                lightRadius = 60f;

                status = StatusEffects.melting;
                statusDuration = 160f;

                pierce = true;
                pierceCap = 2;
                pierceBuilding = true;

                hitSize = 4f;
            }
        };

        crimsonNova = new BasicBulletType(8f, 720) {
            {
                lifetime = 50f;

                width = 18f;
                height = 18f;
                sprite = "large-bomb";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("990000");
                frontColor = Color.valueOf("ffdddd");
                lightColor = Color.valueOf("ff2222");
                lightOpacity = 1f;
                lightRadius = 130f;

                trailLength = 18;
                trailWidth = 6f;
                trailColor = Color.valueOf("cc1111");
                trailInterval = 2.5f;

                trailEffect = new Effect(60f, e -> {
                    Draw.color(Color.valueOf("cc0000"));
                    Draw.alpha(e.fout() * 0.6f);
                    circle(e.x, e.y, (18f + Mathf.absin(e.time, 2f, 4f)) * e.fout());

                    Draw.color(Color.valueOf("ff2222"));
                    Draw.alpha(e.fout() * 0.75f);
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 2f, 3f)) * e.fout());

                    Draw.color(Color.valueOf("ff6666"));
                    circle(e.x, e.y, (8f + Mathf.absin(e.time, 2f, 2f)) * e.fout());

                    Draw.color(Color.valueOf("ffcccc"));
                    circle(e.x, e.y, (4f + Mathf.absin(e.time, 2f, 1f)) * e.fout());

                    Draw.color(Color.valueOf("ff3333"));
                    Draw.alpha(e.fout() * 0.7f);
                    Lines.stroke(2.5f * e.fout());

                    for (int i = 0; i < 3; i++) {
                        final int fi = i;
                        float radius = (10f + fi * 5f) * e.fout();
                        Lines.circle(e.x, e.y, radius + Mathf.absin(e.time + fi, 2f, 2f));
                    }
                });

                trailChance = 0.7f;

                hitEffect = new Effect(120f, 400f, e -> {

                    Draw.color(Color.valueOf("880000"));
                    Draw.alpha(e.fout() * 0.4f);
                    circle(e.x, e.y, e.finpow() * 180f);

                    Draw.color(Color.valueOf("cc0000"));
                    Draw.alpha(e.fout() * 0.5f);
                    circle(e.x, e.y, e.finpow() * 150f);

                    Draw.color(Color.valueOf("ff1a1a"));
                    Draw.alpha(e.fout() * 0.6f);
                    circle(e.x, e.y, e.finpow() * 120f);

                    Draw.color(Color.valueOf("ffeeee"));
                    circle(e.x, e.y, (45f + Mathf.absin(e.time, 1.5f, 8f)) * e.fout());

                    Draw.color(Color.valueOf("ff6666"));
                    circle(e.x, e.y, (35f + Mathf.absin(e.time, 1.5f, 6f)) * e.fout());

                    Draw.color(Color.valueOf("ff2222"));
                    circle(e.x, e.y, (25f + Mathf.absin(e.time, 1.5f, 4f)) * e.fout());

                    Draw.color(Color.valueOf("ff3333"), Color.valueOf("ffcccc"), e.fin() * 0.7f);

                    float exVal = e.x;
                    float eyVal = e.y;

                    for (int w = 0; w < 8; w++) {
                        final int fw = w;
                        e.scaled(15f + fw * 10f, wave -> {
                            Lines.stroke((8f - fw * 0.8f) * wave.fout());
                            float radius = 20f + wave.fin(Interp.pow2Out) * (100f + fw * 15f);
                            Lines.circle(exVal, eyVal, radius);
                        });
                    }

                    for (int i = 0; i < 24; i++) {
                        final int fi = i;
                        float angle = fi * 15f + e.fin() * 120f;

                        e.scaled(30f + (fi % 3) * 20f, pulse -> {
                            float len = pulse.finpow() * (80f + Mathf.random(30f));

                            Draw.color(Color.valueOf("ff2222"), Color.valueOf("ffeeee"), pulse.fout() * 0.8f);
                            Lines.stroke(4f * pulse.fout());
                            Lines.lineAngle(exVal, eyVal, angle, len);

                            Tmp.v1.trns(angle, len);
                            Draw.color(Color.valueOf("ff6666"));
                            circle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, pulse.fout() * 8f);
                        });
                    }

                    float foutVal = e.fout();
                    float finpowVal = e.finpow();

                    Angles.randLenVectors(e.id, 30, 25f + 110f * finpowVal, (x, y) -> {
                        Draw.color(Color.valueOf("ff2222"));
                        Draw.alpha(foutVal * 0.8f);
                        circle(exVal + x, eyVal + y, foutVal * 12f);

                        Draw.color(Color.valueOf("ff6666"));
                        Draw.alpha(foutVal * 0.6f);
                        circle(exVal + x, eyVal + y, foutVal * 18f);

                        Draw.color(Color.valueOf("ffcccc"));
                        circle(exVal + x, eyVal + y, foutVal * 6f);
                    });

                    Drawf.light(e.x, e.y, e.fout() * 280f, Color.valueOf("ff2222"), 0.95f);
                });

                despawnHit = true;

                shootEffect = new Effect(50f, e -> {
                    Draw.color(Color.valueOf("cc0000"));
                    Draw.alpha(e.fin() * 0.9f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float finVal = e.fin();
                    float foutVal = e.fout();

                    for (int i = 0; i < 5; i++) {
                        final int fi = i;
                        float rot = e.time * (2.5f + fi * 0.7f);
                        Tmp.v1.trns(rot * 60f, (12f + fi * 5f) * finVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, finVal * (14f - fi * 2f));
                    }

                    Draw.color(Color.valueOf("ff2222"), Color.white, e.fin() * 0.6f);

                    for (int i = 0; i < 6; i++) {
                        final int fi = i;
                        float delay = fi * 0.15f;
                        if (e.fin() > delay) {
                            float progress = (e.fin() - delay) / (1f - delay);

                            Lines.stroke((5f - fi * 0.7f) * (1f - progress));
                            Lines.circle(e.x, e.y, (60f - fi * 8f) * (1f - progress));
                        }
                    }

                    Angles.randLenVectors(e.id, 20, 60f * (1f - finVal), (x, y) -> {
                        Draw.color(Color.valueOf("ff6666"), Color.valueOf("ff1a1a"), finVal);
                        Draw.alpha(finVal * 0.9f);
                        circle(xVal + x * (1f - finVal), yVal + y * (1f - finVal), finVal * 5f);
                    });

                    Draw.color(Color.white);
                    circle(e.x, e.y, foutVal * 20f);

                    Draw.color(Color.valueOf("ffcccc"));
                    circle(e.x, e.y, foutVal * 15f);

                    Drawf.light(e.x, e.y, foutVal * 90f, Color.valueOf("ff2222"), 0.85f);
                });

                smokeEffect = new Effect(65f, e -> {
                    Draw.color(Color.valueOf("993333"));
                    Draw.alpha(e.fout() * 0.7f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float timeVal = e.time;
                    float foutVal = e.fout();

                    for (int i = 0; i < 4; i++) {
                        final int fi = i;
                        float angle = timeVal * (1.8f + fi * 0.6f) * (fi % 2 == 0 ? 1 : -1);
                        Tmp.v1.trns(angle * 50f, (8f + fi * 4f) * foutVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, foutVal * (12f - fi * 2f));
                    }

                    Draw.color(Color.valueOf("ff3333"));
                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 22f);
                });

                pierce = false;
                pierceBuilding = false;

                splashDamageRadius = 110f;
                splashDamage = damage * 0.95f;

                knockback = 9f;
                hitShake = 7f;

                status = StatusEffects.burning;
                statusDuration = 200f;

                homingPower = 0.05f;
                homingRange = 100f;

                despawnHit = true;

                drag = -0.003f;
                weaveMag = 0f;
                weaveScale = 0f;
            }
        };

        absoluteZero = new BasicBulletType(6f, 1990) {
            {
                lifetime = 100f;

                width = 28f;
                height = 28f;
                sprite = "large-bomb";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("0a2f5f");
                frontColor = Color.valueOf("e0f0ff");
                lightColor = Color.valueOf("4488ff");
                lightOpacity = 1f;
                lightRadius = 180f;

                trailLength = 24;
                trailWidth = 8f;
                trailColor = Color.valueOf("2255aa");
                trailInterval = 2f;

                trailEffect = new Effect(65f, e -> {

                    Draw.color(Color.valueOf("0a2f5f"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, (20f + Mathf.absin(e.time, 2f, 4f)) * e.fout());

                    Draw.color(Color.valueOf("2255aa"));
                    Draw.alpha(e.fout() * 0.8f);
                    circle(e.x, e.y, (14f + Mathf.absin(e.time, 2f, 3f)) * e.fout());

                    Draw.color(Color.valueOf("4488ff"));
                    circle(e.x, e.y, (10f + Mathf.absin(e.time, 2f, 2f)) * e.fout());

                    Draw.color(Color.valueOf("c0e0ff"));
                    circle(e.x, e.y, (5f + Mathf.absin(e.time, 2f, 1f)) * e.fout());

                    float xVal = e.x;
                    float yVal = e.y;
                    float foutVal = e.fout();
                    float timeVal = e.time;

                    for (int i = 0; i < 6; i++) {
                        final int fi = i;
                        float angle = fi * 60f + timeVal * 1.5f;
                        float rad = foutVal * (12f + Mathf.absin(timeVal + fi, 2f, 3f));

                        Tmp.v1.trns(angle, rad);

                        Draw.color(Color.valueOf("6699ff"));
                        Draw.alpha(foutVal * 0.8f);

                        for (int j = 0; j < 3; j++) {
                            final int fj = j;
                            float off = fj * 120f;
                            Tmp.v2.trns(angle + off, foutVal * 2f);
                            circle(xVal + Tmp.v1.x + Tmp.v2.x, yVal + Tmp.v1.y + Tmp.v2.y, foutVal * 2.5f);
                        }
                    }

                    Angles.randLenVectors(e.id, 8, 4f + e.fin() * 14f, (x, y) -> {
                        Draw.color(Color.valueOf("88bbff"), Color.valueOf("2255aa"), Mathf.random());
                        circle(xVal + x, yVal + y, foutVal * 2f);
                    });
                });

                trailChance = 0.85f;

                hitEffect = new Effect(140f, 450f, e -> {

                    Draw.color(Color.valueOf("001122"));
                    Draw.alpha(e.fout() * 0.5f);
                    circle(e.x, e.y, e.finpow() * 280f);

                    Draw.color(Color.valueOf("0a2f5f"));
                    Draw.alpha(e.fout() * 0.6f);
                    circle(e.x, e.y, e.finpow() * 240f);

                    Draw.color(Color.valueOf("2255aa"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.finpow() * 200f);

                    Draw.color(Color.valueOf("e0f0ff"));
                    circle(e.x, e.y, (55f + Mathf.absin(e.time, 1.5f, 10f)) * e.fout());

                    Draw.color(Color.valueOf("88bbff"));
                    circle(e.x, e.y, (45f + Mathf.absin(e.time, 1.5f, 8f)) * e.fout());

                    Draw.color(Color.valueOf("4488ff"));
                    circle(e.x, e.y, (35f + Mathf.absin(e.time, 1.5f, 6f)) * e.fout());

                    Draw.color(Color.valueOf("2255aa"));
                    circle(e.x, e.y, (25f + Mathf.absin(e.time, 1.5f, 4f)) * e.fout());

                    Draw.color(Color.valueOf("4488ff"), Color.valueOf("e0f0ff"), e.fin() * 0.7f);

                    float exVal = e.x;
                    float eyVal = e.y;

                    for (int w = 0; w < 10; w++) {
                        final int fw = w;
                        e.scaled(20f + fw * 12f, wave -> {
                            Lines.stroke((10f - fw * 0.9f) * wave.fout());
                            float radius = 25f + wave.fin(Interp.pow2Out) * (130f + fw * 18f);
                            Lines.circle(exVal, eyVal, radius);

                            if (fw % 2 == 0) {
                                for (int s = 0; s < 12; s++) {
                                    float angle = s * 30f;
                                    Tmp.v1.trns(angle, radius);

                                    Draw.color(Color.valueOf("88bbff"));
                                    Lines.stroke(3f * wave.fout());
                                    Lines.lineAngle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, angle, wave.fout() * 12f);
                                }
                            }
                        });
                    }

                    for (int i = 0; i < 36; i++) {
                        final int fi = i;
                        float angle = fi * 10f + e.fin() * 180f;

                        e.scaled(35f + (fi % 4) * 20f, beam -> {
                            float len = beam.finpow() * (100f + Mathf.random(40f));

                            Draw.color(Color.valueOf("4488ff"), Color.valueOf("e0f0ff"), beam.fout() * 0.9f);
                            Lines.stroke(4f * beam.fout());
                            Lines.lineAngle(exVal, eyVal, angle, len);

                            Tmp.v1.trns(angle, len);
                            Draw.color(Color.valueOf("88bbff"));
                            circle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, beam.fout() * 7f);

                            for (int k = 0; k < 3; k++) {
                                final int fk = k;
                                float crystalAngle = angle + fk * 120f;
                                Tmp.v2.trns(crystalAngle, beam.fout() * 6f);
                                circle(exVal + Tmp.v1.x + Tmp.v2.x, eyVal + Tmp.v1.y + Tmp.v2.y, beam.fout() * 3f);
                            }
                        });
                    }

                    float foutVal = e.fout();
                    float finpowVal = e.finpow();

                    Angles.randLenVectors(e.id, 40, 30f + 140f * finpowVal, (x, y) -> {
                        float angle = Mathf.angle(x, y);

                        Draw.color(Color.valueOf("2255aa"));
                        Draw.alpha(foutVal * 0.85f);
                        circle(exVal + x, eyVal + y, foutVal * 14f);

                        Draw.color(Color.valueOf("6699ff"));
                        Draw.alpha(foutVal * 0.7f);
                        circle(exVal + x, eyVal + y, foutVal * 20f);

                        Draw.color(Color.valueOf("c0e0ff"));
                        circle(exVal + x, eyVal + y, foutVal * 7f);

                        for (int i = 0; i < 6; i++) {
                            final int fi = i;
                            float off = fi * 60f;
                            Tmp.v1.trns(angle + off, foutVal * 8f);
                            Draw.color(Color.valueOf("88bbff"));
                            circle(exVal + x + Tmp.v1.x, eyVal + y + Tmp.v1.y, foutVal * 4f);
                        }
                    });

                    for (int r = 0; r < 10; r++) {
                        final int fr = r;
                        float angle = e.time * (2.8f + fr * 0.7f) * (fr % 2 == 0 ? 1 : -1);
                        float radius = e.finpow() * (90f + fr * 22f);

                        Draw.color(Color.valueOf("4488ff"));
                        Draw.alpha(e.fout() * 0.75f);
                        Lines.stroke(4.5f * e.fout());
                        Lines.arc(e.x, e.y, radius, 0.5f, angle * 60f);
                    }

                    Draw.color(Color.valueOf("2255aa"), Color.valueOf("88bbff"), e.fout() * 0.8f);
                    Lines.stroke(5f * e.fout());

                    for (int i = 0; i < 12; i++) {
                        final int fi = i;
                        float angle1 = fi * 30f;
                        float angle2 = (fi + 1) * 30f;
                        float rad = e.finpow() * 150f;

                        Tmp.v1.trns(angle1, rad);
                        Tmp.v2.trns(angle2, rad);

                        Lines.line(
                                e.x + Tmp.v1.x, e.y + Tmp.v1.y,
                                e.x + Tmp.v2.x, e.y + Tmp.v2.y);

                        Draw.color(Color.valueOf("6699ff"));
                        Lines.stroke(3f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle1, e.fout() * 15f);
                    }

                    if (e.fin() < 0.7f) {
                        float particleProgress = e.fin() / 0.7f;

                        for (int i = 0; i < 70; i++) {
                            final int fi = i;
                            float particleAngle = Mathf.random(360f);
                            float particleDst = Mathf.random(particleProgress * 160f);

                            Tmp.v1.trns(particleAngle, particleDst);

                            Draw.color(Color.valueOf("88bbff"), Color.valueOf("2255aa"), Mathf.random());
                            Draw.alpha((1f - particleProgress) * 0.8f);

                            
                            for (int j = 0; j < 4; j++) {
                                final int fj = j;
                                Tmp.v2.trns(particleAngle + fj * 90f, (1f - particleProgress) * 2f);
                                circle(e.x + Tmp.v1.x + Tmp.v2.x, e.y + Tmp.v1.y + Tmp.v2.y,
                                        (1f - particleProgress) * 3f);
                            }
                        }
                    }

                    for (int ring = 0; ring < 16; ring++) {
                        final int fr = ring;
                        float ringAngle = fr * 22.5f + e.fin() * 90f;
                        float ringDst = e.finpow() * 170f;

                        Tmp.v1.trns(ringAngle, ringDst);

                        Draw.color(Color.valueOf("4488ff"));
                        Draw.alpha(e.fout() * 0.85f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 10f);

                        Draw.color(Color.valueOf("c0e0ff"));
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 5f);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 320f, Color.valueOf("4488ff"), 1f);
                });

                despawnHit = true;

                shootEffect = new Effect(55f, e -> {
                    Draw.color(Color.valueOf("0a2f5f"));
                    Draw.alpha(e.fin() * 0.95f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float finVal = e.fin();
                    float foutVal = e.fout();

                    for (int i = 0; i < 7; i++) {
                        final int fi = i;
                        float rot = e.time * (3f + fi);
                        Tmp.v1.trns(rot * 60f, (14f + fi * 6f) * finVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, finVal * (16f - fi * 2f));
                    }

                    Draw.color(Color.valueOf("4488ff"), Color.white, e.fin() * 0.5f);

                    for (int i = 0; i < 9; i++) {
                        final int fi = i;
                        float delay = fi * 0.1f;
                        if (e.fin() > delay) {
                            float progress = (e.fin() - delay) / (1f - delay);

                            Lines.stroke((6f - fi * 0.65f) * (1f - progress));
                            Lines.circle(e.x, e.y, (90f - fi * 9f) * (1f - progress));
                        }
                    }

                    Angles.randLenVectors(e.id, 24, 85f * (1f - finVal), (x, y) -> {
                        Draw.color(Color.valueOf("88bbff"), Color.valueOf("2255aa"), finVal);
                        Draw.alpha(finVal * 0.95f);

                        float angle = Mathf.angle(x, y);
                        for (int i = 0; i < 4; i++) {
                            final int fi = i;
                            Tmp.v1.trns(angle + fi * 90f, finVal * 3f);
                            circle(xVal + x * (1f - finVal) + Tmp.v1.x, yVal + y * (1f - finVal) + Tmp.v1.y,
                                    finVal * 4f);
                        }
                    });

                    Draw.color(Color.white);
                    circle(e.x, e.y, foutVal * 24f);

                    Draw.color(Color.valueOf("c0e0ff"));
                    circle(e.x, e.y, foutVal * 18f);

                    Drawf.light(e.x, e.y, foutVal * 100f, Color.valueOf("4488ff"), 0.9f);
                });

                smokeEffect = new Effect(70f, e -> {
                    Draw.color(Color.valueOf("224466"));
                    Draw.alpha(e.fout() * 0.75f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float timeVal = e.time;
                    float foutVal = e.fout();

                    for (int i = 0; i < 5; i++) {
                        final int fi = i;
                        float angle = timeVal * (2.2f + fi * 0.8f) * (fi % 2 == 0 ? 1 : -1);
                        Tmp.v1.trns(angle * 50f, (10f + fi * 5f) * foutVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, foutVal * (14f - fi * 2f));
                    }

                    Draw.color(Color.valueOf("4488ff"));
                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 24f);
                });

                pierce = true;
                pierceCap = 5;
                pierceBuilding = true;

                splashDamageRadius = 140f;
                splashDamage = damage * 0.95f;

                knockback = 12f;
                hitShake = 9f;

                status = StatusEffects.freezing;
                statusDuration = 300f;

                homingPower = 0.08f;
                homingRange = 180f;

                despawnHit = true;

                drag = -0.005f;
                weaveMag = 0f;
                weaveScale = 0f;

                fragOnHit = true;
                fragBullets = 24;
                fragBullet = new BasicBulletType(6f, 85) {
                    {
                        width = 9f;
                        height = 14f;
                        lifetime = 30f;

                        backColor = Color.valueOf("2255aa");
                        frontColor = Color.valueOf("e0f0ff");
                        lightColor = Color.valueOf("6699ff");
                        lightRadius = 35f;

                        trailLength = 8;
                        trailWidth = 2.5f;
                        trailColor = Color.valueOf("4488ff");

                        splashDamageRadius = 22f;
                        splashDamage = 40f;

                        status = StatusEffects.freezing;
                        statusDuration = 120f;

                        hitEffect = new Effect(25f, e -> {
                            Draw.color(Color.valueOf("4488ff"));
                            circle(e.x, e.y, e.fout() * 10f);

                            Draw.color(Color.valueOf("c0e0ff"));
                            circle(e.x, e.y, e.fout() * 5f);

                            Lines.stroke(2.5f * e.fout());
                            Lines.circle(e.x, e.y, e.fin() * 20f);

                            for (int i = 0; i < 6; i++) {
                                final int fi = i;
                                float angle = fi * 60f;
                                Tmp.v1.trns(angle, e.fin() * 12f);

                                Draw.color(Color.valueOf("88bbff"));
                                circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 2.5f);
                            }
                        });
                    }
                };
                fragVelocityMin = 0.8f;
                fragVelocityMax = 1.6f;
            }
        };

        guidedZeroMissile = new BasicBulletType(8.5f, 680) {
            {
                lifetime = 170f;

                width = 12f;
                height = 32f;
                sprite = "missile-large";

                shrinkY = 0.1f;
                shrinkX = 0f;

                backColor = Color.valueOf("2b6cff");
                frontColor = Color.valueOf("a8d8ff");
                lightColor = Color.valueOf("5ecbff");
                lightOpacity = 0.9f;
                lightRadius = 75f;

                trailLength = 22;
                trailWidth = 4.5f;
                trailColor = Color.valueOf("3f8cff");
                trailInterval = 1.5f;

                trailEffect = new Effect(45f, e -> {

                    Draw.color(Color.valueOf("2b6cff"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.fout() * 7f);

                    Draw.color(Color.valueOf("4da6ff"));
                    circle(e.x, e.y, e.fout() * 4.5f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 2f);

                    Draw.color(Color.valueOf("3f8cff"));
                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(1.8f * e.fout());
                    Lines.circle(e.x, e.y, e.fout() * 9f);

                    Angles.randLenVectors(e.id, 4, e.fin() * 6f, (x, y) -> {
                        Draw.color(Color.valueOf("a8d8ff"), Color.valueOf("2b6cff"), Mathf.random());
                        circle(e.x + x, e.y + y, e.fout() * 1.8f);
                    });
                });

                trailChance = 0.7f;

                hitEffect = new Effect(60f, 150f, e -> {

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 18f);

                    Draw.color(Color.valueOf("2b6cff"), Color.valueOf("a8d8ff"), e.fin() * 0.6f);

                    e.scaled(25f, s -> {
                        Lines.stroke(5f * s.fout());
                        Lines.circle(e.x, e.y, 6f + s.fin(Interp.pow3Out) * 60f);
                    });

                    e.scaled(40f, s -> {
                        Lines.stroke(3f * s.fout());
                        Lines.circle(e.x, e.y, 10f + s.fin(Interp.pow2Out) * 80f);
                    });

                    Draw.color(Color.valueOf("4da6ff"), Color.white, e.fout() * 0.7f);
                    Lines.stroke(2.5f * e.fout());

                    Angles.randLenVectors(e.id, 18, 15f + 60f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, angle, e.fout() * (12f + Mathf.random(10f)));
                    });

                    Angles.randLenVectors(e.id + 1, 14, 8f + 45f * e.finpow(), (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("2b6cff"), e.fin() * 0.8f);
                        circle(e.x + x, e.y + y, e.fout() * 5f);
                    });

                    for (int i = 0; i < 6; i++) {
                        float angle = i * 60f + e.fin() * 30f;
                        float dst = e.finpow() * 65f;

                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("4da6ff"), Color.white, e.fout() * 0.6f);
                        Lines.stroke(2.2f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle + 90f, e.fout() * 16f);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 100f, Color.valueOf("5ecbff"), 0.85f);
                });

                despawnHit = true;

                shootEffect = new Effect(30f, e -> {
                    Draw.color(Color.white, Color.valueOf("2b6cff"), e.fin() * 0.7f);

                    Lines.stroke(e.fout() * 3.5f);
                    Angles.randLenVectors(e.id, 12, 35f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 10f + 4f);
                    });

                    circle(e.x, e.y, e.fout() * 9f);

                    Draw.color(Color.valueOf("4da6ff"));
                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 22f);

                    Drawf.light(e.x, e.y, e.fout() * 50f, Color.valueOf("5ecbff"), 0.7f);
                });

                smokeEffect = new Effect(40f, e -> {
                    Draw.color(Color.valueOf("2b6cff"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.fout() * 6f);

                    Draw.color(Color.valueOf("3f8cff"));
                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 14f);
                });

                pierce = false;
                pierceBuilding = false;

                splashDamageRadius = 50f;
                splashDamage = damage * 0.75f;

                knockback = 5f;
                hitShake = 4f;

                status = StatusEffects.shocked;
                statusDuration = 150f;

                homingPower = 0.12f;
                homingRange = 285f;
                homingDelay = 5f;

                trailRotation = true;
                despawnHit = true;

                weaveMag = 2f;
                weaveScale = 4f;
            }
        };

        azureStream = new BasicBulletType(11f, 1020) {
            {
                lifetime = 65f;

                width = 11f;
                height = 55f;
                sprite = "missile-large";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("0066ff");
                frontColor = Color.valueOf("ffffff");
                lightColor = Color.valueOf("44aaff");
                lightOpacity = 1f;
                lightRadius = 160f;

                trailLength = 40;
                trailWidth = 10f;
                trailColor = Color.valueOf("1188ff");
                trailInterval = 0.5f;

                trailEffect = new Effect(50f, e -> {
                    float xVal = e.x;
                    float yVal = e.y;
                    float foutVal = e.fout();
                    float timeVal = e.time;

                    Draw.color(Color.valueOf("1188ff"));
                    Draw.alpha(foutVal * 0.9f);
                    Lines.stroke(5f * foutVal);

                    for (int i = 0; i < 3; i++) {
                        final int fi = i;
                        float offset = fi * 120f;
                        float waveSpeed = timeVal * 3f + fi * 30f;

                        for (int j = 0; j < 8; j++) {
                            float angle = offset + j * 15f;
                            float waveMag = Mathf.sin(waveSpeed + j * 0.5f) * 6f * foutVal;
                            float radius = 8f * foutVal;

                            Tmp.v1.trns(angle, radius + waveMag);

                            if (j > 0) {
                                float prevAngle = offset + (j - 1) * 15f;
                                float prevWave = Mathf.sin(waveSpeed + (j - 1) * 0.5f) * 6f * foutVal;
                                Tmp.v2.trns(prevAngle, radius + prevWave);

                                Lines.line(
                                        xVal + Tmp.v2.x, yVal + Tmp.v2.y,
                                        xVal + Tmp.v1.x, yVal + Tmp.v1.y);
                            }
                        }
                    }

                    Draw.color(Color.valueOf("44aaff"));
                    circle(e.x, e.y, foutVal * 6f);

                    Draw.color(Color.valueOf("ccffff"));
                    circle(e.x, e.y, foutVal * 3f);
                });

                trailChance = 1f;

                hitEffect = new Effect(100f, 350f, e -> {
                    float exVal = e.x;
                    float eyVal = e.y;

                    Draw.color(Color.valueOf("1188ff"), Color.valueOf("ffffff"), e.fin() * 0.5f);
                    Lines.stroke(6f * e.fout());

                    for (int ribbon = 0; ribbon < 16; ribbon++) {
                        final int fr = ribbon;
                        float baseAngle = fr * 22.5f;

                        e.scaled(40f + (fr % 3) * 20f, flow -> {
                            float flowDist = flow.finpow() * 110f;

                            for (int seg = 0; seg < 12; seg++) {
                                float progress = seg / 12f;
                                float nextProgress = (seg + 1) / 12f;

                                float angle1 = baseAngle + Mathf.sin(progress * 10f + flow.time * 0.5f) * 25f;
                                float angle2 = baseAngle + Mathf.sin(nextProgress * 10f + flow.time * 0.5f) * 25f;

                                float dist1 = flowDist * progress;
                                float dist2 = flowDist * nextProgress;

                                Tmp.v1.trns(angle1, dist1);
                                Tmp.v2.trns(angle2, dist2);

                                Lines.line(
                                        exVal + Tmp.v1.x, eyVal + Tmp.v1.y,
                                        exVal + Tmp.v2.x, eyVal + Tmp.v2.y);
                            }
                        });
                    }

                    Draw.color(Color.valueOf("44aaff"));
                    circle(e.x, e.y, (35f + Mathf.absin(e.time, 1.5f, 7f)) * e.fout());

                    Draw.color(Color.valueOf("ffffff"));
                    circle(e.x, e.y, (20f + Mathf.absin(e.time, 1.5f, 4f)) * e.fout());

                    for (int spiral = 0; spiral < 6; spiral++) {
                        final int fs = spiral;
                        float spiralAngle = e.time * (1.5f + fs * 0.4f) * (fs % 2 == 0 ? 1 : -1);

                        e.scaled(35f + fs * 10f, s -> {
                            Draw.color(Color.valueOf("66bbff"));
                            Draw.alpha(s.fout() * 0.8f);
                            Lines.stroke(4f * s.fout());

                            for (int i = 0; i < 20; i++) {
                                float angle = i * 18f + spiralAngle;
                                float radius = s.finpow() * (40f + fs * 12f) * (i / 20f);

                                Tmp.v1.trns(angle, radius);

                                if (i > 0) {
                                    float prevAngle = (i - 1) * 18f + spiralAngle;
                                    float prevRadius = s.finpow() * (40f + fs * 12f) * ((i - 1) / 20f);
                                    Tmp.v2.trns(prevAngle, prevRadius);

                                    Lines.line(
                                            exVal + Tmp.v2.x, eyVal + Tmp.v2.y,
                                            exVal + Tmp.v1.x, eyVal + Tmp.v1.y);
                                }
                            }
                        });
                    }

                    float foutVal = e.fout();

                    for (int w = 0; w < 8; w++) {
                        final int fw = w;
                        e.scaled(15f + fw * 10f, wave -> {
                            Draw.color(Color.valueOf("1188ff"), Color.valueOf("ccffff"), wave.fout());
                            Lines.stroke((7f - fw * 0.7f) * wave.fout());

                            float waveRadius = 20f + wave.fin(Interp.pow3Out) * (90f + fw * 12f);
                            Lines.circle(exVal, eyVal, waveRadius);
                        });
                    }

                    Angles.randLenVectors(e.id, 30, 25f + 100f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);

                        Draw.color(Color.valueOf("44aaff"));
                        Draw.alpha(foutVal * 0.85f);

                        Tmp.v1.trns(angle, foutVal * 8f);
                        Tmp.v2.trns(angle + 180f, foutVal * 4f);

                        Lines.stroke(foutVal * 3f);
                        Lines.line(
                                exVal + x + Tmp.v2.x, eyVal + y + Tmp.v2.y,
                                exVal + x + Tmp.v1.x, eyVal + y + Tmp.v1.y);

                        circle(exVal + x + Tmp.v1.x, eyVal + y + Tmp.v1.y, foutVal * 4f);
                    });

                    Draw.color(Color.valueOf("1188ff"));
                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(2.5f * e.fout());

                    for (int i = 0; i < 12; i++) {
                        final int fi = i;

                        float angle = fi * 30f + e.fin() * 40f;
                        float dst = e.finpow() * 120f;

                        Tmp.v1.trns(angle, dst);

                        Lines.line(exVal, eyVal, exVal + Tmp.v1.x, eyVal + Tmp.v1.y);

                        if (fi < 11) {
                            float nextAngle = (fi + 1) * 30f + e.fin() * 40f;
                            Tmp.v2.trns(nextAngle, dst);

                            for (int arc = 0; arc < 5; arc++) {
                                float arcProgress = arc / 5f;
                                float midAngle = angle + (nextAngle - angle) * arcProgress;
                                float arcDst = dst * (1f - arcProgress * 0.3f);

                                if (arc > 0) {
                                    float prevProgress = (arc - 1) / 5f;
                                    float prevAngle = angle + (nextAngle - angle) * prevProgress;
                                    float prevDst = dst * (1f - prevProgress * 0.3f);

                                    Tmp.v1.trns(prevAngle, prevDst);
                                    Tmp.v2.trns(midAngle, arcDst);

                                    Lines.line(
                                            exVal + Tmp.v1.x, eyVal + Tmp.v1.y,
                                            exVal + Tmp.v2.x, eyVal + Tmp.v2.y);
                                }
                            }
                        }
                    }

                    Drawf.light(e.x, e.y, e.fout() * 280f, Color.valueOf("44aaff"), 0.95f);
                });

                despawnHit = true;

                shootEffect = new Effect(45f, e -> {
                    float xVal = e.x;
                    float yVal = e.y;
                    float finVal = e.fin();
                    float foutVal = e.fout();

                    Draw.color(Color.valueOf("1188ff"));
                    Draw.alpha(finVal * 0.9f);
                    Lines.stroke(4f * finVal);

                    for (int stream = 0; stream < 12; stream++) {
                        final int fs = stream;
                        float angle = fs * 30f;
                        float startDist = (1f - finVal) * 70f;
                        float endDist = (1f - finVal) * 20f;

                        for (int seg = 0; seg < 6; seg++) {
                            float progress = seg / 6f;
                            float nextProgress = (seg + 1) / 6f;

                            float dist1 = startDist - (startDist - endDist) * progress;
                            float dist2 = startDist - (startDist - endDist) * nextProgress;

                            float wave1 = Mathf.sin(e.time * 3f + seg) * 5f;
                            float wave2 = Mathf.sin(e.time * 3f + seg + 1) * 5f;

                            Tmp.v1.trns(angle + wave1, dist1);
                            Tmp.v2.trns(angle + wave2, dist2);

                            Lines.line(
                                    xVal + Tmp.v1.x, yVal + Tmp.v1.y,
                                    xVal + Tmp.v2.x, yVal + Tmp.v2.y);
                        }
                    }

                    Draw.color(Color.white);
                    circle(e.x, e.y, foutVal * 20f);

                    Draw.color(Color.valueOf("ccffff"));
                    circle(e.x, e.y, foutVal * 15f);

                    Draw.color(Color.valueOf("44aaff"));
                    Lines.stroke(3f * foutVal);

                    for (int i = 0; i < 4; i++) {
                        final int fi = i;
                        float rot = e.time * (2f + fi) * (fi % 2 == 0 ? 1 : -1);
                        Lines.arc(e.x, e.y, (15f + fi * 8f) * foutVal, 0.4f, rot * 60f);
                    }

                    Drawf.light(e.x, e.y, foutVal * 85f, Color.valueOf("44aaff"), 0.85f);
                });

                smokeEffect = Fx.none;

                pierce = true;
                pierceCap = 7;
                pierceBuilding = true;

                splashDamageRadius = 120f;
                splashDamage = damage * 0.88f;

                knockback = 10f;
                hitShake = 7.5f;

                status = StatusEffects.shocked;
                statusDuration = 260f;

                homingPower = 0.14f;
                homingRange = 160f;

                despawnHit = true;

                drag = -0.008f;
                weaveMag = 1.5f;
                weaveScale = 3f;

                fragOnHit = true;
                fragBullets = 18;
                fragBullet = new BasicBulletType(8f, 95) {
                    {
                        width = 8f;
                        height = 8f;
                        sprite = "large-bomb";

                        backColor = Color.valueOf("1188ff");
                        frontColor = Color.valueOf("ffffff");
                        lightColor = Color.valueOf("66bbff");
                        lightRadius = 40f;

                        trailLength = 12;
                        trailWidth = 3.5f;
                        trailColor = Color.valueOf("44aaff");

                        splashDamageRadius = 26f;
                        splashDamage = 50f;

                        status = StatusEffects.shocked;
                        statusDuration = 150f;

                        lifetime = 32f;

                        hitEffect = new Effect(30f, e -> {

                            Draw.color(Color.valueOf("44aaff"));
                            circle(e.x, e.y, e.fout() * 12f);

                            Draw.color(Color.valueOf("ffffff"));
                            circle(e.x, e.y, e.fout() * 6f);

                            Lines.stroke(2f * e.fout());
                            for (int i = 0; i < 6; i++) {
                                final int fi = i;
                                float angle = fi * 60f + e.fin() * 120f;
                                Tmp.v1.trns(angle, e.finpow() * 18f);

                                Lines.line(e.x, e.y, e.x + Tmp.v1.x, e.y + Tmp.v1.y);
                            }
                        });

                        weaveMag = 2f;
                        weaveScale = 4f;
                    }
                };
                fragVelocityMin = 0.9f;
                fragVelocityMax = 1.7f;
            }
        };

        frostCascade = new BasicBulletType(8f, 1060) {
            {
                lifetime = 70f;

                width = 16f;
                height = 16f;
                sprite = "large-bomb";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("3399dd");
                frontColor = Color.valueOf("ffffff");
                lightColor = Color.valueOf("66ccff");
                lightOpacity = 1f;
                lightRadius = 160f;

                trailLength = 22;
                trailWidth = 7f;
                trailColor = Color.valueOf("4488cc");
                trailInterval = 2f;

                trailEffect = new Effect(55f, e -> {
                    float xVal = e.x;
                    float yVal = e.y;
                    float foutVal = e.fout();

                    Draw.color(Color.valueOf("3399dd"));
                    Draw.alpha(foutVal * 0.8f);
                    Lines.stroke(3f * foutVal);
                    Lines.circle(e.x, e.y, (6f + Mathf.absin(e.time, 1.5f, 4f)) * foutVal);

                    Draw.color(Color.valueOf("66ccff"));
                    Lines.stroke(2f * foutVal);
                    Lines.circle(e.x, e.y, (10f + Mathf.absin(e.time, 1.5f, 6f)) * foutVal);

                    Draw.color(Color.valueOf("ffffff"));
                    circle(e.x, e.y, foutVal * 4f);

                    Draw.color(Color.valueOf("aaddff"));
                    circle(e.x, e.y, foutVal * 2f);
                });

                trailChance = 0.8f;

                hitEffect = new Effect(120f, 400f, e -> {
                    float exVal = e.x;
                    float eyVal = e.y;

                    Draw.color(Color.valueOf("ffffff"));
                    circle(exVal, eyVal, (40f + Mathf.absin(e.time, 1.2f, 8f)) * e.fout());

                    Draw.color(Color.valueOf("aaddff"));
                    circle(exVal, eyVal, (28f + Mathf.absin(e.time, 1.2f, 6f)) * e.fout());

                    Draw.color(Color.valueOf("3399dd"));
                    circle(exVal, eyVal, (18f + Mathf.absin(e.time, 1.2f, 4f)) * e.fout());

                    int totalNodes = 12;

                    for (int node = 0; node < totalNodes; node++) {
                        final int fn = node;
                        float nodeDelay = fn * 0.07f;

                        if (e.fin() > nodeDelay) {
                            float nodeProgress = Math.min((e.fin() - nodeDelay) / 0.15f, 1f);
                            float nodeFade = Math.max((0.9f - e.fin()) / 0.9f, 0f);
                            float nodeAlpha = Math.min(nodeProgress, nodeFade);

                            float angle = fn * 30f;
                            float distance = 85f;

                            Tmp.v1.trns(angle, distance);

                            Draw.color(Color.valueOf("66ccff"));
                            Draw.alpha(nodeAlpha * 0.9f);
                            circle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, nodeAlpha * 20f);

                            Draw.color(Color.valueOf("ffffff"));
                            circle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, nodeAlpha * 10f);

                            Draw.color(Color.valueOf("3399dd"), Color.valueOf("aaddff"), nodeProgress * 0.6f);
                            Draw.alpha(nodeAlpha * 0.8f);
                            Lines.stroke(4f * nodeAlpha);
                            Lines.line(exVal, eyVal, exVal + Tmp.v1.x, eyVal + Tmp.v1.y);

                            for (int pulse = 0; pulse < 3; pulse++) {
                                float pulseProgress = (e.time * 0.1f + pulse * 0.33f) % 1f;
                                Tmp.v2.set(Tmp.v1).scl(pulseProgress);

                                Draw.color(Color.valueOf("66ccff"));
                                Draw.alpha(nodeAlpha * (1f - pulseProgress) * 0.8f);
                                circle(exVal + Tmp.v2.x, eyVal + Tmp.v2.y, nodeAlpha * 4f * (1f - pulseProgress));
                            }

                            if (nodeProgress > 0.7f && fn % 3 == 0) {
                                for (int sub = 0; sub < 4; sub++) {
                                    final int fs = sub;
                                    float subAngle = angle + 90f + fs * 90f;
                                    float subDist = 35f;

                                    Tmp.v2.trns(subAngle, subDist);

                                    float subAlpha = (nodeProgress - 0.7f) / 0.3f * nodeFade;

                                    Draw.color(Color.valueOf("66ccff"));
                                    Draw.alpha(subAlpha * 0.7f);
                                    circle(exVal + Tmp.v1.x + Tmp.v2.x, eyVal + Tmp.v1.y + Tmp.v2.y,
                                            subAlpha * 12f);

                                    Draw.color(Color.valueOf("4488cc"));
                                    Draw.alpha(subAlpha * 0.6f);
                                    Lines.stroke(2f * subAlpha);
                                    Lines.line(
                                            exVal + Tmp.v1.x, eyVal + Tmp.v1.y,
                                            exVal + Tmp.v1.x + Tmp.v2.x, eyVal + Tmp.v1.y + Tmp.v2.y);
                                }
                            }

                            if (fn < totalNodes - 1 && nodeProgress > 0.5f) {
                                float nextAngle = (fn + 1) * 30f;
                                Tmp.v2.trns(nextAngle, distance);

                                float linkAlpha = (nodeProgress - 0.5f) / 0.5f * nodeFade;

                                Draw.color(Color.valueOf("4488cc"));
                                Draw.alpha(linkAlpha * 0.5f);
                                Lines.stroke(2.5f * linkAlpha);
                                Lines.line(
                                        exVal + Tmp.v1.x, eyVal + Tmp.v1.y,
                                        exVal + Tmp.v2.x, eyVal + Tmp.v2.y);
                            }
                        }
                    }

                    for (int w = 0; w < 7; w++) {
                        final int fw = w;
                        e.scaled(20f + fw * 13f, wave -> {
                            Draw.color(Color.valueOf("3399dd"), Color.valueOf("aaddff"), wave.fout());
                            Lines.stroke((6f - fw * 0.7f) * wave.fout());
                            Lines.circle(exVal, eyVal, 15f + wave.fin(Interp.pow2Out) * (100f + fw * 16f));
                        });
                    }

                    Draw.color(Color.valueOf("3399dd"));
                    Draw.alpha(e.fout() * 0.5f);
                    Lines.stroke(2f * e.fout());

                    float gridRadius = e.finpow() * 130f;
                    int gridSections = 8;

                    for (int i = 0; i < gridSections; i++) {
                        final int fi = i;
                        float angle = fi * (360f / gridSections);
                        Tmp.v1.trns(angle, gridRadius);

                        Lines.line(exVal, eyVal, exVal + Tmp.v1.x, eyVal + Tmp.v1.y);

                        for (int ring = 1; ring <= 3; ring++) {
                            if (fi < gridSections - 1) {
                                float nextAngle = (fi + 1) * (360f / gridSections);
                                Tmp.v2.trns(angle, gridRadius * ring / 3f);
                                Tmp.v1.trns(nextAngle, gridRadius * ring / 3f);

                                Lines.line(
                                        exVal + Tmp.v2.x, eyVal + Tmp.v2.y,
                                        exVal + Tmp.v1.x, eyVal + Tmp.v1.y);
                            }
                        }
                    }

                    Drawf.light(exVal, eyVal, e.fout() * 300f, Color.valueOf("66ccff"), 0.95f);
                });

                despawnHit = true;

                shootEffect = new Effect(50f, e -> {
                    float xVal = e.x;
                    float yVal = e.y;
                    float finVal = e.fin();
                    float foutVal = e.fout();

                    Draw.color(Color.valueOf("3399dd"));
                    Draw.alpha(finVal * 0.9f);

                    for (int stream = 0; stream < 6; stream++) {
                        final int fs = stream;
                        float angle = fs * 60f;
                        float streamDist = (1f - finVal) * 70f;

                        Tmp.v1.trns(angle, streamDist);

                        Draw.alpha(finVal * 0.8f);
                        Lines.stroke(3f * finVal);
                        Lines.line(xVal + Tmp.v1.x, yVal + Tmp.v1.y, xVal, yVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, finVal * 6f);
                    }

                    Draw.color(Color.valueOf("66ccff"), Color.white, e.fin() * 0.5f);

                    for (int i = 0; i < 5; i++) {
                        final int fi = i;
                        float delay = fi * 0.15f;
                        if (e.fin() > delay) {
                            float progress = (e.fin() - delay) / (1f - delay);

                            Lines.stroke((4f - fi * 0.7f) * (1f - progress));
                            Lines.circle(e.x, e.y, (70f - fi * 12f) * (1f - progress));
                        }
                    }

                    Draw.color(Color.white);
                    circle(e.x, e.y, foutVal * 20f);

                    Draw.color(Color.valueOf("aaddff"));
                    circle(e.x, e.y, foutVal * 14f);

                    Drawf.light(e.x, e.y, foutVal * 85f, Color.valueOf("66ccff"), 0.85f);
                });

                smokeEffect = Fx.none;

                pierce = true;
                pierceCap = 5;
                pierceBuilding = true;

                splashDamageRadius = 130f;
                splashDamage = damage * 0.9f;

                knockback = 10f;
                hitShake = 8f;

                status = StatusEffects.freezing;
                statusDuration = 280f;

                homingPower = 0.1f;
                homingRange = 170f;

                despawnHit = true;

                drag = -0.006f;
                weaveMag = 0f;
                weaveScale = 0f;

                fragOnHit = true;
                fragBullets = 20;
                fragBullet = new BasicBulletType(7f, 88) {
                    {
                        width = 8f;
                        height = 8f;
                        sprite = "large-bomb";

                        backColor = Color.valueOf("3399dd");
                        frontColor = Color.valueOf("ffffff");
                        lightColor = Color.valueOf("66ccff");
                        lightRadius = 38f;

                        trailLength = 10;
                        trailWidth = 3f;
                        trailColor = Color.valueOf("4488cc");

                        splashDamageRadius = 25f;
                        splashDamage = 46f;

                        status = StatusEffects.freezing;
                        statusDuration = 140f;

                        lifetime = 32f;

                        hitEffect = new Effect(30f, e -> {

                            Draw.color(Color.valueOf("66ccff"));
                            circle(e.x, e.y, e.fout() * 12f);

                            Draw.color(Color.valueOf("ffffff"));
                            circle(e.x, e.y, e.fout() * 6f);

                            Lines.stroke(2f * e.fout());
                            for (int i = 0; i < 8; i++) {
                                final int fi = i;
                                float angle = fi * 45f;
                                float len = e.finpow() * 16f;

                                Draw.color(Color.valueOf("aaddff"), Color.valueOf("3399dd"), e.fin());
                                Lines.lineAngle(e.x, e.y, angle, len);
                            }

                            Draw.color(Color.valueOf("3399dd"));
                            Lines.stroke(2.5f * e.fout());
                            Lines.circle(e.x, e.y, e.fin() * 20f);
                        });
                    }
                };
                fragVelocityMin = 0.9f;
                fragVelocityMax = 1.65f;
            }
        };

        frostShotgun = new ContinuousLaserBulletType(1000f) {
            {
                length = 350f;
                width = 3.5f;

                Color crystalBlue = Color.valueOf("7dd7ff");
                Color frostCyan = Color.valueOf("6bddff");
                Color deepBlue = Color.valueOf("4a9fd8");
                Color iceWhite = Color.valueOf("d4f4ff");

                colors = new Color[] {
                        deepBlue.cpy().a(0.4f),
                        frostCyan.cpy().a(0.7f),
                        crystalBlue,
                        iceWhite
                };

                pierce = true;
                pierceCap = 999;
                pierceBuilding = true;

                status = StatusEffects.freezing;
                statusDuration = 90f;

                hitEffect = new MultiEffect(
                        new ParticleEffect() {
                            {
                                particles = 6;
                                length = 20f;
                                lifetime = 25f;
                                sizeFrom = 4f;
                                sizeTo = 0f;
                                colorFrom = iceWhite;
                                colorTo = crystalBlue.cpy().a(0f);
                                cone = 360f;
                            }
                        },
                        new ParticleEffect() {
                            {
                                particles = 3;
                                length = 15f;
                                lifetime = 20f;
                                sizeFrom = 3f;
                                sizeTo = 0f;
                                colorFrom = frostCyan;
                                colorTo = deepBlue.cpy().a(0f);
                                cone = 360f;
                            }
                        });

                shootEffect = new ParticleEffect() {
                    {
                        particles = 8;
                        length = 30f;
                        lifetime = 20f;
                        sizeFrom = 5f;
                        sizeTo = 0f;
                        colorFrom = iceWhite;
                        colorTo = frostCyan.cpy().a(0f);
                        cone = 25f;
                    }
                };

                shootSound = Sounds.shootFuse;
                continuous = true;
            }
        };

        crimsonVortex = new BasicBulletType(7f, 1840) {
            {
                lifetime = 105f;

                width = 20f;
                height = 20f;
                sprite = "large-bomb";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("aa0000");
                frontColor = Color.valueOf("ffcccc");
                lightColor = Color.valueOf("ff3333");
                lightOpacity = 1f;
                lightRadius = 180f;

                trailLength = 16;
                trailWidth = 6f;
                trailColor = Color.valueOf("dd0000");
                trailInterval = 3f;

                trailEffect = new Effect(45f, e -> {
                    Draw.color(Color.valueOf("aa0000"));
                    Draw.alpha(e.fout() * 0.8f);
                    circle(e.x, e.y, (8f + Mathf.absin(e.time, 2f, 2f)) * e.fout());

                    Draw.color(Color.valueOf("ff3333"));
                    circle(e.x, e.y, (5f + Mathf.absin(e.time, 2f, 1.5f)) * e.fout());
                });

                trailChance = 0.6f;

                hitEffect = new Effect(130f, 380f, e -> {
                    float exVal = e.x;
                    float eyVal = e.y;

                    Draw.color(Color.valueOf("ffffff"));
                    circle(exVal, eyVal, (45f + Mathf.absin(e.time, 1.3f, 8f)) * e.fout());

                    Draw.color(Color.valueOf("ffaaaa"));
                    circle(exVal, eyVal, (35f + Mathf.absin(e.time, 1.3f, 6f)) * e.fout());

                    Draw.color(Color.valueOf("ff3333"));
                    circle(exVal, eyVal, (25f + Mathf.absin(e.time, 1.3f, 4f)) * e.fout());

                    Draw.color(Color.valueOf("ff3333"), Color.valueOf("ffcccc"), e.fin() * 0.7f);

                    for (int spiral = 0; spiral < 12; spiral++) {
                        final int fs = spiral;
                        float spiralAngle = e.time * (2.5f + fs * 0.5f) * (fs % 2 == 0 ? 1 : -1);

                        e.scaled(25f + fs * 8f, s -> {
                            Lines.stroke((6f - fs * 0.4f) * s.fout());

                            for (int i = 0; i < 30; i++) {
                                float angle = i * 12f + spiralAngle;
                                float radius = s.finpow() * (50f + fs * 15f) * (i / 30f);

                                Tmp.v1.trns(angle, radius);

                                if (i > 0) {
                                    float prevAngle = (i - 1) * 12f + spiralAngle;
                                    float prevRadius = s.finpow() * (50f + fs * 15f) * ((i - 1) / 30f);
                                    Tmp.v2.trns(prevAngle, prevRadius);

                                    Lines.line(
                                            exVal + Tmp.v2.x, eyVal + Tmp.v2.y,
                                            exVal + Tmp.v1.x, eyVal + Tmp.v1.y);
                                }
                            }
                        });
                    }

                    for (int w = 0; w < 8; w++) {
                        final int fw = w;
                        e.scaled(18f + fw * 12f, wave -> {
                            Draw.color(Color.valueOf("ff3333"), Color.valueOf("ffcccc"), wave.fout());
                            Lines.stroke((7f - fw * 0.75f) * wave.fout());
                            Lines.circle(exVal, eyVal, 20f + wave.fin(Interp.pow2Out) * (110f + fw * 15f));
                        });
                    }

                    float foutVal = e.fout();
                    float finpowVal = e.finpow();

                    for (int orbit = 0; orbit < 16; orbit++) {
                        final int fo = orbit;
                        float orbitAngle = fo * 22.5f + e.fin() * 180f;
                        float orbitDist = finpowVal * 130f;

                        Tmp.v1.trns(orbitAngle, orbitDist);

                        Draw.color(Color.valueOf("ff3333"));
                        Draw.alpha(foutVal * 0.9f);
                        circle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, foutVal * 18f);

                        Draw.color(Color.valueOf("ffaaaa"));
                        Draw.alpha(foutVal * 0.7f);
                        circle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, foutVal * 25f);

                        Draw.color(Color.valueOf("ffffff"));
                        circle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, foutVal * 10f);
                    }

                    Drawf.light(exVal, eyVal, e.fout() * 300f, Color.valueOf("ff3333"), 0.95f);
                });

                despawnHit = true;

                shootEffect = new Effect(50f, e -> {

                    float xVal = e.x;
                    float yVal = e.y;
                    float finVal = e.fin();
                    float foutVal = e.fout();

                    Draw.color(Color.valueOf("ff3333"), Color.white, e.fin() * 0.5f);

                    for (int i = 0; i < 6; i++) {
                        final int fi = i;
                        float delay = fi * 0.12f;
                        if (e.fin() > delay) {
                            float progress = (e.fin() - delay) / (1f - delay);

                            Lines.stroke((5f - fi * 0.7f) * (1f - progress));
                            Lines.circle(e.x, e.y, (70f - fi * 10f) * (1f - progress));
                        }
                    }

                    Draw.color(Color.white);
                    circle(e.x, e.y, foutVal * 20f);

                    Drawf.light(e.x, e.y, foutVal * 85f, Color.valueOf("ff3333"), 0.85f);
                });

                smokeEffect = new Effect(60f, e -> {
                    Draw.color(Color.valueOf("883333"));
                    Draw.alpha(e.fout() * 0.75f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float timeVal = e.time;
                    float foutVal = e.fout();

                    for (int i = 0; i < 4; i++) {
                        final int fi = i;
                        float angle = timeVal * (2f + fi * 0.7f) * (fi % 2 == 0 ? 1 : -1);
                        Tmp.v1.trns(angle * 50f, (8f + fi * 4f) * foutVal);

                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, foutVal * (12f - fi * 2f));
                    }
                });

                pierce = true;
                pierceCap = 5;
                pierceBuilding = true;

                splashDamageRadius = 125f;
                splashDamage = damage * 0.9f;

                knockback = 11f;
                hitShake = 8f;

                status = StatusEffects.burning;
                statusDuration = 280f;

                homingPower = 0.09f;
                homingRange = 165f;

                despawnHit = true;

                drag = -0.005f;
                weaveMag = 0f;
                weaveScale = 0f;

                fragOnHit = true;
                fragBullets = 18;
                fragBullet = new BasicBulletType(7f, 85) {
                    {
                        width = 9f;
                        height = 13f;
                        lifetime = 28f;

                        backColor = Color.valueOf("aa0000");
                        frontColor = Color.valueOf("ffcccc");
                        lightColor = Color.valueOf("ff5555");
                        lightRadius = 36f;

                        trailLength = 8;
                        trailWidth = 2.5f;
                        trailColor = Color.valueOf("ff3333");

                        splashDamageRadius = 22f;
                        splashDamage = 42f;

                        status = StatusEffects.burning;
                        statusDuration = 130f;

                        hitEffect = new Effect(26f, e -> {
                            Draw.color(Color.valueOf("ff3333"));
                            circle(e.x, e.y, e.fout() * 10f);

                            Draw.color(Color.valueOf("ffcccc"));
                            circle(e.x, e.y, e.fout() * 5f);

                            Lines.stroke(2.5f * e.fout());
                            Lines.circle(e.x, e.y, e.fin() * 20f);
                        });
                    }
                };
                fragVelocityMin = 0.85f;
                fragVelocityMax = 1.7f;
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                int orbitCount = 8;
                float orbitRadius = 28f;
                float orbitSpeed = 4f;

                Draw.z(Layer.bullet - 0.001f);

                for (int i = 0; i < orbitCount; i++) {
                    final int fi = i;

                    float angle = (b.time * orbitSpeed) + (fi * 360f / orbitCount);

                    Tmp.v1.trns(angle, orbitRadius);
                    float orbX = b.x + Tmp.v1.x;
                    float orbY = b.y + Tmp.v1.y;

                    Draw.color(Color.valueOf("ff3333"));
                    circle(orbX, orbY, 5f);

                    Draw.color(Color.valueOf("ffdddd"));
                    circle(orbX, orbY, 3f);

                    Draw.color(Color.valueOf("ff4444"));
                    Draw.alpha(0.6f);
                    Lines.stroke(1.5f);

                    Draw.color(Color.valueOf("ff3333"));
                    Draw.alpha(0.3f);
                    Lines.stroke(1f);
                    Lines.line(b.x, b.y, orbX, orbY);

                    Drawf.light(orbX, orbY, 20f, Color.valueOf("ff5555"), 0.5f);
                }
                Draw.reset();
            }
        };

        infernoCore = new BasicBulletType(9.2f, 1300) {
            {
                lifetime = 80f;

                width = 18f;
                height = 38f;
                sprite = "missile-large";

                shrinkX = 0.3f;
                shrinkY = 0f;

                backColor = Color.valueOf("550011");
                frontColor = Color.valueOf("ff1133");
                lightColor = Color.valueOf("ff0033");
                lightOpacity = 1f;
                lightRadius = 280f;
                trailLength = 32;
                trailWidth = 16f;
                trailColor = Color.valueOf("cc0022");
                trailInterval = 1f;

                trailEffect = JBFx.polyTrail(
                        Color.valueOf("ff1133"),
                        Color.valueOf("330011"),
                        12f, 55f);
                trailChance = 0.9f;
                shootEffect = JBFx.crossBlast(Color.valueOf("ff1133"), 55f);

                smokeEffect = new Effect(85f, e -> {
                    float fout = e.fout();

                    Draw.color(Color.valueOf("1a0008"));
                    Draw.alpha(fout);
                    for (int i = 0; i < 5; i++) {
                        final int fi = i;
                        float angle = e.time * (2f + fi * 0.6f) * (fi % 2 == 0 ? 1f : -1f);
                        Tmp.v1.trns(angle * 52f, (12f + fi * 5f) * fout);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, fout * (20f - fi * 2.5f));
                    }
                    Draw.color(Color.valueOf("990022"));
                    Draw.alpha(fout * 0.7f);
                    circle(e.x, e.y, fout * 14f);

                    Draw.color(Color.valueOf("dd0000"));
                    Draw.alpha(fout * 0.45f);
                    circle(e.x, e.y, fout * 7f);

                    Drawf.light(e.x, e.y, 60f * fout, Color.valueOf("ff0033"), 0.72f);
                });

                hitEffect = new Effect(210f, 600f, e -> {
                    float ex = e.x, ey = e.y;

                    e.scaled(40f, flash -> {
                        float ff = flash.fout();

                        Draw.color(Color.valueOf("ffffff"));
                        Draw.alpha(ff);
                        circle(ex, ey, ff * 110f);

                        Draw.color(Color.valueOf("ff0033"));
                        Draw.alpha(ff * 0.9f);
                        circle(ex, ey, ff * 145f);

                        Draw.color(Color.valueOf("110005"));
                        Draw.alpha(ff * 0.95f);
                        Lines.stroke(18f * ff);
                        Lines.circle(ex, ey, 18f + flash.fin(Interp.pow3Out) * 120f);

                        Draw.color(Color.valueOf("dd0000"));
                        Draw.alpha(ff * 0.85f);
                        Lines.stroke(9f * ff);
                        Lines.circle(ex, ey, 14f + flash.fin(Interp.pow2Out) * 95f);

                        Draw.color(Color.valueOf("ffffff"));
                        Draw.alpha(ff * 0.9f);
                        Lines.stroke(4f * ff);
                        for (int i = 0; i < 16; i++) {
                            float a = i * 22.5f;
                            Tmp.v1.trns(a, flash.fin(Interp.pow2Out) * 100f);
                            Lines.line(ex, ey, ex + Tmp.v1.x, ey + Tmp.v1.y);
                        }

                        Drawf.light(ex, ey, ff * 520f, Color.valueOf("ff1133"), 1f);
                    });

                    for (int em = 0; em < 24; em++) {
                        final int fem = em;
                        float emStart = 10f + fem * 2.5f;
                        float emDur = 110f + fem * 2f;
                        e.scaled(emStart + emDur, ember -> {
                            if (ember.time < emStart)
                                return;
                            float ep = (ember.time - emStart) / emDur;
                            float efout = 1f - ep;

                            float emAngle = fem * (360f / 24f) + Mathf.sin(fem * 53.7f) * 22f;

                            float emDist = Interp.pow2Out.apply(ep) * (160f + fem * 5f);

                            Tmp.v1.trns(emAngle, emDist);
                            float ex2 = ex + Tmp.v1.x;
                            float ey2 = ey + Tmp.v1.y;

                            for (int seg = 1; seg <= 4; seg++) {
                                final int fseg = seg;
                                float segT = ep - fseg * 0.06f;
                                if (segT < 0f)
                                    continue;
                                float segDist = Interp.pow2Out.apply(Mathf.clamp(segT)) * (160f + fem * 5f);
                                Tmp.v2.trns(emAngle, segDist);
                                float segAlpha = efout * (1f - fseg * 0.22f);

                                Draw.color(Color.valueOf("880000"));
                                Draw.alpha(segAlpha * 0.85f);
                                circle(ex + Tmp.v2.x, ey + Tmp.v2.y, (5f - fseg * 0.8f) * efout);

                                Draw.color(Color.valueOf("ff2200"));
                                Draw.alpha(segAlpha * 0.6f);
                                circle(ex + Tmp.v2.x, ey + Tmp.v2.y, (2.5f - fseg * 0.4f) * efout);
                            }

                            Draw.color(Color.valueOf("660000"));
                            Draw.alpha(efout * 0.95f);
                            circle(ex2, ey2, (9f - fem * 0.15f) * efout);

                            Draw.color(Color.valueOf("ff0000"));
                            Draw.alpha(efout * 0.9f);
                            circle(ex2, ey2, (6f - fem * 0.1f) * efout);

                            Draw.color(Color.valueOf("ff4400"));
                            Draw.alpha(efout * 0.8f);
                            circle(ex2, ey2, (3.5f - fem * 0.06f) * efout);

                            Draw.color(Color.valueOf("ff8800"));
                            Draw.alpha(efout * 0.55f);
                            circle(ex2, ey2, 1.8f * efout);

                            if (ep > 0.7f) {
                                float ashP = (ep - 0.7f) * 3.33f;

                                Draw.color(Color.valueOf("331111"));
                                Draw.alpha(efout * ashP * 0.75f);
                                Lines.stroke(2f * efout * ashP);
                                Lines.circle(ex2, ey2, ashP * 18f);

                                Draw.color(Color.valueOf("220808"));
                                Draw.alpha(efout * ashP * 0.6f);
                                for (int a = 0; a < 3; a++) {
                                    float aa = emAngle + a * 120f + ep * 180f;
                                    Tmp.v2.trns(aa, ashP * 10f);
                                    circle(ex2 + Tmp.v2.x, ey2 + Tmp.v2.y, 2f * efout * ashP);
                                }
                            }

                            Drawf.light(ex2, ey2, (50f - fem * 0.8f) * efout, Color.valueOf("ff2200"), 0.82f);
                        });
                    }

                    e.scaled(60f, ring1 -> {
                        float rp = ring1.fin(Interp.pow2Out);
                        float rfout = ring1.fout();

                        Draw.color(Color.valueOf("ff0033"), Color.valueOf("dd1111"), rfout * 0.4f);
                        Lines.stroke(12f * rfout);
                        Lines.circle(ex, ey, 10f + rp * 180f);

                        Draw.color(Color.valueOf("dd0000"));
                        Lines.stroke(6f * rfout);
                        Lines.circle(ex, ey, 8f + rp * 160f);

                        Draw.color(Color.valueOf("cc1111"));
                        Lines.stroke(2.5f * rfout);
                        Lines.circle(ex, ey, 6f + rp * 145f);

                        Drawf.light(ex, ey, rfout * 300f, Color.valueOf("ff0033"), 0.88f);
                    });

                    e.scaled(90f, ring2 -> {
                        if (ring2.time < 20f)
                            return;
                        float rp = ((ring2.time - 20f) / 70f);
                        rp = Mathf.clamp(rp);
                        float rfout = 1f - rp;

                        Draw.color(Color.valueOf("cc0022"), Color.valueOf("bb0000"), rfout * 0.35f);
                        Lines.stroke(9f * rfout);
                        Lines.circle(ex, ey, 10f + Interp.pow2Out.apply(rp) * 220f);

                        Draw.color(Color.valueOf("ff1133"));
                        Lines.stroke(4.5f * rfout);
                        Lines.circle(ex, ey, 8f + Interp.pow2Out.apply(rp) * 200f);

                        Drawf.light(ex, ey, rfout * 200f, Color.valueOf("ff0033"), 0.75f);
                    });

                    e.scaled(130f, ring3 -> {
                        if (ring3.time < 45f)
                            return;
                        float rp = ((ring3.time - 45f) / 85f);
                        rp = Mathf.clamp(rp);
                        float rfout = 1f - rp;

                        Draw.color(Color.valueOf("880022"), Color.valueOf("cc0000"), rfout * 0.3f);
                        Lines.stroke(7f * rfout);
                        Lines.circle(ex, ey, 10f + Interp.pow3Out.apply(rp) * 260f);

                        Draw.color(Color.valueOf("cc0033"));
                        Lines.stroke(3.5f * rfout);
                        Lines.circle(ex, ey, 8f + Interp.pow3Out.apply(rp) * 240f);

                        Drawf.light(ex, ey, rfout * 150f, Color.valueOf("cc0022"), 0.65f);
                    });

                    for (int d = 0; d < 40; d++) {
                        final int fd = d;
                        float dStart = 40f + fd * 2f;
                        float dDur = 120f + fd * 1.5f;
                        e.scaled(dStart + dDur, drop -> {
                            if (drop.time < dStart)
                                return;
                            float dp = (drop.time - dStart) / dDur;
                            float dfout = 1f - dp;

                            float dropAngle = fd * (360f / 40f) + Mathf.sin(fd * 41f) * 25f;
                            float dropDist = dp * (100f + fd * 4f);
                            float height = Mathf.sin(dp * Mathf.PI) * (70f + fd * 3f);

                            Tmp.v1.trns(dropAngle, dropDist);
                            float dx = ex + Tmp.v1.x;
                            float dy = ey + Tmp.v1.y + height;

                            Drawf.light(dx, dy, 35f * dfout, Color.valueOf("ff0033"), 0.7f);

                            Draw.color(Color.valueOf("770018"));
                            Draw.alpha(dfout * 0.9f);
                            circle(dx, dy, (6f + fd * 0.1f) * dfout);

                            Draw.color(Color.valueOf("ff1133"));
                            Draw.alpha(dfout * 0.8f);
                            circle(dx, dy, (4f + fd * 0.08f) * dfout);

                            Draw.color(Color.valueOf("cc1111"));
                            Draw.alpha(dfout * 0.45f);
                            circle(dx - 0.7f, dy + 0.7f, 1.8f * dfout);

                            if (dp > 0.65f) {
                                float splat = (dp - 0.65f) * 2.85f;
                                Draw.color(Color.valueOf("cc0033"));
                                Draw.alpha(dfout * splat * 0.75f);
                                Lines.stroke(1.8f * dfout * splat);
                                Lines.circle(ex + Tmp.v1.x, ey + Tmp.v1.y, splat * 12f);
                            }
                        });
                    }

                    e.scaled(195f, fog -> {
                        float ff2 = fog.fout();
                        for (int p = 0; p < 12; p++) {
                            final int fp = p;
                            float fogAngle = fp * 30f + fog.time * (0.45f + fp * 0.07f);
                            float fogDist = fog.fin(Interp.pow3Out) * (150f + fp * 14f);
                            Tmp.v1.trns(fogAngle, fogDist);

                            Draw.color(Color.valueOf("1a0008"));
                            Draw.alpha(ff2 * (0.88f - fp * 0.05f));
                            circle(ex + Tmp.v1.x, ey + Tmp.v1.y,
                                    (28f - fp * 1.2f) * ff2 * (1f + Mathf.absin(fog.time, 6f, 0.16f)));

                            Draw.color(Color.valueOf("660018"));
                            Draw.alpha(ff2 * 0.5f);
                            circle(ex + Tmp.v1.x, ey + Tmp.v1.y, (14f - fp * 0.7f) * ff2);

                            Drawf.light(ex + Tmp.v1.x, ey + Tmp.v1.y, 50f * ff2, Color.valueOf("ff0033"), 0.62f);
                        }
                    });

                    Drawf.light(ex, ey, e.fout() * 400f, Color.valueOf("880022"), 0.97f);
                });

                despawnHit = true;

                pierce = true;
                pierceCap = 7;
                pierceBuilding = true;

                splashDamageRadius = 190f;
                splashDamage = damage * 1.08f;

                knockback = 15f;
                hitShake = 16f;

                status = StatusEffects.burning;
                statusDuration = 380f;

                homingPower = 0.07f;
                homingRange = 160f;

                despawnHit = true;
                drag = -0.004f;
                weaveMag = 1.5f;
                weaveScale = 8f;

                fragOnHit = true;
                fragBullets = 18;
                fragVelocityMin = 0.75f;
                fragVelocityMax = 1.95f;
                fragBullet = new BasicBulletType(8f, 210) {
                    {
                        width = 7f;
                        height = 16f;
                        lifetime = 34f;

                        backColor = Color.valueOf("330011");
                        frontColor = Color.valueOf("ff1133");
                        lightColor = Color.valueOf("ff0033");
                        lightRadius = 48f;

                        trailLength = 8;
                        trailWidth = 2.8f;
                        trailColor = Color.valueOf("880022");

                        splashDamageRadius = 28f;
                        splashDamage = 85f;

                        status = StatusEffects.burning;
                        statusDuration = 170f;

                        hitEffect = new Effect(42f, e -> {
                            float fout = e.fout();

                            Draw.color(Color.valueOf("ff1133"), Color.valueOf("cc1111"), fout * 0.3f);
                            Lines.stroke(fout * 1.8f);
                            Angles.randLenVectors(e.id, 20, e.finpow() * 55f, (x, y) -> {
                                float ang = Mathf.angle(x, y);
                                Lines.lineAngle(e.x + x, e.y + y, ang, fout * 8f + 3f);
                            });

                            Draw.color(Color.valueOf("cc0033"));
                            Draw.alpha(fout * 0.85f);
                            circle(e.x, e.y, fout * 14f);

                            Draw.color(Color.valueOf("cc0000"));
                            Draw.alpha(fout * 0.7f);
                            circle(e.x, e.y, fout * 9f);

                            Draw.color(Color.valueOf("ffffff"));
                            Draw.alpha(fout * 0.55f);
                            circle(e.x, e.y, fout * 5f);

                            Drawf.light(e.x, e.y, 60f * fout, Color.valueOf("ff0033"), 0.85f);
                        });
                    }
                };

            }

            @Override
            public void draw(Bullet b) {

                float pressure = Mathf.absin(b.time, 7f, 1f) + Mathf.absin(b.time * 1.8f, 3f, 0.4f);
                float scale = 1f + pressure * 0.12f;

                Draw.z(Layer.bullet + 0.002f);

                Draw.color(Color.valueOf("880022"));
                Draw.alpha(0.42f + pressure * 0.1f);
                circle(b.x, b.y, (36f + pressure * 5f) * scale);

                Draw.color(Color.valueOf("550011"));
                Draw.alpha(0.75f + pressure * 0.1f);
                circle(b.x, b.y, (26f + pressure * 3f) * scale);

                Draw.color(Color.valueOf("ff0033"));
                Draw.alpha(0.95f);
                circle(b.x, b.y, (16f + pressure * 2f) * scale);

                Draw.color(Color.valueOf("cc0000"));
                Draw.alpha(0.85f + pressure * 0.12f);
                circle(b.x, b.y, (10f + pressure * 2f) * scale);

                for (int i = 0; i < 6; i++) {
                    final int fi = i;
                    float arcAngle = (b.time * 1.3f) + fi * 60f + Mathf.sin(b.time * 0.08f + fi) * 18f;
                    float arcRadius = (12f + Mathf.absin(b.time + fi * 22f, 7f, 3.5f)) * scale;
                    float arcStart = arcAngle - 35f;
                    float arcSweep = 70f + pressure * 10f;

                    Draw.color(Color.valueOf("440011"));
                    Draw.alpha(0.9f);
                    Lines.stroke(3f * scale + pressure * 0.3f);
                    Lines.arc(b.x, b.y, arcRadius, arcSweep / 360f, arcStart);

                    Draw.color(Color.valueOf("ff0033"));
                    Draw.alpha(0.85f + pressure * 0.12f);
                    Lines.stroke(1.7f * scale);
                    Lines.arc(b.x, b.y, arcRadius, arcSweep / 360f, arcStart);

                    Draw.color(Color.valueOf("dd1111"));
                    Draw.alpha(0.5f + pressure * 0.18f);
                    Lines.stroke(0.7f);
                    Lines.arc(b.x, b.y, arcRadius, arcSweep / 360f, arcStart);

                    Drawf.light(
                            b.x + Angles.trnsx(arcAngle, arcRadius),
                            b.y + Angles.trnsy(arcAngle, arcRadius),
                            28f + pressure * 10f, Color.valueOf("ff0033"), 0.78f);
                }

                Draw.color(Color.valueOf("bb0000"));
                Draw.alpha(0.88f + pressure * 0.1f);
                for (int i = 0; i < 6; i++) {
                    float a = (b.time * 1.3f) + i * 60f;
                    float r = (12f + Mathf.absin(b.time + i * 22f, 7f, 3.5f)) * scale;
                    circle(
                            b.x + Angles.trnsx(a, r),
                            b.y + Angles.trnsy(a, r),
                            (3f + pressure * 1.2f) * scale);
                }

                Draw.color(Color.valueOf("dd0000"));
                Draw.alpha(0.92f + pressure * 0.08f);
                circle(b.x, b.y, (6f + pressure * 2.5f) * scale);

                Draw.color(Color.valueOf("aa0000"));
                Draw.alpha(0.7f + pressure * 0.2f);
                circle(b.x, b.y, (3.5f + pressure * 1.5f) * scale);

                Draw.color(Color.valueOf("ffffff"));
                Draw.alpha(0.55f + pressure * 0.28f);
                circle(b.x, b.y, (2f + pressure * 0.8f) * scale);

                Drawf.light(b.x, b.y,
                        (115f + pressure * 45f) * scale,
                        Color.valueOf("ff0033"), 0.96f);

                Draw.reset();
            }
        };

        guidedCrimsonLance = new BasicBulletType(8.5f, 680) {
            {
                lifetime = 170f;

                width = 12f;
                height = 32f;
                sprite = "missile-large";

                shrinkY = 0.1f;
                shrinkX = 0f;

                backColor = Color.valueOf("ff2222");
                frontColor = Color.valueOf("ffffff");
                lightColor = Color.valueOf("ff3333");
                lightOpacity = 1f;
                lightRadius = 65f;

                trailLength = 18;
                trailWidth = 4.5f;
                trailColor = Color.valueOf("ff3333");
                trailInterval = 1.2f;

                trailEffect = new Effect(35f, e -> {
                    Draw.color(Color.valueOf("ff3333"));
                    Draw.alpha(e.fout() * 0.8f);
                    circle(e.x, e.y, e.fout() * 6f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 3.5f);

                    Draw.color(Color.valueOf("ff4444"));
                    Draw.alpha(e.fout() * 0.7f);
                    Lines.stroke(1.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fout() * 8f);
                });

                trailChance = 0.6f;

                hitEffect = new Effect(50f, 140f, e -> {
                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 15f);

                    Draw.color(Color.valueOf("ff3333"), Color.white, e.fin() * 0.5f);

                    e.scaled(22f, s -> {
                        Lines.stroke(4f * s.fout());
                        Lines.circle(e.x, e.y, 5f + s.fin(Interp.pow3Out) * 45f);
                    });

                    e.scaled(35f, s -> {
                        Lines.stroke(2.5f * s.fout());
                        Lines.circle(e.x, e.y, 8f + s.fin(Interp.pow2Out) * 65f);
                    });

                    Draw.color(Color.white, Color.valueOf("ff4444"), e.fin() + 0.25f);
                    Lines.stroke(2.2f * e.fout());
                    Angles.randLenVectors(e.id, 16, 10f + 50f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, angle, e.fout() * (10f + Mathf.random(10f)));
                    });

                    Draw.color(Color.valueOf("ff6666"), Color.white, e.fout() * 0.7f);
                    Lines.stroke(1.2f * e.fout());
                    Angles.randLenVectors(e.id + 1, 12, 6f + 35f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 14f);
                    });

                    Angles.randLenVectors(e.id + 2, 10, 4f + 28f * e.finpow(), (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("ff3333"), e.fin() * 0.8f);
                        circle(e.x + x, e.y + y, e.fout() * 4f);
                    });

                    Draw.color(Color.valueOf("ff2222"));
                    Angles.randLenVectors(e.id + 3, 8, 20f + 45f * e.fin(), (x, y) -> {
                        Lines.stroke(1.5f * e.fout());
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 16f);
                    });

                    Drawf.light(e.x, e.y, e.fout() * 85f, Color.valueOf("ff3333"), 0.85f);

                    for (int i = 0; i < 6; i++) {
                        float angle = i * 60f + e.fin() * 30f;
                        float dst = e.finpow() * 55f;
                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("ff4444"), Color.white, e.fout() * 0.6f);
                        Lines.stroke(1.8f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle + 90f, e.fout() * 12f);
                    }
                });

                despawnHit = true;

                shootEffect = new Effect(25f, e -> {
                    Draw.color(Color.white, Color.valueOf("ff3333"), e.fin() * 0.7f);

                    Lines.stroke(e.fout() * 3f);
                    Angles.randLenVectors(e.id, 10, 30f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 8f + 3f);
                    });

                    circle(e.x, e.y, e.fout() * 8f);

                    Draw.color(Color.valueOf("ff4444"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 18f);

                    Drawf.light(e.x, e.y, e.fout() * 45f, Color.valueOf("ff3333"), 0.7f);
                });

                smokeEffect = new Effect(30f, e -> {
                    Draw.color(Color.valueOf("ff3333"));
                    Draw.alpha(e.fout() * 0.6f);
                    circle(e.x, e.y, e.fout() * 5f);

                    Lines.stroke(1.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 12f);
                });

                pierce = false;
                pierceBuilding = false;

                splashDamageRadius = 55f;
                splashDamage = damage * 0.9f;

                knockback = 6f;
                hitShake = 5f;

                status = StatusEffects.burning;
                statusDuration = 180f;

                homingPower = 0.12f;
                homingRange = 285f;
                homingDelay = 5f;

                trailRotation = true;
                despawnHit = true;

                weaveMag = 3.5f;
                weaveScale = 4f;
            }
        };

        absoluteInferno = new BasicBulletType(7f, 1090) {
            {
                lifetime = 100f;

                width = 28f;
                height = 28f;
                sprite = "large-bomb";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("660000");
                frontColor = Color.white;
                lightColor = Color.valueOf("ff3333");
                lightOpacity = 1f;
                lightRadius = 180f;

                trailLength = 24;
                trailWidth = 8f;
                trailColor = Color.valueOf("ff2222");
                trailInterval = 2f;

                trailEffect = new Effect(65f, e -> {
                    Draw.color(Color.valueOf("330000"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, (20f + Mathf.absin(e.time, 2f, 4f)) * e.fout());

                    Draw.color(Color.valueOf("990000"));
                    Draw.alpha(e.fout() * 0.8f);
                    circle(e.x, e.y, (14f + Mathf.absin(e.time, 2f, 3f)) * e.fout());

                    Draw.color(Color.valueOf("ff2222"));
                    circle(e.x, e.y, (10f + Mathf.absin(e.time, 2f, 2f)) * e.fout());

                    Draw.color(Color.valueOf("ffffff"));
                    circle(e.x, e.y, (5f + Mathf.absin(e.time, 2f, 1f)) * e.fout());

                    float xVal = e.x;
                    float yVal = e.y;
                    float foutVal = e.fout();
                    float timeVal = e.time;

                    for (int i = 0; i < 6; i++) {
                        final int fi = i;
                        float angle = fi * 60f + timeVal * 1.5f;
                        float rad = foutVal * (12f + Mathf.absin(timeVal + fi, 2f, 3f));

                        Tmp.v1.trns(angle, rad);

                        Draw.color(Color.valueOf("ff4444"));
                        Draw.alpha(foutVal * 0.8f);

                        for (int j = 0; j < 3; j++) {
                            final int fj = j;
                            float off = fj * 120f;
                            Tmp.v2.trns(angle + off, foutVal * 2f);
                            circle(xVal + Tmp.v1.x + Tmp.v2.x, yVal + Tmp.v1.y + Tmp.v2.y, foutVal * 2.5f);
                        }
                    }

                    Angles.randLenVectors(e.id, 8, 4f + e.fin() * 14f, (x, y) -> {
                        Draw.color(Color.valueOf("ff6666"), Color.valueOf("ff2222"), Mathf.random());
                        circle(xVal + x, yVal + y, foutVal * 2f);
                    });
                });

                trailChance = 0.85f;

                hitEffect = new Effect(140f, 450f, e -> {

                    Draw.color(Color.valueOf("220000"));
                    Draw.alpha(e.fout() * 0.5f);
                    circle(e.x, e.y, e.finpow() * 280f);

                    Draw.color(Color.valueOf("660000"));
                    Draw.alpha(e.fout() * 0.6f);
                    circle(e.x, e.y, e.finpow() * 240f);

                    Draw.color(Color.valueOf("aa0000"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.finpow() * 200f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, (55f + Mathf.absin(e.time, 1.5f, 10f)) * e.fout());

                    Draw.color(Color.valueOf("ff4444"));
                    circle(e.x, e.y, (45f + Mathf.absin(e.time, 1.5f, 8f)) * e.fout());

                    Draw.color(Color.valueOf("ff2222"));
                    circle(e.x, e.y, (35f + Mathf.absin(e.time, 1.5f, 6f)) * e.fout());

                    float exVal = e.x;
                    float eyVal = e.y;

                    for (int w = 0; w < 10; w++) {
                        final int fw = w;
                        e.scaled(20f + fw * 12f, wave -> {
                            Lines.stroke((10f - fw * 0.9f) * wave.fout());
                            float radius = 25f + wave.fin(Interp.pow2Out) * (130f + fw * 18f);
                            Lines.circle(exVal, eyVal, radius);
                        });
                    }

                    for (int i = 0; i < 36; i++) {
                        final int fi = i;
                        float angle = fi * 10f + e.fin() * 180f;

                        e.scaled(35f + (fi % 4) * 20f, beam -> {
                            float len = beam.finpow() * (100f + Mathf.random(40f));

                            Draw.color(Color.valueOf("ff3333"), Color.white, beam.fout() * 0.9f);
                            Lines.stroke(4f * beam.fout());
                            Lines.lineAngle(exVal, eyVal, angle, len);

                            Tmp.v1.trns(angle, len);
                            circle(exVal + Tmp.v1.x, eyVal + Tmp.v1.y, beam.fout() * 7f);
                        });
                    }

                    Angles.randLenVectors(e.id, 40, 30f + 140f * e.finpow(), (x, y) -> {
                        Draw.color(Color.valueOf("ff2222"));
                        Draw.alpha(e.fout() * 0.85f);
                        circle(exVal + x, eyVal + y, e.fout() * 14f);

                        Draw.color(Color.valueOf("ff6666"));
                        circle(exVal + x, eyVal + y, e.fout() * 7f);
                    });

                    Drawf.light(e.x, e.y, e.fout() * 320f, Color.valueOf("ff3333"), 1f);
                });

                despawnHit = true;

                shootEffect = new Effect(55f, e -> {
                    Draw.color(Color.valueOf("660000"));
                    Draw.alpha(e.fin() * 0.95f);

                    float xVal = e.x;
                    float yVal = e.y;
                    float finVal = e.fin();
                    float foutVal = e.fout();

                    for (int i = 0; i < 7; i++) {
                        final int fi = i;
                        float rot = e.time * (3f + fi);
                        Tmp.v1.trns(rot * 60f, (14f + fi * 6f) * finVal);
                        circle(xVal + Tmp.v1.x, yVal + Tmp.v1.y, finVal * (16f - fi * 2f));
                    }

                    Draw.color(Color.valueOf("ff3333"), Color.white, e.fin() * 0.5f);

                    for (int i = 0; i < 9; i++) {
                        final int fi = i;
                        float delay = fi * 0.1f;
                        if (e.fin() > delay) {
                            float progress = (e.fin() - delay) / (1f - delay);
                            Lines.stroke((6f - fi * 0.65f) * (1f - progress));
                            Lines.circle(e.x, e.y, (90f - fi * 9f) * (1f - progress));
                        }
                    }

                    Draw.color(Color.white);
                    circle(e.x, e.y, foutVal * 24f);

                    Drawf.light(e.x, e.y, foutVal * 100f, Color.valueOf("ff3333"), 0.9f);
                });

                smokeEffect = new Effect(70f, e -> {
                    Draw.color(Color.valueOf("440000"));
                    Draw.alpha(e.fout() * 0.75f);

                    for (int i = 0; i < 5; i++) {
                        float angle = e.time * (2.2f + i * 0.8f);
                        Tmp.v1.trns(angle * 50f, (10f + i * 5f) * e.fout());
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * (14f - i * 2f));
                    }

                    Draw.color(Color.valueOf("ff3333"));
                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 24f);
                });

                pierce = true;
                pierceCap = 5;
                pierceBuilding = true;

                splashDamageRadius = 140f;
                splashDamage = damage * 0.95f;

                knockback = 12f;
                hitShake = 9f;

                status = StatusEffects.burning;
                statusDuration = 300f;

                homingPower = 0.08f;
                homingRange = 180f;

                despawnHit = true;

                drag = -0.005f;
                weaveMag = 0f;
                weaveScale = 0f;

                fragOnHit = true;
                fragBullets = 24;

                fragBullet = new BasicBulletType(6f, 85) {
                    {
                        width = 9f;
                        height = 14f;
                        lifetime = 30f;

                        backColor = Color.valueOf("990000");
                        frontColor = Color.white;
                        lightColor = Color.valueOf("ff3333");
                        lightRadius = 35f;

                        trailLength = 8;
                        trailWidth = 2.5f;
                        trailColor = Color.valueOf("ff2222");

                        splashDamageRadius = 22f;
                        splashDamage = 40f;

                        status = StatusEffects.burning;
                        statusDuration = 120f;

                        hitEffect = new Effect(25f, e -> {
                            Draw.color(Color.valueOf("ff3333"));
                            circle(e.x, e.y, e.fout() * 10f);

                            Draw.color(Color.white);
                            circle(e.x, e.y, e.fout() * 5f);

                            Lines.stroke(2.5f * e.fout());
                            Lines.circle(e.x, e.y, e.fin() * 20f);
                        });
                    }
                };

                fragVelocityMin = 0.8f;
                fragVelocityMax = 1.6f;
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

        verdantCollapse = new BasicBulletType(7f, 8500) {
            public BulletType damageLogicField;

            {

                lifetime = 130f;

                width = 24f;
                height = 42f;
                sprite = "missile-large";

                shrinkX = 0f;
                shrinkY = 0f;

                backColor = Color.valueOf("1a5c3a");
                frontColor = Color.valueOf("75ffb0");
                lightColor = Color.valueOf("75ffb0");
                lightOpacity = 1f;
                lightRadius = 260f;

                trailLength = 28;
                trailWidth = 10f;
                trailColor = Color.valueOf("44cc88");
                trailInterval = 1f;

                trailEffect = JBFx.polyTrail(
                        Color.valueOf("75ffb0"),
                        Color.valueOf("1a5c3a"),
                        18f, 60f);
                trailChance = 0.85f;

                shootEffect = JBFx.crossBlast(Color.valueOf("75ffb0"), 100f);

                smokeEffect = new Effect(45f, e -> {
                    Draw.color(Color.valueOf("1a5c3a"));
                    Angles.randLenVectors(e.id, 6, 2f + 19f * e.finpow(),
                            (x, y) -> circle(e.x + x / 2f, e.y + y / 2f, e.fout() * 2.5f));
                    e.scaled(28f, i -> Angles.randLenVectors(i.id, 6, 2f + 19f * i.finpow(),
                            (x, y) -> circle(e.x + x, e.y + y, i.fout() * 5f)));
                });

                despawnEffect = new Effect(460f, 1400f, e -> {
                    final float ex = e.x;
                    final float ey = e.y;
                    final float etime = e.time;

                    e.scaled(20f, col -> {
                        float cf = col.fin(Interp.pow3Out);

                        Draw.color(Color.valueOf("75ffb0"));
                        Angles.randLenVectors(col.id, 32, (1f - cf) * 220f, (x, y) -> {
                            Draw.alpha(cf * 0.9f);
                            circle(ex + x * (1f - cf), ey + y * (1f - cf), (1f - cf) * 7f + 1f);
                        });

                        Draw.color(Color.valueOf("75ffb0"), Color.white, cf * 0.4f);
                        for (int i = 0; i < 4; i++) {
                            final int fi = i;
                            float delay = fi * 0.18f;
                            if (col.fin() > delay) {
                                float p = (col.fin() - delay) / (1f - delay);
                                Lines.stroke((9f - fi * 1.5f) * (1f - p));
                                Lines.circle(ex, ey, (180f - fi * 35f) * (1f - p));
                            }
                        }

                        Draw.color(Color.black);
                        Draw.alpha(cf);
                        circle(ex, ey, cf * 40f);

                        Draw.color(Color.valueOf("75ffb0"));
                        Draw.alpha(col.fout());
                        Lines.stroke(6f * col.fout());
                        Lines.circle(ex, ey, cf * 40f + 1f);
                        Drawf.light(ex, ey, cf * 450f, Color.valueOf("75ffb0"), 1f);
                    });

                    if (etime >= 5f && etime < 5f + Time.delta) {
                        JBSounds.shock.at(ex, ey, 0.85f, 1.1f);
                    }

                    for (int wave = 0; wave < 5; wave++) {
                        final int fw = wave;
                        float wStart = 5f + fw * 16f;
                        float wDur = 265f;

                        e.scaled(wStart + wDur, wv -> {
                            if (wv.time < wStart)
                                return;
                            float wp = Math.min((wv.time - wStart) / wDur, 1f);
                            float wfo = 1f - wp;

                            float waveRot = fw * 13f + wv.time * (fw % 2 == 0 ? 0.18f : -0.22f);
                            int seed = wv.id * 31 + fw * 7;

                            Angles.randLenVectors(seed, 16,
                                    40f + Interp.pow2Out.apply(wp) * (420f + fw * 55f),
                                    (x, y) -> {
                                        float baseAngle = Mathf.angle(x, y) + waveRot;
                                        float dist = Mathf.len(x, y);

                                        float blastLen = (280f + dist * 0.6f) * wfo
                                                * Interp.pow2OutInverse.apply(wfo);
                                        float blastW = (22f - fw * 2f) * wfo * wfo;

                                        float bx = ex + x, by = ey + y;

                                        Draw.color(Color.valueOf("0a2e18"));
                                        Draw.alpha(wfo * 0.95f);
                                        Drawf.tri(bx, by, blastW * 1.4f, blastLen * 1.05f, baseAngle);
                                        Drawf.tri(bx, by, blastW * 0.9f, blastLen * 0.25f, baseAngle + 180f);

                                        Draw.color(Color.valueOf("75ffb0"));
                                        Draw.alpha(wfo * 0.93f);
                                        Drawf.tri(bx, by, blastW, blastLen, baseAngle);
                                        Drawf.tri(bx, by, blastW * 0.65f, blastLen * 0.22f, baseAngle + 180f);

                                        Draw.color(Color.valueOf("eeffee"));
                                        Draw.alpha(wfo * 0.72f);
                                        Drawf.tri(bx, by, blastW * 0.38f, blastLen * 0.85f, baseAngle);

                                        Tmp.v1.trns(baseAngle, blastLen);
                                        Drawf.light(bx + Tmp.v1.x, by + Tmp.v1.y,
                                                65f * wfo, Color.valueOf("75ffb0"), 0.82f);
                                    });

                            Drawf.light(ex, ey, 200f * wfo, Color.valueOf("44cc88"), 0.7f);
                        });
                    }

                    e.scaled(235f, core -> {
                        if (core.time < 5f)
                            return;

                        float cfo = core.time < 220f
                                ? 1f
                                : 1f - (core.time - 220f) / 15f;
                        cfo = Math.max(cfo, 0f);

                        float pulse = Mathf.absin(core.time, 4f, 1f);

                        Draw.color(Color.black);
                        Draw.alpha(cfo * 0.85f);
                        circle(ex, ey, (28f + pulse * 3f) * cfo);

                        Draw.color(Color.valueOf("75ffb0"));
                        Draw.alpha(cfo * 0.95f);
                        Lines.stroke((5f + pulse * 1.5f) * cfo);
                        Lines.circle(ex, ey, (32f + pulse * 4f) * cfo);

                        Draw.color(Color.valueOf("44cc88"));
                        Draw.alpha(cfo * 0.8f);
                        Lines.stroke(3f * cfo);
                        Lines.arc(ex, ey, (24f + pulse * 2f) * cfo, 0.65f, core.time * 2.8f);

                        Draw.color(Color.valueOf("eeffee"));
                        Draw.alpha(cfo * 0.7f);
                        Lines.stroke(2f * cfo);
                        Lines.arc(ex, ey, (16f + pulse * 2f) * cfo, 0.5f, -core.time * 3.5f);

                        Draw.color(Color.valueOf("75ffb0"));
                        Draw.alpha(cfo * 0.9f);
                        circle(ex, ey, (10f + pulse * 2f) * cfo);

                        Draw.color(Color.valueOf("eeffee"));
                        Draw.alpha(cfo * 0.75f);
                        circle(ex, ey, (5f + pulse) * cfo);

                        Draw.color(Color.white);
                        Draw.alpha(cfo * 0.55f);
                        circle(ex, ey, (2.5f + pulse * 0.5f) * cfo);

                        for (int i = 0; i < 4; i++) {
                            float a = core.time * 4f + i * 90f;
                            float r = (20f + pulse * 3f) * cfo;
                            float sx = ex + Angles.trnsx(a, r);
                            float sy = ey + Angles.trnsy(a, r);
                            Draw.color(Color.valueOf("75ffb0"));
                            Draw.alpha(cfo * 0.9f);
                            circle(sx, sy, (3.5f + pulse) * cfo);
                            Draw.color(Color.white);
                            Draw.alpha(cfo * 0.6f);
                            circle(sx, sy, (1.5f + pulse * 0.5f) * cfo);
                            Drawf.light(sx, sy, 20f * cfo, Color.valueOf("75ffb0"), 0.8f);
                        }
                        Drawf.light(ex, ey, (80f + pulse * 25f) * cfo, Color.valueOf("75ffb0"), 0.95f);
                    });

                    e.scaled(230f, ring -> {
                        if (ring.time < 5f)
                            return;
                        float rTime = ring.time;
                        float ringRadius, ringAlpha, ringStroke;

                        if (rTime < 195f) {
                            float pulse = Mathf.absin(rTime, 6f, 1f);
                            ringRadius = 380f + pulse * 10f;
                            ringAlpha = 0.55f + pulse * 0.1f;
                            ringStroke = 3f + pulse * 1f;
                        } else {
                            float colP = (rTime - 195f) / 35f;
                            ringRadius = (1f - Interp.pow2In.apply(colP)) * 380f;
                            ringAlpha = 0.9f;
                            ringStroke = 4f + colP * 6f;
                        }

                        if (ringRadius > 1f) {
                            Draw.color(Color.valueOf("0a2e18"));
                            Draw.alpha(ringAlpha * 0.8f);
                            Lines.stroke(ringStroke * 2.2f);
                            Lines.circle(ex, ey, ringRadius);

                            Draw.color(Color.valueOf("75ffb0"));
                            Draw.alpha(ringAlpha);
                            Lines.stroke(ringStroke);
                            Lines.circle(ex, ey, ringRadius);

                            Draw.color(Color.valueOf("eeffee"));
                            Draw.alpha(ringAlpha * 0.6f);
                            Lines.stroke(ringStroke * 0.4f);
                            Lines.circle(ex, ey, ringRadius);

                            for (int i = 0; i < 8; i++) {
                                float ma = i * 45f + rTime * (rTime < 195f ? 0.5f : 5f);
                                float mx = ex + Angles.trnsx(ma, ringRadius);
                                float my = ey + Angles.trnsy(ma, ringRadius);
                                Draw.color(Color.valueOf("75ffb0"));
                                Draw.alpha(ringAlpha);
                                circle(mx, my, ringStroke * 1.8f);
                                Draw.color(Color.white);
                                Draw.alpha(ringAlpha * 0.7f);
                                circle(mx, my, ringStroke * 0.8f);
                            }
                            Drawf.light(ex, ey, ringRadius * 0.5f, Color.valueOf("75ffb0"), 0.3f);
                        }
                    });

                    e.scaled(230f, ltng -> {
                        if (ltng.time < 10f)
                            return;
                        float lfo = ltng.fout();

                        Draw.color(Color.valueOf("75ffb0"), Color.white, lfo * 0.35f);
                        Lines.stroke(lfo * 2.5f);
                        Angles.randLenVectors(ltng.id + 3, 10,
                                15f + ltng.fin(Interp.pow3Out) * 180f,
                                (x, y) -> Lines.lineAngle(ex + x, ey + y,
                                        Mathf.angle(x, y), lfo * 25f + 8f));

                        Draw.color(Color.valueOf("ccffdd"), Color.white, lfo * 0.5f);
                        Lines.stroke(lfo * 1.5f);
                        Angles.randLenVectors(ltng.id + 17, 12,
                                8f + ltng.fin() * 90f,
                                (x, y) -> Lines.lineAngle(ex + x, ey + y,
                                        Mathf.angle(x, y), lfo * 18f + 5f));

                        Drawf.light(ex, ey, 80f * lfo, Color.valueOf("75ffb0"), 0.6f);
                    });

                    if (etime >= 230f && etime < 230f + Time.delta) {
                        JBSounds.hugeBlast.at(ex, ey, 1f, 1f);

                        JBFx.blast(Color.valueOf("75ffb0"), 340f).at(ex, ey);
                        JBFx.circleOut(Color.valueOf("75ffb0"), 500f).at(ex, ey);
                        JBFx.circleOut(Color.valueOf("44cc88"), 380f).at(ex, ey);
                        JBFx.crossBlast(Color.valueOf("75ffb0"), 240f).at(ex, ey);
                        JBFx.hitSparkHuge.at(ex, ey, 0f, Color.valueOf("75ffb0"));

                        for (int i = 0; i < 8; i++) {
                            float la = i * 45f;
                            float ld = 100f + (i % 3) * 60f;
                            JBFx.lightningHitLarge(Color.valueOf("75ffb0"))
                                    .at(ex + Angles.trnsx(la, ld),
                                            ey + Angles.trnsy(la, ld));
                        }
                    }

                    e.scaled(460f, boom -> {
                        if (boom.time < 230f)
                            return;

                        float bp = Math.min((boom.time - 230f) / 140f, 1f);
                        float bfo = 1f - bp;

                        e.scaled(255f, flash -> {
                            if (flash.time < 230f)
                                return;
                            float ff = 1f - Math.min((flash.time - 230f) / 22f, 1f);
                            Draw.color(Color.white);
                            Draw.alpha(ff * 0.95f);
                            circle(ex, ey, ff * 500f);
                            Draw.color(Color.valueOf("eeffee"));
                            Draw.alpha(ff * 0.85f);
                            circle(ex, ey, ff * 420f);
                            Draw.color(Color.valueOf("75ffb0"));
                            Draw.alpha(ff * 0.7f);
                            circle(ex, ey, ff * 360f);
                            Drawf.light(ex, ey, ff * 1000f, Color.valueOf("75ffb0"), 1f);
                        });

                        for (int wv = 0; wv < 5; wv++) {
                            final int fwv = wv;
                            float wvDelay = fwv * 0.1f;
                            if (bp < wvDelay)
                                continue;
                            float wvP = Math.min((bp - wvDelay) / (1f - wvDelay), 1f);
                            float wvFo = 1f - wvP;

                            Draw.color(Color.valueOf("0a2e18"));
                            Draw.alpha(wvFo * 0.85f);
                            Lines.stroke((20f - fwv * 3f) * wvFo);
                            Lines.circle(ex, ey, 10f + Interp.pow3Out.apply(wvP) * (520f + fwv * 70f));

                            Draw.color(Color.valueOf("75ffb0"));
                            Draw.alpha(wvFo * 0.9f);
                            Lines.stroke((12f - fwv * 1.8f) * wvFo);
                            Lines.circle(ex, ey, 8f + Interp.pow3Out.apply(wvP) * (500f + fwv * 70f));

                            Draw.color(Color.white);
                            Draw.alpha(wvFo * 0.55f);
                            Lines.stroke((5f - fwv * 0.8f) * wvFo);
                            Lines.circle(ex, ey, 6f + Interp.pow3Out.apply(wvP) * (482f + fwv * 70f));

                            Drawf.light(ex, ey,
                                    wvFo * (420f - fwv * 40f), Color.valueOf("75ffb0"), 0.9f);
                        }

                        Angles.randLenVectors(boom.id + 44, 32,
                                100f + Interp.pow2Out.apply(bp) * 460f,
                                (x, y) -> {
                                    float ang = Mathf.angle(x, y);
                                    Fx.rand.setSeed(boom.id + (long) (x * 100));
                                    float rLen = Fx.rand.random(360f, 580f) * bfo;
                                    float rW = Fx.rand.random(18f, 44f) * bfo * bfo;

                                    Draw.color(Color.valueOf("0a2e18"));
                                    Draw.alpha(bfo * 0.95f);
                                    Drawf.tri(ex + x, ey + y, rW * 1.35f, rLen * 1.08f, ang);
                                    Drawf.tri(ex + x, ey + y, rW * 0.8f, rLen * 0.28f, ang + 180f);

                                    Draw.color(Color.valueOf("75ffb0"));
                                    Draw.alpha(bfo * 0.93f);
                                    Drawf.tri(ex + x, ey + y, rW, rLen, ang);
                                    Drawf.tri(ex + x, ey + y, rW * 0.55f, rLen * 0.24f, ang + 180f);

                                    Draw.color(Color.valueOf("eeffee"));
                                    Draw.alpha(bfo * 0.68f);
                                    Drawf.tri(ex + x, ey + y, rW * 0.42f, rLen * 0.78f, ang);

                                    Drawf.light(ex + x, ey + y, rW * 3f * bfo,
                                            Color.valueOf("75ffb0"), 0.75f);
                                });

                        Angles.randLenVectors(boom.id + 77, 36,
                                25f + Interp.pow2Out.apply(bp) * 350f, (x, y) -> {
                                    float ang = Mathf.angle(x, y);
                                    Fx.rand.setSeed(boom.id * 3L + (long) (x + y * 10));
                                    float ss = Fx.rand.random(8f, 24f) * bfo;
                                    float sl = Fx.rand.random(25f, 70f) * bfo;
                                    float baseRot = Fx.rand.random(-40f, 40f);
                                    float spinRate = Fx.rand.random(40f, 140f);
                                    float sr = ang + baseRot + bp * spinRate;

                                    Draw.color(Color.valueOf("0a2e18"));
                                    Draw.alpha(bfo * 0.92f);
                                    Drawf.tri(ex + x, ey + y, ss * 1.2f, sl * 1.1f, sr);
                                    Drawf.tri(ex + x, ey + y, ss * 0.8f, sl * 0.5f, sr + 180f);

                                    Draw.color(Color.valueOf("44cc88"),
                                            Color.valueOf("75ffb0"), Fx.rand.random(1f));
                                    Draw.alpha(bfo * 0.88f);
                                    Drawf.tri(ex + x, ey + y, ss, sl, sr);
                                    Drawf.tri(ex + x, ey + y, ss * 0.5f, sl * 0.4f, sr + 180f);

                                    Draw.color(Color.valueOf("eeffee"));
                                    Draw.alpha(bfo * 0.55f);
                                    Drawf.tri(ex + x, ey + y, ss * 0.35f, sl * 0.7f, sr);

                                    Drawf.light(ex + x, ey + y, ss * 3f * bfo,
                                            Color.valueOf("75ffb0"), 0.7f);
                                });

                        float expRadius = 480f;

                        for (int li = 0; li < 24; li++) {
                            final int fli = li;
                            Fx.rand.setSeed(boom.id * 11L + fli * 17L);

                            float lAngle = fli * 15f + Fx.rand.random(-7f, 7f);

                            float startDist = expRadius * (0.6f + Fx.rand.random(0.4f));

                            float lx = ex + Angles.trnsx(lAngle, startDist);
                            float ly = ey + Angles.trnsy(lAngle, startDist);

                            float toCenter = startDist * bfo;

                            float dev1 = Fx.rand.random(-18f, 18f);
                            float dev2 = Fx.rand.random(-28f, 28f);

                            Draw.color(Color.valueOf("0a2e18"));
                            Draw.alpha(bfo * 0.7f);
                            Lines.stroke(bfo * 4.5f);
                            Lines.lineAngle(lx, ly, lAngle + 180f + dev1, toCenter);

                            Draw.color(Color.valueOf("75ffb0"), Color.white, bfo * 0.4f);
                            Lines.stroke(bfo * 2.8f);
                            Lines.lineAngle(lx, ly, lAngle + 180f + dev1, toCenter);

                            Draw.color(Color.white);
                            Draw.alpha(bfo * 0.65f);
                            Lines.stroke(bfo * 1.0f);
                            Lines.lineAngle(lx, ly, lAngle + 180f + dev1, toCenter * 0.8f);

                            if (toCenter > 30f) {
                                float midX = lx + Angles.trnsx(lAngle + 180f + dev1, toCenter * 0.45f);
                                float midY = ly + Angles.trnsy(lAngle + 180f + dev1, toCenter * 0.45f);
                                Draw.color(Color.valueOf("ccffdd"));
                                Draw.alpha(bfo * 0.6f);
                                Lines.stroke(bfo * 1.4f);
                                Lines.lineAngle(midX, midY,
                                        lAngle + 180f + dev2,
                                        toCenter * 0.35f * bfo);
                            }

                            Drawf.light(lx, ly, 55f * bfo, Color.valueOf("75ffb0"), 0.75f);
                            Drawf.light(ex, ey, 30f * bfo, Color.valueOf("75ffb0"), 0.5f);
                        }

                        for (int li = 0; li < 16; li++) {
                            final int fli = li;
                            Fx.rand.setSeed(boom.id * 7L + fli * 13L);
                            float lAngle = Fx.rand.random(360f);
                            float lDist = (30f + Fx.rand.random(240f))
                                    * Interp.pow2Out.apply(bp);
                            float lLen = (35f + Fx.rand.random(55f)) * bfo;
                            float lDev = Fx.rand.random(-40f, 40f);

                            float lx = ex + Angles.trnsx(lAngle, lDist);
                            float ly = ey + Angles.trnsy(lAngle, lDist);

                            Draw.color(Color.valueOf("75ffb0"), Color.white, bfo * 0.45f);
                            Lines.stroke(bfo * (2.5f + Fx.rand.random(1.5f)));
                            Lines.lineAngle(lx, ly, lAngle + lDev, lLen);

                            Draw.color(Color.valueOf("ccffdd"));
                            Lines.stroke(bfo * 1.2f);
                            Lines.lineAngle(
                                    lx + Angles.trnsx(lAngle + lDev, lLen * 0.4f),
                                    ly + Angles.trnsy(lAngle + lDev, lLen * 0.4f),
                                    lAngle + lDev + Fx.rand.random(-60f, 60f),
                                    lLen * 0.45f * bfo);

                            Drawf.light(lx, ly, lLen * 0.8f * bfo,
                                    Color.valueOf("75ffb0"), 0.65f);
                        }

                        Draw.color(Color.gray, Color.darkGray, bp);
                        Draw.alpha(bfo * 0.6f);
                        Angles.randLenVectors(boom.id, 20,
                                30f + bp * 230f,
                                (x, y) -> circle(ex + x, ey + y, bfo * 10f));

                        Drawf.light(ex, ey, bfo * 820f, Color.valueOf("44cc88"), 0.98f);
                    });

                    Drawf.light(ex, ey, e.fout() * 180f, Color.valueOf("75ffb0"), 0.8f);
                });

                damageLogicField = new BasicBulletType(0f, 0f) {
                    {
                        lifetime = 230f;
                        collides = false;
                        hittable = false;
                        absorbable = false;
                        hitEffect = despawnEffect = Fx.none;
                    }

                    @Override
                    public void update(Bullet b) {
                        super.update(b);
                        if (b.time >= 10f) {
                            float damagePerTick = 880f;
                            Damage.damage(b.team, b.x, b.y, 400f, damagePerTick, true, true);
                        }
                    }

                    @Override
                    public void despawned(Bullet b) {
                        super.despawned(b);
                        float finalCoreDamage = 19200f;
                        float finalOuterDamage = 9500f;
                        float finalCoreRadius = 530f;
                        float finalOuterRadius = 900f;

                        Damage.damage(b.team, b.x, b.y, finalCoreRadius, finalCoreDamage, true, true);
                        Damage.damage(b.team, b.x, b.y, finalOuterRadius, finalOuterDamage, true, true);
                    }
                };

                hitEffect = JBFx.crossBlast(Color.valueOf("#75FFB0"), 10f);

                pierce = true;
                pierceCap = 8;
                pierceBuilding = true;

                splashDamageRadius = 240f;
                splashDamage = damage * 1.1f;

                knockback = 18f;
                hitShake = 24f;

                status = StatusEffects.burning;
                statusDuration = 480f;

                homingPower = 0.06f;
                homingRange = 220f;

                despawnHit = false;
                drag = -0.004f;

                fragOnHit = true;
                fragBullets = 20;
                fragVelocityMin = 0.9f;
                fragVelocityMax = 2.4f;
                fragRandomSpread = 360f;

                fragBullet = new LightningLinkerBulletType() {
                    {
                        effectLightningChance = 0.15f;
                        damage = 420;

                        backColor = trailColor = lightColor = lightningColor = hitColor = Color.valueOf("75ffb0");
                        size = 8f;
                        frontColor = Color.valueOf("eeffee");
                        range = 800f;
                        spreadEffect = Fx.none;

                        trailWidth = 5f;
                        trailLength = 15;
                        speed = 7f;
                        linkRange = 200f;
                        maxHit = 8;
                        drag = 0.008f;

                        hitSound = Sounds.explosion;
                        splashDamageRadius = 60f;
                        splashDamage = lightningDamage = damage / 3f;
                        lifetime = 70f;

                        despawnEffect = JBFx.lightningHitLarge(hitColor);
                        hitEffect = JBFx.sharpBlast(hitColor, frontColor, 25,
                                splashDamageRadius * 1.2f);
                        shootEffect = JBFx.hitSpark(backColor, 35f, 10, 45, 2.5f, 6);
                        smokeEffect = JBFx.hugeSmoke;
                    }
                };

            }

            @Override
            public void despawned(Bullet b) {
                super.despawned(b);
                damageLogicField.create(b.owner, b.team, b.x, b.y, 0f);
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                float pulse = Mathf.absin(b.time, 7f, 1f);
                float scale = 1f + pulse * 0.15f;

                Draw.z(Layer.bullet + 0.003f);

                Draw.color(Color.valueOf("1a5c3a"));
                Draw.alpha(0.5f + pulse * 0.1f);
                circle(b.x, b.y, (42f + pulse * 6f) * scale);

                Draw.color(Color.valueOf("2a9960"));
                Draw.alpha(0.78f + pulse * 0.1f);
                circle(b.x, b.y, (30f + pulse * 4f) * scale);

                Draw.color(Color.valueOf("75ffb0"));
                Draw.alpha(0.92f);
                circle(b.x, b.y, (20f + pulse * 3f) * scale);

                Draw.color(Color.valueOf("ccffdd"));
                Draw.alpha(0.85f + pulse * 0.12f);
                circle(b.x, b.y, (13f + pulse * 2f) * scale);

                for (int i = 0; i < 6; i++) {
                    float orbAngle = b.time * 2.2f + i * 60f;
                    float orbR = (20f + Mathf.absin(b.time + i * 18f, 5f, 3f)) * scale;
                    float ox = b.x + Angles.trnsx(orbAngle, orbR);
                    float oy = b.y + Angles.trnsy(orbAngle, orbR);

                    Draw.color(Color.valueOf("75ffb0"));
                    Draw.alpha(0.88f + pulse * 0.1f);
                    circle(ox, oy, (4f + pulse * 1.2f) * scale);

                    Draw.color(Color.valueOf("eeffee"));
                    Draw.alpha(0.6f + pulse * 0.2f);
                    circle(ox, oy, (2f + pulse * 0.6f) * scale);

                    Drawf.light(ox, oy, 25f + pulse * 8f, Color.valueOf("75ffb0"), 0.8f);
                }

                Draw.color(Color.valueOf("75ffb0"));
                Draw.alpha(0.9f + pulse * 0.1f);
                circle(b.x, b.y, (7f + pulse * 2f) * scale);

                Draw.color(Color.valueOf("eeffee"));
                Draw.alpha(0.75f + pulse * 0.2f);
                circle(b.x, b.y, (3.5f + pulse) * scale);

                Draw.color(Color.valueOf("ffffff"));
                Draw.alpha(0.55f + pulse * 0.3f);
                circle(b.x, b.y, (1.8f + pulse * 0.5f) * scale);

                Drawf.light(b.x, b.y,
                        (140f + pulse * 55f) * scale,
                        Color.valueOf("75ffb0"), 0.97f);

                Draw.reset();
            }
        };
        verdantBeamBurst = new BasicBulletType(20f, 2400) {
            {
                lifetime = 75f;

                width = 4f;
                height = 28f;
                sprite = "bullet";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("1a5c3a");
                frontColor = Color.valueOf("75ffb0");
                lightColor = Color.valueOf("75ffb0");
                lightOpacity = 0.9f;
                lightRadius = 100f;

                trailLength = 25;
                trailWidth = 2.5f;
                trailColor = Color.valueOf("75ffb0");
                trailInterval = 0.8f;

                trailEffect = new Effect(25f, e -> {
                    Draw.color(Color.valueOf("75ffb0"), Color.valueOf("ccffdd"), e.fin());
                    Draw.alpha(e.fout() * 0.8f);
                    circle(e.x, e.y, e.fout() * 3.5f);
                    Drawf.light(e.x, e.y, e.fout() * 15f, Color.valueOf("75ffb0"), 0.6f);
                });
                trailChance = 0.8f;

                shootEffect = JBFx.crossBlast(Color.valueOf("75ffb0"), 80f);
                smokeEffect = Fx.none;

                hitEffect = new Effect(120f, 1200f, e -> {
                    final float ex = e.x;
                    final float ey = e.y;
                    final float etime = e.time;

                    Fx.rand.setSeed(e.id);
                    final float angle1 = Fx.rand.random(360f);
                    final float angle2 = angle1 + Fx.rand.random(100f, 140f);
                    final float angle3 = angle2 + Fx.rand.random(100f, 140f);

                    e.scaled(15f, flash -> {
                        Draw.color(Color.white);
                        Draw.alpha(flash.fout() * 0.95f);
                        circle(ex, ey, flash.fin() * 45f);

                        Draw.color(Color.valueOf("ccffdd"));
                        circle(ex, ey, flash.fin() * 35f);

                        Draw.color(Color.valueOf("75ffb0"));
                        circle(ex, ey, flash.fin() * 25f);

                        Lines.stroke(flash.fout() * 3f);
                        Lines.circle(ex, ey, flash.fin() * 55f);
                        Lines.circle(ex, ey, flash.fin() * 70f);

                        Drawf.light(ex, ey, flash.fin() * 150f, Color.valueOf("75ffb0"), 0.95f);
                    });

                    e.scaled(40f, shards -> {
                        Angles.randLenVectors(shards.id, 16,
                                10f + shards.finpow() * 60f, (x, y) -> {
                                    float angle = Mathf.angle(x, y);
                                    float dist = Mathf.len(x, y);

                                    Draw.color(Color.valueOf("1a5c3a"));
                                    Draw.alpha(shards.fout() * 0.9f);
                                    Drawf.tri(ex + x, ey + y,
                                            shards.fout() * 4f,
                                            shards.fout() * 12f,
                                            angle);

                                    Draw.color(Color.valueOf("75ffb0"));
                                    Draw.alpha(shards.fout() * 0.85f);
                                    Drawf.tri(ex + x, ey + y,
                                            shards.fout() * 2.5f,
                                            shards.fout() * 8f,
                                            angle);

                                    Drawf.light(ex + x, ey + y,
                                            shards.fout() * 12f,
                                            Color.valueOf("75ffb0"), 0.7f);
                                });
                    });

                    Draw.z(Layer.effect - 0.001f);

                    final float beam1Start = 20f;
                    final float beamDelay = 10f;

                    if (etime >= beam1Start && etime < beam1Start + Time.delta) {
                        JBSounds.shock.at(ex, ey, 1.2f, 1.3f);
                    }

                    if (etime >= beam1Start && etime < 100f) {
                        float bp = (etime - beam1Start) / (100f - beam1Start);
                        float bf = 1f - bp;

                        float beamLen = 650f * Interp.pow2Out.apply(bp);
                        float beamW = (20f - bp * 8f) * bf; 

                        float endX = ex + Angles.trnsx(angle1, beamLen);
                        float endY = ey + Angles.trnsy(angle1, beamLen);

                        Draw.color(Color.valueOf("0a2e18"));
                        Draw.alpha(bf * 0.85f);
                        Lines.stroke(beamW * 1.5f);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.valueOf("75ffb0"));
                        Draw.alpha(bf * 0.92f);
                        Lines.stroke(beamW);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.white);
                        Draw.alpha(bf * 0.65f);
                        Lines.stroke(beamW * 0.35f);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.white);
                        Draw.alpha(bf * 0.85f);
                        circle(endX, endY, beamW * 1.5f * bf);

                        Draw.color(Color.valueOf("75ffb0"));
                        circle(endX, endY, beamW * 0.9f * bf);

                        Drawf.light(endX, endY, beamW * 5f * bf,
                                Color.valueOf("75ffb0"), 0.8f);
                    }

                    final float beam2Start = beam1Start + beamDelay;

                    if (etime >= beam2Start && etime < beam2Start + Time.delta) {
                        JBSounds.shock.at(ex, ey, 1.2f, 1.3f);
                    }

                    if (etime >= beam2Start && etime < 100f) {
                        float bp = (etime - beam2Start) / (100f - beam2Start);
                        float bf = 1f - bp;

                        float beamLen = 650f * Interp.pow2Out.apply(bp);
                        float beamW = (20f - bp * 8f) * bf;

                        float endX = ex + Angles.trnsx(angle2, beamLen);
                        float endY = ey + Angles.trnsy(angle2, beamLen);

                        Draw.color(Color.valueOf("0a2e18"));
                        Draw.alpha(bf * 0.85f);
                        Lines.stroke(beamW * 1.5f);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.valueOf("75ffb0"));
                        Draw.alpha(bf * 0.92f);
                        Lines.stroke(beamW);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.white);
                        Draw.alpha(bf * 0.65f);
                        Lines.stroke(beamW * 0.35f);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.white);
                        Draw.alpha(bf * 0.85f);
                        circle(endX, endY, beamW * 1.5f * bf);

                        Draw.color(Color.valueOf("75ffb0"));
                        circle(endX, endY, beamW * 0.9f * bf);

                        Drawf.light(endX, endY, beamW * 5f * bf,
                                Color.valueOf("75ffb0"), 0.8f);
                    }

                    final float beam3Start = beam2Start + beamDelay;

                    if (etime >= beam3Start && etime < beam3Start + Time.delta) {
                        JBSounds.shock.at(ex, ey, 1.2f, 1.3f);
                    }

                    if (etime >= beam3Start && etime < 100f) {
                        float bp = (etime - beam3Start) / (100f - beam3Start);
                        float bf = 1f - bp;

                        float beamLen = 650f * Interp.pow2Out.apply(bp);
                        float beamW = (20f - bp * 8f) * bf;

                        float endX = ex + Angles.trnsx(angle3, beamLen);
                        float endY = ey + Angles.trnsy(angle3, beamLen);

                        Draw.color(Color.valueOf("0a2e18"));
                        Draw.alpha(bf * 0.85f);
                        Lines.stroke(beamW * 1.5f);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.valueOf("75ffb0"));
                        Draw.alpha(bf * 0.92f);
                        Lines.stroke(beamW);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.white);
                        Draw.alpha(bf * 0.65f);
                        Lines.stroke(beamW * 0.35f);
                        Lines.line(ex, ey, endX, endY);

                        Draw.color(Color.white);
                        Draw.alpha(bf * 0.85f);
                        circle(endX, endY, beamW * 1.5f * bf);

                        Draw.color(Color.valueOf("75ffb0"));
                        circle(endX, endY, beamW * 0.9f * bf);

                        Drawf.light(endX, endY, beamW * 5f * bf,
                                Color.valueOf("75ffb0"), 0.8f);
                    }

                    Draw.z(Layer.effect);

                    if (etime >= 5f && etime < 105f) {
                        float nfo = etime < 95f
                                ? 1f
                                : 1f - (etime - 95f) / 10f;
                        nfo = Math.max(nfo, 0f);

                        float pulse = Mathf.absin(etime, 3f, 1f);

                        Draw.color(Color.black);
                        Draw.alpha(nfo * 0.75f);
                        circle(ex, ey, (28f + pulse * 3f) * nfo);

                        Draw.color(Color.valueOf("75ffb0"));
                        Draw.alpha(nfo * 0.95f);
                        Lines.stroke((6f + pulse * 1.5f) * nfo);
                        Lines.circle(ex, ey, (34f + pulse * 4f) * nfo);

                        Draw.color(Color.valueOf("44cc88"));
                        Draw.alpha(nfo * 0.8f);
                        Lines.stroke((3f + pulse) * nfo);
                        Lines.circle(ex, ey, (26f + pulse * 3f) * nfo);

                        Draw.color(Color.valueOf("ccffdd"));
                        Draw.alpha(nfo * 0.88f);
                        circle(ex, ey, (15f + pulse * 2f) * nfo);

                        Draw.color(Color.white);
                        Draw.alpha(nfo * 0.65f);
                        circle(ex, ey, (8f + pulse * 1.2f) * nfo);

                        for (int i = 0; i < 8; i++) {
                            float angle = etime * 5f + i * 45f;
                            float r = (24f + pulse * 3f) * nfo;
                            float px = ex + Angles.trnsx(angle, r);
                            float py = ey + Angles.trnsy(angle, r);

                            Draw.color(Color.valueOf("75ffb0"));
                            Draw.alpha(nfo * 0.92f);
                            circle(px, py, (3.5f + pulse * 0.8f) * nfo);

                            Draw.color(Color.white);
                            Draw.alpha(nfo * 0.6f);
                            circle(px, py, (1.5f + pulse * 0.4f) * nfo);
                        }

                        Drawf.light(ex, ey, (85f + pulse * 20f) * nfo,
                                Color.valueOf("75ffb0"), 0.95f);
                    }

                    Team team = e.data instanceof Bullet b ? b.team : Team.sharded;

                    if (etime >= beam1Start && etime < beam1Start + 2f) {
                        for (int i = 1; i <= 20; i++) {
                            float dist = (i / 20f) * 650f;
                            float dx = ex + Angles.trnsx(angle1, dist);
                            float dy = ey + Angles.trnsy(angle1, dist);
                            Damage.damage(team, dx, dy, 40f, 4200f, true, true);
                        }
                    }

                    if (etime >= beam2Start && etime < beam2Start + 2f) {
                        for (int i = 1; i <= 20; i++) {
                            float dist = (i / 20f) * 650f;
                            Damage.damage(team,
                                    ex + Angles.trnsx(angle2, dist),
                                    ey + Angles.trnsy(angle2, dist),
                                    40f, 4200f, true, true);
                        }
                    }

                    if (etime >= beam3Start && etime < beam3Start + 2f) {
                        for (int i = 1; i <= 20; i++) {
                            float dist = (i / 20f) * 650f;
                            Damage.damage(team,
                                    ex + Angles.trnsx(angle3, dist),
                                    ey + Angles.trnsy(angle3, dist),
                                    40f, 4200f, true, true);
                        }
                    }

                    Draw.z(Layer.effect);

                    if (etime >= 100f) {
                        float fadeP = (etime - 100f) / 20f;
                        float fadeO = 1f - fadeP;

                        Draw.color(Color.valueOf("75ffb0"), Color.white, fadeP * 0.3f);
                        Draw.alpha(fadeO * 0.75f);
                        circle(ex, ey, fadeO * 32f);

                        Lines.stroke(fadeO * 4f);
                        Lines.circle(ex, ey, fadeO * 48f);

                        Drawf.light(ex, ey, fadeO * 100f, Color.valueOf("75ffb0"), 0.75f);
                    }

                    Draw.z(Layer.effect);
                    Drawf.light(ex, ey, e.fout() * 120f, Color.valueOf("75ffb0"), 0.8f);
                });

                despawnEffect = Fx.none;
                despawnHit = true;

                pierce = false;
                pierceBuilding = false;

                splashDamageRadius = 100f;
                splashDamage = damage * 0.5f;

                knockback = 7f;
                hitShake = 10f;

                status = StatusEffects.burning;
                statusDuration = 220f;

                homingPower = 0.06f;
                homingRange = 170f;

                despawnHit = false;
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

        photosynthesisBullet = new BasicBulletType(17f, 1220f) {
            {
                Color main = Color.valueOf("#75FFB0");
                Color core = Color.valueOf("#DFFFEF");
                Color glow = Color.valueOf("#2AFF80");

                backColor = trailColor = lightColor = hitColor = main;
                frontColor = core;

                width = 5f;
                height = 24f;

                lifetime = 160f;
                speed = 11f;
                drag = 0.003f;

                trailWidth = 6f;
                trailLength = 45;

                lightRadius = 80f;
                lightOpacity = 0.75f;

                splashDamageRadius = 60f;
                splashDamage = damage * 0.35f;

                hitSound = Sounds.explosion;

                
                shootEffect = new Effect(40f, e -> {
                    Draw.color(main, Color.white, e.fout() * 0.4f);
                    Lines.stroke(e.fout() * 2.5f);
                    Angles.randLenVectors(e.id, 8, e.finpow() * 55f, e.rotation, 20f, (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 10f + 3f);
                    });
                    circle(e.x, e.y, e.fout() * 7f);
                    Drawf.light(e.x, e.y, e.fout() * 60f, main, 0.8f);
                });

                smokeEffect = Fx.none;

                
                hitEffect = new Effect(55f, 200f, e -> {
                    
                    Draw.color(main, Color.white, e.fout() * 0.3f);
                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, 10f + e.fin(Interp.pow3Out) * 90f);

                    
                    e.scaled(30f, s -> {
                        Draw.color(main);
                        Lines.stroke(2f * s.fout());
                        Lines.circle(e.x, e.y, 5f + s.fin(Interp.pow2Out) * 55f);
                    });

                    
                    Draw.color(main, Color.white, e.fout() * 0.45f);
                    Lines.stroke(e.fout() * 1.8f);
                    Angles.randLenVectors(e.id, 14, e.finpow() * 80f, (x, y) -> {
                        float ang = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 14f + 4f);
                    });

                    
                    e.scaled(18f, s -> {
                        Draw.color(Color.white, main, s.fin());
                        circle(e.x, e.y, s.fout() * 22f);
                        Drawf.light(e.x, e.y, s.fout() * 120f, main, 0.9f);
                    });

                    
                    Draw.color(main);
                    Angles.randLenVectors(e.id + 1, 18, 12f + 65f * e.finpow(),
                            (x, y) -> circle(e.x + x, e.y + y, e.fout() * 3.5f));
                });

                despawnEffect = new Effect(60f, 220f, e -> {
                    Draw.color(main, Color.white, e.fout() * 0.3f);
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, 8f + e.fin(Interp.pow3Out) * 75f);

                    Draw.color(main);
                    Angles.randLenVectors(e.id, 10, 10f + 60f * e.finpow(),
                            (x, y) -> circle(e.x + x, e.y + y, e.fout() * 3f));
                    e.scaled(15f, s -> {
                        Draw.color(Color.white, main, s.fin());
                        circle(e.x, e.y, s.fout() * 16f);
                    });
                });
            }

            @Override
            public void update(Bullet b) {
                super.update(b);

                Color main = Color.valueOf("#75FFB0");
                Color core = Color.valueOf("#DFFFEF");

                
                if (Mathf.chanceDelta(0.5f)) {
                    float offset = Mathf.range(8f);
                    float perpAngle = b.rotation() + 90f;
                    float ox = Angles.trnsx(perpAngle, offset);
                    float oy = Angles.trnsy(perpAngle, offset);

                    new Effect(18f, e -> {
                        Draw.color(main, Color.white, e.fout() * 0.5f);
                        Lines.stroke(e.fout() * 1.4f);
                        float len = Mathf.random(18f, 40f);
                        Lines.line(
                                e.x - Angles.trnsx(e.rotation, len * 0.3f),
                                e.y - Angles.trnsy(e.rotation, len * 0.3f),
                                e.x + Angles.trnsx(e.rotation, len * 0.7f),
                                e.y + Angles.trnsy(e.rotation, len * 0.7f));
                        Drawf.light(e.x, e.y, e.fout() * 20f, main, 0.5f);
                    }).at(b.x + ox, b.y + oy, b.rotation(), main);
                }

                
                if (Mathf.chanceDelta(0.18f)) {
                    Lightning.create(b.team, main, 10f,
                            b.x, b.y,
                            b.rotation() + Mathf.range(40f),
                            Mathf.random(4, 9));
                }

                
                if (Mathf.chanceDelta(0.45f)) {
                    new Effect(22f, e -> {
                        Draw.color(Color.white, main, e.fout() * 0.4f);
                        Lines.stroke(e.fout() * 2f);
                        Lines.lineAngle(e.x, e.y, e.rotation + Mathf.range(25f), e.fout() * 14f + 4f);
                        Drawf.light(e.x, e.y, e.fout() * 18f, main, 0.75f);
                    }).at(
                            b.x + Mathf.range(5f),
                            b.y + Mathf.range(5f),
                            b.rotation(),
                            main);
                }

                

                
                if (Mathf.chanceDelta(0.85f)) {
                    
                    float spawnX = b.x + Angles.trnsx(b.rotation(), Mathf.random(-5f, 8f));
                    float spawnY = b.y + Angles.trnsy(b.rotation(), Mathf.random(-5f, 8f));
                    
                    float scatterAngle = b.rotation() + Mathf.range(90f);

                    new Effect(45f, 120f, e -> {
                        float travel = e.fin(Interp.pow2Out) * Mathf.randomSeed(e.id, 25f, 55f);
                        float tx = e.x + Angles.trnsx(e.rotation, travel);
                        float ty = e.y + Angles.trnsy(e.rotation, travel);

                        
                        Draw.color(main, Color.white, 0.5f + e.fout() * 0.3f);
                        Lines.stroke(e.fout() * 3.5f);
                        Lines.line(e.x, e.y, tx, ty);

                        
                        Draw.color(Color.white, main, e.fin() * 0.4f);
                        circle(tx, ty, e.fout() * 5f);
                        Drawf.light(tx, ty, e.fout() * 28f, main, 0.95f);
                        Drawf.light(e.x, e.y, e.fout() * 16f, main, 0.7f);
                    }).at(spawnX, spawnY, scatterAngle, main);
                }

                
                if (Mathf.chanceDelta(0.9f)) {
                    float spawnX = b.x + Angles.trnsx(b.rotation(), Mathf.random(-8f, 10f));
                    float spawnY = b.y + Angles.trnsy(b.rotation(), Mathf.random(-8f, 10f));
                    float scatterAngle = b.rotation() + Mathf.range(100f);

                    new Effect(32f, 100f, e -> {
                        float travel = e.fin(Interp.pow2Out) * Mathf.randomSeed(e.id, 12f, 32f);
                        float tx = e.x + Angles.trnsx(e.rotation, travel);
                        float ty = e.y + Angles.trnsy(e.rotation, travel);

                        Draw.color(main, Color.white, 0.4f + e.fout() * 0.4f);
                        Lines.stroke(e.fout() * 2.2f);
                        Lines.line(e.x, e.y, tx, ty);

                        Draw.color(Color.white);
                        circle(tx, ty, e.fout() * 3.2f);
                        Drawf.light(tx, ty, e.fout() * 20f, main, 0.9f);
                    }).at(spawnX, spawnY, scatterAngle, main);
                }

                
                if (Mathf.chanceDelta(0.95f)) {
                    for (int i = 0; i < 3; i++) {
                        float spawnX = b.x + Angles.trnsx(b.rotation(), Mathf.random(-6f, 10f)) + Mathf.range(4f);
                        float spawnY = b.y + Angles.trnsy(b.rotation(), Mathf.random(-6f, 10f)) + Mathf.range(4f);
                        float scatterAngle = b.rotation() + Mathf.range(120f);
                        int fi = i;

                        new Effect(25f, 80f, e -> {
                            float travel = e.fin(Interp.pow3Out) * Mathf.randomSeed(e.id + fi, 8f, 20f);
                            float tx = e.x + Angles.trnsx(e.rotation, travel);
                            float ty = e.y + Angles.trnsy(e.rotation, travel);

                            Draw.color(Color.white, main, e.fin() * 0.35f);
                            circle(tx, ty, e.fout() * 2.4f);
                            Drawf.light(tx, ty, e.fout() * 12f, main, 0.8f);
                        }).at(spawnX, spawnY, scatterAngle, main);
                    }
                }
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                Color main = Color.valueOf("#75FFB0");
                Color core = Color.valueOf("#DFFFEF");

                float progress = b.fin();

                
                Draw.color(core, Color.white, 0.6f);
                Fill.rect(b.x, b.y, 6f, 28f * (1f - progress * 0.2f), b.rotation() - 90f);

                
                Draw.color(main, 0.55f);
                Fill.rect(b.x, b.y, 11f, 22f * (1f - progress * 0.15f), b.rotation() - 90f);

                
                for (int i = -1; i <= 1; i += 2) {
                    float sideOff = 5f + Mathf.absin(b.time(), 4f, 1.5f);
                    float perpA = b.rotation() + 90f;
                    float sx = b.x + Angles.trnsx(perpA, i * sideOff);
                    float sy = b.y + Angles.trnsy(perpA, i * sideOff);

                    Draw.color(main, 0.3f + Mathf.absin(b.time(), 6f, 0.15f));
                    Fill.rect(sx, sy, 2.5f, 16f, b.rotation() - 90f);
                }

                
                Drawf.light(b.x, b.y, 70f, main, 0.65f);

                Draw.reset();
            }
        };

        verdantLightningWeb = new AdaptedLightningBulletType() {
            {
                Color main = Color.valueOf("#75FFB0");

                damage = 5985f;
                lightningColor = main;
                lightningLength = 192;
                lightningLengthRand = 10;
                lightningDamage = 2985f;

                hitEffect = JBFx.lightningHitLarge(main);
                shootEffect = JBFx.lightningSpark;
                despawnEffect = Fx.none;

                status = StatusEffects.shocked;
                statusDuration = 120f;

                hitColor = main;
                lightColor = main;
                lightRadius = 50f;
                lightOpacity = 0.75f;

                splashDamage = 5835f;
                splashDamageRadius = 192f;
            }

            @Override
            public void init(Bullet b) {
                Color main = Color.valueOf("#75FFB0");

                Lightning.create(b, main, damage,
                        b.x, b.y,
                        b.rotation(),
                        lightningLength + Mathf.random(lightningLengthRand));

                for (int i = 0; i < 2; i++) {
                    Lightning.create(b, main, damage * 0.6f,
                            b.x, b.y,
                            b.rotation() + Mathf.range(12f),
                            (lightningLength - 8) + Mathf.random(lightningLengthRand));
                }

                for (int i = 0; i < 3; i++) {
                    Lightning.create(b, main, damage * 0.3f,
                            b.x, b.y,
                            b.rotation() + Mathf.range(30f),
                            (lightningLength / 2) + Mathf.random(10));
                }

                JBFx.lightningSpark.at(b.x, b.y, b.rotation(), main);
                JBFx.lightningHitSmall.at(b.x, b.y, b.rotation(), main);
            }
        };

        verdantBeam = new LaserBulletType(5350f) {
            final Color main = Color.valueOf("#75FFB0");
            final Color core = Color.valueOf("#EAFFF4");

            {
                length = 1520f;
                width = 18f;

                hitColor = main;
                colors = new Color[] {
                        main.cpy().a(0.12f),
                        main.cpy().a(0.35f),
                        main,
                        core
                };

                lightColor = main;
                lightOpacity = 0.9f;
                

                smokeEffect = Fx.none;

                shootEffect = new Effect(40f, e -> {
                    Draw.color(core, main, e.fin() * 0.5f);
                    Lines.stroke(e.fout() * 4f);
                    Angles.randLenVectors(e.id, 7, e.finpow() * 50f, e.rotation, 18f, (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 14f + 4f);
                    });
                    Draw.color(main, e.fout() * 0.8f);
                    circle(e.x, e.y, e.fout() * 14f);
                    Drawf.light(e.x, e.y, e.fout() * 80f, main, 0.9f);
                });

                hitEffect = new Effect(50f, 180f, e -> {
                    Draw.color(main, Color.white, e.fout() * 0.25f);
                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, 8f + e.fin(Interp.pow3Out) * 70f);

                    Draw.color(core, main, e.fin() * 0.5f);
                    Lines.stroke(e.fout() * 2.2f);
                    Angles.randLenVectors(e.id, 12, e.finpow() * 65f, (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 12f + 4f);
                    });

                    e.scaled(16f, s -> {
                        Draw.color(Color.white, main, s.fin() * 0.4f);
                        circle(e.x, e.y, s.fout() * 20f);
                        Drawf.light(e.x, e.y, s.fout() * 110f, main, 0.95f);
                    });

                    Draw.color(main, Color.white, 0.3f);
                    Angles.randLenVectors(e.id + 1, 16, 10f + 55f * e.finpow(),
                            (x, y) -> circle(e.x + x, e.y + y, e.fout() * 3.2f));
                });

                despawnEffect = new Effect(55f, 200f, e -> {
                    Draw.color(main, Color.white, e.fout() * 0.2f);
                    Lines.stroke(2f * e.fout());
                    Lines.circle(e.x, e.y, 5f + e.fin(Interp.pow2Out) * 55f);

                    Draw.color(main, e.fout() * 0.5f);
                    Angles.randLenVectors(e.id, 10, 8f + 45f * e.finpow(),
                            (x, y) -> circle(e.x + x, e.y + y, e.fout() * 2.5f));

                    e.scaled(14f, s -> {
                        Draw.color(Color.white, main, s.fin() * 0.35f);
                        circle(e.x, e.y, s.fout() * 13f);
                    });
                });

                chargeEffect = new Effect(45f, e -> {
                    Draw.color(main, Color.white, e.fout() * 0.3f);
                    Lines.stroke(e.fin() * 2.5f);
                    Lines.circle(e.x, e.y, e.fout(Interp.pow3In) * 24f);
                    Draw.color(main, e.fin() * 0.7f);
                    circle(e.x, e.y, e.fin() * 9f);
                    Drawf.light(e.x, e.y, e.fin() * 55f, main, 0.75f);
                });
            }

            @Override
            public void draw(Bullet b) {
                float baseWidth = this.width;
                float len = length * b.fout(Interp.pow2Out);

                float bx = b.x;
                float by = b.y;
                float rot = b.rotation();

                float endX = bx + Mathf.cosDeg(rot) * len;
                float endY = by + Mathf.sinDeg(rot) * len;

                float[] widths = { baseWidth * 2.4f, baseWidth * 1.3f, baseWidth * 0.45f };
                float[] alphas = { 0.28f, 0.75f, 1.0f };
                Color[] layerCol = { main, main, core };

                float perpCos = Mathf.cosDeg(rot + 90f);
                float perpSin = Mathf.sinDeg(rot + 90f);

                for (int i = 0; i < widths.length; i++) {
                    float hw = widths[i] / 2f;
                    float ew = 0.3f;

                    Draw.color(layerCol[i], alphas[i]);

                    float x1 = bx + perpCos * hw;
                    float y1 = by + perpSin * hw;
                    float x2 = bx - perpCos * hw;
                    float y2 = by - perpSin * hw;
                    float x3 = endX - perpCos * ew;
                    float y3 = endY - perpSin * ew;
                    float x4 = endX + perpCos * ew;
                    float y4 = endY + perpSin * ew;

                    Fill.quad(x1, y1, x2, y2, x3, y3, x4, y4);

                    Drawf.light(bx, by, widths[i] * 3f, layerCol[i], alphas[i] * 0.8f);
                }

                
                Drawf.light(
                        bx + Mathf.cosDeg(rot) * len / 2f,
                        by + Mathf.sinDeg(rot) * len / 2f,
                        baseWidth * 4f, main, 0.4f);

                Draw.color(main, 0.25f);
                circle(bx, by, baseWidth * 2.5f);

                Draw.color(main, 0.55f);
                circle(bx, by, baseWidth * 1.65f);

                Draw.color(main, 0.9f);
                circle(bx, by, baseWidth * 1.25f);

                Draw.color(core);
                circle(bx, by, baseWidth * 0.7f);

                Drawf.light(bx, by, baseWidth * 7f, main, 0.85f);
                Drawf.light(bx, by, baseWidth * 3f, core, 0.6f);

                Draw.reset();
            }
        };

        crossSpinLaser = new BasicBulletType(0.001f, 0f) {
            final Color main = Color.valueOf("#75FFB0");
            final Color core = Color.valueOf("#EAFFF4");
            final float laserLength = 1250f;
            final float baseWidth = 38f;

            {

                lifetime = 600f;
                width = 1f;
                height = 1f;
                drawSize = 2200f;

                hitColor = main;
                lightColor = main;
                lightOpacity = 0.8f;

                smokeEffect = Fx.none;
                shootEffect = Fx.none;
                hitEffect = Fx.none;
                despawnEffect = Fx.none;

                keepVelocity = false;
                hittable = false;
                reflectable = false;
                absorbable = false;
                collidesAir = false;
                collidesGround = false;
                collides = false;
            }

            float scl(Bullet b) {
                float appear = Interp.smooth.apply(Mathf.curve(b.fin(), 0f, 0.12f));
                float disappear = Interp.smooth.apply(Mathf.curve(b.fout(), 0f, 0.12f));
                return Math.min(appear, disappear);
            }

            @Override
            public void draw(Bullet b) {
                float spin = b.time * 1.1f;
                float bx = b.x;
                float by = b.y;
                float scl = scl(b);

                float curLength = laserLength * scl;
                float curWidth = baseWidth * scl;

                float[] widths = { curWidth * 1.3f, curWidth * 0.45f };
                float[] alphas = { 0.75f * scl, 1.0f * scl };
                Color[] layerCol = { main, core };

                for (int arm = 0; arm < 4; arm++) {
                    float rot = spin + arm * 90f;

                    float cos = Mathf.cosDeg(rot);
                    float sin = Mathf.sinDeg(rot);
                    float perpCos = Mathf.cosDeg(rot + 90f);
                    float perpSin = Mathf.sinDeg(rot + 90f);

                    float endX = bx + cos * curLength;
                    float endY = by + sin * curLength;

                    for (int i = 0; i < widths.length; i++) {
                        float hw = widths[i] / 2f;

                        Draw.color(layerCol[i], alphas[i]);

                        Fill.quad(
                                bx + perpCos * hw, by + perpSin * hw,
                                bx - perpCos * hw, by - perpSin * hw,
                                endX - perpCos * 0.4f, endY - perpSin * 0.4f,
                                endX + perpCos * 0.4f, endY + perpSin * 0.4f);
                    }

                    Drawf.light(
                            bx + cos * curLength / 2f,
                            by + sin * curLength / 2f,
                            curWidth * 5f, main, 0.4f * scl);
                    Drawf.light(endX, endY, curWidth * 3f, core, 0.6f * scl);
                }

                float coreSize = curWidth * 1.1f;
                Draw.color(main, 0.6f * scl);
                circle(bx, by, coreSize * 1.4f);
                Draw.color(main, 0.9f * scl);
                circle(bx, by, coreSize);
                Draw.color(core, scl);
                circle(bx, by, coreSize * 0.45f);
                Drawf.light(bx, by, coreSize * 8f, main, 0.9f * scl);
                Drawf.light(bx, by, coreSize * 3f, core, 0.7f * scl);

                Draw.reset();
            }

            @Override
            protected float calculateRange() {
                return laserLength; 
            }

            @Override
            public void update(Bullet b) {
                super.update(b);
                b.vel.setZero();

                
                if (b.owner instanceof Unit unit && unit.isAdded()) {
                    b.x = unit.x;
                    b.y = unit.y;
                }

                float scl = scl(b);
                float spin = b.time * 1.1f;
                float bx = b.x; 
                float by = b.y;
                float curLen = laserLength * scl;

                if (scl < 0.25f)
                    return;

                for (int arm = 0; arm < 4; arm++) {
                    float rot = spin + arm * 90f;
                    float cos = Mathf.cosDeg(rot);
                    float sin = Mathf.sinDeg(rot);

                    int steps = 12;
                    for (int s = 1; s <= steps; s++) {
                        float t = (float) s / steps;
                        float px = bx + cos * curLen * t;
                        float py = by + sin * curLen * t;

                        Damage.damage(b.team, px, py, baseWidth * 2.8f, 14080f / steps, true, true);

                        if (Mathf.chanceDelta(0.05f)) {
                            new Effect(28f, e -> {
                                Draw.color(main, Color.white, e.fout() * 0.35f);
                                Lines.stroke(e.fout() * 2.5f);
                                Lines.circle(e.x, e.y, e.fin(Interp.pow2Out) * 18f);
                                Draw.color(main, e.fout() * 0.7f);
                                circle(e.x, e.y, e.fout() * 5f);
                                Drawf.light(e.x, e.y, e.fout() * 28f, main, 0.75f);
                            }).at(px, py, rot, main);
                        }
                    }

                    if (Mathf.chanceDelta(0.2f)) {
                        float tipX = bx + cos * curLen;
                        float tipY = by + sin * curLen;
                        new Effect(22f, e -> {
                            Draw.color(core, main, e.fin() * 0.5f);
                            circle(e.x, e.y, e.fout() * 7f);
                            Drawf.light(e.x, e.y, e.fout() * 30f, main, 0.8f);
                        }).at(tipX, tipY, rot, main);
                    }
                }
            }
        };

        verdantApex = new ArtilleryBulletType(7.2f, 7000f) {
            {
                lifetime = 180f;
                width = 30f;
                height = 40f;
                shrinkY = 0.3f;

                splashDamage = 1400f;
                splashDamageRadius = 160f;

                frontColor = Color.white;
                backColor = Color.valueOf("#75FFB0");
                trailColor = backColor;
                trailWidth = 7f;
                trailLength = 45;

                hitEffect = Fx.massiveExplosion;
                despawnEffect = Fx.scatheExplosion;
                
                shootEffect = JBFx.hitSpark(backColor, 45f, 12, 60, 3, 8);
                hitShake = 12f;

                fragBullets = 8;
                fragBullet = apexShard;
                fragVelocityMin = 0.7f;
                fragVelocityMax = 1.4f;
                fragLifeMin = 0.6f;
                fragLifeMax = 1.2f;
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

        quantarBullet = new EmpBulletType() {
            {
                float rad = 100f;

                rangeOverride = 400f;

                scaleLife = true;
                lightOpacity = 0.7f;
                healPercent = 20f;
                timeIncrease = 3f;
                timeDuration = 60f * 20f;
                powerDamageScl = 3f;
                damage = 600;
                hitColor = lightColor = Pal.techBlue;
                lightRadius = 70f;
                shootEffect = new Effect(40, e -> {
                    color(Pal.techBlue);
                    stroke(e.fout() * 1.6f);

                    randLenVectors(e.id, 18, e.finpow() * 27f, e.rotation, 360f, (x, y) -> {
                        float ang = Mathf.angle(x, y);
                        lineAngle(e.x + x, e.y + y, ang, e.fout() * 6 + 1f);
                    });
                });

                smokeEffect = new Effect(22, e -> {
                    color(Pal.techBlue);
                    stroke(e.fout() * 2f);
                    Lines.circle(e.x, e.y, 4f + e.finpow() * 60f);
                });

                lifetime = 60f;
                lightningColor = backColor = Pal.techBlue;
                frontColor = Color.white;

                lightning = 3;
                lightningDamage = damage;
                lightningLength = 7;
                lightningLengthRand = 16;

                width = 16f;
                height = 35f;
                speed = 8f;
                trailLength = 20;
                trailWidth = 2.7f;
                trailColor = Pal.techBlue;
                trailInterval = 3f;
                splashDamage = damage * 0.75f;
                splashDamageRadius = rad;
                hitShake = 4f;
                trailRotation = true;
                status = StatusEffects.electrified;
                hitSound = Sounds.explosionArtilleryShockBig;

                trailEffect = new Effect(16f, e -> {
                    Draw.color(Pal.techBlue);
                    for (int s : Mathf.signs) {
                        DrawFunc.tri(e.x, e.y, 4f, 30f * Mathf.curve(e.fin(), 0, 0.1f) * e.fout(0.9f),
                                e.rotation + 135f * s);
                    }
                });

                hitEffect = new OptionalMultiEffect(JBFx.blast(backColor, rad),
                        JBFx.hitSpark(backColor, 120f, 40, rad * 1.7f, 2.5f, 12f));
                despawnEffect = JBFx.crossBlast(backColor, rad * 1.8f, 45);
            }

            @Override
            public void hit(Bullet b) {
                super.hit(b);

                JBFunc.extinguish(b, splashDamageRadius, 3000);
            }
        };

        ionPulseBullet = new BasicBulletType() {
            {
                
                damage = 285f;
                speed = 7.5f;
                lifetime = 65f;
                width = 11f;
                height = 28f;

                
                frontColor = Color.white;
                backColor = Pal.techBlue;
                hitColor = Pal.techBlue;

                
                trailLength = 22;
                trailWidth = 3.5f;
                trailColor = Pal.techBlue;
                trailInterval = 2f;
                trailRotation = true;

                
                lightColor = Pal.techBlue;
                lightRadius = 45f;
                lightOpacity = 0.65f;

                
                splashDamage = 45f;
                splashDamageRadius = 38f;

                
                status = StatusEffects.slow;
                statusDuration = 60f * 4f;

                hitShake = 2.2f;
                hitSound = Sounds.explosion;

                
                trailEffect = new Effect(18f, e -> {
                    Draw.color(Pal.techBlue);
                    Draw.alpha(e.fout() * 0.55f);
                    Lines.stroke(e.fout() * 1.6f);
                    Lines.circle(e.x, e.y, 4f + e.finpow() * 11f);

                    Draw.color(Color.white);
                    Draw.alpha(e.fout() * 0.2f);
                    circle(e.x, e.y, e.fout() * 2.5f);
                    Draw.reset();
                });

                
                shootEffect = new Effect(28f, e -> {
                    color(Pal.techBlue);
                    stroke(e.fout() * 1.5f);
                    randLenVectors(e.id, 10, e.finpow() * 22f, e.rotation, 35f, (x, y) -> {
                        float ang = Mathf.angle(x, y);
                        lineAngle(e.x + x, e.y + y, ang, e.fout() * 7f + 2f);
                    });

                    color(Pal.techBlue, Color.white, e.fout() * 0.4f);
                    stroke(e.fout() * 2f);
                    Lines.circle(e.x, e.y, e.finpow() * 18f);
                });

                
                smokeEffect = new Effect(18f, e -> {
                    color(Pal.techBlue);
                    stroke(e.fout() * 2.2f);
                    Lines.circle(e.x, e.y, 5f + e.finpow() * 22f);
                });

                
                hitEffect = new OptionalMultiEffect(
                        JBFx.hitSpark(Pal.techBlue, 45f, 14, 42f, 2f, 9f),
                        JBFx.circleOut(Pal.techBlue, 40f));
                despawnEffect = JBFx.crossBlast(Pal.techBlue, 48f, 22);
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                
                float pulse = Mathf.absin(b.time, 5f, 0.18f);
                Draw.color(Pal.techBlue);
                Draw.alpha(0.22f + pulse);
                circle(b.x, b.y, width * 1.1f + pulse * 3f);

                
                Draw.color(Color.white);
                Draw.alpha(0.35f + pulse * 0.5f);
                circle(b.x, b.y, width * 0.35f);

                Draw.reset();
            }
        };

        plasmaBoltBullet = new LightningLinkerBulletType() {
            {
                effectLightningChance = 0.15f;
                damage = 1200;
                backColor = trailColor = lightColor = lightningColor = hitColor = Pal.techBlue;
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
                lifetime = 130f;
                despawnEffect = JBFx.lightningHitLarge(hitColor);
                hitEffect = JBFx.sharpBlast(hitColor, frontColor, 35, splashDamageRadius * 1.25f);
                shootEffect = JBFx.hitSpark(backColor, 45f, 12, 60, 3, 8);
                smokeEffect = JBFx.hugeSmoke;
            }
        };

        hyperBeamBullet = new BasicBulletType() {
            {
                
                damage = 1220f;
                speed = 11f;
                lifetime = 75f;
                width = 13f;
                height = 44f;

                
                frontColor = Color.white;
                backColor = Pal.techBlue;
                hitColor = Pal.techBlue;

                
                trailLength = 30;
                trailWidth = 5.5f;
                trailColor = Pal.techBlue;
                trailInterval = 1f;
                trailRotation = false;

                
                lightColor = Pal.techBlue;
                lightRadius = 75f;
                lightOpacity = 0.8f;

                
                splashDamage = 160f;
                splashDamageRadius = 70f;

                
                lightning = 3;
                lightningDamage = 55f;
                lightningLength = 8;
                lightningLengthRand = 14;
                lightningColor = Pal.techBlue;

                
                status = StatusEffects.electrified;
                statusDuration = 60f * 5f;

                hitShake = 5f;
                hitSound = Sounds.explosion;

                
                shootEffect = new Effect(35f, e -> {
                    for (int i = 0; i < 3; i++) {
                        float delay = i * 0.18f;
                        float progress = Mathf.curve(e.fin(), delay, 1f);
                        if (progress <= 0f)
                            continue;
                        float fout = 1f - progress;

                        color(Pal.techBlue, Color.white, fout * 0.5f);
                        stroke(fout * (2.8f - i * 0.6f));
                        Lines.circle(e.x, e.y, progress * (22f + i * 10f));
                    }

                    
                    color(Pal.techBlue);
                    stroke(e.fout() * 2f);
                    randLenVectors(e.id, 6, e.finpow() * 30f, e.rotation, 15f, (x, y) -> {
                        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 10f + 3f);
                    });
                });

                
                smokeEffect = new Effect(25f, e -> {
                    color(Pal.techBlue);
                    Draw.alpha(e.fout() * 0.5f);
                    randLenVectors(e.id, 5, e.finpow() * 18f, (x, y) -> {
                        circle(e.x + x, e.y + y, e.fout() * 4f);
                    });
                    Draw.reset();
                });

                
                trailEffect = new Effect(28f, e -> {
                    Draw.color(Pal.techBlue, Color.white, e.fin() * 0.25f);
                    Draw.alpha(e.fout() * 0.65f);
                    Lines.stroke(e.fout() * 1.5f);
                    Lines.square(e.x, e.y, e.fout() * Mathf.randomSeed(e.id, 3f, 7f), 45f);

                    Draw.color(Pal.techBlue);
                    Draw.alpha(e.fout() * 0.3f);
                    Fill.square(e.x, e.y, e.fout() * Mathf.randomSeed(e.id + 1, 1.5f, 3f), 45f);
                    Draw.reset();
                });

                
                hitEffect = new OptionalMultiEffect(
                        JBFx.blast(Pal.techBlue, 70f),
                        JBFx.hitSparkHuge, 
                        JBFx.crossBlast(Pal.techBlue, 85f, 22));
                despawnEffect = new OptionalMultiEffect(
                        JBFx.crossBlast(Pal.techBlue, 60f, 0),
                        JBFx.hitSpark(Pal.techBlue, 35f, 10, 45f, 2f, 10f));
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                float pulse = Mathf.absin(b.time, 4f, 1f);
                float rot = b.time * 3f;

                
                Draw.color(Pal.techBlue);
                Draw.alpha(0.35f + pulse * 0.1f);
                Lines.stroke(1.8f + pulse * 0.4f);

                for (int i = 0; i < 4; i++) {
                    Lines.arc(b.x, b.y, width * 1.6f + pulse, 0.18f, rot + i * 90f);
                }

                
                Draw.color(Pal.techBlue, Color.white, 0.3f);
                Draw.alpha(0.4f + pulse * 0.15f);
                Lines.stroke(1.3f);
                Lines.square(b.x, b.y, width * 0.85f + pulse * 0.5f, rot * 1.5f);

                
                Draw.color(Color.white);
                Draw.alpha(0.9f);
                circle(b.x, b.y, width * 0.38f);

                Draw.color(Pal.techBlue);
                Draw.alpha(0.5f + pulse * 0.2f);
                circle(b.x, b.y, width * 0.75f + pulse);

                
                Drawf.light(b.x, b.y, lightRadius * (0.7f + pulse * 0.1f), Pal.techBlue, lightOpacity);

                Draw.reset();
            }

            @Override
            public void hit(Bullet b) {
                super.hit(b);

                
                for (int i = 0; i < 4; i++) {
                    Lightning.create(b, lightningColor, lightningDamage * 0.75f,
                            b.x, b.y,
                            b.rotation() + i * 90f + Mathf.range(25f),
                            lightningLength + Mathf.random(lightningLengthRand));
                }
            }

            @Override
            public void despawned(Bullet b) {
                super.despawned(b);
                Sounds.explosionAfflict.at(b.x, b.y, 1.3f);
            }
        };

        voidLanceBullet = new BasicBulletType() {
            {
                
                damage = 2750f;
                speed = 13f;
                lifetime = 80f;
                width = 18f;
                height = 72f;

                
                frontColor = Color.white;
                backColor = Pal.techBlue;
                hitColor = Pal.techBlue;

                
                trailLength = 40;
                trailWidth = 7f;
                trailColor = Pal.techBlue;
                trailInterval = 1f;
                trailRotation = false;

                
                lightColor = Pal.techBlue;
                lightRadius = 110f;
                lightOpacity = 0.9f;

                
                splashDamage = 380f;
                splashDamageRadius = 95f;

                
                lightning = 5;
                lightningDamage = 90f;
                lightningLength = 12;
                lightningLengthRand = 20;
                lightningColor = Pal.techBlue;

                
                status = StatusEffects.electrified;
                statusDuration = 60f * 8f;

                hitShake = 8f;
                hitSound = Sounds.explosionArtilleryShockBig;

                
                shootEffect = new Effect(45f, e -> {
                    
                    color(Pal.techBlue, Color.white, e.fout() * 0.4f);
                    stroke(e.fout() * 4f);
                    Lines.circle(e.x, e.y, e.finpow() * 55f);

                    
                    color(Pal.techBlue);
                    stroke(e.fout() * 2.5f);
                    Lines.circle(e.x, e.y, e.finpow() * 35f);

                    
                    randLenVectors(e.id, 14, e.finpow() * 45f, e.rotation, 18f, (x, y) -> {
                        color(Pal.techBlue, Color.white, e.fout() * 0.5f);
                        stroke(e.fout() * 2.2f);
                        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 14f + 4f);
                    });

                    
                    color(Pal.techBlue);
                    stroke(e.fout() * 3f);
                    for (int s : Mathf.signs) {
                        float bx = e.x + Angles.trnsx(e.rotation + 90f * s, 22f * e.finpow());
                        float by = e.y + Angles.trnsy(e.rotation + 90f * s, 22f * e.finpow());
                        Lines.line(e.x, e.y, bx, by);
                    }
                });

                
                smokeEffect = new Effect(30f, e -> {
                    color(Pal.techBlue, Color.white, e.fin() * 0.3f);
                    Draw.alpha(e.fout() * 0.6f);
                    Angles.randLenVectors(e.id, 8, 5f + 28f * e.finpow(), (x, y) -> {
                        circle(e.x + x, e.y + y, e.fout() * 5.5f);
                    });
                    Draw.reset();
                });

                
                trailEffect = new Effect(35f, e -> {
                    rand.setSeed(e.id);

                    
                    Draw.color(Pal.techBlue, Color.white, rand.random(0.4f));
                    Draw.alpha(e.fout() * 0.75f);

                    float ang = e.rotation + rand.range(55f);
                    float len = rand.random(12f, 28f) * e.fout(Interp.pow2Out);
                    DrawFunc.tri(
                            e.x + rand.range(5f),
                            e.y + rand.range(5f),
                            2.8f * e.fout(),
                            len,
                            ang);

                    
                    Draw.color(Pal.techBlue);
                    Draw.alpha(e.fout() * 0.5f);
                    circle(
                            e.x + rand.range(8f),
                            e.y + rand.range(8f),
                            rand.random(1.5f, 3.5f) * e.fout());

                    Drawf.light(e.x, e.y, e.fout() * 35f, Pal.techBlue, 0.5f);
                    Draw.reset();
                });

                
                hitEffect = new OptionalMultiEffect(
                        JBFx.blast(Pal.techBlue, 95f),
                        JBFx.sharpBlast(Pal.techBlue, Color.white, 55f, 100f),
                        JBFx.hitSpark(Pal.techBlue, 70f, 24, 95f, 3f, 16f));

                despawnEffect = new OptionalMultiEffect(
                        JBFx.sharpBlast(Pal.techBlue, Color.white, 40f, 75f),
                        JBFx.crossBlast(Pal.techBlue, 90f, 0),
                        JBFx.crossBlast(Pal.techBlue, 70f, 45));
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                float pulse = Mathf.absin(b.time, 3f, 1f);
                float rot = b.time * 5f;
                float progress = b.fin();

                
                Lines.stroke(2.2f + pulse * 0.4f);

                Draw.color(Pal.techBlue, Color.white, 0.2f);
                Draw.alpha(0.5f + pulse * 0.12f);
                for (int i = 0; i < 3; i++) {
                    Lines.arc(b.x, b.y, width * 2f + pulse, 0.22f, rot + i * 120f);
                }

                Draw.color(Pal.techBlue);
                Draw.alpha(0.38f + pulse * 0.1f);
                for (int i = 0; i < 3; i++) {
                    Lines.arc(b.x, b.y, width * 1.45f + pulse, 0.18f, -rot * 1.4f + i * 120f);
                }

                
                Draw.color(Pal.techBlue, Color.white, 0.35f);
                Draw.alpha(0.6f + pulse * 0.15f);
                for (int i = 0; i < 4; i++) {
                    float angle = rot * 1.2f + i * 90f;
                    float dist = width * 1.1f + pulse * 1.5f;
                    DrawFunc.tri(
                            b.x + Angles.trnsx(angle, dist),
                            b.y + Angles.trnsy(angle, dist),
                            3.5f + pulse * 0.5f,
                            14f + pulse * 2f,
                            angle);
                }

                
                
                Draw.color(Pal.techBlue);
                Draw.alpha(0.45f + pulse * 0.2f);
                circle(b.x, b.y, width * 1.05f + pulse * 2f);

                
                Draw.color(Pal.techBlue, Color.white, 0.45f);
                Draw.alpha(0.7f + pulse * 0.15f);
                circle(b.x, b.y, width * 0.6f + pulse);

                
                Draw.color(Color.white);
                Draw.alpha(0.95f);
                circle(b.x, b.y, width * 0.28f + pulse * 0.5f);

                
                Drawf.light(b.x, b.y, lightRadius * (0.85f + pulse * 0.08f), Pal.techBlue, lightOpacity);

                Draw.reset();
            }

            @Override
            public void hit(Bullet b) {
                super.hit(b);

                
                for (int i = 0; i < 6; i++) {
                    Lightning.create(b, lightningColor, lightningDamage,
                            b.x, b.y,
                            b.rotation() + i * 60f + Mathf.range(15f),
                            lightningLength + Mathf.random(lightningLengthRand));
                }

                
                for (int i = 0; i < 8; i++) {
                    Lightning.create(b, lightningColor, lightningDamage * 0.5f,
                            b.x, b.y,
                            Mathf.random(360f),
                            lightningLength / 2 + Mathf.random(8));
                }
            }

            @Override
            public void despawned(Bullet b) {
                super.despawned(b);
                Sounds.explosionCore.at(b.x, b.y, 0.7f);
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

        deathBeam = new ContinuousFlameBulletType(5000) {
            {
                shake = 6;
                hitColor = flareColor = lightColor = lightningColor = JBColor.thurmixRed;

                colors = new Color[] {
                        JBColor.thurmixRed.cpy().mul(0.4f, 0.1f, 0.1f, 0.5f),
                        JBColor.thurmixRed.cpy().mul(0.8f, 0.2f, 0.2f, 0.7f),
                        JBColor.thurmixRed.cpy().mul(1f, 0.4f, 0.4f, 0.85f),
                        JBColor.thurmixRedLight
                };

                width = 22;
                length = 1200f;
                oscScl = 1.4f;
                oscMag *= 3f;
                lifetime = 350f;

                lightning = 6;
                lightningLength = 4;
                lightningLengthRand = 24;
                flareLength = 120;
                flareWidth = 10;

                lightningDamage = damage / 5f;
                despawnHit = false;
                pierceArmor = true;

                hitEffect = new Effect(35f, e -> {
                    Draw.color(JBColor.thurmixRed, JBColor.thurmixRedLight, e.fout() * 0.5f);

                    
                    for (int i = 0; i < 3; i++) {
                        float delay = i * 0.2f;
                        float p = Mathf.curve(e.fin(), delay, 1f);
                        if (p <= 0f)
                            continue;
                        float fo = 1f - p;

                        Lines.stroke((5f - i * 1.2f) * fo);
                        Lines.circle(e.x, e.y, p * (30f + i * 18f));
                    }

                    
                    Draw.color(JBColor.thurmixRedLight, Color.white, e.fout() * 0.4f);
                    Lines.stroke(e.fout() * 2f);
                    Angles.randLenVectors(e.id, 10, e.finpow() * 55f, (x, y) -> {
                        float ang = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 14f + 4f);
                    });

                    
                    e.scaled(14f, s -> {
                        Draw.color(Color.white, JBColor.thurmixRed, s.fin());
                        Fill.circle(e.x, e.y, s.fout() * 18f);
                        Drawf.light(e.x, e.y, s.fout() * 80f, JBColor.thurmixRed, 0.9f);
                    });

                    Drawf.light(e.x, e.y, e.fout() * 60f, JBColor.thurmixRed, 0.75f);
                });

                shootEffect = new Effect(55f, e -> {
                    
                    Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.5f);
                    for (int i = 0; i < 4; i++) {
                        DrawFunc.tri(e.x, e.y,
                                14f * e.fout(),
                                80f * e.fout(Interp.pow3Out),
                                i * 90f + e.rotation);
                    }

                    
                    Lines.stroke(e.fout() * 3f);
                    Lines.circle(e.x, e.y, e.finpow() * 50f);
                    Lines.stroke(e.fout() * 2f);
                    Lines.circle(e.x, e.y, e.fin(Interp.pow2Out) * 75f);

                    
                    Draw.color(Color.white, JBColor.thurmixRedLight, e.fout() * 0.4f);
                    Lines.stroke(e.fout() * 2.5f);
                    Angles.randLenVectors(e.id, 14, e.finpow() * 60f, e.rotation, 30f, (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 18f + 5f);
                    });

                    e.scaled(20f, s -> {
                        Draw.color(Color.white);
                        Draw.alpha(s.fout(Interp.pow3Out));
                        Fill.circle(e.x, e.y, s.fout() * 28f);
                        Drawf.light(e.x, e.y, s.fout() * 120f, Color.white, 1f);
                    });

                    Drawf.light(e.x, e.y, e.fout() * 180f, JBColor.thurmixRed, 0.9f);
                });
            }

            
            final Color edgeColor = JBColor.thurmixRed.cpy().mul(0.5f, 0.1f, 0.1f, 1f);
            final Color coreColor = Color.valueOf("ffffff");
            float beamPulse = 0f;

            @Override
            public void update(Bullet b) {
                super.update(b);

                beamPulse = Mathf.absin(b.time, 6f, 1f);

                
                if (Mathf.chanceDelta(0.18f)) {
                    for (int i = 0; i < lightning; i++) {
                        Lightning.create(b, lightningColor,
                                lightningDamage < 0 ? damage : lightningDamage,
                                b.x, b.y,
                                b.rotation() + Mathf.range(lightningCone / 2f) + lightningAngle,
                                lightningLength + Mathf.random(lightningLengthRand));
                    }
                }

                
                if (Mathf.chanceDelta(0.06f)) {
                    Lightning.create(b, JBColor.thurmixRedLight,
                            lightningDamage * 1.8f,
                            b.x, b.y,
                            b.rotation() + Mathf.range(55f),
                            lightningLength * 2 + Mathf.random(30));
                }

                
                if (Mathf.chanceDelta(0.5f)) {
                    float dist = Mathf.random(length * 0.2f, length * 0.85f);
                    float bx = b.x + Angles.trnsx(b.rotation(), dist);
                    float by = b.y + Angles.trnsy(b.rotation(), dist);

                    new Effect(30f, e -> {
                        Draw.color(JBColor.thurmixRed, JBColor.thurmixRedLight, e.fout() * 0.5f);
                        Draw.alpha(e.fout() * 0.75f);
                        Fill.circle(e.x, e.y, e.fout() * Mathf.randomSeed(e.id, 4f, 9f));

                        Draw.color(Color.white);
                        Draw.alpha(e.fout() * 0.3f);
                        Fill.circle(e.x, e.y, e.fout() * 3f);

                        Drawf.light(e.x, e.y, e.fout() * 25f, JBColor.thurmixRed, 0.7f);
                    }).at(bx + Mathf.range(width * 1.5f),
                            by + Mathf.range(width * 1.5f),
                            b.rotation(), JBColor.thurmixRed);
                }

                
                if (Mathf.chanceDelta(0.35f)) {
                    new Effect(22f, e -> {
                        Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.35f);
                        Draw.alpha(e.fout() * 0.7f);
                        Lines.stroke(e.fout() * 2.5f);
                        Lines.arc(e.x, e.y,
                                (12f + Mathf.randomSeed(e.id, 5f, 14f)) * e.fout(),
                                0.3f,
                                e.rotation + Mathf.randomSeed(e.id, 360f));
                        Drawf.light(e.x, e.y, e.fout() * 35f, JBColor.thurmixRed, 0.6f);
                    }).at(b.x + Mathf.range(8f),
                            b.y + Mathf.range(8f),
                            b.rotation(), JBColor.thurmixRed);
                }

                
                if (Mathf.chanceDelta(0.4f)) {
                    float spawnDist = Mathf.random(length * 0.1f, length * 0.7f);
                    float spawnX = b.x + Angles.trnsx(b.rotation(), spawnDist);
                    float spawnY = b.y + Angles.trnsy(b.rotation(), spawnDist);
                    float scatterAngle = b.rotation() + Mathf.range(80f);

                    new Effect(40f, 100f, e -> {
                        float travel = e.fin(Interp.pow2Out) * Mathf.randomSeed(e.id, 20f, 50f);
                        float tx = e.x + Angles.trnsx(e.rotation, travel);
                        float ty = e.y + Angles.trnsy(e.rotation, travel);

                        Draw.color(JBColor.thurmixRed, Color.white, 0.4f + e.fout() * 0.3f);
                        Lines.stroke(e.fout() * 3f);
                        Lines.line(e.x, e.y, tx, ty);

                        Draw.color(Color.white);
                        Fill.circle(tx, ty, e.fout() * 4f);

                        Drawf.light(tx, ty, e.fout() * 22f, JBColor.thurmixRed, 0.9f);
                    }).at(spawnX, spawnY, scatterAngle, JBColor.thurmixRed);
                }
            }

            @Override
            public void hit(Bullet b, float x, float y) {
                hitEffect.at(x, y, b.rotation(), hitColor);
                hitSound.at(x, y, hitSoundPitch, hitSoundVolume);
                Effect.shake(hitShake * 1.5f, hitShake * 1.5f, b);

                
                Lightning.create(b, lightningColor,
                        lightningDamage < 0 ? damage : lightningDamage,
                        x, y,
                        b.rotation() + Mathf.range(lightningCone / 2f) + lightningAngle,
                        lightningLength + Mathf.random(lightningLengthRand));

                
                for (int i = 0; i < 3; i++) {
                    Lightning.create(b, JBColor.thurmixRedLight,
                            lightningDamage * 0.7f,
                            x, y,
                            b.rotation() + i * 120f + Mathf.range(30f),
                            lightningLength + Mathf.random(20));
                }

                
                new Effect(45f, 150f, e -> {
                    Draw.color(JBColor.thurmixRed, JBColor.thurmixRedLight, e.fin() * 0.6f);

                    Lines.stroke(5f * e.fout());
                    Lines.circle(x, y, e.fin(Interp.pow2Out) * 55f);

                    Lines.stroke(3f * e.fout());
                    Lines.circle(x, y, e.fin(Interp.pow3Out) * 80f);

                    Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.5f);
                    Lines.stroke(e.fout() * 2.5f);
                    Angles.randLenVectors(e.id, 14, e.finpow() * 65f, (px, py) -> {
                        float ang = Mathf.angle(px, py);
                        Lines.lineAngle(x + px, y + py, ang, e.fout() * 16f + 5f);
                        Fill.circle(x + px, y + py, e.fout() * 4f);
                    });

                    e.scaled(18f, s -> {
                        Draw.color(Color.white, JBColor.thurmixRed, s.fin());
                        Fill.circle(x, y, s.fout() * 22f);
                        Drawf.light(x, y, s.fout() * 100f, Color.white, 1f);
                    });

                    Drawf.light(x, y, e.fout() * 120f, JBColor.thurmixRed, 0.85f);
                }).at(x, y);
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                float pulse = Mathf.absin(b.time, 5f, 1f);

                
                Draw.z(Layer.bullet + 0.003f);

                Draw.color(edgeColor);
                Draw.alpha(0.55f + pulse * 0.1f);
                Fill.circle(b.x, b.y, (width * 2.2f + pulse * 3f));

                Draw.color(JBColor.thurmixRed);
                Draw.alpha(0.85f + pulse * 0.1f);
                Fill.circle(b.x, b.y, (width * 1.4f + pulse * 2f));

                Draw.color(JBColor.thurmixRedLight);
                Draw.alpha(0.9f);
                Fill.circle(b.x, b.y, (width * 0.8f + pulse * 1.5f));

                Draw.color(coreColor);
                Draw.alpha(0.95f);
                Fill.circle(b.x, b.y, (width * 0.3f + pulse * 0.8f));

                
                for (int i = 0; i < 6; i++) {
                    float arcAngle = b.time * 3f + i * 60f;
                    float arcR = (width * 1.6f + pulse * 2f);

                    Draw.color(edgeColor);
                    Draw.alpha(0.9f);
                    Lines.stroke(3f);
                    Lines.arc(b.x, b.y, arcR, 0.2f, arcAngle);

                    Draw.color(JBColor.thurmixRed);
                    Draw.alpha(0.8f + pulse * 0.1f);
                    Lines.stroke(1.8f);
                    Lines.arc(b.x, b.y, arcR, 0.2f, arcAngle);

                    Drawf.light(
                            b.x + Angles.trnsx(arcAngle, arcR),
                            b.y + Angles.trnsy(arcAngle, arcR),
                            30f + pulse * 10f, JBColor.thurmixRed, 0.75f);
                }

                Drawf.light(b.x, b.y,
                        (width * 9f + pulse * 30f),
                        JBColor.thurmixRed, 0.95f);

                Draw.reset();
            }
        };

        ancientBall = new AccelBulletType(2.85f, 240f, MINE_BULLET) {
            {
                frontColor = Color.white;
                backColor = lightningColor = trailColor = hitColor = lightColor = JBColor.thurmixRed;
                lifetime = 95f;

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

                        lightningColor = trailColor = hitColor = lightColor = JBColor.thurmixRedLight.cpy()
                                .lerp(Pal.accent, 0.055f);

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

        oraxiaBullet = new LightningLinkerBulletType() {
            {
                effectLightningChance = 0.15f;
                damage = 200;
                backColor = trailColor = lightColor = lightningColor = hitColor = JBColor.thurmixRed;
                size = 10f;
                frontColor = JBColor.thurmixRedLight;
                range = 600f;
                spreadEffect = Fx.none;

                trailWidth = 8f;
                trailLength = 20;

                speed = 6f;

                linkRange = 280f;

                maxHit = 12;
                drag = 0.0065f;
                hitSound = Sounds.explosion;
                splashDamageRadius = 60f;
                splashDamage = lightningDamage = damage / 3f;
                lifetime = 130f;
                despawnEffect = JBFx.lightningHitLarge(hitColor);
                hitEffect = JBFx.sharpBlast(hitColor, frontColor, 35, splashDamageRadius * 1.25f);
                shootEffect = JBFx.hitSpark(backColor, 45f, 12, 60, 3, 8);
                smokeEffect = JBFx.hugeSmoke;
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

        broodmotherDeathBeam = new ContinuousFlameBulletType(3500) {

            
            
            
            
            final float CHARGE_END = 300f;
            final float BURST_END = 345f;

            {
                shake = 8;
                hitColor = flareColor = lightColor = lightningColor = JBColor.thurmixRed;

                colors = new Color[] {
                        JBColor.thurmixDeep.cpy().a(0.45f),
                        JBColor.thurmixRed.cpy().a(0.70f),
                        JBColor.thurmixFlare.cpy().a(0.88f),
                        JBColor.thurmixCore
                };

                width = 32f;
                length = 2400f;
                oscScl = 1.6f;
                oscMag *= 3.5f;
                lifetime = 720f;

                lightning = 7;
                lightningLength = 5;
                lightningLengthRand = 28;
                flareLength = 160f;
                flareWidth = 14f;

                lightningDamage = damage / 5f;
                despawnHit = false;
                pierceArmor = true;

                
                hitEffect = new Effect(40f, e -> {
                    Draw.color(JBColor.thurmixRed, JBColor.thurmixCore, e.fout() * 0.5f);

                    for (int i = 0; i < 3; i++) {
                        float p = Mathf.curve(e.fin(), i * 0.2f, 1f);
                        if (p <= 0f)
                            continue;
                        Lines.stroke((6f - i * 1.5f) * (1f - p));
                        Lines.circle(e.x, e.y, p * (35f + i * 22f));
                    }

                    Draw.color(JBColor.thurmixFlare, JBColor.thurmixCore, e.fout() * 0.4f);
                    Lines.stroke(e.fout() * 2.5f);
                    Angles.randLenVectors(e.id, 12, e.finpow() * 70f, (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 18f + 5f);
                    });

                    e.scaled(15f, s -> {
                        Draw.color(JBColor.thurmixCore);
                        Draw.alpha(s.fout(Interp.pow3Out));
                        Fill.circle(e.x, e.y, s.fout() * 24f);
                        Drawf.light(e.x, e.y, s.fout() * 100f, JBColor.thurmixCore, 1f);
                    });

                    Drawf.light(e.x, e.y, e.fout() * 80f, JBColor.thurmixRed, 0.8f);
                });

                
                shootEffect = new Effect(80f, 600f, e -> {
                    final float ex = e.x, ey = e.y;

                    
                    e.scaled(22f, flash -> {
                        Draw.color(JBColor.thurmixCore);
                        Draw.alpha(flash.fout(Interp.pow3Out));
                        Fill.circle(ex, ey, flash.fout() * 160f);
                        Drawf.light(ex, ey, flash.fout() * 500f, JBColor.thurmixCore, 1f);
                    });

                    
                    for (int w = 0; w < 5; w++) {
                        final int fw = w;
                        float wDelay = fw * 0.14f;
                        float p = Mathf.curve(e.fin(), wDelay, 1f);
                        if (p <= 0f)
                            continue;
                        float fo = 1f - p;

                        Draw.color(fw % 2 == 0 ? JBColor.thurmixRed : JBColor.thurmixDeep,
                                JBColor.thurmixFlare, fo * 0.5f);
                        Lines.stroke((14f - fw * 2.2f) * fo);
                        Lines.circle(ex, ey, 10f + Interp.pow2Out.apply(p) * (220f + fw * 60f));
                    }

                    
                    Angles.randLenVectors(e.id, 28, 40f + 180f * e.finpow(), (x, y) -> {
                        float ang = Mathf.angle(x, y);
                        Fx.rand.setSeed(e.id + (long) (x * 77));
                        float w = Fx.rand.random(12f, 28f) * e.fout() * e.fout();
                        float len = Fx.rand.random(100f, 220f) * e.fout(Interp.pow2Out);

                        Draw.color(JBColor.thurmixDeep);
                        Draw.alpha(e.fout() * 0.95f);
                        DrawFunc.tri(ex + x, ey + y, w * 1.3f, len * 1.1f, ang);
                        DrawFunc.tri(ex + x, ey + y, w * 0.65f, len * 0.22f, ang + 180f);

                        Draw.color(JBColor.thurmixRed);
                        Draw.alpha(e.fout() * 0.90f);
                        DrawFunc.tri(ex + x, ey + y, w, len, ang);
                        DrawFunc.tri(ex + x, ey + y, w * 0.45f, len * 0.2f, ang + 180f);

                        Draw.color(JBColor.thurmixFlare);
                        Draw.alpha(e.fout() * 0.6f);
                        DrawFunc.tri(ex + x, ey + y, w * 0.35f, len * 0.75f, ang);

                        Drawf.light(ex + x, ey + y, w * 3f * e.fout(), JBColor.thurmixRed, 0.8f);
                    });

                    Drawf.light(ex, ey, e.fout() * 350f, JBColor.thurmixRed, 0.97f);
                });

                smokeEffect = JBFx.hugeSmokeGray;
            }

            
            void drawCharge(Bullet b) {
                float t = b.time;
                float progress = Mathf.clamp(t / CHARGE_END);
                float pulse = Mathf.absin(t, 5f, 1f);
                float bx = b.x, by = b.y;

                
                Draw.color(JBColor.thurmixDeep);
                Draw.alpha(progress * 0.55f + pulse * 0.08f);
                Fill.circle(bx, by, (80f + pulse * 8f) * progress);

                Draw.color(JBColor.thurmixRed);
                Draw.alpha(progress * 0.75f + pulse * 0.1f);
                Fill.circle(bx, by, (55f + pulse * 6f) * progress);

                Draw.color(JBColor.thurmixFlare);
                Draw.alpha(progress * 0.85f);
                Fill.circle(bx, by, (30f + pulse * 4f) * progress);

                Draw.color(JBColor.thurmixCore);
                Draw.alpha(progress * 0.95f);
                Fill.circle(bx, by, (12f + pulse * 2f) * progress);

                
                for (int i = 0; i < 5; i++) {
                    float ringProgress = ((t * 0.6f + i * 22f) % 110f) / 110f;
                    float ringRadius = (1f - ringProgress) * (180f + i * 20f) * progress;
                    float ringAlpha = ringProgress * (1f - ringProgress) * 4f * progress;

                    Draw.color(i % 2 == 0 ? JBColor.thurmixRed : JBColor.thurmixFlare);
                    Draw.alpha(ringAlpha * 0.8f);
                    Lines.stroke((3.5f - i * 0.4f) * progress);
                    Lines.circle(bx, by, ringRadius);
                }

                
                Angles.randLenVectors((int) (b.id + t * 0.3f), 20,
                        (1f - progress * 0.7f) * 150f * progress,
                        (x, y) -> {
                            Draw.color(JBColor.thurmixFlare, JBColor.thurmixCore, progress * 0.5f);
                            Draw.alpha(progress * 0.8f);
                            Fill.circle(bx + x, by + y, progress * 4.5f);
                            Drawf.light(bx + x, by + y, progress * 16f, JBColor.thurmixRed, 0.7f);
                        });

                
                float crossRot = t * 2.8f;
                for (int i = 0; i < 4; i++) {
                    float armAngle = crossRot + i * 90f;
                    float armLen = (120f + pulse * 10f) * progress;
                    float armW = (14f + pulse * 2f) * progress;

                    
                    Draw.color(JBColor.thurmixDeep);
                    Draw.alpha(progress * 0.9f);
                    DrawFunc.tri(bx, by, armW * 1.25f, armLen * 1.05f, armAngle);
                    DrawFunc.tri(bx, by, armW * 0.7f, armLen * 0.22f, armAngle + 180f);

                    
                    Draw.color(JBColor.thurmixRed);
                    Draw.alpha(progress * 0.95f);
                    DrawFunc.tri(bx, by, armW, armLen, armAngle);
                    DrawFunc.tri(bx, by, armW * 0.5f, armLen * 0.2f, armAngle + 180f);

                    
                    Draw.color(JBColor.thurmixFlare);
                    Draw.alpha(progress * 0.7f);
                    DrawFunc.tri(bx, by, armW * 0.38f, armLen * 0.78f, armAngle);

                    
                    float tipX = bx + Angles.trnsx(armAngle, armLen);
                    float tipY = by + Angles.trnsy(armAngle, armLen);
                    Draw.color(JBColor.thurmixCore);
                    Draw.alpha(progress * 0.8f);
                    Fill.circle(tipX, tipY, (5f + pulse) * progress);

                    Drawf.light(tipX, tipY, armLen * 0.25f * progress, JBColor.thurmixRed, 0.75f);
                }

                
                float crossRot2 = t * 1.5f + 45f;
                for (int i = 0; i < 4; i++) {
                    float armAngle = crossRot2 + i * 90f;
                    float armLen = (70f + pulse * 6f) * progress;
                    float armW = (7f + pulse) * progress;

                    Draw.color(JBColor.thurmixDeep);
                    Draw.alpha(progress * 0.75f);
                    DrawFunc.tri(bx, by, armW * 1.2f, armLen * 1.05f, armAngle);

                    Draw.color(JBColor.thurmixRed);
                    Draw.alpha(progress * 0.85f);
                    DrawFunc.tri(bx, by, armW, armLen, armAngle);

                    Draw.color(JBColor.thurmixFlare);
                    Draw.alpha(progress * 0.55f);
                    DrawFunc.tri(bx, by, armW * 0.4f, armLen * 0.72f, armAngle);
                }

                
                for (int i = 0; i < 8; i++) {
                    float arcAngle = t * (1.8f + i * 0.25f) * (i % 2 == 0 ? 1f : -1f) + i * 45f;
                    float arcRadius = (38f + i * 8f + pulse * 3f) * progress;

                    Draw.color(i % 3 == 0 ? JBColor.thurmixFlare : JBColor.thurmixRed);
                    Draw.alpha(progress * (0.5f + (i % 2) * 0.2f));
                    Lines.stroke((2.8f - i * 0.2f) * progress);
                    Lines.arc(bx, by, arcRadius, 0.28f, arcAngle * 60f);

                    Drawf.light(
                            bx + Angles.trnsx(arcAngle * 60f, arcRadius),
                            by + Angles.trnsy(arcAngle * 60f, arcRadius),
                            20f * progress, JBColor.thurmixRed, 0.6f);
                }

                Drawf.light(bx, by, (200f + pulse * 50f) * progress, JBColor.thurmixRed, 0.95f);
            }

            
            void drawActiveCross(Bullet b) {
                float t = b.time;
                float pulse = Mathf.absin(t, 4f, 1f);
                float bx = b.x, by = b.y;

                float crossRot = t * 2.8f;
                for (int i = 0; i < 4; i++) {
                    float armAngle = crossRot + i * 90f;
                    float armLen = 120f + pulse * 10f;
                    float armW = 14f + pulse * 2f;

                    Draw.color(JBColor.thurmixDeep);
                    Draw.alpha(0.9f);
                    DrawFunc.tri(bx, by, armW * 1.25f, armLen * 1.05f, armAngle);
                    DrawFunc.tri(bx, by, armW * 0.7f, armLen * 0.22f, armAngle + 180f);

                    Draw.color(JBColor.thurmixRed);
                    Draw.alpha(0.95f);
                    DrawFunc.tri(bx, by, armW, armLen, armAngle);
                    DrawFunc.tri(bx, by, armW * 0.5f, armLen * 0.2f, armAngle + 180f);

                    Draw.color(JBColor.thurmixFlare);
                    Draw.alpha(0.7f);
                    DrawFunc.tri(bx, by, armW * 0.38f, armLen * 0.78f, armAngle);

                    float tipX = bx + Angles.trnsx(armAngle, armLen);
                    float tipY = by + Angles.trnsy(armAngle, armLen);
                    Draw.color(JBColor.thurmixCore);
                    Fill.circle(tipX, tipY, 5f + pulse);
                    Drawf.light(tipX, tipY, 35f, JBColor.thurmixRed, 0.75f);
                }

                float crossRot2 = t * 1.5f + 45f;
                for (int i = 0; i < 4; i++) {
                    float armAngle = crossRot2 + i * 90f;
                    float armLen = 70f + pulse * 6f;
                    float armW = 7f + pulse;

                    Draw.color(JBColor.thurmixDeep);
                    Draw.alpha(0.75f);
                    DrawFunc.tri(bx, by, armW * 1.2f, armLen * 1.05f, armAngle);

                    Draw.color(JBColor.thurmixRed);
                    Draw.alpha(0.85f);
                    DrawFunc.tri(bx, by, armW, armLen, armAngle);

                    Draw.color(JBColor.thurmixFlare);
                    Draw.alpha(0.55f);
                    DrawFunc.tri(bx, by, armW * 0.4f, armLen * 0.72f, armAngle);
                }

                Drawf.light(bx, by, 160f + pulse * 40f, JBColor.thurmixRed, 0.9f);
            }

            @Override
            public void draw(Bullet b) {
                float t = b.time;

                if (t < CHARGE_END) {
                    
                    drawCharge(b);
                } else if (t < BURST_END) {
                    
                    drawActiveCross(b);
                    super.draw(b);
                } else {
                    
                    drawActiveCross(b);
                    super.draw(b);

                    
                    float pulse = Mathf.absin(t, 5f, 1f);
                    Draw.z(Layer.bullet + 0.003f);

                    Draw.color(JBColor.thurmixDeep);
                    Draw.alpha(0.5f + pulse * 0.1f);
                    Fill.circle(b.x, b.y, width * 2.4f + pulse * 4f);

                    Draw.color(JBColor.thurmixRed);
                    Draw.alpha(0.8f + pulse * 0.1f);
                    Fill.circle(b.x, b.y, width * 1.5f + pulse * 2f);

                    Draw.color(JBColor.thurmixFlare);
                    Draw.alpha(0.9f);
                    Fill.circle(b.x, b.y, width * 0.85f + pulse * 1.5f);

                    Draw.color(JBColor.thurmixCore);
                    Draw.alpha(0.95f);
                    Fill.circle(b.x, b.y, width * 0.32f + pulse * 0.8f);

                    Drawf.light(b.x, b.y, width * 10f + pulse * 35f, JBColor.thurmixRed, 0.97f);
                    Draw.reset();
                }
            }

            @Override
            public void update(Bullet b) {
                float t = b.time;

                if (t < CHARGE_END) {
                    

                    
                    if (Mathf.chanceDelta(0.12f)) {
                        Lightning.create(b, JBColor.thurmixRed,
                                lightningDamage * 0.4f,
                                b.x + Mathf.range(120f),
                                b.y + Mathf.range(120f),
                                Mathf.random(360f), 6);
                    }

                    
                    Effect.shake(3f * (t / CHARGE_END), 3f, b);

                } else if (t < BURST_END) {
                    
                    super.update(b);

                    Effect.shake(12f, 12f, b);

                    
                    if (Mathf.chanceDelta(0.35f)) {
                        for (int i = 0; i < 4; i++) {
                            Lightning.create(b, JBColor.thurmixFlare,
                                    lightningDamage * 1.5f,
                                    b.x, b.y,
                                    Mathf.random(360f),
                                    lightningLength * 2 + Mathf.random(35));
                        }
                    }

                } else {
                    
                    super.update(b);

                    
                    if (Mathf.chanceDelta(0.18f)) {
                        for (int i = 0; i < lightning; i++) {
                            Lightning.create(b, lightningColor,
                                    lightningDamage < 0 ? damage : lightningDamage,
                                    b.x, b.y,
                                    b.rotation() + Mathf.range(lightningCone / 2f) + lightningAngle,
                                    lightningLength + Mathf.random(lightningLengthRand));
                        }
                    }

                    
                    if (Mathf.chanceDelta(0.06f)) {
                        Lightning.create(b, JBColor.thurmixFlare,
                                lightningDamage * 2f,
                                b.x, b.y,
                                b.rotation() + Mathf.range(60f),
                                lightningLength * 2 + Mathf.random(35));
                    }

                    
                    if (Mathf.chanceDelta(0.45f)) {
                        float dist = Mathf.random(length * 0.15f, length * 0.88f);
                        float spx = b.x + Angles.trnsx(b.rotation(), dist);
                        float spy = b.y + Angles.trnsy(b.rotation(), dist);
                        float sAngle = b.rotation() + Mathf.range(75f);

                        new Effect(38f, 100f, e -> {
                            float travel = e.fin(Interp.pow2Out) * Mathf.randomSeed(e.id, 18f, 48f);
                            float tx = e.x + Angles.trnsx(e.rotation, travel);
                            float ty = e.y + Angles.trnsy(e.rotation, travel);

                            Draw.color(JBColor.thurmixRed, JBColor.thurmixCore, 0.4f + e.fout() * 0.3f);
                            Lines.stroke(e.fout() * 3f);
                            Lines.line(e.x, e.y, tx, ty);

                            Draw.color(JBColor.thurmixCore);
                            Fill.circle(tx, ty, e.fout() * 4.5f);
                            Drawf.light(tx, ty, e.fout() * 24f, JBColor.thurmixRed, 0.9f);
                        }).at(spx + Mathf.range(width * 1.5f),
                                spy + Mathf.range(width * 1.5f),
                                sAngle, JBColor.thurmixRed);
                    }
                }
            }

            @Override
            public void hit(Bullet b, float x, float y) {
                if (b.time < CHARGE_END)
                    return; 

                hitEffect.at(x, y, b.rotation(), hitColor);
                hitSound.at(x, y, hitSoundPitch, hitSoundVolume);
                Effect.shake(hitShake * 1.8f, hitShake * 1.8f, b);

                Lightning.create(b, lightningColor,
                        lightningDamage < 0 ? damage : lightningDamage,
                        x, y,
                        b.rotation() + Mathf.range(lightningCone / 2f) + lightningAngle,
                        lightningLength + Mathf.random(lightningLengthRand));

                for (int i = 0; i < 3; i++) {
                    Lightning.create(b, JBColor.thurmixFlare,
                            lightningDamage * 0.75f,
                            x, y,
                            b.rotation() + i * 120f + Mathf.range(30f),
                            lightningLength + Mathf.random(22));
                }

                new Effect(50f, 180f, e -> {
                    Draw.color(JBColor.thurmixRed, JBColor.thurmixFlare, e.fin() * 0.6f);

                    Lines.stroke(6f * e.fout());
                    Lines.circle(x, y, e.fin(Interp.pow2Out) * 65f);
                    Lines.stroke(3.5f * e.fout());
                    Lines.circle(x, y, e.fin(Interp.pow3Out) * 95f);

                    Draw.color(JBColor.thurmixFlare, JBColor.thurmixCore, e.fout() * 0.45f);
                    Lines.stroke(e.fout() * 2.8f);
                    Angles.randLenVectors(e.id, 16, e.finpow() * 80f, (px, py) -> {
                        Lines.lineAngle(x + px, y + py, Mathf.angle(px, py), e.fout() * 20f + 6f);
                        Fill.circle(x + px, y + py, e.fout() * 5f);
                    });

                    e.scaled(20f, s -> {
                        Draw.color(JBColor.thurmixCore);
                        Draw.alpha(s.fout(Interp.pow3Out));
                        Fill.circle(x, y, s.fout() * 28f);
                        Drawf.light(x, y, s.fout() * 130f, JBColor.thurmixCore, 1f);
                    });

                    Drawf.light(x, y, e.fout() * 140f, JBColor.thurmixRed, 0.88f);
                }).at(x, y);
            }
        };

        supernovaLaser = new BasicBulletType(16f, 2500f) {
            {
                width = 14f;
                height = 60f;
                lifetime = 35f;

                
                pierce = true;
                pierceBuilding = true;
                pierceCap = 10;

                backColor = JBColor.thurmixRed;
                frontColor = Color.white;
                lightColor = JBColor.thurmixRed;

                trailColor = JBColor.thurmixRed;
                trailWidth = 5f;
                trailLength = 25;

                hitEffect = JBFx.hitSparkLarge;
                despawnEffect = Fx.hitLaserColor;
                hitColor = JBColor.thurmixRed;
            }
        };

        
        supernovaCore = new BasicBulletType(0f, 0f) {
            {
                lifetime = 240f; 

                
                
                
                collides = false;
                collidesAir = false;
                collidesGround = false;
                absorbable = false;
                hittable = false;

                
                bulletInterval = 3f; 
                intervalBullets = 4; 
                intervalRandomSpread = 360f; 
                intervalBullet = supernovaLaser;

                
                splashDamage = 45000f; 
                splashDamageRadius = 400f; 

                hitEffect = Fx.none;
                
                despawnEffect = new MultiEffect(
                        JBFx.blast(JBColor.thurmixRed, 600f),
                        JBFx.crossBlast(JBColor.thurmixRed, 750f),
                        JBFx.lightningHitLarge,
                        new Effect(120f, e -> {
                            Draw.color(JBColor.thurmixRed, Color.white, e.fin());
                            Lines.stroke(25f * e.fout());
                            Lines.circle(e.x, e.y, 500f * e.fin());

                            Angles.randLenVectors(e.id, 80, 600f * e.fin(), (x, y) -> {
                                Fill.circle(e.x + x, e.y + y, 12f * e.fout());
                            });
                            Drawf.light(e.x, e.y, 1000f * e.fout(), JBColor.thurmixRed, 2f);
                        }));
                hitSound = JBSounds.blastHuge; 
            }

            
            @Override
            public void draw(Bullet b) {
                super.draw(b);
                
                float pulse = 1f + Mathf.absin(Time.time, 4f, 0.2f);

                
                Draw.color(JBColor.thurmixRed, Color.white, Mathf.absin(Time.time, 8f, 0.5f));
                Fill.circle(b.x, b.y, 55f * pulse);

                
                Draw.color(JBColor.thurmixRed);
                Lines.stroke(5f);
                Lines.circle(b.x, b.y, 85f * pulse);

                Drawf.light(b.x, b.y, 400f * pulse, JBColor.thurmixRed, 1.5f);
            }
        };

        
        supernovaArtillery = new ArtilleryBulletType(7f, 2000f) {
            {
                lifetime = 220f; 
                width = 40f;
                height = 40f;
                sprite = "large-bomb";

                backColor = JBColor.thurmixRed;
                frontColor = Color.white;

                trailLength = 60;
                trailWidth = 15f;
                trailColor = JBColor.thurmixRed;
                trailEffect = JBFx.hitSparkLarge;
                trailInterval = 4f;

                splashDamage = 8000f; 
                splashDamageRadius = 120f;

                
                fragBullets = 1;
                fragBullet = supernovaCore;
                fragVelocityMin = 0f;
                fragVelocityMax = 0f;

                hitEffect = JBFx.crossBlast(JBColor.thurmixRed, 250f);
                despawnEffect = Fx.none;
                hitSound = JBSounds.blastShockwave;
            }
        };

        repeater = new BasicBulletType(14f, 1100f) {
            { 
                width = 18f;
                height = 45f;
                lifetime = 70f;

                backColor = JBColor.thurmixRed;
                frontColor = Color.white;
                lightColor = JBColor.thurmixRed;
                lightRadius = 60f;

                
                trailColor = JBColor.thurmixRed;
                trailWidth = 6f;
                trailLength = 20;

                
                pierce = true;
                pierceCap = 3;

                
                homingPower = 0.09f;
                homingRange = 250f;

                
                lightning = 3;
                lightningLength = 15;
                lightningLengthRand = 10;
                lightningDamage = 600f;
                lightningColor = JBColor.thurmixRed;

                
                hitEffect = JBFx.hitSparkLarge;
                despawnEffect = Fx.hitBulletColor; 

                
                incendChance = 0.2f;
                incendAmount = 2;
            }
        };

        gammaReaper = new BasicBulletType(4f, 14000, "missile-large") {
            
            final BulletType[] fragRef = {null};

            final BulletType damageField = new BasicBulletType(0f, 1) {
                {
                    lifetime = 480f;
                    despawnEffect = hitEffect = smokeEffect = shootEffect = Fx.none;
                    hittable = false; absorbable = false; collides = false; drag = 0f;
                }

                @Override
                public void update(Bullet b) {
                    Color lc = JBColor.thurmixRed;

                    
                    if (b.time < 90f) {
                        if (b.timer.get(0, 7f)) {
                            float r = 80f + (b.time / 90f) * 320f;
                            Damage.damage(b.team, b.x, b.y, r, 3500f);
                            Damage.status(b.team, b.x, b.y, r, StatusEffects.burning, 90f, true, true);

                            if (!Vars.headless) {
                                
                                JBFx.hitSparkHuge.at(b.x, b.y, lc);
                                JBFx.lightningHitLarge(lc).at(b.x, b.y, r, lc);
                                
                                JBFx.circleSplash(lc, 60f, 8, r * 0.6f, 6f).at(b.x, b.y, 0f, lc);
                            }
                        }
                        
                        if (!Vars.headless && b.timer.get(1, 30f)) {
                            PosLightning.createRandomRange(b.team, b, 200f, lc, false, 0f, 0, PosLightning.WIDTH, 1, 1, PosLightning.none);
                        }

                        
                    } else if (b.time < 300f) {
                        if (b.timer.get(0, 5f)) {
                            float progress = (b.time - 90f) / 210f;
                            float r = 300f + progress * 700f;
                            Damage.damage(b.team, b.x, b.y, r, 8000f);
                            Damage.status(b.team, b.x, b.y, r, StatusEffects.melting, 200f, true, true);
                            Units.nearbyEnemies(b.team, b.x, b.y, r * 0.5f, u -> {
                                float dx = b.x - u.x, dy = b.y - u.y;
                                float dst = Mathf.dst(dx, dy) + 0.1f;
                                u.vel.add(dx / dst * 2f, dy / dst * 2f);
                            });

                            if (!Vars.headless) {
                                
                                JBFx.hitSparkHuge.at(
                                        b.x + Mathf.range(r * 0.5f),
                                        b.y + Mathf.range(r * 0.5f),
                                        lc
                                );
                                
                                if (b.timer.get(2, 15f)) {
                                    float bx = b.x + Mathf.range(r * 0.4f);
                                    float by = b.y + Mathf.range(r * 0.4f);
                                    JBFx.blast(lc, 60f + progress * 80f).at(bx, by, 0f, lc);
                                    JBFx.lightningHitLarge(lc).at(bx, by, 0f, lc);
                                }
                                
                                if (b.timer.get(3, 20f)) {
                                    JBFx.circleOut(lc, r).at(b.x, b.y, 0f, lc);
                                }
                            }
                        }
                        
                        if (!Vars.headless && b.timer.get(1, 35f)) {
                            PosLightning.createRandomRange(b.team, b, 500f, lc, false, 0f, 0, PosLightning.WIDTH, 2, 2, PosLightning.none);
                        }

                        
                    } else if (Math.abs(b.time - 300f) < 3f) {
                        Damage.damage(b.team, b.x, b.y, 1400f, 120000f);
                        Damage.status(b.team, b.x, b.y, 1400f, StatusEffects.melting, 1500f, true, true);
                        Damage.status(b.team, b.x, b.y, 900f, StatusEffects.slow, 900f, true, true);

                        if (!Vars.headless) {
                            
                            for (int i = 0; i < 6; i++) {
                                Vec2 t = new Vec2().trns(i * 60f, Mathf.random(600f, 1400f)).add(b.x, b.y);
                                PosLightning.createEffect(b, t, lc, 3, PosLightning.WIDTH * 1.8f);
                            }
                            
                            JBFx.blast(lc, 400f).at(b.x, b.y, 0f, lc);
                            
                            JBFx.hitSpark(lc, 120f, 80, 500f, 3f, 20f).at(b.x, b.y, 0f, lc);
                            
                            JBFx.sharpBlast(lc, JBColor.thurmixRedLight, 200f, 500f).at(b.x, b.y, 0f, lc);
                            
                            JBFx.square(lc, 200f, 20, 400f, 14f).at(b.x, b.y, 0f, lc);
                        }

                        
                        if (fragRef[0] != null) {
                            for (int i = 0; i < 36; i++) {
                                fragRef[0].create(b.owner, b.team, b.x, b.y, i * 10f);
                            }
                        }

                        JBSounds.hugeBlast.at(b.x, b.y);

                        
                    } else if (b.time > 300f) {
                        if (b.timer.get(0, 20f)) {
                            float progress = (b.time - 300f) / 180f;
                            Damage.damage(b.team, b.x, b.y, 900f * (1f - progress * 0.6f), 2500f);
                            if (!Vars.headless) {
                                JBFx.hitSparkHuge.at(
                                        b.x + Mathf.range(200f),
                                        b.y + Mathf.range(200f),
                                        lc
                                );
                            }
                        }
                    }
                }
            };

            {
                backColor = trailColor = lightColor = lightningColor = hitColor = JBColor.thurmixRed;
                frontColor = JBColor.thurmixRedLight;

                shrinkY = 0.2f; shrinkX = 0f;
                width = 28f; height = 72f;

                lifetime = 200f;
                scaleLife = true;
                collides = false;
                despawnHit = true;

                trailWidth = 10f;
                trailLength = 75;
                trailInterp = Interp.slope;
                trailInterval = 1f;
                trailEffect = JBFx.hugeTrail;
                trailParam = 10f;
                trailChance = 0.45f;

                lightning = 8;
                lightningLength = lightningLengthRand = 30;
                lightningDamage = 500f;

                splashDamage = 5000f;
                splashDamageRadius = 180f;
                scaledSplashDamage = true;
                despawnShake = hitShake = 28f;
                drawSize = 450f;

                
                shootEffect = new Effect(55f, 800f, e -> {
                    Draw.blend(Blending.additive);
                    Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, e.fin());
                    Fill.circle(e.x, e.y, e.fout() * 90f);
                    Draw.color(JBColor.thurmixRed);
                    for (int i = 0; i < 10; i++) {
                        Drawf.tri(e.x, e.y, 20f * e.fout(), 240f * e.fout(Interp.pow3In), e.rotation + i * 36f);
                    }
                    Lines.stroke(e.fout() * 7f);
                    Lines.circle(e.x, e.y, e.fin(Interp.circleOut) * 180f);
                    Drawf.light(e.x, e.y, 600f * e.fout(), JBColor.thurmixRed, 0.85f);
                    Draw.blend();
                });

                smokeEffect = JBFx.instShoot(JBColor.thurmixRed, JBColor.thurmixRedLight);

                
                hitEffect = new MultiEffect(
                        JBFx.hitSparkHuge,
                        JBFx.blast(JBColor.thurmixRed, splashDamageRadius),
                        JBFx.hitSpark(JBColor.thurmixRed, 180f, 100, splashDamageRadius * 2f, 3f, 16f),
                        JBFx.sharpBlast(JBColor.thurmixRed, JBColor.thurmixRedLight, 130f, splashDamageRadius * 1.6f),
                        JBFx.square(JBColor.thurmixRed, 180f, 22, splashDamageRadius + 100f, 12f),
                        JBFx.subEffect(150f, splashDamageRadius + 20f, 36, 36f, Interp.pow2Out, (id, x, y, rot, fin) -> {
                            float fout = 1f - fin;
                            Draw.color(JBColor.thurmixRed, JBColor.thurmixRedDark, fin);
                            for (int s : Mathf.signs) {
                                Drawf.tri(x, y, 13f * fout, 50f * Mathf.curve(fin, 0f, 0.12f), rot + s * 90f);
                            }
                            Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, fin);
                            Fill.circle(x, y, fout * 5f);
                        })
                );

                
                
                
                
                despawnEffect = new Effect(650f, 2800f, e -> {
                    Draw.blend(Blending.additive);
                    Rand rand = new Rand();
                    rand.setSeed(e.id);

                    
                    
                    
                    
                    if (e.time < 90f) {
                        float p = e.time / 90f;
                        float ip = 1f - p;

                        
                        
                        for (int i = 0; i < 36; i++) {
                            float angle = Mathf.randomSeed(e.id + i, 360f);
                            float startDst = Mathf.randomSeed(e.id + i * 2, 200f, 600f);
                            float dst = startDst * ip + 15f;
                            float sz = Mathf.randomSeed(e.id + i * 3, 4f, 14f) * (ip * 0.7f + 0.3f);
                            
                            if (i % 3 == 0) Draw.color(JBColor.thurmixRedLight);
                            else if (i % 3 == 1) Draw.color(JBColor.thurmixRed);
                            else Draw.color(JBColor.thurmixRedDark);
                            Fill.circle(e.x + Angles.trnsx(angle, dst), e.y + Angles.trnsy(angle, dst), sz);
                        }

                        
                        int points = 6;
                        Draw.color(JBColor.thurmixRed, JBColor.thurmixRedLight, p);
                        Lines.stroke(3f * p + 1f);
                        float starR = 40f + p * 80f;
                        float[] vx = new float[points * 2], vy = new float[points * 2];
                        for (int i = 0; i < points; i++) {
                            float a = i * (360f / points) - 90f;
                            vx[i * 2]     = e.x + Angles.trnsx(a, starR);
                            vy[i * 2]     = e.y + Angles.trnsy(a, starR);
                            vx[i * 2 + 1] = e.x + Angles.trnsx(a + 360f / points / 2f, starR * 0.45f);
                            vy[i * 2 + 1] = e.y + Angles.trnsy(a + 360f / points / 2f, starR * 0.45f);
                        }
                        for (int i = 0; i < points * 2; i++) {
                            int next = (i + 1) % (points * 2);
                            Lines.line(vx[i], vy[i], vx[next], vy[next]);
                        }

                        
                        Draw.color(JBColor.thurmixRedDark, JBColor.thurmixRed, p);
                        for (int i = 0; i < 12; i++) {
                            float a = i * 30f + e.time * 1.5f;
                            float len = p * Mathf.randomSeed(e.id + i + 50, 60f, 180f);
                            Lines.stroke(p * Mathf.randomSeed(e.id + i + 60, 1.5f, 4f));
                            Lines.lineAngle(e.x, e.y, a, len);
                        }

                        
                        Draw.color(JBColor.thurmixRedDark);
                        Fill.circle(e.x, e.y, p * 55f + Mathf.absin(e.time * 5f, 3f, 8f));
                        Draw.color(JBColor.thurmixRed);
                        Lines.stroke(2.5f * p);
                        Lines.circle(e.x, e.y, p * 55f + Mathf.absin(e.time * 5f, 3f, 8f) + 5f);

                        Drawf.light(e.x, e.y, p * 700f + 100f, JBColor.thurmixRed, 0.8f);

                        
                        
                        
                        
                    } else if (e.time < 420f) {
                        float p = (e.time - 90f) / 330f;
                        float ip = 1f - p;

                        
                        
                        float ballGrow = Mathf.curve(p, 0f, 0.25f);    
                        float ballFade = 1f - Mathf.curve(p, 0.6f, 1f); 
                        float ballR = ballGrow * ballFade * 320f + Mathf.absin(e.time * 2f, 5f, 15f) * ip;

                        if (ballR > 2f) {
                            
                            Draw.color(JBColor.thurmixRedDark, ip * 0.9f);
                            Fill.circle(e.x, e.y, ballR);
                            Draw.color(JBColor.thurmixRed, ip * 0.7f);
                            Fill.circle(e.x, e.y, ballR * 0.75f);
                            Draw.color(JBColor.thurmixRedLight, ip * 0.5f);
                            Fill.circle(e.x, e.y, ballR * 0.4f);

                            
                            Draw.color(JBColor.thurmixRed);
                            Lines.stroke(4f * ip * ballGrow);
                            Lines.circle(e.x, e.y, ballR + 5f + Mathf.absin(e.time * 3f, 4f, 8f));
                            Lines.stroke(2f * ip * ballGrow);
                            Lines.circle(e.x, e.y, ballR + 18f);
                        }

                        
                        
                        for (int arm = 0; arm < 4; arm++) {
                            float armBaseAngle = arm * 90f + p * 180f; 
                            float armLen = Mathf.curve(p, 0.05f, 0.8f) * (700f + arm * 30f);
                            float armFade = 1f - Mathf.curve(p, 0.65f, 1f);

                            if (armLen < 5f || armFade <= 0f) continue;

                            
                            int segments = 18;
                            for (int seg = 0; seg < segments; seg++) {
                                float t = (float) seg / segments;
                                float segAngle = armBaseAngle + t * 55f; 
                                float segDst = t * armLen;
                                float segW = (1f - t) * 22f * armFade * ip + 1f;
                                float sx = e.x + Angles.trnsx(segAngle, segDst);
                                float sy = e.y + Angles.trnsy(segAngle, segDst);

                                
                                if (t < 0.3f) Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, t / 0.3f);
                                else Draw.color(JBColor.thurmixRed, JBColor.thurmixRedDark, (t - 0.3f) / 0.7f);
                                Draw.alpha(armFade * (1f - t * 0.6f));

                                if (seg < segments - 1) {
                                    float t2 = (float)(seg + 1) / segments;
                                    float nextAngle = armBaseAngle + t2 * 55f;
                                    float nextDst = t2 * armLen;
                                    Lines.stroke(segW);
                                    Lines.line(sx, sy,
                                            e.x + Angles.trnsx(nextAngle, nextDst),
                                            e.y + Angles.trnsy(nextAngle, nextDst));
                                }

                                
                                if (seg % 3 == 0) {
                                    Draw.color(JBColor.thurmixRed, armFade * (1f - t));
                                    Fill.circle(sx, sy, segW * 0.6f);
                                }
                            }

                            
                            float tipX = e.x + Angles.trnsx(armBaseAngle + 55f, armLen);
                            float tipY = e.y + Angles.trnsy(armBaseAngle + 55f, armLen);
                            Draw.color(JBColor.thurmixRed, armFade);
                            Fill.circle(tipX, tipY, 8f * armFade * ip);
                            Drawf.light(tipX, tipY, 50f * armFade, JBColor.thurmixRed, 0.6f);
                        }

                        
                        
                        for (int ring = 0; ring < 3; ring++) {
                            float rStart = ring * 0.08f;
                            float rp = Mathf.clamp((p - rStart) / (0.7f - rStart));
                            if (rp <= 0f) continue;
                            float rFade = 1f - Mathf.clamp((p - 0.5f - ring * 0.08f) / 0.4f);
                            float ringR = rp * (600f + ring * 150f);
                            Draw.color(JBColor.thurmixRed, JBColor.thurmixRedDark, rp);
                            Lines.stroke((8f - ring * 2f) * rFade + 0.5f);
                            Lines.circle(e.x, e.y, ringR);

                            
                            int spikeCount = 20 - ring * 4;
                            Draw.color(ring == 0 ? JBColor.thurmixRedLight : JBColor.thurmixRed, rFade * 0.8f);
                            for (int i = 0; i < spikeCount; i++) {
                                float a = i * (360f / spikeCount) + rp * (20f + ring * 10f);
                                float bx = e.x + Angles.trnsx(a, ringR);
                                float by = e.y + Angles.trnsy(a, ringR);
                                Drawf.tri(bx, by, (5f - ring) * rFade, (25f - ring * 5f) * rFade, a);
                            }
                        }

                        
                        int blobCount = 55;
                        for (int i = 0; i < blobCount; i++) {
                            float angle = Mathf.randomSeed(e.id + i + 100, 360f);
                            float speed = Mathf.randomSeed(e.id + i + 200, 150f, 800f);
                            float blobP = Mathf.clamp((p - Mathf.randomSeed(e.id + i + 300, 0f, 0.2f)) / 0.8f);
                            if (blobP <= 0f) continue;
                            float blobFade = 1f - Mathf.clamp((blobP - 0.5f) / 0.5f);
                            float dst = blobP * speed;
                            float sz = Mathf.randomSeed(e.id + i + 400, 4f, 18f) * blobFade;
                            Draw.color(i % 2 == 0 ? JBColor.thurmixRed : JBColor.thurmixRedDark, blobFade * 0.9f);
                            Fill.circle(e.x + Angles.trnsx(angle, dst), e.y + Angles.trnsy(angle, dst), sz);
                            
                            Lines.stroke(sz * 0.3f * blobFade);
                            Draw.color(JBColor.thurmixRedDark, blobFade * 0.5f);
                            Lines.lineAngle(e.x + Angles.trnsx(angle, dst), e.y + Angles.trnsy(angle, dst),
                                    angle + 180f, blobP * speed * 0.2f);
                        }

                        
                        float pulseAlpha = ip * (0.5f + Mathf.absin(e.time * 4f, 3f, 0.4f));
                        Drawf.light(e.x, e.y, (300f + ballR * 2f) * ip, JBColor.thurmixRed, pulseAlpha);

                        
                        
                        
                    } else {
                        float p = (e.time - 420f) / 230f;
                        float ip = 1f - p;

                        
                        Draw.color(JBColor.thurmixRedDark, ip * 0.7f);
                        Angles.randLenVectors(e.id, (int)(25 * ip + 1), 150f + 700f * p, (x, y) ->
                                Fill.circle(e.x + x, e.y + y, rand.random(18f, 60f) * ip));

                        
                        Draw.color(JBColor.thurmixRed, ip * 0.55f);
                        Angles.randLenVectors(e.id + 9, (int)(18 * ip + 1), 80f + 400f * p, (x, y) ->
                                Fill.circle(e.x + x, e.y + y, rand.random(6f, 18f) * ip));

                        
                        for (int arm = 0; arm < 4; arm++) {
                            float armAngle = arm * 90f + 0.5f * 180f + p * 60f;
                            float armLen = (1f - p * 0.4f) * 700f;
                            Draw.color(JBColor.thurmixRedDark, ip * ip * 0.5f);
                            Lines.stroke(ip * ip * 5f);
                            
                            float t1 = 0.7f, t2 = 1f;
                            Lines.line(
                                    e.x + Angles.trnsx(armAngle + t1 * 55f, t1 * armLen),
                                    e.y + Angles.trnsy(armAngle + t1 * 55f, t1 * armLen),
                                    e.x + Angles.trnsx(armAngle + t2 * 55f, t2 * armLen),
                                    e.y + Angles.trnsy(armAngle + t2 * 55f, t2 * armLen)
                            );
                        }

                        Drawf.light(e.x, e.y, ip * 380f, JBColor.thurmixRed, 0.5f * ip);
                    }

                    Draw.blend();
                });

                hitSound = despawnSound = JBSounds.hugeBlast;

                
                fragBullets = 11;
                fragBullet = new BasicBulletType(7f, 6000, "missile-large") {{
                    backColor = trailColor = lightColor = lightningColor = hitColor = JBColor.thurmixRed;
                    frontColor = JBColor.thurmixRedLight;
                    trailEffect = JBFx.hugeTrail;
                    trailParam = 6f;
                    trailChance = 0.2f;
                    trailInterval = 3;

                    lifetime = 60f;
                    scaleLife = true;

                    trailWidth = 5f;
                    trailLength = 55;
                    trailInterp = Interp.slope;

                    lightning = 6;
                    lightningLength = lightningLengthRand = 22;
                    splashDamage = damage;
                    lightningDamage = damage / 15;
                    splashDamageRadius = 120;
                    scaledSplashDamage = true;
                    despawnHit = true;
                    collides = false;

                    shrinkY = shrinkX = 0.33f;
                    width = 17f;
                    height = 55f;

                    despawnShake = hitShake = 12f;

                    hitEffect = new MultiEffect(JBFx.square(hitColor, 200, 20, splashDamageRadius + 80, 10), JBFx.lightningHitLarge, JBFx.hitSpark(hitColor, 130, 85, splashDamageRadius * 1.5f, 2.2f, 10f), JBFx.subEffect(140, splashDamageRadius + 12, 33, 34f, Interp.pow2Out, ((i, x, y, rot, fin) -> {
                        float fout = Interp.pow2Out.apply(1 - fin);
                        for (int s : Mathf.signs) {
                            Drawf.tri(x, y, 12 * fout, 45 * Mathf.curve(fin, 0, 0.1f)  , rot + s * 90);
                        }
                    })));
                    despawnEffect = JBFx.circleOut(145f, splashDamageRadius + 15f, 3f);

                    shootEffect = EffectWrapper.wrap(JBFx.missileShoot, hitColor);
                    smokeEffect = JBFx.instShoot(hitColor, frontColor);

                    despawnSound = hitSound = Sounds.explosion;

                    fragBullets = 22;
                    fragBullet = new BasicBulletType(2f, 300, "circle-bolt") {{
                        width = height = 10f;
                        shrinkY = shrinkX = 0.7f;
                        backColor = trailColor = lightColor = lightningColor = hitColor = JBColor.thurmixRed;
                        frontColor = JBColor.thurmixRedLight;
                        trailEffect = Fx.missileTrail;
                        trailParam = 3.5f;
                        splashDamage = 80;
                        splashDamageRadius = 40;

                        lifetime = 18f;

                        lightning = 2;
                        lightningLength = lightningLengthRand = 4;
                        lightningDamage = 30;

                        hitSoundVolume /= 2.2f;
                        despawnShake = hitShake = 4f;
                        despawnSound = hitSound = Sounds.explosionDull;

                        trailWidth = 5f;
                        trailLength = 35;
                        trailInterp = Interp.slope;

                        despawnEffect = JBFx.blast(hitColor, 40f);
                        hitEffect = JBFx.hitSparkHuge;
                    }};

                    fragLifeMax = 5f;
                    fragLifeMin = 1.5f;
                    fragVelocityMax = 2f;
                    fragVelocityMin = 0.35f;
                }};

                fragLifeMax = 5f;
                fragLifeMin = 1.5f;
                fragVelocityMax = 2f;
                fragVelocityMin = 0.35f;

                
                fragRef[0] = fragBullet;
            }

            @Override
            public void update(Bullet b) {
                super.update(b);

                
                if (!Vars.headless) {
                    if (b.timer.get(0, 6f)) {
                        JBFx.hitSparkHuge.at(b.x, b.y, JBColor.thurmixRed);
                        Lightning.create(b.team, JBColor.thurmixRed, 20f, b.x, b.y, Mathf.random(360f), 14);
                    }
                }

                
                if (b.timer.get(1, 20f) && b.time > 10f) {
                    for (int i = 0; i < 3; i++) {
                        fragBullet.create(b.owner, b.team, b.x, b.y, b.rotation() + Mathf.range(40f) + i * 120f);
                    }
                }
            }

            @Override
            public void despawned(Bullet b) {
                super.despawned(b);
                despawnEffect.at(b.x, b.y, b.rotation(), hitColor);
                damageField.create(b.owner, b.team, b.x, b.y, 0f);
                
                for (int i = 0; i < 30; i++) {
                    fragBullet.create(b.owner, b.team, b.x, b.y, i * 12f);
                }
            }
        };
    }
}
