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

public class EnergyBullets {

    public static void load() {
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

    }
}
