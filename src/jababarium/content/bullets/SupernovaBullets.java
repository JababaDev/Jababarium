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

public class SupernovaBullets {

    public static void load() {
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
