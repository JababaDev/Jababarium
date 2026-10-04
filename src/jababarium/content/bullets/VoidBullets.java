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

public class VoidBullets {

    public static void load() {
        voidPlasma = new BasicBulletType(11f, 1020) {
            {
                lifetime = 65f;

                width = 18f;
                height = 42f;
                sprite = "missile-large";

                shrinkY = 0f;
                shrinkX = 0f;

                backColor = Color.valueOf("4bc8ad");
                frontColor = Color.valueOf("e6fef8");
                lightColor = Color.valueOf("92ead7");
                lightOpacity = 1f;
                lightRadius = 110f;

                trailLength = 32;
                trailWidth = 8f;
                trailColor = Color.valueOf("5cd6bc");
                trailInterval = 0.8f;

                trailEffect = new Effect(60f, e -> {

                    Draw.color(Color.valueOf("4bc8ad"));
                    Draw.alpha(e.fout() * 0.9f);
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 4f, 3f)) * e.fout());

                    Draw.color(Color.valueOf("a6efde"));
                    circle(e.x, e.y, (8f + Mathf.absin(e.time, 4f, 2f)) * e.fout());

                    Draw.color(Color.white);
                    circle(e.x, e.y, (5f + Mathf.absin(e.time, 4f, 1.5f)) * e.fout());

                    Draw.color(Color.valueOf("92ead7"));
                    Draw.alpha(e.fout() * 0.85f);

                    for (int i = 0; i < 3; i++) {
                        float rot = e.time * (1.5f + i * 0.5f);
                        Lines.stroke(2.5f * e.fout());
                        Lines.arc(e.x, e.y, (10f + i * 3f) * e.fout(), 0.4f, rot * 60f);
                    }

                    Angles.randLenVectors(e.id, 6, (8f + e.fin() * 4f) * e.fout(), (x, y) -> {
                        Draw.color(Color.valueOf("a6efde"), Color.white, Mathf.random(0.3f, 0.8f));
                        circle(e.x + x, e.y + y, e.fout() * 2.5f);
                    });

                    for (int i = 0; i < 4; i++) {
                        float angle = e.fin() * 720f + i * 90f;
                        float rad = e.fout() * 14f;

                        Tmp.v1.trns(angle, rad);

                        Draw.color(Color.valueOf("e6fef8"));
                        Draw.alpha(e.fout() * 0.7f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 2f);
                    }
                });

                trailChance = 0.85f;

                hitEffect = new Effect(90f, 250f, e -> {

                    Draw.color(Color.white);
                    circle(e.x, e.y, (28f + Mathf.absin(e.time, 3f, 4f)) * e.fout());

                    Draw.color(Color.valueOf("4bc8ad"), Color.valueOf("e6fef8"), e.fin() * 0.6f);

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

                    Draw.color(Color.white, Color.valueOf("92ead7"), e.fin() + 0.3f);
                    Lines.stroke(3.5f * e.fout());

                    for (int i = 0; i < 12; i++) {
                        float angle = i * 30f + e.fin() * 45f;
                        float len = e.finpow() * (90f + Mathf.random(20f));

                        Tmp.v1.trns(angle, len);
                        Lines.lineAngle(e.x, e.y, angle, len * e.fout());
                    }

                    Draw.color(Color.valueOf("a6efde"), Color.white, e.fout() * 0.7f);
                    Lines.stroke(2.5f * e.fout());

                    Angles.randLenVectors(e.id, 24, 20f + 80f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, angle, e.fout() * (15f + Mathf.random(18f)));

                        Tmp.v1.set(x, y);
                        Draw.color(Color.white, Color.valueOf("e6fef8"), e.fin());
                        circle(e.x + x, e.y + y, e.fout() * 3.5f);
                    });

                    Draw.color(Color.valueOf("92ead7"), Color.white, e.fout() * 0.6f);
                    Lines.stroke(1.8f * e.fout());

