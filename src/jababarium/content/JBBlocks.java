package jababarium.content;

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

public class JBBlocks {

    public static Block manualArtillery, cryostalConveyor, cryostalRouter, cryostalJunction, cryostalBridge,
            fluxReactor, helix, selfhealingConduit, singularityNeedle, selfhealingJunction, selfhealingRouter,
            entropyChain, cryostalDrill, selfhealingliquidBridge, ionizer, solarApex, chronos, antiMatterWarper, ignis,
            hastae,
            adamantiumSynthesizer, overlord, transgression, apex, avangard, omega, nyx, abbys, entropy, nokko, cascade,
            nexus, reaper, tempest, basicUnitPrinter, advancedUnitPrinter, gammaReaper, cryostalUnloader;

    public static void load() {

        manualArtillery = new BombLauncher("tochka-u") {
            {
                size = 3;
                storage = 4;

                requirements(
                        Category.defense,
                        with(
                                Items.copper, 300,
                                Items.lead, 200,
                                Items.silicon, 150,
                                Items.surgeAlloy, 100));

                bullet = new BasicBulletType(0f, 0f) {
                    {
                        lifetime = 1f;
                        width = height = 0f;
                        collides = collidesAir = collidesGround = false;
                        despawnHit = true;
                        keepVelocity = false;

                        backColor = hitColor = lightColor = lightningColor = JBColor.thurmixRed;
                        frontColor = JBColor.thurmixRedLight;

                        splashDamage = 1400f;
                        splashDamageRadius = 200f;

                        hitShake = despawnShake = 30f;
                        hitSound = Sounds.explosionAfflict;
                        hitSoundVolume = 2f;

                        lightning = 5;
                        lightningLength = 14;
                        lightningLengthRand = 20;
                        lightningDamage = 300f;
                        lightningCone = 360f;

                        shootEffect = smokeEffect = Fx.none;

                        hitEffect = despawnEffect = new Effect(100f, 600f, e -> {
                            
                            e.scaled(12f, s -> {
                                Draw.color(Color.white);
                                Fill.circle(e.x, e.y, s.fout(Interp.pow5Out) * 120f);
                            });

                            
                            e.scaled(55f, s -> {
                                Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, s.fin());
                                Lines.stroke(5f * s.fout(Interp.pow3Out));
                                Lines.circle(e.x, e.y, s.fin(Interp.pow2Out) * 240f);
                            });

                            e.scaled(70f, s -> {
                                Draw.color(JBColor.thurmixRed);
                                Lines.stroke(3f * s.fout());
                                Lines.circle(e.x, e.y, s.fin(Interp.pow3Out) * 170f);
                            });

                            e.scaled(40f, s -> {
                                Draw.color(Color.white, JBColor.thurmixRedLight, s.fin());
                                Lines.stroke(2f * s.fout());
                                Lines.circle(e.x, e.y, s.fin(Interp.pow4Out) * 100f);
                            });

                            
                            e.scaled(45f, s -> {
                                Draw.color(Color.white, JBColor.thurmixRed, s.fin());
                                Fill.circle(e.x, e.y, s.fout(Interp.pow4Out) * 80f);
                            });

                            
                            Draw.color(JBColor.thurmixRed, Color.white, e.fout() * 0.4f);
                            Angles.randLenVectors(e.id, 20, 30f + 220f * e.finpow(), (x, y) -> {
                                float ang = Mathf.angle(x, y);
                                Lines.stroke(e.fout() * 3.5f);
                                Lines.lineAngle(e.x + x, e.y + y, ang, e.fslope() * 32f + 8f);
                            });

                            
                            Draw.color(JBColor.thurmixRedLight, JBColor.thurmixRed, e.fin());
                            Angles.randLenVectors(e.id + 2, 14, 20f + 160f * e.finpow(), (x, y) -> {
                                Fill.square(e.x + x, e.y + y, e.fout() * 6f, 45f);
                                Drawf.light(e.x + x, e.y + y, e.fout() * 16f, JBColor.thurmixRed, 0.5f);
                            });

                            
                            Draw.color(Color.gray, Color.darkGray, e.fin());
                            Draw.alpha(0.7f * e.fout());
                            Angles.randLenVectors(e.id + 1, 14, 20f + 130f * e.finpow(), (x, y) ->
                                    Fill.circle(e.x + x, e.y + y, e.fout() * 24f)
                            );

                            Drawf.light(e.x, e.y, e.fout() * 420f, JBColor.thurmixRed, 0.99f);
                        });
                    }
                };

                reloadTime = 300f;

                consumePowerCond(26f, BombLauncherBuild::isCharging);
                consumeItem(Items.surgeAlloy, 2);
                itemCapacity = 16;
                health = 1200;
            }
        };

        cryostalBridge = new ItemBridge("cryostal-item-bridge") {
            {
                requirements(Category.distribution, with(
                        JBItems.cryostal, 3,
                        JBItems.adamantium, 3));
                health = 15;
                range = 8;
                canOverdrive = true;
                itemCapacity = 14;
                transportTime = 5f;
            }
        };

        cryostalJunction = new Junction("cryostal-junction") {
            {
                requirements(Category.distribution, with(
                        JBItems.cryostal, 2,
                        JBItems.adamantium, 3));
                health = 10;
                itemCapacity = 20;
            }
        };

        cryostalConveyor = new Conveyor("cryostal-conveyor") {
            {
                requirements(Category.distribution, with(
                        JBItems.cryostal, 1,
                        JBItems.adamantium, 1));

                speed = 0.1f;
                displayedSpeed = 16;
                itemCapacity = 14;
                health = 10;
                canOverdrive = true;
                junctionReplacement = cryostalJunction;
                bridgeReplacement = cryostalBridge;
            }
        };

        cryostalRouter = new Router("cryostal-router") {
            {
                requirements(Category.distribution, with(
                        JBItems.cryostal, 2,
                        JBItems.adamantium, 2));
                health = 15;
                canOverdrive = false;
                speed = 0f;
                itemCapacity = 20;
            }
        };

        cryostalUnloader = new Unloader("cryostal-unloader") {
            {
                requirements(Category.distribution, with(
                        JBItems.cryostal, 20,
                        JBItems.adamantium, 15,
                        Items.surgeAlloy, 10));
                health = 60;
                speed = 0.33f;
                canOverdrive = true;
            }
        };

        fluxReactor = new FluxReactor("flux-reactor") {
            {
                requirements(Category.power, with(
                        JBItems.amalgam, 3000,
                        JBItems.sergium, 3500,
                        JBItems.pulsarite, 3000));
                size = 9;
                coolant = JBLiquids.argon;
                ambientSound = JBSounds.fluxReactorWorking;

                consumeLiquid(JBLiquids.argon, 2f);
                consumeItems(with(JBItems.sergium, 2, JBItems.pulsarite, 3, JBItems.adamantium, 4));

            }
        };

        selfhealingliquidBridge = new SelfHealingLiquidBlocks.SelfHealingLiquidBridge("self-healing-liquid-bridge") {
            {
                requirements(Category.liquid, ItemStack.with(
                        JBItems.cryostal, 5,
                        JBItems.adamantium, 5,
                        JBItems.metaglass, 5));
            }
        };

        selfhealingRouter = new SelfHealingLiquidBlocks.SelfHealingRouter("self-healing-router") {
            {
                requirements(Category.liquid, ItemStack.with(
                        JBItems.cryostal, 5,
                        JBItems.metaglass, 10,
                        JBItems.adamantium, 7));
            }
        };

        selfhealingJunction = new SelfHealingLiquidBlocks.SelfHealingJunction("self-healing-junction") {
            {
                requirements(Category.liquid, ItemStack.with(
                        JBItems.cryostal, 5,
                        JBItems.adamantium, 6,
                        JBItems.metaglass, 9));
                health = 30;
            }
        };

        selfhealingConduit = new SelfHealingLiquidBlocks.SelfHealingConduit("self-healing-conduit") {
            {
                requirements(Category.liquid, ItemStack.with(
                        JBItems.cryostal, 4,
                        JBItems.adamantium, 5,
                        JBItems.metaglass, 7));
                health = 30;
                junctionReplacement = selfhealingJunction;
                bridgeReplacement = selfhealingliquidBridge;
            }
        };

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

