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

public class ProductionBlocks {

    public static void load() {
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

    }
}
