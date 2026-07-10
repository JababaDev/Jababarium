package jababarium.content.blocks;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Rand;
import jababarium.expand.block.drawer.CollapseCharge;
import jababarium.expand.block.drawer.SingularCharge;
import jababarium.expand.block.power.EffectPowerGenerator;
import jababarium.expand.block.special.AntiMatterWarper;
import jababarium.expand.block.special.UnitPrinter;
import mindustry.content.*;
import mindustry.type.LiquidStack;
import mindustry.world.draw.*;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import jababarium.util.graphic.DrawFunc;
import jababarium.expand.block.special.FluxReactor;
import jababarium.expand.block.special.SelfHealingLiquidBlocks;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.UnitSorts;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.pattern.ShootPattern;
import mindustry.entities.pattern.ShootSpread;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.part.HaloPart;
import mindustry.entities.part.RegionPart;
import jababarium.expand.block.commandable.BombLauncher;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.storage.Unloader;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.meta.BuildVisibility;

import static arc.graphics.g2d.Lines.lineAngle;
import static mindustry.type.ItemStack.with;

import jababarium.content.*;
import static jababarium.content.JBBlocks.*;

public class EliteTurrets {

    public static void load() {
        apex = new ItemTurret("apex") {
            {
                requirements(Category.turret, with(
                        JBItems.singularium, 1400,
                        JBItems.sergium, 1200,
                        JBItems.pulsarite, 1100,
                        Items.surgeAlloy, 1200,
                        Items.phaseFabric, 800));

                size = 8;
                health = 950000;
                range = 1500f;
                reload = 420f;
                recoil = 14f;
                recoilTime = 140f;
                shake = 10f;
                rotateSpeed = 0.35f;
                shootCone = 2f;

                itemCapacity = 500;

                consumePower(120f);

                heatColor = Color.valueOf("#4dff7a");
                shootSound = JBSounds.blastShockwave;
                chargeSound = JBSounds.largeBeam;

                drawer = new DrawTurret() {
                    {
                        parts.add(new RegionPart("-glow") {
                            {
                                blending = Blending.additive;
                                color = heatColor;
                                progress = PartProgress.warmup;
                                outline = false;
                            }
                        });
                    }
                };

                shootEffect = new Effect(80f, e -> {
                    Draw.color(heatColor, Color.white, e.fin());
                    Lines.stroke(e.fout() * 8f);
                    Lines.circle(e.x, e.y, e.fin() * 220f);
                    Drawf.light(e.x, e.y, e.fin() * 260f, heatColor, 0.9f);
                });

                ammoUseEffect = new Effect(140f, e -> {
                    Draw.color(heatColor);
                    Lines.stroke(Mathf.curve(e.fin(), 0, 1) * 6f);
                    Lines.circle(e.x, e.y, e.fout() * 200f);
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fin() * 240f, heatColor, 0.9f);
                });

                ammo(JBItems.plastanium, JBBullets.apexShell);
            }
        };