        reaper = new ItemTurret("reaper") {
            {
                requirements(Category.turret, with(
                        JBItems.feronium, 800,
                        Items.graphite, 400,
                        Items.titanium, 500,
                        Items.surgeAlloy, 300));

                size = 5;
                health = 12400;
                armor = 6;

                range = 480f;
                reload = 60f;
                recoil = 5f;
                shake = 4f;

                shootSound = Sounds.explosionArtillery;

                rotateSpeed = 3f;
                cooldownTime = 50f;

                targetAir = true;
                targetGround = true;

                squareSprite = false;

                
                Color reaperRed = Color.valueOf("ff3838");
                Color darkRed = Color.valueOf("d62828");
                Color gunMetal = Color.valueOf("4a4a4a");

                heatColor = reaperRed;

                drawer = new DrawTurret("") {
                    @Override
                    public void draw(Building build) {
                        super.draw(build);

                        
                        if (build instanceof ItemTurretBuild) {
                            ItemTurretBuild turret = (ItemTurretBuild) build;

                            
                            float warmup = turret.heat;

                            if (warmup > 0.01f) {
                                Draw.z(Layer.turret + 0.1f);
                                Draw.color(reaperRed, warmup * 0.6f);
                                Draw.blend(Blending.additive);

                                
                                float pulse = 1f + Mathf.absin(Time.time, 4f, 0.3f);
                                Fill.circle(build.x, build.y, 8f * warmup * pulse);

                                
                                Lines.stroke(2f * warmup);
                                for (int i = 0; i < 6; i++) {
                                    float angle = turret.rotation + i * 60f;
                                    float len = 14f * warmup * pulse;

                                    Lines.lineAngle(
                                            build.x, build.y,
                                            angle,
                                            len);
                                }

                                Draw.blend();
                                Draw.reset();
                            }
                        }
                    }
                };

                shoot = new ShootBarrel() {
                    {
                        barrels = new float[] {
                                -5f, 0f, 0f,
                                0f, 0f, 0f,
                                5f, 0f, 0f
                        };
                        shots = 3;
                        shotDelay = 3f;
                    }
                };

                ammo(
                        Items.blastCompound, new BasicBulletType(4f, 650f) {
                            {
                                sprite = "missile-large";
                                width = 9f;
                                height = 13f;
                                lifetime = 120f;

                                frontColor = Color.white;
                                backColor = reaperRed;
                                trailColor = darkRed;

                                trailLength = 8;
                                trailWidth = 2f;

                                
                                splashDamage = 65f;
                                splashDamageRadius = 35f;

                                status = StatusEffects.blasted;
                                statusDuration = 60f;

                                homingPower = 0.04f;
                                homingRange = 80f;

                                hitEffect = new MultiEffect(
                                        new ParticleEffect() {
                                            {
                                                particles = 15;
                                                length = 45f;
                                                lifetime = 30f;
                                                sizeFrom = 6f;
                                                sizeTo = 0f;
                                                colorFrom = reaperRed;
                                                colorTo = darkRed.cpy().a(0f);
                                                cone = 360f;
                                            }
                                        },
                                        new ParticleEffect() {
                                            {
                                                particles = 20;
                                                length = 35f;
                                                lifetime = 25f;
                                                sizeFrom = 4f;
                                                sizeTo = 0f;
                                                colorFrom = Color.white;
                                                colorTo = reaperRed.cpy().a(0f);
                                                cone = 360f;
                                            }
                                        });

                                despawnEffect = hitEffect;

                                shootEffect = new ParticleEffect() {
                                    {
                                        particles = 10;
                                        length = 25f;
                                        lifetime = 20f;
                                        sizeFrom = 5f;
                                        sizeTo = 0f;
                                        colorFrom = reaperRed;
                                        colorTo = darkRed.cpy().a(0f);
                                        cone = 15f;
                                    }
                                };

                                smokeEffect = new ParticleEffect() {
                                    {
                                        particles = 5;
                                        length = 20f;
                                        lifetime = 30f;
                                        sizeFrom = 4f;
                                        sizeTo = 0f;
                                        colorFrom = gunMetal.cpy().a(0.5f);
                                        colorTo = Color.clear;
                                        cone = 20f;
                                    }
                                };
                            }
                        },

                        
                        Items.thorium, new BasicBulletType(4.5f, 870f) {
                            {
                                sprite = "missile-large";
                                width = 10f;
                                height = 14f;
                                lifetime = 106f;

                                frontColor = Color.white;
                                backColor = Color.valueOf("f9a3c7");
                                trailColor = Color.valueOf("ea8878");

                                trailLength = 10;
                                trailWidth = 2.5f;

                                splashDamage = 85f;
                                splashDamageRadius = 45f;

                                status = StatusEffects.blasted;
                                statusDuration = 90f;

                                homingPower = 0.06f;
                                homingRange = 100f;

                                
                                fragBullets = 6;
                                fragRandomSpread = 60f;
                                fragSpread = 10f;

                                fragBullet = new BasicBulletType(3f, 15f) {
                                    {
                                        sprite = "bullet";
                                        width = 5f;
                                        height = 7f;
                                        lifetime = 20f;

                                        frontColor = Color.white;
                                        backColor = Color.valueOf("f9a3c7");

                                        splashDamage = 10f;
                                        splashDamageRadius = 20f;

                                        despawnEffect = new ParticleEffect() {
                                            {
                                                particles = 5;
                                                length = 15f;
                                                lifetime = 15f;
                                                sizeFrom = 3f;
                                                sizeTo = 0f;
                                                colorFrom = Color.valueOf("f9a3c7");
                                                colorTo = Color.valueOf("ea8878").cpy().a(0f);
                                            }
                                        };
                                    }
                                };

                                hitEffect = new MultiEffect(
                                        new ParticleEffect() {
                                            {
                                                particles = 20;
                                                length = 55f;
                                                lifetime = 35f;
                                                sizeFrom = 7f;
                                                sizeTo = 0f;
                                                colorFrom = Color.valueOf("f9a3c7");
                                                colorTo = Color.valueOf("ea8878").cpy().a(0f);
                                                cone = 360f;
                                                lightOpacity = 0.7f;
                                            }
                                        },
                                        new ParticleEffect() {
                                            {
                                                particles = 25;
                                                length = 40f;
                                                lifetime = 30f;
                                                sizeFrom = 5f;
                                                sizeTo = 0f;
                                                colorFrom = Color.white;
                                                colorTo = Color.valueOf("f9a3c7").cpy().a(0f);
                                                cone = 360f;
                                            }
                                        });

                                despawnEffect = hitEffect;

                                shootEffect = new ParticleEffect() {
                                    {
                                        particles = 12;
                                        length = 30f;
                                        lifetime = 25f;
                                        sizeFrom = 6f;
                                        sizeTo = 0f;
                                        colorFrom = Color.valueOf("f9a3c7");
                                        colorTo = Color.valueOf("ea8878").cpy().a(0f);
                                        cone = 15f;
                                    }
                                };
                            }
                        },

                        
                        Items.pyratite, new BasicBulletType(3.8f, 600f) {
                            {
                                sprite = "missile-large";
                                width = 9f;
                                height = 13f;
                                lifetime = 126f;

                                frontColor = Color.white;
                                backColor = Color.valueOf("ffaa5f");
                                trailColor = Color.valueOf("ff9850");

                                trailLength = 12;
                                trailWidth = 2.5f;

                                splashDamage = 55f;
                                splashDamageRadius = 40f;

                                status = StatusEffects.burning;
                                statusDuration = 180f;

                                makeFire = true;

                                homingPower = 0.03f;
                                homingRange = 70f;

                                hitEffect = new MultiEffect(
                                        new ParticleEffect() {
                                            {
                                                particles = 18;
                                                length = 50f;
                                                lifetime = 40f;
                                                sizeFrom = 6f;
                                                sizeTo = 0f;
                                                colorFrom = Color.valueOf("ffaa5f");
                                                colorTo = Color.valueOf("ff6838").cpy().a(0f);
                                                cone = 360f;
                                            }
                                        },
                                        new ParticleEffect() {
                                            {
                                                particles = 15;
                                                length = 35f;
                                                lifetime = 50f;
                                                sizeFrom = 5f;
                                                sizeTo = 0f;
                                                colorFrom = Color.valueOf("ffd37f");
                                                colorTo = Color.valueOf("ffaa5f").cpy().a(0f);
                                                cone = 360f;
                                                interp = Interp.circleOut;
                                            }
                                        });

                                despawnEffect = hitEffect;

                                shootEffect = new ParticleEffect() {
                                    {
                                        particles = 10;
                                        length = 25f;
                                        lifetime = 20f;
                                        sizeFrom = 5f;
                                        sizeTo = 0f;
                                        colorFrom = Color.valueOf("ffaa5f");
                                        colorTo = Color.valueOf("ff9850").cpy().a(0f);
                                        cone = 15f;
                                    }
                                };
                            }
                        });

                
                smokeEffect = new ParticleEffect() {
                    {
                        particles = 8;
                        length = 40f;
                        lifetime = 40f;
                        sizeFrom = 6f;
                        sizeTo = 0f;
                        colorFrom = gunMetal.cpy().a(0.4f);
                        colorTo = Color.clear;
                        cone = 360f;
                    }
                };
            }
        };

