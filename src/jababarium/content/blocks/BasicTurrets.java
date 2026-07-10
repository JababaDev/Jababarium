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

public class BasicTurrets {

    public static void load() {
        entropyChain = new ItemTurret("entropy-chain") {
            {
                requirements(Category.turret, with(
                        Items.titanium, 200,
                        Items.plastanium, 150,
                        Items.silicon, 200,
                        JBItems.feronium, 200));

                size = 3;
                health = 1400;
                range = 260f;
                reload = 40f;
                inaccuracy = 5f;

                consumePower(6f);

                ammo(
                        Items.plastanium, JBBullets.entropyBolt);
            }
        };

        avangard = new ItemTurret("avangard") { 
            {
                requirements(Category.turret,
                        with(Items.graphite, 220, JBItems.feronium, 200, Items.silicon, 120, JBItems.plastanium, 100));

                size = 4;
                health = 1350;
                reload = 100f;
                range = 350f;
                recoil = 4f;
                rotateSpeed = 4f;
                inaccuracy = 3f;
                shootCone = 20f;
                ammoUseEffect = Fx.casing2;

                offset = 100;

                heatColor = JBColor.thurmixRed;

                shoot = new ShootAlternate(8f) {
                    {
                        shots = 20;
                        shotDelay = 0f;
                    }
                };

                ammo(
                        Items.sporePod, new BasicBulletType(6f, 100) {
                            {
                                width = 14f;
                                height = 20f;
                                lifetime = 58f;
                                homingPower = 0.08f;
                                homingRange = 80f;
                                status = StatusEffects.corroded;
                                ammoMultiplier = 5;
                                frontColor = Color.valueOf("#bf92f9");
                                backColor = Color.valueOf("#665c9f");
                            }
                        });

                smokeEffect = new Effect(50, e -> {
                    Draw.color(heatColor);
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fout() * 120, heatColor, 0.7f);
                });
            }
        };

