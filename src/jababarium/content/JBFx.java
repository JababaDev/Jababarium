package jababarium.content;

import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.IntMap;
import arc.util.Time;
import arc.math.geom.Position;
import arc.util.Tmp;
import jababarium.util.graphic.DrawFunc;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.graphics.Trail;

import static mindustry.Vars.state;
import static mindustry.Vars.tilesize;
import static arc.math.Angles.randLenVectors;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import java.util.Arrays;

public class JBFx {
    public static final float EFFECT_MASK = Layer.effect + 0.0001f;
    public static final float EFFECT_BOTTOM = Layer.bullet - 0.11f;
    public static final IntMap<Effect> same = new IntMap<>();
    public static final float lightningAlign = 0.5f;
    private static final Rand rand = new Rand();
    private static final Rand rand2 = new Rand();

    public static Effect trailFadeFast = new Effect(600f, e -> {
        if (!(e.data instanceof Trail))
            return;
        Trail trail = e.data();

        e.lifetime = trail.length * 1.4f;

        if (!state.isPaused()) {
            trail.shorten();
            trail.shorten();
        }
        trail.drawCap(e.color, e.rotation * e.foutpow());
        trail.draw(e.color, e.rotation * e.foutpow());
    }),

            boolSelector = new Effect(0, 0, e -> {
            }),

            attackWarningPos = new Effect(120f, 2000f, e -> {
                if (!(e.data instanceof Position))
                    return;

                e.lifetime = e.rotation;
                Position pos = e.data();

                Draw.color(e.color);
                TextureRegion arrowRegion = JBContent.arrowRegion;
                float scl = Mathf.curve(e.fout(), 0f, 0.1f);
                Lines.stroke(2 * scl);
                Lines.line(pos.getX(), pos.getY(), e.x, e.y);
                Fill.circle(pos.getX(), pos.getY(), Lines.getStroke());
                Fill.circle(e.x, e.y, Lines.getStroke());
                Tmp.v1.set(e.x, e.y).sub(pos).scl(e.fin(Interp.pow2In)).add(pos);
                Draw.rect(arrowRegion, Tmp.v1.x, Tmp.v1.y, arrowRegion.width * scl * Draw.scl,
                        arrowRegion.height * scl * Draw.scl, pos.angleTo(e.x, e.y) - 90f);
            }),

            circleOut = new Effect(60f, 500f, e -> {
                Lines.stroke(2.5f * e.fout(), e.color);
                Lines.circle(e.x, e.y, e.rotation * e.fin(Interp.pow3Out));
            }),

            trailToGray = new Effect(50.0F, e -> {
                Draw.color(e.color, Color.gray, e.fin());
                randLenVectors(e.id, 2, tilesize * e.fin(), (x, y) -> Fill.circle(e.x + x, e.y + y, e.rotation * e.fout()));
            }),

            hitSparkLarge = new Effect(40, e -> {
                color(e.color, Color.white, e.fout() * 0.3f);
                stroke(e.fout() * 1.6f);

                rand.setSeed(e.id);
                randLenVectors(e.id, 18, e.finpow() * 27f, (x, y) -> {
                    float ang = Mathf.angle(x, y);
                    lineAngle(e.x + x, e.y + y, ang, e.fout() * rand.random(4, 8) + 2f);
                });
            }),