                    Angles.randLenVectors(e.id + 1, 20, 12f + 65f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 25f);
                    });

                    Angles.randLenVectors(e.id + 2, 18, 8f + 55f * e.finpow(), (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("4bc8ad"), e.fin() * 0.8f);
                        circle(e.x + x, e.y + y, e.fout() * 7f);

                        Draw.color(Color.valueOf("92ead7"));
                        Draw.alpha(e.fout() * 0.5f);
                        circle(e.x + x, e.y + y, e.fout() * 10f);
                    });

                    Draw.color(Color.valueOf("4bc8ad"), Color.valueOf("a6efde"), e.fout());
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

                        Draw.color(Color.valueOf("92ead7"), Color.white, e.fout() * 0.6f);
                        Lines.stroke(3f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle + 90f, e.fout() * 25f);

                        Draw.color(Color.white);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 4f);
                    }

                    Draw.color(Color.valueOf("a6efde"));
                    Lines.stroke(2.5f * e.fout());
                    for (int i = 0; i < 4; i++) {
                        float rot = e.fin() * 360f * (i % 2 == 0 ? 1 : -1);
                        float radius = (40f + i * 20f) * e.fin();

                        Lines.arc(e.x, e.y, radius, 0.5f, rot);
                    }

                    Draw.color(Color.valueOf("29957d"));
                    Draw.alpha(e.fout() * 0.9f);
                    circle(e.x, e.y, (20f + Mathf.absin(e.time, 3f, 5f)) * e.fout());

                    Draw.color(Color.valueOf("4bc8ad"));
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 3f, 3f)) * e.fout());

                    Drawf.light(e.x, e.y, e.fout() * 180f, Color.valueOf("92ead7"), 0.95f);

                    Angles.randLenVectors(e.id + 3, 30, 30f + 90f * e.fin(), (x, y) -> {
                        float angle = Mathf.angle(x, y);

                        Draw.color(Color.valueOf("e6fef8"), Color.valueOf("4bc8ad"), Mathf.random());
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

                    Draw.color(Color.valueOf("29957d"));
                    Draw.alpha(e.fin() * 0.8f);
                    circle(e.x, e.y, e.fin() * 20f);

                    Draw.color(Color.valueOf("4bc8ad"), Color.white, e.fin() * 0.7f);

                    for (int i = 0; i < 4; i++) {
                        float rot = e.time * (2f + i * 0.8f) * (i % 2 == 0 ? 1 : -1);
                        Lines.stroke((3f - i * 0.5f) * e.fin());
                        Lines.arc(e.x, e.y, (12f + i * 6f) * e.fin(), 0.6f, rot * 60f);
                    }

                    Lines.stroke(e.fout() * 4f);
                    Angles.randLenVectors(e.id, 16, 45f * e.finpow(), (x, y) -> {
                        Draw.color(Color.valueOf("a6efde"), Color.white, e.fout() * 0.8f);
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 14f + 5f);
                    });

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 12f);

                    Draw.color(Color.valueOf("e6fef8"));
                    circle(e.x, e.y, e.fout() * 8f);

                    Draw.color(Color.valueOf("92ead7"));
                    Lines.stroke(4f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 30f);

                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin(Interp.pow2Out) * 40f);

                    for (int i = 0; i < 12; i++) {
                        float angle = i * 30f;
                        float distance = (1f - e.fin()) * 50f;

                        Tmp.v1.trns(angle, distance);

                        Draw.color(Color.valueOf("a6efde"), Color.white, e.fin() * 0.7f);
                        Draw.alpha(e.fin() * 0.9f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fin() * 4f);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 70f, Color.valueOf("92ead7"), 0.8f);
                });

                smokeEffect = new Effect(50f, e -> {
                    Draw.color(Color.valueOf("4bc8ad"));
                    Draw.alpha(e.fout() * 0.8f);

                    circle(e.x, e.y, e.fout() * 10f);

                    Draw.color(Color.valueOf("a6efde"));
                    Lines.stroke(2.5f * e.fout());
                    Lines.circle(e.x, e.y, e.fin() * 20f);

                    Angles.randLenVectors(e.id, 6, e.fin() * 15f, (x, y) -> {
                        Draw.color(Color.valueOf("e6fef8"), Color.valueOf("4bc8ad"), Mathf.random());
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

                backColor = Color.valueOf("4bc8ad");
                frontColor = Color.valueOf("d9fbf2");
                lightColor = Color.valueOf("92ead7");
                lightOpacity = 0.9f;
                lightRadius = 75f;

                trailLength = 22;
                trailWidth = 4.5f;
                trailColor = Color.valueOf("5cd6bc");
                trailInterval = 1.5f;

                trailEffect = new Effect(45f, e -> {

                    Draw.color(Color.valueOf("4bc8ad"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.fout() * 7f);

                    Draw.color(Color.valueOf("92ead7"));
                    circle(e.x, e.y, e.fout() * 4.5f);

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 2f);

                    Draw.color(Color.valueOf("5cd6bc"));
                    Draw.alpha(e.fout() * 0.6f);
                    Lines.stroke(1.8f * e.fout());
                    Lines.circle(e.x, e.y, e.fout() * 9f);

                    Angles.randLenVectors(e.id, 4, e.fin() * 6f, (x, y) -> {
                        Draw.color(Color.valueOf("d9fbf2"), Color.valueOf("4bc8ad"), Mathf.random());
                        circle(e.x + x, e.y + y, e.fout() * 1.8f);
                    });
                });

                trailChance = 0.7f;

                hitEffect = new Effect(60f, 150f, e -> {

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 18f);

                    Draw.color(Color.valueOf("4bc8ad"), Color.valueOf("d9fbf2"), e.fin() * 0.6f);

                    e.scaled(25f, s -> {
                        Lines.stroke(5f * s.fout());
                        Lines.circle(e.x, e.y, 6f + s.fin(Interp.pow3Out) * 60f);
                    });

                    e.scaled(40f, s -> {
                        Lines.stroke(3f * s.fout());
                        Lines.circle(e.x, e.y, 10f + s.fin(Interp.pow2Out) * 80f);
                    });

                    Draw.color(Color.valueOf("92ead7"), Color.white, e.fout() * 0.7f);
                    Lines.stroke(2.5f * e.fout());

                    Angles.randLenVectors(e.id, 18, 15f + 60f * e.finpow(), (x, y) -> {
                        float angle = Mathf.angle(x, y);
                        Lines.lineAngle(e.x + x, e.y + y, angle, e.fout() * (12f + Mathf.random(10f)));
                    });

                    Angles.randLenVectors(e.id + 1, 14, 8f + 45f * e.finpow(), (x, y) -> {
                        Draw.color(Color.white, Color.valueOf("4bc8ad"), e.fin() * 0.8f);
                        circle(e.x + x, e.y + y, e.fout() * 5f);
                    });

                    for (int i = 0; i < 6; i++) {
                        float angle = i * 60f + e.fin() * 30f;
                        float dst = e.finpow() * 65f;

                        Tmp.v1.trns(angle, dst);

                        Draw.color(Color.valueOf("92ead7"), Color.white, e.fout() * 0.6f);
                        Lines.stroke(2.2f * e.fout());
                        Lines.lineAngle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle + 90f, e.fout() * 16f);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 100f, Color.valueOf("92ead7"), 0.85f);
                });

                despawnHit = true;

                shootEffect = new Effect(30f, e -> {
                    Draw.color(Color.white, Color.valueOf("4bc8ad"), e.fin() * 0.7f);

                    Lines.stroke(e.fout() * 3.5f);
                    Angles.randLenVectors(e.id, 12, 35f * e.finpow(), (x, y) -> {
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 10f + 4f);
                    });

                    circle(e.x, e.y, e.fout() * 9f);

                    Draw.color(Color.valueOf("92ead7"));
                    Lines.stroke(3f * e.fout());
                    Lines.circle(e.x, e.y, e.finpow() * 22f);

                    Drawf.light(e.x, e.y, e.fout() * 50f, Color.valueOf("92ead7"), 0.7f);
                });

                smokeEffect = new Effect(40f, e -> {
                    Draw.color(Color.valueOf("4bc8ad"));
                    Draw.alpha(e.fout() * 0.7f);
                    circle(e.x, e.y, e.fout() * 6f);

                    Draw.color(Color.valueOf("5cd6bc"));
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

                backColor = Color.valueOf("114439");
                frontColor = Color.valueOf("d9fbf2");
                lightColor = JBColor.nemesisLight;
                lightOpacity = 1f;
                lightRadius = 130f;

                trailLength = 25;
                trailWidth = 7f;
                trailColor = Color.valueOf("1d715f");
                trailInterval = 1.8f;

                trailEffect = new Effect(65f, e -> {
                    Draw.color(Color.valueOf("114439"));
                    Draw.alpha(e.fout() * 0.9f);
                    circle(e.x, e.y, (12f + Mathf.absin(e.time, 2.5f, 3f)) * e.fout());

                    Draw.color(JBColor.nemesisLight);
                    circle(e.x, e.y, (8f + Mathf.absin(e.time, 2.5f, 2f)) * e.fout());

                    Draw.color(Color.white);
                    circle(e.x, e.y, (4f + Mathf.absin(e.time, 2.5f, 1f)) * e.fout());

                    Draw.color(Color.valueOf("8de9d5"));
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

                    Draw.color(Color.valueOf("081d19"));
                    circle(e.x, e.y, (55f + Mathf.absin(e.time, 1.8f, 12f)) * e.fout());

                    Draw.color(Color.valueOf("114439"));
                    circle(e.x, e.y, (42f + Mathf.absin(e.time, 1.8f, 9f)) * e.fout());

                    Draw.color(JBColor.nemesisLight);
                    circle(e.x, e.y, (28f + Mathf.absin(e.time, 1.8f, 6f)) * e.fout());

                    Draw.color(Color.white);
                    circle(e.x, e.y, (14f + Mathf.absin(e.time, 1.8f, 3f)) * e.fout());

                    Draw.color(JBColor.nemesisLight, Color.valueOf("d9fbf2"), e.fin() * 0.5f);

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

                        Draw.color(Color.valueOf("d9fbf2"), Color.valueOf("114439"), e.fin() * 0.6f);
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

                        Draw.color(Color.valueOf("114439"));
                        Draw.alpha(e.fout() * 0.8f);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 14f);

                        Draw.color(JBColor.nemesisLight);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 9f);

                        Draw.color(Color.white);
                        circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 4f);

                        Draw.color(Color.valueOf("8de9d5"));
                        Draw.alpha(e.fout() * 0.6f);
                        Lines.stroke(3f * e.fout());
                        Lines.line(e.x, e.y, e.x + Tmp.v1.x, e.y + Tmp.v1.y);
                    }

                    Drawf.light(e.x, e.y, e.fout() * 260f, JBColor.nemesisLight, 1f);
                });

                despawnHit = true;

                shootEffect = new Effect(55f, e -> {
                    Draw.color(Color.valueOf("081d19"));
                    Draw.alpha(e.fin() * 0.95f);
                    circle(e.x, e.y, e.fin() * 35f);

                    Draw.color(Color.valueOf("114439"), Color.white, e.fin() * 0.4f);

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
                        Draw.color(Color.valueOf("d9fbf2"), JBColor.nemesisLight, finVal);
                        Draw.alpha(finVal * 0.95f);
                        circle(xVal + x * (1f - finVal), yVal + y * (1f - finVal), finVal * 5f);
                    });

                    Draw.color(Color.white);
                    circle(e.x, e.y, e.fout() * 20f);

                    Drawf.light(e.x, e.y, e.fout() * 100f, JBColor.nemesisLight, 0.9f);
                });

                smokeEffect = new Effect(70f, e -> {
                    Draw.color(Color.valueOf("114439"));
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

        voidLanceBullet = new BasicBulletType() {
            {
                
                damage = 2750f;
                speed = 13f;
                lifetime = 80f;
                width = 18f;
                height = 72f;

                
                frontColor = Color.white;
                backColor = Color.valueOf("859cef");
                hitColor = Color.valueOf("859cef");

                
                trailLength = 40;
                trailWidth = 7f;
                trailColor = Color.valueOf("859cef");
                trailInterval = 1f;
                trailRotation = false;

                
                lightColor = Color.valueOf("859cef");
                lightRadius = 110f;
                lightOpacity = 0.9f;

                
                splashDamage = 380f;
                splashDamageRadius = 95f;

                
                lightning = 5;
                lightningDamage = 90f;
                lightningLength = 12;
                lightningLengthRand = 20;
                lightningColor = Color.valueOf("859cef");

                
                status = StatusEffects.electrified;
                statusDuration = 60f * 8f;

                hitShake = 8f;
                hitSound = Sounds.explosionArtilleryShockBig;

                
                shootEffect = new Effect(45f, e -> {
                    
                    color(Color.valueOf("859cef"), Color.white, e.fout() * 0.4f);
                    stroke(e.fout() * 4f);
                    Lines.circle(e.x, e.y, e.finpow() * 55f);

                    
                    color(Color.valueOf("859cef"));
                    stroke(e.fout() * 2.5f);
                    Lines.circle(e.x, e.y, e.finpow() * 35f);

                    
                    randLenVectors(e.id, 14, e.finpow() * 45f, e.rotation, 18f, (x, y) -> {
                        color(Color.valueOf("859cef"), Color.white, e.fout() * 0.5f);
                        stroke(e.fout() * 2.2f);
                        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 14f + 4f);
                    });

                    
                    color(Color.valueOf("859cef"));
                    stroke(e.fout() * 3f);
                    for (int s : Mathf.signs) {
                        float bx = e.x + Angles.trnsx(e.rotation + 90f * s, 22f * e.finpow());
                        float by = e.y + Angles.trnsy(e.rotation + 90f * s, 22f * e.finpow());
                        Lines.line(e.x, e.y, bx, by);
                    }
                });

                
                smokeEffect = new Effect(30f, e -> {
                    color(Color.valueOf("859cef"), Color.white, e.fin() * 0.3f);
                    Draw.alpha(e.fout() * 0.6f);
                    Angles.randLenVectors(e.id, 8, 5f + 28f * e.finpow(), (x, y) -> {
                        circle(e.x + x, e.y + y, e.fout() * 5.5f);
                    });
                    Draw.reset();
                });

                
                trailEffect = new Effect(35f, e -> {
                    rand.setSeed(e.id);

                    
                    Draw.color(Color.valueOf("859cef"), Color.white, rand.random(0.4f));
                    Draw.alpha(e.fout() * 0.75f);

                    float ang = e.rotation + rand.range(55f);
                    float len = rand.random(12f, 28f) * e.fout(Interp.pow2Out);
                    DrawFunc.tri(
                            e.x + rand.range(5f),
                            e.y + rand.range(5f),
                            2.8f * e.fout(),
                            len,
                            ang);

                    
                    Draw.color(Color.valueOf("859cef"));
                    Draw.alpha(e.fout() * 0.5f);
                    circle(
                            e.x + rand.range(8f),
                            e.y + rand.range(8f),
                            rand.random(1.5f, 3.5f) * e.fout());

                    Drawf.light(e.x, e.y, e.fout() * 35f, Color.valueOf("859cef"), 0.5f);
                    Draw.reset();
                });

                
                hitEffect = new OptionalMultiEffect(
                        JBFx.blast(Color.valueOf("859cef"), 95f),
                        JBFx.sharpBlast(Color.valueOf("859cef"), Color.white, 55f, 100f),
                        JBFx.hitSpark(Color.valueOf("859cef"), 70f, 24, 95f, 3f, 16f));

                despawnEffect = new OptionalMultiEffect(
                        JBFx.sharpBlast(Color.valueOf("859cef"), Color.white, 40f, 75f),
                        JBFx.crossBlast(Color.valueOf("859cef"), 90f, 0),
                        JBFx.crossBlast(Color.valueOf("859cef"), 70f, 45));
            }

            @Override
            public void draw(Bullet b) {
                super.draw(b);

                float pulse = Mathf.absin(b.time, 3f, 1f);
                float rot = b.time * 5f;
                float progress = b.fin();

                
                Lines.stroke(2.2f + pulse * 0.4f);

                Draw.color(Color.valueOf("859cef"), Color.white, 0.2f);
                Draw.alpha(0.5f + pulse * 0.12f);
                for (int i = 0; i < 3; i++) {
                    Lines.arc(b.x, b.y, width * 2f + pulse, 0.22f, rot + i * 120f);
                }

                Draw.color(Color.valueOf("859cef"));
                Draw.alpha(0.38f + pulse * 0.1f);
                for (int i = 0; i < 3; i++) {
                    Lines.arc(b.x, b.y, width * 1.45f + pulse, 0.18f, -rot * 1.4f + i * 120f);
                }

                
                Draw.color(Color.valueOf("859cef"), Color.white, 0.35f);
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

                
                
                Draw.color(Color.valueOf("859cef"));
                Draw.alpha(0.45f + pulse * 0.2f);
                circle(b.x, b.y, width * 1.05f + pulse * 2f);

                
                Draw.color(Color.valueOf("859cef"), Color.white, 0.45f);
                Draw.alpha(0.7f + pulse * 0.15f);
                circle(b.x, b.y, width * 0.6f + pulse);

                
                Draw.color(Color.white);
                Draw.alpha(0.95f);
                circle(b.x, b.y, width * 0.28f + pulse * 0.5f);

                
                Drawf.light(b.x, b.y, lightRadius * (0.85f + pulse * 0.08f), Color.valueOf("859cef"), lightOpacity);

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

    }
}
