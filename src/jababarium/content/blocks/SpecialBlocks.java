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

public class SpecialBlocks {

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

        basicUnitPrinter = new UnitPrinter("basic-unit-printer") {
            {
                requirements(Category.units, ItemStack.with(
                        JBItems.sergium, 1000,
                        JBItems.pulsarite, 1500,
                        JBItems.cryostal, 2300,
                        JBItems.adamantium, 3000));
                recipe = ItemStack.with(JBItems.sergium, 1200, JBItems.pulsarite, 1500, JBItems.adamantium, 3000);
                buildTime = 60 * 20;
                size = 8;
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
                size = 8;
                consumePower(280f);
                upgrades.put(JBUnits.revenant, JBUnits.nemesis);
                upgrades.put(JBUnits.oraxia, JBUnits.broodmother);
            }
        };

    }
}
