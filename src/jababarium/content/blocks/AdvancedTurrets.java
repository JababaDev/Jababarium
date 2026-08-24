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

public class AdvancedTurrets {

    public static void load() {
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

        overlord = new PowerTurret("overlord") {
            {
                shootY = 17.5f;
                shootX = 0f;
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
                                -20.5f, 1f, 0f,
                                21f, 1f, 0f
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
                shootY = 15.5f;
                shootX = 0f;
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

    }
}