        nexus = new PowerTurret("nexus") {
            {
                requirements(Category.turret, with(
                        JBItems.feronium, 1800,
                        Items.silicon, 1400,
                        Items.thorium, 600,
                        Items.surgeAlloy, 400));

                size = 7;
                health = 23800;
                armor = 8;

                range = 640f;
                reload = 90f;
                recoil = 3f;
                shake = 2f;

                shootSound = Sounds.explosion;
                loopSoundVolume = 0.5f;

                rotateSpeed = 2.2f;
                cooldownTime = 80f;

                consumePower(35f);

                targetAir = true;
                targetGround = true;

                squareSprite = false;

                
                Color plasmaWhite = Color.valueOf("f0f8ff");
                Color plasmaCyan = Color.valueOf("8aecff");
                Color plasmaBlue = Color.valueOf("5ab4d9");
                Color darkBlue = Color.valueOf("3a698f");

                heatColor = plasmaCyan;

                
                drawer = new DrawTurret("") {
                    {
                        parts.add(
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                

                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                

                                
                                new HaloPart() {
                                    {
                                        tri = true;
                                        radius = 8f;
                                        radiusTo = 14f;
                                        haloRadius = 28f;
                                        haloRadiusTo = 32f;
                                        color = plasmaCyan;
                                        colorTo = plasmaBlue.cpy().a(0.6f);
                                        layer = 109f;
                                        progress = PartProgress.warmup;
                                        shapes = 4;
                                        shapeRotation = 45f;
                                        haloRotation = 0f;
                                        haloRotateSpeed = 2f;
                                        y = 0f;
                                    }
                                },

                                
                                new HaloPart() {
                                    {
                                        tri = true;
                                        radius = 12f;
                                        radiusTo = 16f;
                                        haloRadius = 36f;
                                        haloRadiusTo = 42f;
                                        color = plasmaBlue;
                                        colorTo = darkBlue.cpy().a(0.4f);
                                        layer = 108f;
                                        progress = PartProgress.warmup;
                                        shapes = 4;
                                        shapeRotation = 0f;
                                        haloRotation = 45f;
                                        haloRotateSpeed = -1.5f;
                                        y = 0f;
                                    }
                                });
                    }
                };

                
                shoot = new ShootPattern() {
                    {
                        shots = 4;
                        shotDelay = 3f;
                    }
                };

                
                shootType = new BasicBulletType(5f, 80f) {
                    {
                        sprite = "circle-bullet";
                        width = 14f;
                        height = 14f;
                        shrinkY = 0f;
                        shrinkX = 0f;

                        frontColor = plasmaWhite;
                        backColor = plasmaCyan;
                        trailColor = plasmaBlue;

                        lifetime = 128f; 

                        splashDamage = 40f;
                        splashDamageRadius = 45f;

                        trailLength = 16;
                        trailWidth = 3.5f;
                        trailEffect = new ParticleEffect() {
                            {
                                particles = 3;
                                length = 15f;
                                lifetime = 30f;
                                sizeFrom = 4f;
                                sizeTo = 0f;
                                colorFrom = plasmaCyan;
                                colorTo = plasmaBlue.cpy().a(0f);
                                cone = 25f;
                            }
                        };
                        trailInterval = 3f;

                        
                        hitEffect = new MultiEffect(
                                
                                new ParticleEffect() {
                                    {
                                        particles = 20;
                                        length = 60f;
                                        lifetime = 40f;
                                        sizeFrom = 8f;
                                        sizeTo = 0f;
                                        colorFrom = plasmaWhite;
                                        colorTo = plasmaCyan.cpy().a(0f);
                                        cone = 360f;
                                        lightOpacity = 0.7f;
                                    }
                                },
                                
                                new ParticleEffect() {
                                    {
                                        particles = 30;
                                        length = 50f;
                                        lifetime = 35f;
                                        sizeFrom = 6f;
                                        sizeTo = 0f;
                                        colorFrom = plasmaCyan;
                                        colorTo = plasmaBlue.cpy().a(0f);
                                        cone = 360f;
                                    }
                                });

                        despawnEffect = hitEffect;
                        shootEffect = new ParticleEffect() {
                            {
                                particles = 12;
                                length = 35f;
                                lifetime = 25f;
                                sizeFrom = 6f;
                                sizeTo = 0f;
                                colorFrom = plasmaWhite;
                                colorTo = plasmaCyan.cpy().a(0f);
                                cone = 20f;
                            }
                        };

                        
                        
                        hitSound = Sounds.shootLancer;
                    }

                    @Override
                    public void hit(Bullet b, float x, float y) {
                        super.hit(b, x, y);

                        
                        createLightningNexus(x, y, b.team);
                    }

                    
                    void createLightningNexus(float x, float y, mindustry.game.Team team) {
                        
                        new ParticleEffect() {
                            {
                                particles = 15;
                                length = 40f;
                                lifetime = 90f; 
                                sizeFrom = 5f;
                                sizeTo = 10f;
                                colorFrom = plasmaWhite.cpy().a(0.8f);
                                colorTo = plasmaCyan.cpy().a(0.3f);
                                cone = 360f;
                                interp = Interp.pow2Out;
                            }
                        }.at(x, y);

                        
                        int lightningCount = 5; 
                        float lightningInterval = 18f; 

                        for (int i = 0; i < lightningCount; i++) {
                            Time.run(i * lightningInterval, () -> {
                                if (!Mathf.chance(0.9f))
                                    return;

                                
                                for (int j = 0; j < 3; j++) {
                                    
                                    Unit target = Groups.unit.intersect(
                                            x - 80f, y - 80f, 160f, 160f)
                                            .min(u -> u.team != team && !u.dead && u.within(x, y, 80f),
                                                    u -> u.dst2(x, y));

                                    if (target != null) {
                                        
                                        Lightning.create(
                                                team,
                                                plasmaCyan,
                                                25f, 
                                                x, y,
                                                Angles.angle(x, y, target.x, target.y),
                                                8 
                                        );

                                        
                                        new ParticleEffect() {
                                            {
                                                particles = 5;
                                                length = 20f;
                                                lifetime = 15f;
                                                sizeFrom = 3f;
                                                sizeTo = 0f;
                                                colorFrom = plasmaWhite;
                                                colorTo = plasmaCyan.cpy().a(0f);
                                            }
                                        }.at(target.x, target.y);
                                    }
                                }

                                
                                new ParticleEffect() {
                                    {
                                        particles = 8;
                                        length = 25f;
                                        lifetime = 20f;
                                        sizeFrom = 4f;
                                        sizeTo = 0f;
                                        colorFrom = plasmaCyan;
                                        colorTo = plasmaBlue.cpy().a(0f);
                                        cone = 360f;
                                    }
                                }.at(x, y);

                                Sounds.shootLancer.at(x, y, 1f, 0.8f);
                            });
                        }
                    }
                };

                
                smokeEffect = new ParticleEffect() {
                    {
                        particles = 10;
                        length = 55f;
                        lifetime = 50f;
                        sizeFrom = 7f;
                        sizeTo = 0f;
                        colorFrom = plasmaCyan.cpy().a(0.3f);
                        colorTo = darkBlue.cpy().a(0f);
                        cone = 360f;
                    }
                };
            }
        };

