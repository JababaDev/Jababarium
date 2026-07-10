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

public class VerdantBullets {

    public static void load() {
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

    }
}
