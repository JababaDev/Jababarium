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

public class BossBullets {

    public static void load() {
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

    }
}