        cascade = new ItemTurret("cascade") {
            {
                requirements(Category.turret, with(
                        Items.titanium, 2200,
                        Items.thorium, 1500,
                        JBItems.cryostal, 1800,
                        Items.plastanium, 800,
                        Items.surgeAlloy, 500));

                size = 8;
                health = 28200;
                armor = 15;

                range = 200f;
                reload = 60f; 
                
                recoil = 2f;
                shake = 2f;

                shootSound = JBSounds.missile;
                loopSound = JBSounds.missile;
                loopSoundVolume = 0.8f;

                rotateSpeed = 2.5f;
                cooldownTime = 150f;

                consumePower(22f);
                consumeLiquid(Liquids.cryofluid, 0.6f);

                targetAir = true;
                targetGround = true;

                squareSprite = false;

                
                Color crystalBlue = Color.valueOf("7dd7ff");
                Color frostCyan = Color.valueOf("6bddff");
                Color deepBlue = Color.valueOf("4a9fd8");
                Color iceWhite = Color.valueOf("d4f4ff");

                heatColor = crystalBlue;

                
                drawer = new DrawTurret("") {
                    {
                        parts.add(
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                

                                
                                
                                
                                
                                
                                
                                
                                
                                
                                

                                
                                
                                
                                
                                
                                

                                
                                new HaloPart() {
                                    {
                                        tri = true;
                                        radius = 10f;
                                        radiusTo = 18f;
                                        haloRadius = 32f;
                                        haloRadiusTo = 38f;
                                        color = frostCyan;
                                        colorTo = crystalBlue.cpy().a(0.6f);
                                        layer = 109f;
                                        progress = PartProgress.warmup;
                                        shapes = 6;
                                        shapeRotation = 0f;
                                        haloRotation = 30f;
                                        haloRotateSpeed = 1.2f;
                                        y = 0f;
                                    }
                                },

                                
                                new HaloPart() {
                                    {
                                        tri = true;
                                        radius = 14f;
                                        radiusTo = 20f;
                                        haloRadius = 44f;
                                        haloRadiusTo = 52f;
                                        color = deepBlue;
                                        colorTo = frostCyan.cpy().a(0.4f);
                                        layer = 108f;
                                        progress = PartProgress.warmup;
                                        shapes = 6;
                                        shapeRotation = 30f;
                                        haloRotation = 0f;
                                        haloRotateSpeed = -0.8f;
                                        y = 0f;
                                    }
                                });
                    }
                };

                
                shoot = new ShootSpread() {
                    {
                        shots = 8;
                        spread = 10f; 
                    }
                };

                
                ammo(
                        Items.titanium, new ContinuousLaserBulletType(1000f) {
                            {
                                length = 200f;
                                width = 3.5f;

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
                        });

                
                smokeEffect = new ParticleEffect() {
                    {
                        particles = 12;
                        length = 60f;
                        lifetime = 50f;
                        sizeFrom = 8f;
                        sizeTo = 0f;
                        colorFrom = frostCyan.cpy().a(0.4f);
                        colorTo = deepBlue.cpy().a(0f);
                        cone = 360f;
                    }
                };

                
                buildType = () -> new ItemTurretBuild() {

                    
                    float chillTimer = 0f;
                    float chillInterval = 20f;
                    float auraRadius = 120f;
                    int maxChillStacks = 10;

                    
                    boolean wasShootingLastFrame = false;
                    float detonationCheckTimer = 0f;

                    
                    ObjectMap<Unit, ChillData> chillMap = new ObjectMap<>();

                    
                    class ChillData {
                        int stacks = 0;
                        float timer = 0f;

                        ChillData(int stacks) {
                            this.stacks = stacks;
                            this.timer = 0f;
                        }
                    }

                    @Override
                    public void updateTile() {
                        super.updateTile();

                        
                        if (liquids.get(Liquids.cryofluid) > 0.01f && power.status > 0.01f) {
                            chillTimer += Time.delta;

                            if (chillTimer >= chillInterval) {
                                chillTimer = 0f;

                                
                                for (int i = 0; i < 4; i++) {
                                    float angle = Mathf.random(360f);
                                    float dist = Mathf.random(auraRadius * 0.8f, auraRadius);

                                    float px = x + Mathf.cosDeg(angle) * dist;
                                    float py = y + Mathf.sinDeg(angle) * dist;

                                    new ParticleEffect() {
                                        {
                                            particles = 1;
                                            length = 15f;
                                            lifetime = 80f;
                                            sizeFrom = 5f;
                                            sizeTo = 2f;
                                            colorFrom = Color.valueOf("d4f4ff").cpy().a(0.7f);
                                            colorTo = Color.valueOf("7dd7ff").cpy().a(0f);
                                            cone = 30f;
                                            interp = Interp.pow2Out;
                                        }
                                    }.at(px, py + 20f);
                                }

                                
                                Groups.unit.intersect(
                                        x - auraRadius, y - auraRadius,
                                        auraRadius * 2, auraRadius * 2,
                                        unit -> {
                                            if (unit.team != team && unit.within(x, y, auraRadius) && !unit.dead) {

                                                
                                                ChillData data = chillMap.get(unit);
                                                if (data == null) {
                                                    data = new ChillData(0);
                                                    chillMap.put(unit, data);
                                                }

                                                
                                                data.stacks = Math.min(data.stacks + 1, maxChillStacks);
                                                data.timer = 0f;

                                                
                                                if (data.stacks >= maxChillStacks) {
                                                    
                                                    unit.apply(StatusEffects.freezing, 120f);
                                                    unit.apply(StatusEffects.slow, 120f);

                                                    
                                                    if (Mathf.chance(0.3f)) {
                                                        new ParticleEffect() {
                                                            {
                                                                particles = 8;
                                                                length = 25f;
                                                                lifetime = 40f;
                                                                sizeFrom = 5f;
                                                                sizeTo = 0f;
                                                                colorFrom = Color.valueOf("d4f4ff");
                                                                colorTo = Color.valueOf("6bddff").cpy().a(0f);
                                                                cone = 360f;
                                                            }
                                                        }.at(unit.x, unit.y);
                                                    }
                                                } else {
                                                    
                                                    float freezeDuration = 30f + (data.stacks * 10f);
                                                    unit.apply(StatusEffects.freezing, freezeDuration);
                                                }

                                                
                                                if (Mathf.chance(0.2f)) {
                                                    new ParticleEffect() {
                                                        {
                                                            particles = 2;
                                                            length = 10f;
                                                            lifetime = 20f;
                                                            sizeFrom = 3f;
                                                            sizeTo = 0f;
                                                            colorFrom = Color.valueOf("7dd7ff");
                                                            colorTo = Color.valueOf("4a9fd8").cpy().a(0f);
                                                        }
                                                    }.at(unit.x, unit.y);
                                                }
                                            }
                                        });
                            }

                            
                            chillMap.each((unit, data) -> {
                                data.timer += Time.delta;

                                
                                if (!unit.within(x, y, auraRadius) || unit.dead || data.timer > 180f) {
                                    chillMap.remove(unit);
                                }
                                
                                else if (!unit.within(x, y, auraRadius) && data.timer > 60f) {
                                    data.stacks = Math.max(0, data.stacks - 1);
                                    data.timer = 0f;
                                }
                            });
                        }

                        
                        
                        boolean isShootingNow = isShooting();

                        if (wasShootingLastFrame && !isShootingNow) {
                            
                            detonateFrozenEnemies();
                        }

                        wasShootingLastFrame = isShootingNow;
                    }

                    
                    void detonateFrozenEnemies() {
                        Seq<Unit> toDetonate = new Seq<>();

                        
                        chillMap.each((unit, data) -> {
                            if (data.stacks >= maxChillStacks &&
                                    unit.within(x, y, range) &&
                                    !unit.dead) {
                                toDetonate.add(unit);
                            }
                        });

                        
                        for (Unit unit : toDetonate) {
                            float ux = unit.x;
                            float uy = unit.y;

                            
                            new MultiEffect(
                                    
                                    new ParticleEffect() {
                                        {
                                            particles = 30;
                                            length = 80f;
                                            lifetime = 50f;
                                            sizeFrom = 10f;
                                            sizeTo = 0f;
                                            colorFrom = Color.valueOf("d4f4ff");
                                            colorTo = Color.valueOf("7dd7ff").cpy().a(0f);
                                            cone = 360f;
                                            lightOpacity = 0.8f;
                                        }
                                    },
                                    
                                    new ParticleEffect() {
                                        {
                                            particles = 20;
                                            length = 60f;
                                            lifetime = 40f;
                                            sizeFrom = 6f;
                                            sizeTo = 0f;
                                            colorFrom = Color.valueOf("6bddff");
                                            colorTo = Color.valueOf("4a9fd8").cpy().a(0f);
                                            cone = 360f;
                                            interp = Interp.pow2Out;
                                        }
                                    },
                                    
                                    new ParticleEffect() {
                                        {
                                            particles = 40;
                                            length = 70f;
                                            lifetime = 35f;
                                            sizeFrom = 8f;
                                            sizeTo = 0f;
                                            colorFrom = Color.valueOf("d4f4ff");
                                            colorTo = Color.valueOf("6bddff").cpy().a(0f);
                                            cone = 360f;
                                        }
                                    }).at(ux, uy);

                            
                            JBSounds.missile.at(ux, uy, 1f);

                            
                            float detonationRadius = 80f;
                            float detonationDamage = 300f;

                            Damage.damage(
                                    unit.team,
                                    ux, uy,
                                    detonationRadius,
                                    detonationDamage,
                                    true,
                                    false);

                            
                            Groups.unit.intersect(
                                    ux - detonationRadius, uy - detonationRadius,
                                    detonationRadius * 2, detonationRadius * 2,
                                    nearby -> {
                                        if (nearby != unit &&
                                                nearby.team != team &&
                                                nearby.within(ux, uy, detonationRadius) &&
                                                !nearby.dead) {

                                            
                                            ChillData nearbyData = chillMap.get(nearby);
                                            if (nearbyData == null) {
                                                nearbyData = new ChillData(3);
                                                chillMap.put(nearby, nearbyData);
                                            } else {
                                                nearbyData.stacks = Math.min(nearbyData.stacks + 3, maxChillStacks);
                                                nearbyData.timer = 0f;
                                            }

                                            nearby.apply(StatusEffects.freezing, 120f);

                                            
                                            new ParticleEffect() {
                                                {
                                                    particles = 6;
                                                    length = 20f;
                                                    lifetime = 30f;
                                                    sizeFrom = 4f;
                                                    sizeTo = 0f;
                                                    colorFrom = Color.valueOf("7dd7ff");
                                                    colorTo = Color.valueOf("4a9fd8").cpy().a(0f);
                                                    cone = 360f;
                                                }
                                            }.at(nearby.x, nearby.y);
                                        }
                                    });

                            
                            chillMap.remove(unit);
                        }
                    }

                    @Override
                    public void draw() {
                        super.draw();

                        
                        if (liquids.get(Liquids.cryofluid) > 0.01f && power.status > 0.01f) {
                            Draw.z(Layer.effect);
                            Draw.color(Color.valueOf("7dd7ff"), 0.15f * power.status);
                            Fill.circle(x, y, auraRadius * 0.5f);
                            Draw.color();
                        }
                    }
                };
            }
        };

        tempest = new PowerTurret("tempest") {
            {
                requirements(Category.turret, with(
                        JBItems.feronium, 1800,
                        Items.silicon, 1600,
                        Items.thorium, 1200,
                        Items.surgeAlloy, 800,
                        JBItems.adamantium, 600));

                size = 8;
                health = 30800;
                armor = 12;

                range = 720f;
                reload = 180f;
                recoil = 6f;
                shake = 8f;

                shootSound = Sounds.shootLancer;

                rotateSpeed = 1.8f;
                cooldownTime = 120f;

                consumePower(60f); 

                targetAir = true;
                targetGround = true;

                squareSprite = false;

                
                Color lightningWhite = Color.valueOf("ffffff");
                Color lightningPurple = Color.valueOf("bf92f9");
                Color deepPurple = Color.valueOf("8b6bb0");
                Color darkPurple = Color.valueOf("665c84");

                heatColor = lightningPurple;

                
                drawer = new DrawTurret("");

                
                shootType = new BasicBulletType(6f, 0f) {
                    {
                        sprite = "circle-bullet";
                        width = 18f;
                        height = 18f;
                        shrinkY = 0f;
                        shrinkX = 0f;

                        frontColor = lightningWhite;
                        backColor = lightningPurple;
                        trailColor = deepPurple;

                        lifetime = 120f; 

                        
                        damage = 0f;
                        splashDamage = 0f;

                        trailLength = 20;
                        trailWidth = 4f;
                        trailInterval = 2f;

                        
                        trailEffect = new ParticleEffect() {
                            {
                                particles = 4;
                                length = 20f;
                                lifetime = 40f;
                                sizeFrom = 6f;
                                sizeTo = 0f;
                                colorFrom = lightningPurple;
                                colorTo = deepPurple.cpy().a(0f);
                                cone = 30f;
                            }
                        };

                        
                        shootEffect = new MultiEffect(
                                new ParticleEffect() {
                                    {
                                        particles = 20;
                                        length = 50f;
                                        lifetime = 30f;
                                        sizeFrom = 8f;
                                        sizeTo = 0f;
                                        colorFrom = lightningWhite;
                                        colorTo = lightningPurple.cpy().a(0f);
                                        cone = 25f;
                                        lightOpacity = 0.8f;
                                    }
                                },
                                new ParticleEffect() {
                                    {
                                        particles = 15;
                                        length = 40f;
                                        lifetime = 35f;
                                        sizeFrom = 6f;
                                        sizeTo = 0f;
                                        colorFrom = lightningPurple;
                                        colorTo = deepPurple.cpy().a(0f);
                                        cone = 25f;
                                    }
                                });

                        hitSound = Sounds.explosionNavanax;
                    }

                    @Override
                    public void hit(Bullet b, float x, float y) {
                        super.hit(b, x, y);

                        
                        createLightningNova(x, y, b.team);
                    }

                    @Override
                    public void despawned(Bullet b) {
                        super.despawned(b);

                        
                        if (b.time >= b.lifetime - 1f) {
                            createLightningNova(b.x, b.y, b.team);
                        }
                    }

                    
                    void createLightningNova(float x, float y, mindustry.game.Team team) {

                        
                        int mainBolts = 24; 
                        int boltLength = 16; 
                        float boltDamage = 80f; 
                        float novaRadius = 280f; 

                        
                        new MultiEffect(
                                
                                new ParticleEffect() {
                                    {
                                        particles = 1;
                                        length = 0f;
                                        lifetime = 30f;
                                        sizeFrom = 40f;
                                        sizeTo = 0f;
                                        colorFrom = lightningWhite;
                                        colorTo = lightningWhite.cpy().a(0f);
                                        region = "circle";
                                    }
                                },
                                
                                new ParticleEffect() {
                                    {
                                        particles = 50;
                                        length = 80f;
                                        lifetime = 40f;
                                        sizeFrom = 10f;
                                        sizeTo = 0f;
                                        colorFrom = lightningPurple;
                                        colorTo = deepPurple.cpy().a(0f);
                                        cone = 360f;
                                        lightOpacity = 0.9f;
                                    }
                                },
                                
                                new ParticleEffect() {
                                    {
                                        particles = 80;
                                        length = 120f;
                                        lifetime = 50f;
                                        sizeFrom = 8f;
                                        sizeTo = 0f;
                                        colorFrom = lightningWhite;
                                        colorTo = lightningPurple.cpy().a(0f);
                                        cone = 360f;
                                    }
                                }).at(x, y);

                        
                        Sounds.explosionNavanax.at(x, y, 1f);

                        
                        for (int i = 0; i < mainBolts; i++) {
                            float angle = (360f / mainBolts) * i;

                            
                            Lightning.create(
                                    team,
                                    lightningPurple,
                                    boltDamage,
                                    x, y,
                                    angle,
                                    boltLength);

                            
                            if (i % 2 == 0) {
                                float midAngle = angle + (360f / mainBolts) / 2f;
                                Lightning.create(
                                        team,
                                        deepPurple,
                                        boltDamage * 0.6f,
                                        x, y,
                                        midAngle,
                                        boltLength - 4);
                            }
                        }

                        
                        for (int i = 0; i < 12; i++) {
                            Time.run(Mathf.random(5f, 15f), () -> {
                                float randomAngle = Mathf.random(360f);
                                Lightning.create(
                                        team,
                                        lightningPurple.cpy().a(0.6f),
                                        boltDamage * 0.4f,
                                        x, y,
                                        randomAngle,
                                        boltLength - 6);
                            });
                        }

                        
                        Damage.damage(
                                team,
                                x, y,
                                100f, 
                                200f 
                        );

                        
                        for (int ring = 0; ring < 3; ring++) {
                            final int currentRing = ring;
                            Time.run(ring * 8f, () -> {
                                float ringRadius = 60f + currentRing * 40f;

                                new ParticleEffect() {
                                    {
                                        particles = 30;
                                        length = ringRadius;
                                        lifetime = 35f;
                                        sizeFrom = 6f;
                                        sizeTo = 0f;
                                        colorFrom = lightningPurple.cpy().a(0.7f);
                                        colorTo = deepPurple.cpy().a(0f);
                                        cone = 360f;
                                    }
                                }.at(x, y);

                                Sounds.shootLancer.at(x, y, 1f, 0.8f);
                            });
                        }

                        
                        Effect.shake(8f, 8f, x, y);
                    }
                };

                
                smokeEffect = new ParticleEffect() {
                    {
                        particles = 12;
                        length = 60f;
                        lifetime = 50f;
                        sizeFrom = 8f;
                        sizeTo = 0f;
                        colorFrom = deepPurple.cpy().a(0.4f);
                        colorTo = darkPurple.cpy().a(0f);
                        cone = 360f;
                    }
                };
            }
        };

        singularityNeedle = new ItemTurret("singularity-needle") {
            {
                requirements(Category.turret, with(
                        JBItems.cryostal, 200,
                        JBItems.surgeAlloy, 300,
                        JBItems.chronite, 200,
                        Items.plastanium, 300));

                size = 6;
                health = 24000;
                range = 760f;
                reload = 180f;
                recoil = 2f;
                shake = 2f;

                ammo(
                        Items.phaseFabric, JBBullets.singularityPoint);

                consumePower(12f);
                consumeLiquid(Liquids.cryofluid, 0.5f);
                heatColor = Color.valueOf("#FFC900");

            }
        };

        cryostalDrill = new Drill("cryostal-drill") {
            {

                requirements(Category.production, ItemStack.with(
                        JBItems.feronium, 200,
                        JBItems.cryostal, 150,
                        JBItems.plastanium, 300));

                size = 4;
                health = 200;
                drillTime = 30f;
                itemCapacity = 30;
                heatColor = Color.valueOf("bf92f9");
                tier = 8;

                consumePower(6f);

                updateEffect = JBFx.polyTrail(Color.valueOf("#54D1CC"), Color.valueOf("#1479A8"), 5f, 60f);

                updateEffectChance = 0.06f;
                drawMineItem = true;
                ambientSound = Sounds.loopDrill;
                ambientSoundVolume = 0.05f;

            }
        };

        overlord = new PowerTurret("overlord") {
            {
                requirements(Category.turret, with(
                        Items.surgeAlloy, 600,
                        Items.plastanium, 450,
                        Items.phaseFabric, 350,
                        Items.silicon, 800,
                        JBItems.adamantium, 400));

                size = 5;
                health = 38000;
                range = 500f;
                reload = 420f;
                recoil = 8f;
                recoilTime = 180f;
                shake = 5f;

                consumePower(30f);
                consumeCoolant(1f);

                heatColor = Color.valueOf("bf92f9");

                shootSound = JBSounds.blastShockwave;
                chargeSound = JBSounds.blastShockwave;

                smokeEffect = new Effect(50, e -> {
                    Draw.color(heatColor);
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fout() * 120, heatColor, 0.7f);
                });

                ammoUseEffect = new Effect(120f, e -> {
                    Draw.color(heatColor);
                    Lines.stroke(Mathf.curve(e.fin(), 0, 1) * 4f);
                    Lines.circle(e.x, e.y, e.fout() * 100f);

                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fin() * 150f, heatColor, 0.8f);

                    Angles.randLenVectors(e.id, 40, 20f + e.fin() * 100f, (x, y) -> {
                        Fill.circle(e.x + x, e.y + y, e.fout() * 3f + 1f);
                    });
                });

                shootEffect = new Effect(60f, e -> {
                    Draw.color(heatColor);
                    Lines.stroke(e.fout() * 5f);
                    Lines.circle(e.x, e.y, e.fin() * 130f);
                    Drawf.light(e.x, e.y, e.fout() * 200f, heatColor, 0.9f);
                    Fx.massiveExplosion.at(e.x, e.y, heatColor);
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

                shootType = new BasicBulletType(3.5f, 600f) {
                    {
                        width = 45f;
                        height = 45f;
                        shrinkX = 0f;
                        shrinkY = 0f;
                        spin = 1f;
                        lifetime = 160f;

                        pierce = true;
                        pierceCap = 3;
                        absorbable = false;

                        frontColor = Color.white;
                        backColor = Color.valueOf("bf92f9");
                        trailColor = Color.valueOf("995dff");
                        trailWidth = 18f;
                        trailLength = 80;

                        hitEffect = Fx.coreExplosion;
                        despawnEffect = Fx.coreExplosion;
                        hitSound = JBSounds.blastShockwave;

                        lightningColor = Color.valueOf("bf92f9");
                        lightningDamage = 90f;
                        lightningLength = 25;
                        lightningLengthRand = 15;

                        splashDamage = 5000f;
                        splashDamageRadius = 150f;
                    }

                    @Override
                    public void update(Bullet b) {
                        super.update(b);

                        if (b.timer.get(1, 5f)) {
                            Fx.sparkShoot.at(b.x, b.y, b.rotation() + 90, lightningColor);
                            Lightning.create(b.team, lightningColor, lightningDamage, b.x, b.y, b.rotation() + 90,
                                    lightningLength + Mathf.range(lightningLengthRand));

                            Fx.sparkShoot.at(b.x, b.y, b.rotation() - 90, lightningColor);
                            Lightning.create(b.team, lightningColor, lightningDamage, b.x, b.y, b.rotation() - 90,
                                    lightningLength + Mathf.range(lightningLengthRand));
                        }
                    }
                };
            }
        };

        transgression = new PowerTurret("transgression") {
            {
                requirements(Category.turret, with(
                        Items.silicon, 1200,
                        Items.plastanium, 500,
                        Items.phaseFabric, 400,
                        JBItems.adamantium, 250));

                size = 5;
                health = 35000;
                range = 400f;

                consumePower(25f);
                consumeLiquid(JBLiquids.nectron, 0.4f);

                shootSound = JBSounds.beam;
                loopSound = JBSounds.bioLoop;
                loopSoundVolume = 1.5f;

                shootType = JBBullets.transgression;

                heatColor = Color.valueOf("84f5d9");

                smokeEffect = new Effect(50, e -> {
                    Draw.color(heatColor);
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fout() * 120, heatColor, 0.7f);
                });

                buildType = () -> new PowerTurretBuild() {
                    public float internalHeat = 0f;
                    public boolean isLocked = false;

                    @Override
                    public void updateTile() {
                        if (isLocked) {
                            internalHeat = Mathf.approach(internalHeat, 0f, 0.003f * Time.delta);
                            if (internalHeat <= 0.01f) {
                                isLocked = false;
                            }
                        }

                        if (isShooting() && !isLocked && canConsume()) {
                            internalHeat = Mathf.approach(internalHeat, 1.2f, 0.005f * Time.delta);
                            if (internalHeat >= 1f) {
                                isLocked = true;
                            }
                        } else if (!isShooting() || isLocked) {
                            internalHeat = Mathf.approach(internalHeat, 0f, 0.002f * Time.delta);
                        }

                        super.updateTile();
                    }

                    @Override
                    public boolean canConsume() {
                        return super.canConsume() && !isLocked;
                    }

                    @Override
                    public boolean shouldConsume() {
                        return isShooting() && !isLocked;
                    }
                };
            }
        };