            lightningSpark = new Effect(Fx.chainLightning.lifetime, e -> {
                color(Color.white, e.color, e.fin() + 0.25f);

                stroke(0.65f + e.fout());

                randLenVectors(e.id, 3, e.fin() * e.rotation + 6f,
                        (x, y) -> lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 4 + 2f));

                Fill.circle(e.x, e.y, 2.5f * e.fout());
            }),

            lightningHitLarge = new Effect(50f, 180f, e -> {
                color(e.color);
                Drawf.light(e.x, e.y, e.fout() * 90f, e.color, 0.7f);
                e.scaled(25f, t -> {
                    stroke(3f * t.fout());
                    circle(e.x, e.y, 3f + t.fin(Interp.pow3Out) * 80f);
                });
                Fill.circle(e.x, e.y, e.fout() * 8f);
                randLenVectors(e.id + 1, 4, 1f + 60f * e.finpow(),
                        (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 5f));

                color(Color.gray);
                Angles.randLenVectors(e.id, 8, 2.0F + 30.0F * e.finpow(),
                        (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 4.0F + 0.5F));
            }),

            hitSparkHuge = new Effect(70, e -> {
                color(e.color, Color.white, e.fout() * 0.3f);
                stroke(e.fout() * 1.6f);

                rand.setSeed(e.id);
                randLenVectors(e.id, 26, e.finpow() * 65f, (x, y) -> {
                    float ang = Mathf.angle(x, y);
                    lineAngle(e.x + x, e.y + y, ang, e.fout() * rand.random(6, 9) + 3f);
                });
            }),

            trailFromWhite = new Effect(50.0F, e -> {
                Draw.color(e.color, Color.white, e.fout() * 0.35f);
                randLenVectors(e.id, 2, tilesize * e.fin(), (x, y) -> Fill.circle(e.x + x, e.y + y, e.rotation * e.fout()));
            }),

            lightningHitSmall = new Effect(Fx.chainLightning.lifetime, e -> {
                color(Color.white, e.color, e.fin() + 0.25f);

                e.scaled(7f, s -> {
                    stroke(0.5f + s.fout());
                    Lines.circle(e.x, e.y, s.fin() * (e.rotation + 12f));
                });

                stroke(0.75f + e.fout());

                randLenVectors(e.id, 6, e.fin() * e.rotation + 7f,
                        (x, y) -> lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 4 + 2f));

                Fill.circle(e.x, e.y, 2.5f * e.fout());
            }),

            square45_4_45 = new Effect(45f, e -> {
                Draw.color(e.color);
                randLenVectors(e.id, 5, 20f * e.finpow(), (x, y) -> {
                    Fill.square(e.x + x, e.y + y, 4f * e.fout(), 45);
                    Drawf.light(e.x + x, e.y + y, e.fout() * 6f, e.color, 0.7f);
                });
            }),

            hugeSmokeGray = new Effect(40f, e -> {
                Draw.color(Color.gray, Color.darkGray, e.fin());
                Angles.randLenVectors(e.id, 6, 2.0F + 19.0F * e.finpow(),
                        (x, y) -> Fill.circle(e.x + x / 2.0F, e.y + y / 2.0F, e.fout() * 2f));
                e.scaled(25f, i -> Angles.randLenVectors(e.id, 6, 2.0F + 19.0F * i.finpow(),
                        (x, y) -> Fill.circle(e.x + x, e.y + y, i.fout() * 4.0F)));
            }),

            hugeSmoke = new Effect(40f, e -> {
                Draw.color(e.color);
                Angles.randLenVectors(e.id, 6, 2.0F + 19.0F * e.finpow(), (x, y) -> Fill.circle(e.x + x / 2.0F, e.y + y / 2.0F, e.fout() * 2f));
                e.scaled(25f, i -> Angles.randLenVectors(e.id, 6, 2.0F + 19.0F * i.finpow(), (x, y) -> Fill.circle(e.x + x, e.y + y, i.fout() * 4.0F)));
            }),

            largeDarkEnergyHit = new Effect(50, e -> {
                color(JBColor.darkEnrColor);
                Fill.circle(e.x, e.y, e.fout() * 44);
                stroke(e.fout() * 3.2f);
                circle(e.x, e.y, e.fin() * 80);
                stroke(e.fout() * 2.5f);
                circle(e.x, e.y, e.fin() * 50);
                stroke(e.fout() * 3.2f);
                randLenVectors(e.id, 30, 18 + 80 * e.fin(), (x, y) -> {
                    lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 14 + 5);
                });

                Draw.z(EFFECT_MASK);
                color(JBColor.darkEnr);
                Fill.circle(e.x, e.y, e.fout() * 30);
                Drawf.light(e.x, e.y, e.fout() * 80f, JBColor.darkEnrColor, 0.7f);

                Draw.z(EFFECT_BOTTOM);
                Fill.circle(e.x, e.y, e.fout() * 31);
                Draw.z(Layer.effect - 0.0001f);
            }).layer(Layer.effect - 0.0001f),

            mediumDarkEnergyHit = new Effect(23, e -> {
                color(JBColor.darkEnrColor);
                stroke(e.fout() * 2.8f);
                circle(e.x, e.y, e.fin() * 60);
                stroke(e.fout() * 2.12f);
                circle(e.x, e.y, e.fin() * 35);

                stroke(e.fout() * 2.25f);
                randLenVectors(e.id, 9, 7f + 60f * e.finpow(),
                        (x, y) -> lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 4f + e.fout() * 12f));

                Fill.circle(e.x, e.y, e.fout() * 22);
                color(JBColor.darkEnr);
                Fill.circle(e.x, e.y, e.fout() * 14);
                Drawf.light(e.x, e.y, e.fout() * 80f, JBColor.darkEnrColor, 0.7f);
            }),

            skyTrail = new Effect(22, e -> {
                color(JBColor.lightSkyBack, Pal.gray, e.fin() * 0.6f);

                rand.setSeed(e.id);
                randLenVectors(e.id, 3, e.finpow() * 13, e.rotation - 180, 30f, (x, y) -> {
                    Fill.poly(e.x + x, e.y + y, 6, rand.random(2, 4) * e.foutpow(), e.rotation);
                });
            }),

            square45_6_45 = new Effect(45f, e -> {
                Draw.color(e.color, Color.white, e.fout() * 0.6f);
                randLenVectors(e.id, 6, 27f * e.finpow(), (x, y) -> {
                    Fill.square(e.x + x, e.y + y, 5f * e.fout(), 45);
                    Drawf.light(e.x + x, e.y + y, e.fout() * 9F, e.color, 0.7f);
                });
            }),

            hugeTrail = new Effect(40f, e -> {
                Draw.color(e.color);
                Draw.alpha(e.fout(0.85f) * 0.85f);
                Angles.randLenVectors(e.id, 6, 2.0F + e.rotation * 5f * e.finpow(), (x, y) -> {
                    Fill.circle(e.x + x / 2.0F, e.y + y / 2.0F, e.fout(Interp.pow3Out) * e.rotation);
                });
            }),

            missileShoot = new Effect(130f, 300f, e -> {
                color(e.color);
                alpha(0.67f * e.fout(0.9f));
                rand.setSeed(e.id);

                for (int i = 0; i < 35; i++) {
                    Tmp.v1.trns(
                            e.rotation + 180f + rand.range(21f),
                            rand.random(e.finpow() * 90f)
                    ).add(
                            rand.range(3f),
                            rand.range(3f)
                    );

                    float vx = Tmp.v1.x;
                    float vy = Tmp.v1.y;

                    e.scaled(e.lifetime * rand.random(0.2f, 1f), b -> {
                        Fill.circle(
                                e.x + vx,
                                e.y + vy,
                                b.fout() * 9f + 0.3f
                        );
                    });
                }
            }),

            attackWarningRange = new Effect(120f, 2000f, e -> {
                Draw.color(e.color);
                Lines.stroke(2 * e.fout());
                Lines.circle(e.x, e.y, e.rotation);

                for (float i = 0.75f; i < 1.5f; i += 0.25f) {
                    Lines.spikes(e.x, e.y, e.rotation / i, e.rotation / 10f, 4, e.time);
                    Lines.spikes(e.x, e.y, e.rotation / i / 1.5f, e.rotation / 12f, 4, -e.time * 1.25f);
                }

                TextureRegion arrowRegion = JBContent.arrowRegion;
                float scl = Mathf.curve(e.fout(), 0f, 0.1f);

                for (int l = 0; l < 4; l++) {
                    float angle = 90 * l;
                    float regSize = e.rotation / 150f;
                    for (int i = 0; i < 4; i++) {
                        Tmp.v1.trns(angle, (i - 4) * tilesize * e.rotation / tilesize / 4);
                        float f = (100 - (Time.time - 25 * i) % 100) / 100;

                        Draw.rect(arrowRegion, e.x + Tmp.v1.x, e.y + Tmp.v1.y, arrowRegion.width * regSize * f * scl,
                                arrowRegion.height * regSize * f * scl, angle - 90);
                    }
                }
            });

    public static Effect square(Color color, float lifetime, int num, float range, float size) {
        return new Effect(lifetime, e -> {
            Draw.color(color);
            rand.setSeed(e.id);
            randLenVectors(e.id, num, range * e.finpow(), (x, y) -> {
                float s = e.fout(Interp.pow3In) * (size + rand.range(size / 3f));
                Fill.square(e.x + x, e.y + y, s, 45);
                Drawf.light(e.x + x, e.y + y, s * 2.25f, color, 0.7f);
            });
        });
    }

    public static int hash(String m, Color c) {
        return Arrays.hashCode(new int[] { m.hashCode(), c.hashCode() });
    }

    public static Effect get(String m, Color c, Effect effect) {
        int hash = hash(m, c);
        Effect or = same.get(hash);
        if (or == null)
            same.put(hash, effect);
        return or == null ? effect : or;
    }

    public static Effect crossBlast(Color color) {
        return get("crossBlast", color, crossBlast(color, 72));
    }

    public static Effect crossBlast(Color color, float size) {
        return crossBlast(color, size, 0);
    }

    public static Effect crossBlast(Color color, float size, float rotate) {
        return new Effect(Mathf.clamp(size / 3f, 35f, 240f), size * 2, e -> {
            color(color, Color.white, e.fout() * 0.55f);
            Drawf.light(e.x, e.y, e.fout() * size, color, 0.7f);

            e.scaled(10f, i -> {
                stroke(1.35f * i.fout());
                circle(e.x, e.y, size * 0.7f * i.finpow());
            });

            rand.setSeed(e.id);
            float sizeDiv = size / 1.5f;
            float randL = rand.random(sizeDiv);

            for (int i = 0; i < 4; i++) {
                DrawFunc.tri(e.x, e.y, size / 20 * (e.fout() * 3f + 1) / 4 * (e.fout(Interp.pow3In) + 0.5f) / 1.5f,
                        (sizeDiv + randL) * Mathf.curve(e.fin(), 0, 0.05f) * e.fout(Interp.pow3), i * 90 + rotate);
            }
        });
    }

    public static Effect blast(Color color, float range) {
        float lifetime = Mathf.clamp(range * 1.5f, 90f, 600f);
        return new Effect(lifetime, range * 2.5f, e -> {
            color(color);
            Drawf.light(e.x, e.y, e.fout() * range, color, 0.7f);

            e.scaled(lifetime / 3, t -> {
                stroke(3f * t.fout());
                circle(e.x, e.y, 8f + t.fin(Interp.circleOut) * range * 1.35f);
            });

            e.scaled(lifetime / 2, t -> {
                Fill.circle(t.x, t.y, t.fout() * 8f);
                Angles.randLenVectors(t.id + 1, (int) (range / 13), 2 + range * 0.75f * t.finpow(), (x, y) -> {
                    Fill.circle(t.x + x, t.y + y, t.fout(Interp.pow2Out) * Mathf.clamp(range / 15f, 3f, 14f));
                    Drawf.light(t.x + x, t.y + y, t.fout(Interp.pow2Out) * Mathf.clamp(range / 15f, 3f, 14f), color,
                            0.5f);
                });
            });

            Draw.z(Layer.bullet - 0.001f);
            color(Color.gray);
            alpha(0.85f);
            float intensity = Mathf.clamp(range / 10f, 5f, 25f);
            for (int i = 0; i < 4; i++) {
                rand.setSeed(((long) e.id << 1) + i);
                float lenScl = rand.random(0.4f, 1f);
                int fi = i;
                e.scaled(e.lifetime * lenScl, eIn -> {
                    randLenVectors(eIn.id + fi - 1, eIn.fin(Interp.pow10Out), (int) (intensity / 2.5f), 8f * intensity,
                            (x, y, in, out) -> {
                                float fout = eIn.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                                Fill.circle(eIn.x + x, eIn.y + y, fout * ((2f + intensity) * 1.8f));
                            });
                });
            }
        });
    }

    public static Effect circleOut(float lifetime, float radius, float thick) {
        return new Effect(lifetime, radius * 2f, e -> {
            Draw.color(e.color, Color.white, e.fout() * 0.7f);
            Lines.stroke(thick * e.fout());
            Lines.circle(e.x, e.y, radius * e.fin(Interp.pow3Out));
        });
    }

    public static Effect circleOut(Color color, float range) {
        return new Effect(Mathf.clamp(range / 2, 45f, 360f), range * 1.5f, e -> {
            rand.setSeed(e.id);

            Draw.color(Color.white, color, e.fin() + 0.6f);
            float circleRad = e.fin(Interp.circleOut) * range;
            Lines.stroke(Mathf.clamp(range / 24, 4, 20) * e.fout());
            Lines.circle(e.x, e.y, circleRad);
            
            
            {
                Tmp.v1.set(1, 0).setToRandomDirection(rand).scl(circleRad);
                DrawFunc.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, rand.random(circleRad / 16, circleRad / 12) * e.fout(),
                        rand.random(circleRad / 4, circleRad / 1.5f) * (1 + e.fin()) / 2, Tmp.v1.angle() - 180);
            }
        });
    }

    public static Effect polyTrail(Color fromColor, Color toColor, float size, float lifetime) {
        return new Effect(lifetime, size * 2, e -> {
            color(fromColor, toColor, e.fin());
            Fill.poly(e.x, e.y, 6, size * e.fout(), e.rotation);
            Drawf.light(e.x, e.y, e.fout() * size, fromColor, 0.7f);
        });
    }

    public static Effect squareRand(Color color, float sizeMin, float sizeMax) {
        return new Effect(20f, sizeMax * 2f, e -> {
            Draw.color(Color.white, color, e.fin() + 0.15f);
            if (e.id % 2 == 0) {
                Lines.stroke(1.5f * e.fout(Interp.pow3Out));
                Lines.square(e.x, e.y, Mathf.randomSeed(e.id, sizeMin, sizeMax) * e.fin(Interp.pow2Out) + 3, 45);
            } else {
                Fill.square(e.x, e.y, Mathf.randomSeed(e.id, sizeMin * 0.5f, sizeMin * 0.8f) * e.fout(Interp.pow2Out),
                        45);
            }
        });
    }

    public static Effect energyPulseCore = new Effect(80f, 200f, e -> {
        float scale = 0.35f;

        Color base = e.color;

        Drawf.light(
                e.x, e.y,
                e.fout() * 140f * scale,
                base,
                0.8f);

        Draw.color(base, Color.white, e.fin() * 0.35f);
        Fill.circle(
                e.x,
                e.y,
                (6f + Mathf.absin(Time.time, 6f, 2.5f)) * scale);

        e.scaled(30f, s -> {
            Draw.color(base);
            Lines.stroke(4f * s.fout() * scale);
            Lines.circle(
                    e.x,
                    e.y,
                    (18f + s.fin(Interp.pow3Out) * 90f) * scale);
        });

        e.scaled(45f, s -> {
            Draw.color(base, Color.white, 0.2f);
            Lines.stroke(2f * s.fout() * scale);
            Lines.circle(
                    e.x,
                    e.y,
                    (10f + s.fin(Interp.pow5Out) * 120f) * scale);
        });

        Fx.rand.setSeed(e.id);
        Angles.randLenVectors(
                e.id,
                12,
                (25f + 80f * e.finpow()) * scale,
                (x, y) -> {
                    float ang = Mathf.angle(x, y);
                    Draw.color(base, Color.white, e.fout());
                    Lines.stroke(1.5f * e.fout() * scale);
                    Lines.lineAngle(
                            e.x + x,
                            e.y + y,
                            ang,
                            (6f + Fx.rand.random(8f)) * e.fout() * scale);
                });

        Draw.color(base);
        Angles.randLenVectors(
                e.id + 42,
                6,
                (8f + 50f * e.fin()) * scale,
                (x, y) -> {
                    Fill.circle(
                            e.x + x,
                            e.y + y,
                            e.fout() * 3f * scale);
                });
        Draw.reset();
    });

    public static Effect neutronDissociateUpdate = new Effect(60f, e -> {
        float scale = 0.45f;

        Color base = Color.valueOf("66FF9A");

        float fout = e.fout();
        float shrink = fout * fout;

        Drawf.light(
                e.x,
                e.y,
                55f * scale * shrink,
                base,
                0.35f * fout);

        Draw.color(base, Color.white,
                (0.15f + Mathf.absin(Time.time, 8f, 0.1f)) * fout);
        Fill.circle(
                e.x,
                e.y,
                (3.5f + Mathf.absin(Time.time, 6f, 1.2f)) * scale * shrink);

        Draw.color(base, fout);
        Lines.stroke(1.2f * scale * fout);
        Lines.circle(
                e.x,
                e.y,
                (10f + Mathf.absin(Time.time, 10f, 2.5f)) * scale * shrink);

        Draw.color(base, Color.white, 0.1f * fout);
        Lines.stroke(0.6f * scale * fout);
        Lines.circle(
                e.x,
                e.y,
                (16f + Mathf.absin(Time.time + 12f, 12f, 3.5f)) * scale * shrink);

        Fx.rand.setSeed(e.id);
        Angles.randLenVectors(
                e.id,
                3,
                12f * scale * shrink,
                (x, y) -> {
                    Draw.color(base, Color.white, 0.3f * fout);
                    Fill.circle(
                            e.x + x,
                            e.y + y,
                            1.1f * scale * fout);
                });
        Draw.reset();
    });

    public static Effect sergiumMixerUpdate = new Effect(70f, e -> {
        Color base = Color.valueOf("6fa8ff");

        Drawf.light(
                e.x,
                e.y,
                45f + Mathf.absin(Time.time, 8f, 8f),
                base,
                0.6f);

        Draw.color(base, Color.white, 0.2f + 0.2f * e.fout());
        Fill.circle(
                e.x,
                e.y,
                4.5f + Mathf.absin(Time.time, 6f, 1.4f));

        e.scaled(35f, s -> {
            Draw.color(base, Color.white, 0.15f);
            Lines.stroke(1.8f * s.fout());
            Lines.circle(
                    e.x,
                    e.y,
                    10f + s.fin(Interp.smooth) * 22f);
        });

        Fx.rand.setSeed(e.id);
        Angles.randLenVectors(e.id, 5, 14f, (x, y) -> {
            float flicker = Mathf.absin(Time.time + x, 7f, 1f);
            Draw.color(base, Color.white, 0.3f);
            Fill.circle(
                    e.x + x,
                    e.y + y,
                    (1.1f + flicker) * e.fout());
        });
        Draw.reset();
    });

    public static Effect sergiumMixerCraft = new Effect(70f, e -> {

        float boomStart = 0.58f;
        float boomPeak = 0.75f;
        float boomEnd = 1.05f;
        Color base = Color.valueOf("6fa8ff");

        float t = e.fin();
        float boom = Mathf.clamp((t - boomStart) / (boomPeak - boomStart));
        float tail = Mathf.clamp((boomEnd - t) / (boomEnd - boomPeak));

        float intensity = boom * Interp.sineOut.apply(tail);

        if (intensity > 0f) {

            float shock = Interp.pow2Out.apply(boom);
            float fade = Interp.smooth.apply(intensity);

            Draw.color(Color.white, base, shock);
            Fill.circle(
                    e.x,
                    e.y,
                    8f + 14f * shock);

            Draw.color(base, Color.white, fade);
            Lines.stroke(3.2f * fade);
            Lines.circle(
                    e.x,
                    e.y,
                    18f + 38f * shock);

            Draw.color(base, Color.white, 0.25f * fade);
            Lines.stroke(2f * fade);
            Lines.circle(
                    e.x,
                    e.y,
                    10f + 26f * shock);

            Fx.rand.setSeed(e.id + 777L);
            Angles.randLenVectors(e.id + 777, 12, 26f * shock, (x, y) -> {
                float len = 6f + 10f * fade;
                Lines.stroke(1.4f * fade);
                Lines.lineAngle(
                        e.x + x,
                        e.y + y,
                        Mathf.angle(x, y),
                        len);
            });

            Drawf.light(
                    e.x,
                    e.y,
                    110f * fade,
                    base,
                    0.9f * fade);
        }
    });

    public static Effect etherDenseFog = new Effect(90f, e -> {

        Color fog = Color.white.cpy().lerp(Color.lightGray, 0.45f);

        float alpha = Mathf.pow(e.fin(), 0.7f) * Mathf.pow(e.fout(), 1.4f);

        Draw.color(fog, alpha);

        Drawf.light(
                e.x, e.y,
                34f * e.fin(),
                fog,
                0.35f * alpha);

        Fill.circle(
                e.x,
                e.y + e.fin() * 6f,
                12f + e.fin() * 18f);

        Angles.randLenVectors(
                e.id,
                14,
                6f + 26f * e.fin(),
                (x, y) -> {

                    float driftX = Mathf.sin((e.time + x) * 0.02f) * 2.2f;
                    float rise = e.fin() * 10f;

                    float size = 6f +
                            e.fin() * 14f +
                            Mathf.absin(e.time + x, 8f, 2.5f);

                    Fill.circle(
                            e.x + x + driftX,
                            e.y + y + rise,
                            size * e.fout());
                });

        Draw.reset();
    });

    public static Effect mercurySplitter = new Effect(90f, e -> {
        float x = e.x;
        float y = e.y;
        float fin = e.fin();
        float fout = e.fout();
        float time = Time.time;

        float appear = Mathf.curve(fin, 0f, 0.25f);
        float disappear = Mathf.curve(fout, 0f, 0.35f);
        float scl = appear * disappear;

        if (scl <= 0.001f)
            return;

        Color base = Color.valueOf("9ea3a6");
        Color light = Color.valueOf("cfd3d6");
        Color dark = Color.valueOf("6b6f72");

        Drawf.light(
                x, y,
                28f * scl,
                base,
                0.35f * scl);

        Draw.color(base, light, 0.3f + Mathf.absin(time, 8f, 0.15f));
        Fill.circle(
                x,
                y,
                (5.5f + Mathf.absin(time, 6f, 0.6f)) * scl);

        for (int i = 0; i < 3; i++) {
            float rot = time * (0.25f + i * 0.08f);

            Draw.color(dark, base, 0.4f * scl);
            Lines.stroke((1.2f - i * 0.2f) * scl);
            Lines.arc(
                    x,
                    y,
                    (9f + i * 3f) * scl,
                    0.35f,
                    rot * 30f);
        }

        for (int i = 0; i < 6; i++) {
            float ang = time * 0.6f + i * 60f;
            float rad = (12f + Mathf.absin(time + i * 4f, 10f, 2.2f)) * scl;

            float px = x + Angles.trnsx(ang, rad);
            float py = y + Angles.trnsy(ang, rad);

            Draw.color(base, light, 0.9f * scl);
            Fill.circle(px, py, 1.6f * scl);
        }

        Angles.randLenVectors(e.id, 8, 18f * scl, (dx, dy) -> {
            Draw.color(light, base, 0.7f * scl);
            Fill.circle(
                    x + dx,
                    y + dy,
                    1.3f * scl);
        });

        Draw.reset();
    });

    public static Effect cloudPuff(float smokeSize, Color color) {
        float smokeSizeLfMult = 12f;

        return new Effect(
                smokeSize * smokeSizeLfMult,
                smokeSize * smokeSizeLfMult * 2.85f,
                e -> {

                    Draw.color(color);

                    Angles.randLenVectors(
                            e.id,
                            (int) (6 * smokeSize),
                            12f * e.finpow() * smokeSize / 8f,
                            (x, y) -> {

                                Draw.alpha(0.45f * e.fout());
                                Fill.circle(
                                        e.x + x,
                                        e.y + y,
                                        e.fout() * 3f + 0.1f);
                            });

                    Draw.reset();
                }).layer(Layer.blockOver);
    }

    public static final Effect singularityCollapse = new Effect(200f, e -> {
        Color violet = Color.valueOf("#cada8f");
        Color dark = Color.black;

        Draw.color(violet, dark, e.fin());
        Lines.stroke(2f * e.fout());

        Lines.circle(e.x, e.y, 200f * e.fout(Interp.pow4In));
        Lines.circle(e.x, e.y, 120f * e.fout(Interp.pow2In));

        Draw.color(violet);
        Angles.randLenVectors(e.id, 150, 120f * e.fout(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fin() * 3f);
            Drawf.light(e.x + x, e.y + y, 60f, violet, 0.6f * e.fin());
        });

        Draw.color(Color.black);
        Fill.circle(e.x, e.y, 15f * e.fin(Interp.pow5Out));
    });

    public static Effect antiMatterCharge = new Effect(90f, e -> {

        float fin = e.fin();
        float fout = e.fout();
        float warm = fin * fin;

        float fadeIn = Mathf.curve(e.fin(), 0f, 0.2f);

        Color c1 = Color.valueOf("9d7aff");
        Color c2 = Color.valueOf("5f9cff");

        Draw.color(c1, c2, fin);
        Draw.alpha(0.6f * fout * fadeIn);

        Lines.stroke(1.5f * fout * fadeIn);
        for (int i = 0; i < 3; i++) {
            float rot = e.time * (0.6f + i * 0.2f);
            float radius = (6f + i * 4f) * (1f + warm * 1.5f);

            Lines.circle(
                    e.x,
                    e.y,
                    radius + Mathf.sinDeg(rot * 6f) * 1.5f);
        }

        for (int i = 0; i < 6; i++) {
            float angle = i * 60f + e.time * 2f;
            Tmp.v1.trns(angle, 10f + warm * 18f);
            Lines.lineAngle(
                    e.x + Tmp.v1.x,
                    e.y + Tmp.v1.y,
                    angle + 90f,
                    6f * fout * fadeIn
            );
        }

        Draw.alpha(0.4f * fout * fadeIn);
        for (int i = 0; i < 8; i++) {
            float a = Mathf.randomSeed(e.id + i, 360f);
            float r = Mathf.randomSeed(e.id * 2 + i, 4f, 14f) * warm;

            Tmp.v1.trns(a + e.time, r);
            Fill.circle(
                    e.x + Tmp.v1.x,
                    e.y + Tmp.v1.y,
                    1.3f * fout * fadeIn
            );
        }

        Draw.reset();
    });

    public static Effect adamantiumSynthesizerWork = new Effect(60f, e -> {
        Draw.z(Layer.effect);

        Color c1 = Color.valueOf("#E02D2D");
        Color c2 = Color.white;

        float fin = e.fin();
        float fout = e.fout();

        Draw.color(c1, fout * 0.6f);
        Fill.circle(e.x, e.y, 6f + Mathf.absin(Time.time, 6f, 1.5f));

        Draw.color(c1, c2, fin);
        Lines.stroke(1.4f * fout);

        for (int i = 0; i < 2; i++) {
            float radius = 10f + fin * (8f + i * 4f);
            Lines.circle(e.x, e.y, radius);
        }

        Rand rand = Fx.rand;
        rand.setSeed(e.id);

        for (int i = 0; i < 6; i++) {
            float angle = rand.random(360f);
            float len = rand.random(4f, 10f) * fin;

            Tmp.v1.trns(angle, len);
            Draw.color(c1, fout * 0.8f);
            Fill.circle(
                    e.x + Tmp.v1.x,
                    e.y + Tmp.v1.y,
                    rand.random(0.8f, 1.4f) * fout);
        }

        Draw.reset();
    });

    public static Effect shootCircleSmall(Color color) {
        return get("shootCircleSmall", color, new Effect(30, e -> {
            color(color, Color.white, e.fout() * 0.75f);
            rand.setSeed(e.id);
            randLenVectors(e.id, 3, 3 + 23 * e.fin(), e.rotation, 22, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout() * rand.random(1.5f, 3.2f));
                Drawf.light(e.x + x, e.y + y, e.fout() * 4.5f, color, 0.7f);
            });
        }));
    }

    public static Effect lightningHitLarge(Color color) {
        return get("lightningHitLarge", color, new Effect(50f, 180f, e -> {
            color(color);
            Drawf.light(e.x, e.y, e.fout() * 90f, color, 0.7f);
            e.scaled(25f, t -> {
                stroke(3f * t.fout());
                circle(e.x, e.y, 3f + t.fin(Interp.pow3Out) * 80f);
            });
            Fill.circle(e.x, e.y, e.fout() * 8f);
            randLenVectors(e.id + 1, 4, 1f + 60f * e.finpow(), (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 5f));

            color(Color.gray);
            Angles.randLenVectors(e.id, 8, 2.0F + 30.0F * e.finpow(),
                    (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 4.0F + 0.5F));
        }));
    }

    public static Effect sharpBlast(Color colorExternal, Color colorInternal, float lifetime, float range) {
        return new Effect(lifetime, range * 2, e -> {
            Angles.randLenVectors(e.id, (int) Mathf.clamp(range / 8, 4, 18), range / 8, range * (1 + e.fout(Interp.pow2OutInverse)) / 2f, (x, y) -> {
                float angle = Mathf.angle(x, y);
                float width = e.foutpowdown() * rand.random(range / 6, range / 3) / 2 * e.fout();

                rand.setSeed(e.id);
                float length = rand.random(range / 2, range * 1.1f) * e.fout();

                Draw.color(colorExternal);
                DrawFunc.tri(e.x + x, e.y + y, width, range / 3 * e.fout(Interp.pow2In), angle - 180);
                DrawFunc.tri(e.x + x, e.y + y, width, length, angle);

                Draw.color(colorInternal);

                width *= e.fout();

                DrawFunc.tri(e.x + x, e.y + y, width / 2, range / 3 * e.fout(Interp.pow2In) * 0.9f * e.fout(), angle - 180);
                DrawFunc.tri(e.x + x, e.y + y, width / 2, length / 1.5f * e.fout(), angle);
            });
        });
    }

    public static Effect circleSplash(Color color, float lifetime, int num, float range, float size) {
        return new Effect(lifetime, e -> {
            Draw.color(color);
            rand.setSeed(e.id);
            randLenVectors(e.id, num, range * e.finpow(), (x, y) -> {
                float s = e.fout(Interp.pow3In) * (size + rand.range(size / 3f));
                Fill.circle(e.x + x, e.y + y, s);
                Drawf.light(e.x + x, e.y + y, s * 2.25f, color, 0.7f);
            });
        });
    }

    public static Effect lightningHitSmall(Color color) {
        return get("lightningHitSmall", color, new Effect(20, e -> {
            color(color, Color.white, e.fout() * 0.7f);
            randLenVectors(e.id, 5, 18 * e.fin(), (x, y) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 6 + 2);
                Drawf.light(e.x + x, e.y + y, e.fin() * 12 * e.fout(0.25f), color, 0.7f);
            });
        }));
    }

    public static Effect instHit(Color color, int num, float size) {
        return new Effect(20.0F, size * 1.5f, e -> {
            rand.setSeed(e.id);

            for (int i = 0; i < 2; ++i) {
                Draw.color(i == 0 ? color : color.cpy().lerp(Color.white, 0.25f));
                float m = i == 0 ? 1.0F : 0.5F;

                for (int j = 0; j < num; ++j) {
                    float rot = e.rotation + rand.range(size);
                    float w = 15.0F * e.fout() * m;
                    DrawFunc.tri(e.x, e.y, w, (size + rand.range(size * 0.6f)) * m, rot);
                    DrawFunc.tri(e.x, e.y, w, size * 0.3f * m, rot + 180.0F);
                }
            }

            e.scaled(12.0F, (c) -> {
                Draw.color(color.cpy().lerp(Color.white, 0.25f));
                Lines.stroke(c.fout() * 2.0F + 0.2F);
                Lines.circle(e.x, e.y, c.fin() * size * 1.1f);
            });

            e.scaled(18.0F, (c) -> {
                Draw.color(color);
                Angles.randLenVectors(e.id, 25, 8.0F + e.fin() * size * 1.25f, e.rotation, 60.0F, (x, y) -> {
                    Fill.square(e.x + x, e.y + y, c.fout() * 3.0F, 45.0F);
                });
            });

            Drawf.light(e.x, e.y, e.fout() * size, color, 0.7f);
        });
    }

    public static Effect kamikazeExplosion = new Effect(45f, e -> {
        Draw.color(Color.white, Pal.lightOrange, e.fin());

        Fill.circle(e.x, e.y, e.fout() * 35f);

        Draw.color(Pal.lightOrange, Color.gray, e.fin());

        Lines.stroke(3f * e.fout());
        Lines.circle(e.x, e.y, e.fin() * 40f);

        for(int i = 0; i < 12; i++){
            float angle = i * 30f + e.fin() * 20f;
            float len = e.finpow() * 25f;

            Drawf.tri(
                    e.x + Angles.trnsx(angle, len),
                    e.y + Angles.trnsy(angle, len),
                    4f * e.fout(),
                    12f * e.fout(),
                    angle
            );
        }

        Draw.color(Color.gray, Color.darkGray, e.fin());
        Draw.alpha(0.7f * e.fout());

        for(int i = 0; i < 8; i++){
            float angle = i * 45f;
            float dst = e.fin() * 30f;

            Fill.circle(
                    e.x + Angles.trnsx(angle, dst),
                    e.y + Angles.trnsy(angle, dst),
                    5f * e.fout()
            );
        }
    });

    public static Effect instShoot(Color color, Color colorInner) {
        return new Effect(24f, e -> {
            e.scaled(10.0F, (b) -> {
                Draw.color(Color.white, color, b.fin());
                Lines.stroke(b.fout() * 3.0F + 0.2F);
                Lines.circle(b.x, b.y, b.fin() * 50.0F);
            });
            Draw.color(color);

            for (int i : Mathf.signs) {
                DrawFunc.tri(e.x, e.y, 8.0F * e.fout(), 85.0F, e.rotation + 90.0F * i);
                DrawFunc.tri(e.x, e.y, 8.0F * e.fout(), 50.0F, 90 + 90.0F * i);
            }

            Draw.color(colorInner);

            for (int i : Mathf.signs) {
                DrawFunc.tri(e.x, e.y, 5F * e.fout(), 48.0F, e.rotation + 90.0F * i);
                DrawFunc.tri(e.x, e.y, 5F * e.fout(), 29.0F, 90 + 90.0F * i);
            }
        });
    }

    public interface EffectParam {
        void draw(long id, float x, float y, float rot, float fin);
    }

    public static Effect subEffect(float lifetime, float radius, int num, float childLifetime, Interp spreadOutInterp, EffectParam drawer) {
        return new Effect(lifetime, radius * 2f, e -> {
            rand.setSeed(e.id);
            float finT = e.lifetime * e.fin(spreadOutInterp);

            for (int s = 0; s < num; s++) {
                float sBegin = rand.random(e.lifetime - childLifetime);
                float fin = (finT - sBegin) / childLifetime;

                if (fin < 0 || fin > 1) continue;

                float fout = 1f - fin;

                rand2.setSeed(e.id + s);
                float theta = rand2.random(0f, Mathf.PI2);

                Tmp.v1.set(Mathf.cos(theta), Mathf.sin(theta))
                        .scl(radius * sBegin / (e.lifetime - childLifetime));

                float vx = Tmp.v1.x;
                float vy = Tmp.v1.y;

                Tmp.c1.set(e.color).lerp(Color.white, fout * 0.7f);
                Draw.color(Tmp.c1);

                drawer.draw(
                        e.id + s + 9999,
                        e.x + vx,
                        e.y + vy,
                        Mathf.radiansToDegrees * theta,
                        fin
                );
            }
        });
    }

    public static Effect hitSpark(Color color, float lifetime, int num, float range, float stroke, float length) {
        return new Effect(lifetime, e -> {
            color(color, Color.white, e.fout() * 0.3f);
            stroke(e.fout() * stroke);

            randLenVectors(e.id, num, e.finpow() * range, e.rotation, 360f, (x, y) -> {
                float ang = Mathf.angle(x, y);
                lineAngle(e.x + x, e.y + y, ang, e.fout() * length * 0.85f + length * 0.15f);
            });
        });
    }
}