        hastae = new ItemTurret("hastae") {
            {
                requirements(Category.turret, with(
                        Items.titanium, 250,
                        Items.thorium, 150,
                        Items.plastanium, 100,
                        JBItems.adamantium, 100,
                        JBItems.sergium, 500));

                size = 6;
                health = 940000;
                range = 2000f;
                reload = 1000f;

                recoil = 15f;
                recoilTime = 1000f;
                shake = 6f;
                rotateSpeed = 0.5f;

                heatColor = JBColor.yellow;

                consumePower(12f);
                shootSound = JBSounds.shootGauss3;

                shoot = new ShootPattern() {
                    {
                        firstShotDelay = 60f;
                    }
                };

                ammo(
                        Items.surgeAlloy, new BasicBulletType(75f, 16400) {
                            {
                                width = 15f;
                                height = 100f;
                                lifetime = 30f;

                                pierce = true;
                                pierceCap = -1;

                                frontColor = Color.white;
                                backColor = Color.orange;

                                trailColor = JBColor.yellow;
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
                        });

                smokeEffect = Fx.coreExplosion;

            }

        };

        solarApex = new PowerTurret("solar-apex") {
            {
                requirements(Category.turret, with(
                        JBItems.singularium, 1200,
                        JBItems.pulsarite, 1400,
                        JBItems.sergium, 1600,
                        Items.surgeAlloy, 1200,
                        Items.phaseFabric, 900));

                size = 8;
                health = 120000;
                range = 1200f;
                reload = 10f;
                recoil = 10f;
                recoilTime = 120f;
                shake = 8f;
                rotateSpeed = 0.6f;
                shootCone = 2f;

                consumePower(120f);
                consumeLiquid(Liquids.cryofluid, 5f);
                liquidCapacity = 480f;
                coolantMultiplier = 3.5f;

                Color solarOrange = Color.valueOf("ffd36b");
                Color solarYellow = Color.valueOf("fff1a8");
                Color solarWhite = Color.valueOf("ffffff");
                Color solarCore = Color.valueOf("ffaa5f");

                heatColor = solarOrange;
                shootSound = JBSounds.shootGauss3;
                loopSound = JBSounds.bioLoop;
                loopSoundVolume = 2f;

                shoot = new ShootPattern() {
                    {
                        firstShotDelay = 90f; 
                    }
                };

                
                drawer = new DrawTurret("") {
                    @Override
                    public void draw(Building build) {
                        super.draw(build);

                        if (build instanceof PowerTurretBuild) {
                            PowerTurretBuild turret = (PowerTurretBuild) build;

                            
                            float warmup = Mathf.clamp(turret.heat);

                            if (warmup > 0.01f) {
                                Draw.z(Layer.turret - 0.1f);
                                Draw.blend(Blending.additive);

                                
                                Draw.color(solarYellow, warmup * 0.8f);
                                Lines.stroke(2f * warmup);
                                float innerRadius = 24f + warmup * 4f;
                                for (int i = 0; i < 3; i++) {
                                    float angle = Time.time * 3f + i * 120f;
                                    float x1 = build.x + Angles.trnsx(angle, innerRadius - 6f);
                                    float y1 = build.y + Angles.trnsy(angle, innerRadius - 6f);
                                    float x2 = build.x + Angles.trnsx(angle, innerRadius + 6f);
                                    float y2 = build.y + Angles.trnsy(angle, innerRadius + 6f);
                                    Lines.line(x1, y1, x2, y2);
                                }

                                
                                Draw.color(solarOrange, warmup * 0.6f);
                                Lines.stroke(2f * warmup);
                                float midRadius = 32f + warmup * 6f;
                                for (int i = 0; i < 4; i++) {
                                    float angle = -Time.time * 2f + i * 90f + 45f;
                                    float x1 = build.x + Angles.trnsx(angle, midRadius - 8f);
                                    float y1 = build.y + Angles.trnsy(angle, midRadius - 8f);
                                    float x2 = build.x + Angles.trnsx(angle, midRadius + 8f);
                                    float y2 = build.y + Angles.trnsy(angle, midRadius + 8f);
                                    Lines.line(x1, y1, x2, y2);
                                }

                                
                                Draw.color(solarOrange, warmup * 0.4f);
                                Lines.stroke(1.5f * warmup);
                                float outerRadius = 44f + warmup * 8f;
                                for (int i = 0; i < 6; i++) {
                                    float angle = Time.time * 1.2f + i * 60f;
                                    float x1 = build.x + Angles.trnsx(angle, outerRadius - 10f);
                                    float y1 = build.y + Angles.trnsy(angle, outerRadius - 10f);
                                    float x2 = build.x + Angles.trnsx(angle, outerRadius + 10f);
                                    float y2 = build.y + Angles.trnsy(angle, outerRadius + 10f);
                                    Lines.line(x1, y1, x2, y2);
                                }

                                
                                Draw.color(solarWhite, warmup);
                                float pulse = 1f + Mathf.absin(Time.time, 3f, 0.2f);
                                Fill.circle(build.x, build.y, (8f + warmup * 4f) * pulse);

                                Draw.blend();
                                Draw.reset();
                            }

                            
                            float charge = Mathf.clamp(turret.reloadCounter / 90f);

                            if (charge > 0.01f && turret.isShooting()) {
                                Draw.z(Layer.effect);
                                Draw.color(solarYellow, solarWhite, charge);
                                Draw.blend(Blending.additive);

                                
                                float chargePulse = 1f + Mathf.absin(Time.time, 1.5f, 0.5f);
                                float chargeSize = 16f + charge * 30f;
                                Fill.circle(build.x, build.y, chargeSize * chargePulse * charge);

                                
                                Lines.stroke(4f * charge);
                                for (int i = 0; i < 4; i++) {
                                    float offset = (Time.time * 3f + i * 30f) % 120f;
                                    float ringRadius = 80f - offset * 0.7f;
                                    float alpha = (1f - offset / 120f) * charge;

                                    if (ringRadius > chargeSize) {
                                        Draw.alpha(alpha * 0.8f);
                                        Lines.circle(build.x, build.y, ringRadius);
                                    }
                                }

                                
                                if (charge > 0.5f) {
                                    float beamStrength = (charge - 0.5f) * 2f;
                                    Lines.stroke(3f * beamStrength);

                                    for (int i = 0; i < 12; i++) {
                                        float angle = turret.rotation + i * 30f + Time.time * 4f;
                                        float len = 40f * beamStrength * chargePulse;

                                        Draw.alpha(beamStrength * 0.7f);
                                        Lines.lineAngle(build.x, build.y, angle, len);
                                    }
                                }

                                
                                if (charge > 0.7f) {
                                    float spiralStrength = (charge - 0.7f) * 3.33f;
                                    Draw.color(solarCore, solarYellow, spiralStrength);
                                    Lines.stroke(2f * spiralStrength);

                                    for (int i = 0; i < 6; i++) {
                                        float spiralAngle = Time.time * 5f + i * 60f;
                                        float spiralRadius = 50f + Mathf.sin(Time.time * 0.1f + i) * 10f;

                                        float x = build.x + Angles.trnsx(spiralAngle, spiralRadius);
                                        float y = build.y + Angles.trnsy(spiralAngle, spiralRadius);

                                        Draw.alpha(spiralStrength * 0.6f);
                                        Fill.circle(x, y, 4f * spiralStrength);
                                    }
                                }

                                Draw.blend();
                                Draw.reset();

                                
                                Drawf.light(build.x, build.y, chargeSize * 4f * charge, solarYellow, 0.8f * charge);
                            }
                        }
                    }
                };

                
                shootEffect = new MultiEffect(
                        
                        new Effect(40f, e -> {
                            Draw.color(Color.white);
                            Draw.blend(Blending.additive);
                            Fill.circle(e.x, e.y, e.fout(Interp.pow2Out) * 45f);
                            Draw.blend();
                        }),

                        
                        new Effect(70f, e -> {
                            Draw.color(solarYellow, solarWhite, e.fin());
                            Draw.blend(Blending.additive);

                            Lines.stroke(e.fout() * 8f);
                            Lines.circle(e.x, e.y, e.fin() * 220f);

                            Lines.stroke(e.fout() * 5f);
                            Lines.circle(e.x, e.y, e.fin() * 170f);

                            Lines.stroke(e.fout() * 3f);
                            Lines.circle(e.x, e.y, e.fin() * 130f);

                            Draw.blend();
                            Drawf.light(e.x, e.y, e.fin() * 280f, solarYellow, 0.9f);
                        }),

                        
                        new Effect(60f, e -> {
                            Draw.color(solarOrange, solarYellow, e.fin());
                            Draw.blend(Blending.additive);

                            for (int i = 0; i < 12; i++) {
                                float angle = i * 30f + e.rotation;
                                float len = e.fout(Interp.pow3Out) * 100f;

                                Drawf.tri(e.x, e.y, 10f * e.fout(), len, angle);
                            }

                            Draw.blend();
                        }),

                        
                        new Effect(50f, e -> {
                            Draw.color(solarWhite, solarYellow, e.fin());
                            Draw.blend(Blending.additive);

                            for (int i = 0; i < 24; i++) {
                                float angle = i * 15f + e.rotation + 7.5f;
                                float len = e.fout(Interp.pow2Out) * 60f;

                                Lines.stroke(2f * e.fout());
                                Lines.lineAngle(e.x, e.y, angle, len);
                            }

                            Draw.blend();
                        }),

                        
                        new ParticleEffect() {
                            {
                                particles = 50;
                                length = 150f;
                                lifetime = 70f;
                                sizeFrom = 10f;
                                sizeTo = 0f;
                                colorFrom = solarWhite;
                                colorTo = solarOrange.cpy().a(0f);
                                cone = 360f;
                                lightOpacity = 0.9f;
                                interp = Interp.pow2Out;
                            }
                        },

                        
                        new ParticleEffect() {
                            {
                                particles = 30;
                                length = 100f;
                                lifetime = 50f;
                                sizeFrom = 6f;
                                sizeTo = 0f;
                                colorFrom = solarYellow;
                                colorTo = solarCore.cpy().a(0f);
                                cone = 360f;
                                lightOpacity = 0.7f;
                            }
                        });

                
                ammoUseEffect = new MultiEffect(
                        
                        new Effect(140f, e -> {
                            Draw.color(solarYellow);
                            Draw.blend(Blending.additive);

                            Lines.stroke(Mathf.curve(e.fin(), 0, 0.1f) * 6f);
                            Lines.circle(e.x, e.y, e.fout() * 200f);

                            Draw.color(solarWhite);
                            Lines.stroke(Mathf.curve(e.fin(), 0, 0.1f) * 4f);
                            Lines.circle(e.x, e.y, e.fout() * 170f);

                            Draw.blend();
                            Drawf.light(e.x, e.y, e.fout() * 260f, solarYellow, 0.9f);
                        }),

                        
                        new Effect(120f, e -> {
                            Draw.color(solarOrange, solarYellow, e.fin());
                            Draw.blend(Blending.additive);

                            for (int i = 0; i < 5; i++) {
                                float offset = (e.time * 1.5f + i * 24f) % 120f;
                                float radius = 240f - offset * 2f;
                                float alpha = (1f - offset / 120f) * 0.8f;

                                if (radius > 20f) {
                                    Draw.alpha(alpha);
                                    Lines.stroke(4f * alpha);
                                    Lines.circle(e.x, e.y, radius);
                                }
                            }

                            Draw.blend();
                        }),

                        
                        new Effect(100f, e -> {
                            Draw.blend(Blending.additive);

                            for (int i = 0; i < 20; i++) {
                                float particleLife = (e.time + i * 5f) / 100f;
                                if (particleLife > 1f)
                                    continue;

                                float angle = e.rotation + i * 18f + particleLife * 360f;
                                float dist = (1f - particleLife) * 180f;

                                float px = e.x + Angles.trnsx(angle, dist);
                                float py = e.y + Angles.trnsy(angle, dist);

                                Draw.color(solarYellow, solarWhite, particleLife);
                                Draw.alpha((1f - particleLife) * 0.8f);
                                Fill.circle(px, py, (1f - particleLife) * 6f);
                            }

                            Draw.blend();
                        }),

                        
                        new ParticleEffect() {
                            {
                                particles = 40;
                                length = 180f;
                                lifetime = 130f;
                                sizeFrom = 8f;
                                sizeTo = 0f;
                                colorFrom = solarYellow;
                                colorTo = solarOrange.cpy().a(0f);
                                cone = 360f;
                                interp = Interp.pow3In; 
                                lightOpacity = 0.7f;
                            }
                        },

                        
                        new ParticleEffect() {
                            {
                                particles = 25;
                                length = 120f;
                                lifetime = 90f;
                                sizeFrom = 5f;
                                sizeTo = 0f;
                                colorFrom = solarWhite;
                                colorTo = solarYellow.cpy().a(0f);
                                cone = 360f;
                                interp = Interp.pow2In;
                            }
                        });

                shootType = new ContinuousLaserBulletType(4200f) {
                    {
                        length = 1200f;
                        width = 26f;
                        lifetime = 60f;
                        hitSize = 20f;
                        drawSize = 420f;
                        knockback = 2.5f;
                        pierceArmor = true;

                        hitColor = lightColor = lightningColor = solarOrange;
                        colors = new Color[] {
                                solarOrange.cpy().mul(1f, 0.85f, 0.6f, 0.6f),
                                solarOrange.cpy().mul(1f, 0.9f, 0.7f, 0.8f),
                                solarWhite
                        };

                        hitEffect = JBFx.lightningHitLarge;
                        shootEffect = Fx.shootBigColor;
                        smokeEffect = Fx.smokeCloud;
                        despawnEffect = JBFx.lightningHitLarge;

                        status = StatusEffects.melting;
                        statusDuration = 60f * 6f;
                    }
                };
            }
        };

        entropy = new ItemTurret("entropy") {
            {
                requirements(Category.turret, with(
                        Items.silicon, 1500,
                        Items.titanium, 2200,
                        Items.thorium, 800,
                        JBItems.singularium, 800,
                        Items.phaseFabric, 4000));

                size = 12;
                health = 600000;
                range = 800f;
                reload = 2f;
                recoil = 1.5f;
                inaccuracy = 3f;
                shootCone = 30f;
                rotateSpeed = 2f;

                consumeAmmoOnce = false;
                consumeCoolant(0.5f);

                shoot = new ShootBarrel() {
                    {
                        barrels = new float[] {
                                -10f, 6f, 0f,
                                -30f, -24f, 0f,
                                10f, 6f, 0f,
                                30f, -24f, 0f
                        };
                        shots = 4;
                        shotDelay = 5f;
                    }
                };

                ammo(
                        Items.thorium, new BasicBulletType(7.5f, 3500) {
                            {
                                width = 9f;
                                height = 16f;
                                lifetime = 106f;

                                frontColor = Color.white;
                                backColor = Color.valueOf("ff88cc");
                                trailColor = Color.valueOf("ff88cc");
                                trailWidth = 2.5f;
                                trailLength = 12;

                                status = StatusEffects.slow;

                                pierce = true;
                                pierceCap = 2;

                                hitEffect = new Effect(20f, e -> {
                                    Draw.color(Color.white, Color.valueOf("ff88cc"), e.fin());
                                    Lines.stroke(e.fout() * 2f);
                                    Lines.circle(e.x, e.y, e.fin() * 15f);
                                });

                                despawnEffect = JBFx.lightningSpark;
                            }
                        });

                shootSound = JBSounds.shootGauss1;
                loopSound = JBSounds.beam;
                loopSoundVolume = 1f;

                consumePower(120f);
            }
        };

        abbys = new ItemTurret("abbys") {
            {
                armor = 2000;
                size = 16;
                outlineRadius = 7;
                range = 1200;
                heatColor = Color.valueOf("1a1a1a");
                unitSort = UnitSorts.strongest;

                coolant = consume(new ConsumeLiquid(JBLiquids.argon, 1));
                liquidCapacity = 120;
                coolantMultiplier = 2.5f;

                buildCostMultiplier *= 2;
                canOverdrive = false;

                drawer = new DrawTurret() {
                    {
                        parts.add(new SingularCharge() {{
                            progress = PartProgress.smoothReload.inv();
                            chargeY = t -> -35f;
                            shootY  = t -> 60f;

                            // hexSize = 32f;
                            // coreRad = 10f;
                            // muzzleRad = 16f;
                        }});
                    }
                };

                shootEffect = new Effect(90f, 2000f, e -> {
                    Draw.blend(Blending.normal);
                    Draw.z(Layer.effect + 0.5f);

                    Draw.color(Color.white);
                    Fill.circle(e.x, e.y, e.fin(Interp.pow5Out) * 10f);

                    for (int i = 0; i < 12; i++) {
                        float angle = e.rotation + i * 30f + Mathf.randomSeed(e.id, 360f) * e.fin();
                        DrawFunc.tri(e.x, e.y, 25f * e.fout(), 850f * e.fout(Interp.pow10Out), angle);
                    }

                    Draw.color(Color.white);
                    Angles.randLenVectors(e.id, 50, 10f + 450f * e.fin(), (x, y) -> {
                        Fill.circle(e.x + x, e.y + y, e.fout() * 4f);
                    });

                    Draw.blend();
                    Draw.reset();
                });

                smokeEffect = new Effect(100f, e -> {
                    Draw.color(Color.black);
                    Angles.randLenVectors(e.id, 20, 200f * e.fout(), (x, y) -> {
                        float tx = Mathf.lerp(e.x + x, e.x, e.fin(Interp.pow3In));
                        float ty = Mathf.lerp(e.y + y, e.y, e.fin(Interp.pow3In));
                        Fill.circle(tx, ty, e.fslope() * 5f);
                    });
                });

                recoil = 30f;
                shake = 120f;
                health = 1000000;
                reload = 1800f;
                rotateSpeed = 0.15f;

                ammo(JBItems.singularium, JBBullets.abbys);

                consumePower(1200f);

                requirements(Category.turret, BuildVisibility.shown,
                        with(JBItems.amalgam, 5000, JBItems.singularium, 2000));
            }
        };

        gammaReaper = new ItemTurret("gammaReaper") {
            {
                armor = 2000;
                size = 16;
                outlineRadius = 0;
                range = 1200;
                heatColor = Color.valueOf("1a1a1a");
                unitSort = UnitSorts.strongest;

                coolant = consume(new ConsumeLiquid(JBLiquids.argon, 1));
                liquidCapacity = 120;
                coolantMultiplier = 2.5f;

                buildCostMultiplier *= 2;
                canOverdrive = false;

                drawer = new DrawTurret() {
                    {
                        parts.add(new CollapseCharge() {{
                            progress = PartProgress.smoothReload.inv().curve(Interp.pow5Out);
                            chargeY = t -> -35f;
                            shootY  = t -> 90 * curve.apply(1 - t.smoothReload);
                        }});
                    }
                };

                shootEffect = new Effect(60f, 1800f, e -> {
                    Draw.blend(Blending.additive);
                    Rand rand = new Rand();
                    rand.setSeed(e.id);

                    float fin  = e.fin();
                    float fout = e.fout();

                    
                    Draw.color(Color.white, JBColor.thurmixRedLight, Mathf.curve(fin, 0f, 0.15f));
                    Fill.circle(e.x, e.y, fout * 90f);

                    
                    Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, fin);
                    for (int i = 0; i < 8; i++) {
                        float angle = e.rotation + i * 45f;
                        float len   = (320f + rand.random(120f)) * Mathf.curve(fout, 0f, 0.85f);
                        float width = (28f + rand.random(10f)) * fout;
                        Drawf.tri(e.x, e.y, width, len, angle);
                        
                        Drawf.tri(e.x, e.y, width * 0.5f, len * 0.25f, angle + 180f);
                    }

                    
                    Draw.color(JBColor.thurmixRed, JBColor.thurmixRedDark, fin);
                    for (int i = 0; i < 8; i++) {
                        float angle = e.rotation + 22.5f + i * 45f;
                        float len   = (200f + rand.random(80f)) * Mathf.curve(fout, 0f, 0.75f);
                        float width = (14f + rand.random(6f)) * fout;
                        Drawf.tri(e.x, e.y, width, len, angle);
                    }

                    
                    Draw.color(JBColor.thurmixRed);
                    Angles.randLenVectors(e.id, 40, 60f + 500f * e.finpow(), e.rotation, 360f, (x, y) -> {
                        float s = fout * (rand.random(4f, 12f));
                        Fill.circle(e.x + x, e.y + y, s);
                        Drawf.light(e.x + x, e.y + y, s * 3f, JBColor.thurmixRed, 0.5f);
                    });

                    
                    Draw.color(JBColor.thurmixRedLight, Color.white, fout * 0.4f);
                    for (int i = 0; i < 16; i++) {
                        float angle = e.rotation + i * 22.5f;
                        float len   = rand.random(80f, 220f) * fout;
                        float width = rand.random(6f, 18f) * fout;
                        Drawf.tri(e.x, e.y, width, len, angle);
                    }

                    
                    Draw.color(JBColor.thurmixRed);
                    for (int i = 0; i < 4; i++) {
                        float ringFin = Mathf.clamp((fin - i * 0.08f) * 1.5f);
                        if (ringFin <= 0f) continue;
                        Lines.stroke((1f - ringFin) * 8f + 1f);
                        Lines.circle(e.x, e.y, ringFin * (180f + i * 60f));
                    }

                    
                    Drawf.light(e.x, e.y, 900f * fout, JBColor.thurmixRed, 0.85f * fout);

                    Draw.blend();
                });

                smokeEffect = new Effect(120f, 1600f, e -> {
                    Draw.blend(Blending.additive);
                    Rand rand = new Rand();
                    rand.setSeed(e.id);

                    float fin  = e.fin();
                    float fout = e.fout();

                    
                    Draw.color(JBColor.thurmixRed, JBColor.thurmixRedDark, fin);
                    Angles.randLenVectors(e.id, 30, 80f + 600f * e.finpow(), e.rotation, 360f, (x, y) -> {
                        float s = fout * rand.random(10f, 30f);
                        Fill.circle(e.x + x, e.y + y, s);
                    });

                    
                    Draw.color(JBColor.thurmixRedDark, Color.valueOf("#1a0005"), fin * 0.8f);
                    Angles.randLenVectors(e.id + 1, 20, 200f + 700f * e.finpow(), e.rotation, 360f, (x, y) -> {
                        float s = fout * rand.random(20f, 50f);
                        Fill.circle(e.x + x, e.y + y, s);
                    });

                    
                    Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, fin);
                    Angles.randLenVectors(e.id + 2, 25, 40f + 300f * Interp.pow2Out.apply(fin), e.rotation, 360f, (x, y) -> {
                        float s = fout * rand.random(5f, 15f);
                        Fill.circle(e.x + x, e.y + y, s);
                        Drawf.light(e.x + x, e.y + y, s * 4f, JBColor.thurmixRed, 0.4f * fout);
                    });

                    
                    Draw.color(JBColor.thurmixRed, JBColor.thurmixRedDark, fin);
                    for (int i = 0; i < 5; i++) {
                        float ringFin = Mathf.clamp((fin - i * 0.07f) * 1.4f);
                        if (ringFin <= 0f) continue;
                        float ringFout = 1f - ringFin;
                        Lines.stroke(ringFout * 10f + 1f);
                        Lines.circle(e.x, e.y, ringFin * (250f + i * 80f));
                    }

                    
                    Draw.color(JBColor.thurmixRed, Color.valueOf("#1a0005"), fin * 0.7f);
                    for (int i = 0; i < 12; i++) {
                        float angle = e.rotation + i * 30f + rand.range(8f);
                        float len   = rand.random(150f, 420f) * Mathf.curve(fout, 0f, 0.7f);
                        float width = rand.random(8f, 22f) * fout;
                        Drawf.tri(e.x, e.y, width, len, angle);
                    }

                    
                    e.scaled(18f, s -> {
                        Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, s.fin());
                        Fill.circle(e.x, e.y, s.fout() * 140f);
                        Lines.stroke(s.fout() * 6f);
                        Lines.circle(e.x, e.y, s.fin(Interp.circleOut) * 300f);
                    });

                    Drawf.light(e.x, e.y, 700f * fout, JBColor.thurmixRed, 0.7f * fout);

                    Draw.blend();
                });

                recoil = 30f;
                shake = 120f;
                health = 1000000;
                reload = 2000f;
                rotateSpeed = 0.15f;

                shootSound = JBSounds.hugeBlast;

                ammo(JBItems.singularium, JBBullets.gammaReaper);

                consumePower(2200f);

                requirements(Category.turret, BuildVisibility.shown,
                        with(JBItems.amalgam, 6000, JBItems.singularium, 2500));
            }
        };

    }
}