        nokko = new ItemTurret("nokko") {
            {
                requirements(Category.turret, with(
                        Items.lead, 2400,
                        JBItems.adamantium, 800,
                        Items.thorium, 800,
                        JBItems.feronium, 600));

                size = 9;
                health = 38500;
                armor = 12;

                range = 300f; 
                reload = 120f; 
                recoil = 6f; 
                shake = 5f; 

                shootSound = JBSounds.missile2;
                loopSound = JBSounds.bioLoop;
                loopSoundVolume = 0.6f;

                rotateSpeed = 1.8f;
                cooldownTime = 120f;

                consumePower(25f); 
                consumeLiquid(Liquids.water, 0.5f); 

                targetAir = true;
                targetGround = true;

                squareSprite = false;

                heatColor = JBColor.sporePink;

                
                drawer = new DrawTurret("") {
                    {
                        parts.add(
                                
                                
                                
                                
                                
                                
                                
                                
                                

                                
                                
                                
                                
                                
                                
                                
                                
                                
                                
                                

                                new HaloPart() {
                                    {
                                        tri = true;
                                        radius = 12f; 
                                        radiusTo = 24f; 
                                        haloRadius = 36f; 
                                        haloRadiusTo = 48f; 
                                        color = JBColor.sporePink;
                                        colorTo = JBColor.sporeDark.cpy().a(0.5f);
                                        layer = 109f;
                                        progress = PartProgress.warmup;
                                        shapes = 6; 
                                        shapeRotation = 30f; 
                                        y = 0f;
                                    }
                                });
                    }
                };

                ammo(
                        Items.sporePod, new BulletType(0f, 0f) {
                            {
                                lifetime = 1f;

                                splashDamage = 8000f;
                                splashDamageRadius = 300f;

                                status = StatusEffects.corroded;
                                statusDuration = 1200f;

                                
                                despawnEffect = new MultiEffect(
                                        new ParticleEffect() {
                                            {
                                                particles = 120;
                                                length = 300f;
                                                lifetime = 80f;
                                                sizeFrom = 12f;
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporePink;
                                                colorTo = JBColor.sporeDark.cpy().a(0.5f);
                                                cone = 360f;
                                                lightOpacity = 0.9f;
                                            }
                                        },
                                        new ParticleEffect() {
                                            {
                                                particles = 80;
                                                length = 250f;
                                                lifetime = 60f;
                                                sizeFrom = 16f;
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporeLight;
                                                colorTo = JBColor.sporePink.cpy().a(0f);
                                                cone = 360f;
                                            }
                                        },
                                        new ParticleEffect() {
                                            {
                                                particles = 60;
                                                length = 200f;
                                                lifetime = 90f;
                                                sizeFrom = 10f;
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporeMid;
                                                colorTo = JBColor.sporeLight.cpy().a(0f);
                                                cone = 360f;
                                                interp = Interp.circleOut;
                                            }
                                        });

                                
                                hitEffect = new MultiEffect(
                                        new ParticleEffect() {
                                            {
                                                particles = 100;
                                                length = 280f;
                                                lifetime = 60f;
                                                sizeFrom = 14f;
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporeLight;
                                                colorTo = JBColor.sporePink.cpy().a(0f);
                                                cone = 360f;
                                                interp = Interp.circleOut;
                                            }
                                        },
                                        new ParticleEffect() {
                                            {
                                                particles = 80;
                                                length = 320f;
                                                lifetime = 120f;
                                                sizeFrom = 16f;
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporeMid;
                                                colorTo = JBColor.sporeDark.cpy().a(0f);
                                                cone = 360f;
                                            }
                                        },
                                        new ParticleEffect() {
                                            {
                                                particles = 100;
                                                length = 260f;
                                                lifetime = 150f;
                                                sizeFrom = 12f;
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporeLight;
                                                colorTo = JBColor.sporePink.cpy().a(0f);
                                                cone = 360f;
                                                interp = Interp.circleOut;
                                            }
                                        });

                                
                                shootEffect = new MultiEffect(
                                        new ParticleEffect() {
                                            {
                                                particles = 80; 
                                                length = 140f; 
                                                lifetime = 45f; 
                                                sizeFrom = 14f; 
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporePink;
                                                colorTo = JBColor.sporeLight.cpy().a(0f);
                                                cone = 360f;
                                                lightOpacity = 0.8f;
                                            }
                                        },
                                        new ParticleEffect() {
                                            {
                                                particles = 40; 
                                                length = 120f; 
                                                lifetime = 60f; 
                                                sizeFrom = 14f; 
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporeMid;
                                                colorTo = JBColor.sporeDark.cpy().a(0.4f);
                                                cone = 360f;
                                            }
                                        });

                                
                                fragBullets = 24; 
                                fragRandomSpread = 180f;
                                fragSpread = 15f; 

                                fragBullet = new BasicBulletType(4f, 50f) { 
                                    {
                                        sprite = "circle-bullet";
                                        lifetime = 120f; 
                                        drag = 0.005f; 

                                        width = 14f; 
                                        height = 14f;
                                        shrinkY = 0.4f; 
                                        shrinkX = 0.4f;

                                        frontColor = JBColor.sporeMid;
                                        backColor = JBColor.sporeDark;

                                        splashDamage = 700f; 
                                        splashDamageRadius = 60f; 

                                        status = StatusEffects.corroded;
                                        statusDuration = 900f; 

                                        
                                        homingPower = 0.1f; 
                                        homingRange = 200f; 
                                        homingDelay = 3f; 

                                        
                                        pierce = true;
                                        pierceCap = 3; 
                                        pierceBuilding = true; 

                                        trailLength = 18; 
                                        trailWidth = 3f; 
                                        trailColor = JBColor.sporePink;
                                        trailInterval = 1f; 

                                        despawnEffect = new MultiEffect(
                                                new ParticleEffect() {
                                                    {
                                                        particles = 12; 
                                                        length = 45f; 
                                                        lifetime = 60f; 
                                                        sizeFrom = 8f; 
                                                        sizeTo = 0f;
                                                        colorFrom = JBColor.sporeMid;
                                                        colorTo = JBColor.sporeDark.cpy().a(0.4f);
                                                        cone = 360f;
                                                    }
                                                },
                                                new ParticleEffect() {
                                                    {
                                                        particles = 8;
                                                        length = 35f;
                                                        lifetime = 45f;
                                                        sizeFrom = 6f;
                                                        sizeTo = 0f;
                                                        colorFrom = JBColor.sporeLight;
                                                        colorTo = JBColor.sporePink.cpy().a(0f);
                                                        cone = 360f;
                                                    }
                                                });

                                        hitEffect = new ParticleEffect() {
                                            {
                                                particles = 10;
                                                length = 40f;
                                                lifetime = 35f;
                                                sizeFrom = 7f;
                                                sizeTo = 0f;
                                                colorFrom = JBColor.sporeMid;
                                                colorTo = JBColor.sporeDark.cpy().a(0.3f);
                                                cone = 360f;
                                            }
                                        };
                                    }
                                };
                            }
                        });

                
                shootEffect = new ParticleEffect() {
                    {
                        particles = 25; 
                        length = 90f; 
                        lifetime = 50f; 
                        sizeFrom = 12f; 
                        sizeTo = 0f;
                        colorFrom = JBColor.sporeLight;
                        colorTo = JBColor.sporePink;
                        cone = 30f; 
                    }
                };

                smokeEffect = new ParticleEffect() {
                    {
                        particles = 15; 
                        length = 70f; 
                        lifetime = 80f; 
                        sizeFrom = 14f; 
                        sizeTo = 0f;
                        colorFrom = JBColor.sporeMid.cpy().a(0.5f); 
                        colorTo = JBColor.sporeDark.cpy().a(0f);
                        cone = 360f;
                    }
                };

                
                buildType = () -> new ItemTurretBuild() {
                    float sporeTimer = 0f;
                    float sporeInterval = 35f; 
                    int sporesPerCycle = 5; 
                    float auraRadius = 160f; 
                    float sporeDamage = 1020f; 

                    @Override
                    public void updateTile() {
                        super.updateTile();

                        
                        if (liquids.get(Liquids.water) > 0.01f && power.status > 0.01f) {
                            sporeTimer += Time.delta;

                            if (sporeTimer >= sporeInterval) {
                                sporeTimer = 0f;

                                
                                for (int i = 0; i < sporesPerCycle; i++) {
                                    float angle = Mathf.random(360f);
                                    float dist = Mathf.random(auraRadius * 0.5f, auraRadius);

                                    float px = x + Mathf.cosDeg(angle) * dist;
                                    float py = y + Mathf.sinDeg(angle) * dist;

                                    
                                    new ParticleEffect() {
                                        {
                                            particles = 4; 
                                            length = 30f; 
                                            lifetime = 140f; 
                                            sizeFrom = 8f; 
                                            sizeTo = 3f; 
                                            colorFrom = JBColor.sporeLight.cpy().a(0.6f); 
                                            colorTo = JBColor.sporePink.cpy().a(0f);
                                            cone = 360f;
                                        }
                                    }.at(px, py);

                                    
                                    Groups.unit.intersect(
                                            px - auraRadius * 0.5f,
                                            py - auraRadius * 0.5f,
                                            auraRadius,
                                            auraRadius,
                                            unit -> {
                                                if (unit.team != team && unit.within(px, py, auraRadius * 0.5f)) {
                                                    unit.damagePierce(sporeDamage);
                                                    unit.apply(StatusEffects.sporeSlowed, 300f); 

                                                    
                                                    new ParticleEffect() {
                                                        {
                                                            particles = 6; 
                                                            length = 25f; 
                                                            lifetime = 40f; 
                                                            sizeFrom = 5f; 
                                                            sizeTo = 0f;
                                                            colorFrom = JBColor.sporeMid;
                                                            colorTo = JBColor.sporeDark.cpy().a(0.4f); 
                                                        }
                                                    }.at(unit.x, unit.y);
                                                }
                                            });
                                }
                            }
                        }
                    }
                };
            }
        };

