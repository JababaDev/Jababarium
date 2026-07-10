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

public class HeavyTurrets {

    public static void load() {
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

    }
}
