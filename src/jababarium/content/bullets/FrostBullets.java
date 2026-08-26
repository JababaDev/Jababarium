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

public class FrostBullets {

    public static void load() {
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

                        backColor = Color.valueOf("#2255aa");
                        frontColor = Color.valueOf("#e0f0ff");
                        lightColor = Color.valueOf("#6699ff");
                        lightRadius = 35f;

                        trailLength = 8;
                        trailWidth = 2.5f;
                        trailColor = Color.valueOf("#4488ff");

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

    }
}
