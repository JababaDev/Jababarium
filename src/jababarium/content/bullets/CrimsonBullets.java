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

public class CrimsonBullets {

    public static void load() {
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

    }
}