        ionizer = new PowerTurret("ionizer") {
            {
                requirements(Category.turret, with(
                        JBItems.pulsarite, 200,
                        Items.silicon, 950,
                        Items.plastanium, 600,
                        JBItems.chronite, 400,
                        JBItems.cryostal, 800));

                health = 34600;
                size = 11;
                range = 600f;
                reload = 40f;
                recoil = 3f;
                shake = 2f;
                shootSound = JBSounds.shootGauss1;
                heatColor = Color.valueOf("72d4ff");

                consumePower(35f);
                

                shoot = new ShootBarrel() {
                    {
                        barrels = new float[] {
                                -25f, 4f, 0f,
                                25f, 4f, 0f
                        };
                        shots = 2;
                        shotDelay = 5f;
                    }
                };

                shootType = new BasicBulletType(16f, 1200f) {
                    {
                        width = 50;
                        height = 24f;
                        lifetime = 35f;

                        homingPower = 0.08f;
                        homingRange = 50f;
                        homingDelay = 5f;

                        frontColor = Color.white;
                        backColor = Color.valueOf("72d4ff");
                        trailColor = Color.valueOf("72d4ff");
                        trailWidth = 3f;
                        trailLength = 20;

                        status = JBStatus.ionizedStatus;
                        statusDuration = 180f;

                        hitEffect = Fx.massiveExplosion;
                        despawnEffect = Fx.bigShockwave;
                    }
                };

                smokeEffect = new Effect(30, e -> {
                    Draw.color(heatColor);
                    Lines.stroke(e.fout() * 5f);
                    Lines.circle(e.x, e.y, e.fin() * 50);
                    Lines.stroke(e.fout() * 3f);
                    Lines.circle(e.x, e.y, e.fin() * 30);
                    Lines.stroke(e.fout() * 3.2f);
                    Angles.randLenVectors(e.id, 30, 18 + 80 * e.fin(), (x, y) -> {
                        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 14 + 5);
                    });
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fout() * 120, heatColor, 0.7f);
                });
            }
        };

        chronos = new ItemTurret("chronos") {
            {
                requirements(Category.turret, with(
                        JBItems.chronite, 1200,
                        JBItems.singularium, 900,
                        JBItems.pulsarite, 1000,
                        Items.surgeAlloy, 900,
                        Items.phaseFabric, 700));

                size = 8;
                health = 800200;
                range = 1000f;
                reload = 1000f;
                recoil = 6f;
                recoilTime = 90f;
                shake = 6f;
                rotateSpeed = 0.5f;
                shootCone = 3f;
                inaccuracy = 1.5f;

                consumePower(90f);
                liquidCapacity = 360f;
                coolantMultiplier = 3f;

                heatColor = Color.valueOf("7fd6ff");
                shootSound = JBSounds.hugeBlast;
                loopSound = JBSounds.bioLoop;
                loopSoundVolume = 1.2f;

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
                        parts.add(new RegionPart() {
                            final Color cc = Color.valueOf("a066ff");
                            final Color cl = Color.valueOf("ead9ff");
                            {
                                progress = PartProgress.reload;
                            }

                            @Override
                            public void draw(PartParams params) {
                                if (params.warmup <= 0.001f) return;

                                float reload = params.reload;
                                float warm = params.warmup;
                                float alpha = warm * Mathf.pow(reload, 0.5f);
                                if (alpha <= 0.01f) return;

                                Draw.z(Layer.effect + 0.1f);

                                
                                for (int i = 0; i < 6; i++) {
                                    float orbitRad = 75f - reload * 60f + Mathf.absin(Time.time + i * 10f, 5f, 3f);
                                    float ang = Time.time * (1.5f + i * 0.25f) + i * 60f;
                                    float px = params.x + Angles.trnsx(ang, orbitRad);
                                    float py = params.y + Angles.trnsy(ang, orbitRad);
                                    float psize = (0.8f + reload * 2.5f) * warm;
                                    Draw.color(cc, cl, reload);
                                    Draw.alpha(alpha * (0.5f + 0.5f * reload));
                                    Fill.circle(px, py, psize);
                                    Drawf.light(px, py, psize * 5f, cc, 0.35f * alpha);
                                }

                                
                                Draw.color(cc, cl, reload * 0.6f);
                                Draw.alpha(alpha * 0.8f);
                                Lines.stroke(1.8f * alpha);
                                float arcAmt = reload * 0.85f;
                                if (arcAmt > 0.01f) {
                                    Lines.arc(params.x, params.y, 28f, arcAmt, Time.time * 1.8f);
                                    Lines.arc(params.x, params.y, 20f, arcAmt, -Time.time * 2.2f + 180f);
                                }

                                
                                if (reload > 0.95f) {
                                    float flash = Mathf.absin(Time.time, 2.5f, 1f);
                                    Draw.color(cl, flash * warm * 0.6f);
                                    Lines.stroke(2.2f * flash * warm);
                                    Lines.circle(params.x, params.y, 38f + flash * 6f);
                                    Drawf.light(params.x, params.y, 80f + flash * 30f, cc, 0.5f * warm * flash);
                                }

                                Draw.reset();
                            }
                        });
                    }
                };

                shootEffect = new Effect(80f, e -> {
                    Draw.color(heatColor, Color.white, e.fin());
                    Lines.stroke(e.fout() * 6f);
                    Lines.circle(e.x, e.y, e.fin() * 180f);
                    Drawf.light(e.x, e.y, e.fin() * 220f, heatColor, 0.8f);
                });

                ammoUseEffect = new Effect(120f, e -> {
                    Draw.color(heatColor);
                    Lines.stroke(Mathf.curve(e.fin(), 0, 1) * 4f);
                    Lines.circle(e.x, e.y, e.fout() * 140f);
                    Draw.color(Color.white);
                    Drawf.light(e.x, e.y, e.fin() * 200f, heatColor, 0.8f);
                });

                ammo(JBItems.chronite, JBBullets.chronosShell);
            }
        };

        /*antiMatterWarper = new AntiMatterWarper("anti-matter-warper") {
            {
                requirements(Category.units, ItemStack.with(
                        JBItems.singularium, 700,
                        JBItems.sergium, 1200,
                        JBItems.amalgam, 1000));
                size = 10;

                consumePower(200f);
                consumeItems(ItemStack.with(JBItems.singularium, 2));
                consumeLiquids(LiquidStack.with(JBLiquids.argon, 1f));
            }
        };

         */

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

        adamantiumSynthesizer = new EffectPowerGenerator("adamantium-synthesizer") {
            {
                requirements(Category.power, ItemStack.with(
                        JBItems.adamantium, 220,
                        JBItems.cryostal, 245,
                        JBItems.feronium, 340,
                        JBItems.thorium, 360));
                size = 3;
                itemCapacity = 30;
                scaledHealth = 15;
                powerProduction = 55f;
                updateEffect = JBFx.adamantiumSynthesizerWork;
                itemDuration = 120f;

                consumeItems(ItemStack.with(JBItems.adamantium, 2));
                consumeLiquids(LiquidStack.with(JBLiquids.cryofluid, 0.8f));

                drawer = new DrawMulti(
                        new DrawRegion(),
                        new DrawGlowRegion("-glow") {
                            {
                                color = Color.valueOf("#E02D2D");
                            }
                        }

                );
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

        basicUnitPrinter = new UnitPrinter("basic-unit-printer") {
            {
                requirements(Category.units, ItemStack.with(
                        JBItems.sergium, 1000,
                        JBItems.pulsarite, 1500,
                        JBItems.cryostal, 2300,
                        JBItems.adamantium, 3000));
                recipe = ItemStack.with(JBItems.sergium, 1200, JBItems.pulsarite, 1500, JBItems.adamantium, 3000);
                buildTime = 60 * 20;
                size = 9;
                consumePower(180f);
                upgrades.put(JBUnits.decimator, JBUnits.revenant);
                upgrades.put(JBUnits.vindicator, JBUnits.oblivion);
                upgrades.put(JBUnits.leviathan, JBUnits.tidebreaker);
                upgrades.put(JBUnits.phantom, JBUnits.ocelexis);
                upgrades.put(JBUnits.octoclasm, JBUnits.oraxia);
            }
        };

        advancedUnitPrinter = new UnitPrinter("advanced-unit-printer") {
            {
                requirements(Category.units, ItemStack.with(
                        JBItems.singularium, 1200,
                        JBItems.amalgam, 1500,
                        JBItems.cryostal, 4300,
                        JBItems.sergium, 3000));
                recipe = ItemStack.with(JBItems.singularium, 3200, JBItems.amalgam, 5000, JBItems.sergium, 3000);
                buildTime = 60 * 50;
                size = 9;
                consumePower(280f);
                upgrades.put(JBUnits.revenant, JBUnits.nemesis);
                upgrades.put(JBUnits.oraxia, JBUnits.broodmother);
            }
        };
    }
}