        omega = new ItemTurret("omega") { 
            {
                requirements(Category.turret,
                        with(Items.graphite, 160, JBItems.feronium, 150, Items.silicon, 120, JBItems.phaseFabric, 50));

                size = 6;
                health = 1750;
                reload = 10f;
                range = 280f;
                recoil = 2f;
                rotateSpeed = 4f;
                inaccuracy = 3f;
                shootCone = 20f;
                ammoUseEffect = Fx.casing2;

                heatColor = JBColor.thurmixRed;

                shoot = new ShootAlternate(20f);

                ammo(
                        Items.silicon, new BasicBulletType(6f, 100) {
                            {
                                width = 28f;
                                height = 40f;
                                lifetime = 48f;
                                ammoMultiplier = 5;
                                frontColor = Color.valueOf("#948f9b");
                                backColor = Color.valueOf("#4b4a4f");
                                despawnEffect = Fx.explosion;
                                despawnSound = JBSounds.missile;
                            }
                        });

                smokeEffect = new Effect(50, e -> {
                    Draw.color(heatColor);
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fout() * 120, heatColor, 0.7f);
                });
            }
        };

        ignis = new ItemTurret("ignis") { 
            {
                requirements(Category.turret,
                        with(Items.graphite, 220, JBItems.feronium, 200, Items.silicon, 120, JBItems.cryostal, 100));

                size = 5;
                health = 2350;
                reload = 50f;
                range = 350f;
                recoil = 2f;
                rotateSpeed = 6f;
                inaccuracy = 3f;
                shootCone = 20f;
                ammoUseEffect = Fx.casing2;

                heatColor = JBColor.thurmixRed;

                shoot = new ShootAlternate(8f) {
                    {
                        shots = 6;
                        shotDelay = 4f;
                    }
                };

                ammo(
                        Items.graphite, new BasicBulletType(7f, 95) {
                            {
                                width = 18f;
                                height = 24f;
                                lifetime = 50f;
                                ammoMultiplier = 4;
                                status = StatusEffects.corroded;
                                statusDuration = 150f;
                                frontColor = Color.valueOf("ffaa5f");
                                backColor = Color.valueOf("d37f40");
                                trailWidth = 1.5f;
                                trailLength = 6;
                                trailColor = Color.valueOf("d37f40");
                            }
                        });

                smokeEffect = new Effect(50, e -> {
                    Draw.color(heatColor);
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fout() * 120, heatColor, 0.7f);
                });
            }
        };

        nyx = new ItemTurret("nyx") { 
            {
                requirements(Category.turret,
                        with(Items.graphite, 160, JBItems.feronium, 150, JBItems.cryostal, 120, JBItems.phaseFabric,
                                50));

                size = 8;
                health = 12350;
                reload = 3f;
                range = 350f;
                recoil = 2f;
                rotateSpeed = 4f;
                inaccuracy = 3f;
                shootCone = 20f;
                ammoUseEffect = Fx.casing2;

                shootSound = JBSounds.missile;

                heatColor = JBColor.thurmixRed;

                shoot = new ShootAlternate(20f);

                consumePowerCond(100f, TurretBuild::isActive);

                ammo(
                        Items.titanium, new BasicBulletType(11f, 300) {
                            {
                                width = 20f;
                                height = 30f;
                                lifetime = 30f;
                                ammoMultiplier = 5;
                                status = StatusEffects.corroded;
                                frontColor = Color.valueOf("#12c6de");
                                backColor = Color.valueOf("#008d92");
                                despawnEffect = Fx.explosion;
                                despawnSound = JBSounds.missile;
                            }
                        });

                smokeEffect = new Effect(50, e -> {
                    Draw.color(heatColor);
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fout() * 120, heatColor, 0.7f);
                });
            }
        };

        helix = new ItemTurret("helix") { 
            {
                armor = 30;
                size = 5;
                outlineRadius = 7;
                range = 700;
                heatColor = JBColor.green;
                

                coolant = new ConsumeLiquid(Liquids.cryofluid, 1f);
                liquidCapacity = 120;
                coolantMultiplier = 2.5f;

                buildCostMultiplier *= 2;
                canOverdrive = false;

                shoot = new ShootPattern();
                inaccuracy = 0;

                ammoPerShot = 40;
                coolantMultiplier = 0.8f;
                rotateSpeed = 1f;

                float chargeCircleFrontRad = 12f;

                shootEffect = new Effect(60f, 500f, e -> {
                    float scl = 0.05f;
                    if (e.data instanceof Float)
                        scl *= (float) e.data;
                    Draw.color(heatColor, Color.white, e.fout() * 0.25f);

                    float rand = Mathf.randomSeed(e.id, 60f);
                    float extend = Mathf.curve(e.fin(Interp.pow10Out), 0.075f, 1f) * scl;
                    float rot = e.fout(Interp.pow10In);

                    for (int i : Mathf.signs) {
                        DrawFunc.tri(e.x, e.y, chargeCircleFrontRad * 1.2f * e.foutpowdown() * scl, 200 + 500 * extend,
                                e.rotation + (90 + rand) * rot + 90 * i - 45);
                    }

                    for (int i : Mathf.signs) {
                        DrawFunc.tri(e.x, e.y, chargeCircleFrontRad * 1.2f * e.foutpowdown() * scl, 200 + 500 * extend,
                                e.rotation + (90 + rand) * rot + 90 * i + 45);
                    }
                });

                smokeEffect = new Effect(50, e -> {
                    Draw.color(heatColor);
                    Lines.stroke(e.fout() * 5f);
                    Lines.circle(e.x, e.y, e.fin() * 300);
                    Lines.stroke(e.fout() * 3f);
                    Lines.circle(e.x, e.y, e.fin() * 180);
                    Lines.stroke(e.fout() * 3.2f);
                    Angles.randLenVectors(e.id, 30, 18 + 80 * e.fin(), (x, y) -> {
                        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 14 + 5);
                    });
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fout() * 120, heatColor, 0.7f);
                });

                recoil = 18f;
                
                health = 13000;
                shootCone = 5f;
                maxAmmo = 80;
                consumePowerCond(800f, TurretBuild::isActive);
                reload = 90f;

                ammo(Items.plastanium, JBBullets.burst);
                
                
                
                
                
                
                
                

                requirements(Category.turret, BuildVisibility.shown,
                        with(JBItems.cryostal, 300, JBItems.surgeAlloy, 425, JBItems.plastanium, 300));

            }
        };

    }
}
