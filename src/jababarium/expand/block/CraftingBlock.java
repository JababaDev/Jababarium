package jababarium.expand.block;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Time;
import jababarium.util.graphic.DrawFunc;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.draw.*;
import jababarium.content.*;
import jababarium.expand.block.drawer.*;
import jababarium.expand.block.production.factory.MultiBlockCrafter;
import jababarium.content.JBColor;

public class CraftingBlock {
    public static Color stampingArc, processorBlue;

    public static Block feroniumFactory, nectroneDissociator, cryostalCompressor, sergiumFactory, amalgamPresser,
            chroniteFoundry, pulsariteRefactor,
            singulariumLoom, aerialConverter, mercurySplitter, argonMelter, surgeRefactor;

    public static void loadColors() {
        stampingArc = JBColor.lightSkyBack.cpy().lerp(Color.lightGray, 0.3f);
        processorBlue = Color.valueOf("cee5ed");
    }

    public static void load() {
        loadColors();

        surgeRefactor = new MultiBlockCrafter("surge-refactor"){
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.surgeAlloy, 200,
                        JBItems.feronium, 300,
                        JBItems.plastanium, 250
                ));

                size = 4;
                itemCapacity = 15;
                scaledHealth = 50;
                craftTime = 50;
                outputItems = ItemStack.with(JBItems.surgeAlloy, 2);

                consumeItems(ItemStack.with(JBItems.copper, 3, JBItems.feronium, 2, JBItems.titanium, 3));
                consumePower(12f);

                craftEffect = JBFx.blast(JBColor.ancientLight, 5f);
                updateEffect = Fx.none;

                drawer = new DrawMulti(
                        new DrawRegion(""),
                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.ancient;
                                alpha = 1f;
                            }
                        });

            }
        };

        feroniumFactory = new MultiBlockCrafter("feronium-factory") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.copper, 200,
                        JBItems.graphite, 35,
                        JBItems.silicon, 30));

                size = 2;
                itemCapacity = 15;
                scaledHealth = 50;
                craftTime = 60;
                outputItems = ItemStack.with(JBItems.feronium, 2);

                consumeItems(ItemStack.with(JBItems.silicon, 2, JBItems.thorium, 2));
                consumePower(10f);

                drawer = new DrawMulti(
                        new DrawBaseRegion("-2x2"),
                        new DrawRegion(),
                        new DrawPistons() {
                            {
                                sinScl = 8f;
                                sinMag = 2f;
                                sinOffset = 0;
                                lenOffset = -1f;
                            }
                        },
                        new DrawRegion("-top"));

                craftEffect = JBFx.hugeSmokeGray;
                updateEffect = new Effect(80f, e -> {
                    Fx.rand.setSeed(e.id);
                    Draw.color(Color.lightGray, Color.gray, e.fin());
                    Angles.randLenVectors(e.id, 4, 2.0F + 12.0F * e.fin(Interp.pow3Out), (x, y) -> {
                        Fill.circle(e.x + x, e.y + y, e.fout() * Fx.rand.random(1, 2.5f));
                    });
                }).layer(Layer.blockOver + 1);
            }
        };

        nectroneDissociator = new MultiBlockCrafter("nectrone-dissociator") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.feronium, 300,
                        JBItems.cryostal, 350,
                        JBItems.silicon, 400,
                        JBItems.thorium, 600,
                        JBItems.phaseFabric, 500));

                size = 3;
                itemCapacity = 15;
                scaledHealth = 70;
                armor = 4;

                outputLiquids = LiquidStack.with(JBLiquids.nectron, 0.4f);

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawRegion("-base"),
                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.nectrone;
                            }
                        });
                craftEffect = JBFx.neutronDissociateUpdate;
                updateEffect = JBFx.neutronDissociateUpdate;
                updateEffectChance = 0.1f;

                consumePower(35f);
                consumeItems(ItemStack.with(JBItems.thorium, 4, JBItems.adamantium, 3, JBItems.sergium, 3));
            }
        };

        cryostalCompressor = new MultiBlockCrafter("cryostal-compressor") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.feronium, 275,
                        JBItems.phaseFabric, 250,
                        JBItems.metaglass, 300,
                        JBItems.surgeAlloy, 150));
                size = 3;
                itemCapacity = 20;
                scaledHealth = 40;
                hasLiquids = true;
                craftTime = 120f;
                liquidCapacity = 40;

                craftEffect = JBFx.crossBlast(JBColor.cryostal, 45f, 45f);
                craftEffect.lifetime *= 1.5f;
                updateEffect = JBFx.squareRand(JBColor.cryostal, 5f, 15f);

                outputItems = ItemStack.with(JBItems.cryostal, 2f);

                drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(JBLiquids.cryofluid),
                        new DrawRegion("-bottom-2"),
                        new DrawCrucibleFlame() {
                            {
                                flameColor = JBColor.cryostal;
                                midColor = Color.valueOf("2e2f34");
                                circleStroke = 1.05f;
                                circleSpace = 2.65f;
                            }

                            @Override
                            public void draw(Building build) {
                                if (build.warmup() > 0f && flameColor.a > 0.001f) {
                                    Lines.stroke(circleStroke * build.warmup());

                                    float si = Mathf.absin(flameRadiusScl, flameRadiusMag);
                                    float a = alpha * build.warmup();

                                    Draw.blend(Blending.additive);
                                    Draw.color(flameColor, a);

                                    float base = (Time.time / particleLife);
                                    rand.setSeed(build.id);
                                    for (int i = 0; i < particles; i++) {
                                        float fin = (rand.random(1f) + base) % 1f, fout = 1f - fin;
                                        float angle = rand.random(360f) + (Time.time / rotateScl) % 360f;
                                        float len = particleRad * particleInterp.apply(fout);
                                        Draw.alpha(a * (1f - Mathf.curve(fin, 1f - fadeMargin)));
                                        Fill.square(
                                                build.x + Angles.trnsx(angle, len),
                                                build.y + Angles.trnsy(angle, len),
                                                particleSize * fin * build.warmup(), 45);
                                    }

                                    Draw.blend();

                                    Draw.color(midColor, build.warmup());
                                    Lines.square(build.x, build.y, (flameRad + circleSpace + si) * build.warmup(), 45);

                                    Draw.reset();
                                }
                            }
                        },
                        new DrawDefault(),
                        new DrawGlowRegion() {
                            {
                                color = JBColor.cryostal;
                                layer = -1;
                                glowIntensity = 1.1f;
                                alpha = 1.1f;
                            }
                        },
                        new DrawRotator(1f, "-top") {
                            @Override
                            public void draw(Building build) {
                                Drawf.spinSprite(rotator, build.x + x, build.y + y, DrawFunc.rotator_90(
                                        DrawFunc.cycle(build.totalProgress() * rotateSpeed, 0, craftTime), 0.15f));
                            }
                        });
                consumePower(35f);
                consumeItems(ItemStack.with(JBItems.titanium, 3, JBItems.metaglass, 2, JBItems.feronium, 2));
                consumeLiquids(LiquidStack.with(JBLiquids.cryofluid, 0.3f));

            }
        };

        sergiumFactory = new MultiBlockCrafter("sergium-factory") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.plastanium, 300,
                        JBItems.surgeAlloy, 250,
                        JBItems.cryostal, 190,
                        JBItems.chronite, 200));

                size = 5;
                itemCapacity = 20;
                scaledHealth = 40;
                hasLiquids = true;
                craftTime = 90f;
                liquidCapacity = 50;

                craftEffect = JBFx.sergiumMixerCraft;
                updateEffect = JBFx.sergiumMixerUpdate;
                updateEffectChance = 0.07f;

                outputItems = ItemStack.with(JBItems.sergium, 1f);

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawRegion("-base"),
                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.lightSkyBack;
                            }
                        });

                consumePower(46f);
                consumeItems(ItemStack.with(JBItems.chronite, 2, JBItems.adamantium, 3, JBItems.blastCompound, 2));
                consumeLiquids(LiquidStack.with(JBLiquids.aerial, 0.3f));
            }
        };

        amalgamPresser = new MultiBlockCrafter("amalgam-presser") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.sergium, 260,
                        JBItems.pulsarite, 200,
                        JBItems.phaseFabric, 400,
                        JBItems.surgeAlloy, 200));

                size = 5;
                itemCapacity = 20;
                scaledHealth = 50;
                hasLiquids = true;
                craftTime = 80f;

                craftEffect = JBFx.energyPulseCore;
                updateEffect = JBFx.energyPulseCore;
                updateEffectChance = 0.02f;

                outputItems = ItemStack.with(JBItems.amalgam, 1.6f);

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawRegion("-base"),

                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.lightSkyBack;
                                alpha = 0.5f;
                            }
                        },
                        new DrawGlowRegion("-glow1") {
                            {
                                color = JBColor.lightSkyBack;
                                alpha = 0.5f;
                            }
                        },
                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.lightSkyFront;
                                alpha = 0.25f;
                            }
                        });

                consumePower(55f);
                consumeItems(ItemStack.with(JBItems.cryostal, 2, JBItems.surgeAlloy, 3));
                consumeLiquids(LiquidStack.with(JBLiquids.mercury, 0.3f));
            }
        };

        chroniteFoundry = new MultiBlockCrafter("chronite-foundry") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.cryostal, 310,
                        JBItems.surgeAlloy, 280,
                        JBItems.graphite, 300,
                        JBItems.metaglass, 340));
                size = 5;
                itemCapacity = 20;
                scaledHealth = 40;
                hasLiquids = false;
                craftTime = 70f;

                outputItems = ItemStack.with(JBItems.chronite, 2.1f);

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawRegion("-base"),

                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.lightSkyBack;
                                alpha = 0.5f;
                            }
                        },
                        new DrawChroniteArcs(),
                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.lightSkyFront;
                                alpha = 0.25f;
                            }
                        });

                consumePower(45f);
                consumeItems(ItemStack.with(JBItems.feronium, 3, JBItems.surgeAlloy, 1, JBItems.lead, 3));
            }
        };

        pulsariteRefactor = new MultiBlockCrafter("pulsarite-refactor") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.chronite, 200,
                        JBItems.cryostal, 220,
                        JBItems.plastanium, 400,
                        JBItems.graphite, 600));
                size = 5;
                itemCapacity = 20;
                scaledHealth = 35;
                hasLiquids = true;
                craftTime = 100f;

                outputItems = ItemStack.with(JBItems.pulsarite, 2f);

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawRegion("-base"),
                        new DrawEnergyPulseCore(),
                        new DrawGlowRegion("-glow1") {
                            {
                                color = JBColor.nectrone;
                                alpha = 0.5f;
                            }
                        },
                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.nectrone;
                                alpha = 0.5f;
                            }
                        }

                );

                consumePower(50f);
                consumeItems(ItemStack.with(JBItems.chronite, 1f, JBItems.feronium, 2, JBItems.plastanium, 2));
                consumeLiquids(LiquidStack.with(JBLiquids.nectron, 1f));
            }
        };

        singulariumLoom = new MultiBlockCrafter("singularium-loom") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.amalgam, 200,
                        JBItems.pulsarite, 230,
                        JBItems.sergium, 210,
                        JBItems.adamantium, 1500));
                size = 3;
                itemCapacity = 20;
                scaledHealth = 60;
                hasLiquids = true;
                craftTime = 100f;

                
                
                craftEffect = JBFx.mediumDarkEnergyHit;
                craftEffect.lifetime *= 1.5f;
                updateEffect = JBFx.squareRand(JBColor.darkEnrColor, 5f, 15f);

                outputItems = ItemStack.with(JBItems.singularium, 1.4f);

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawRegion("-base"),
                        new DrawGlowRegion("-glow1") {
                            {
                                color = JBColor.nectrone;
                                alpha = 2f;
                            }
                        },
                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.darkEnrColor;
                                alpha = 2f;
                            }
                        },
                        new DrawRotator(1f, "-top") {
                            @Override
                            public void draw(Building build) {
                                Drawf.spinSprite(rotator, build.x + x, build.y + y, DrawFunc.rotator_90(
                                        DrawFunc.cycle(build.totalProgress() * rotateSpeed, 0, craftTime), 0.15f));
                            }
                        });

                consumePower(60f);
                consumeItems(ItemStack.with(JBItems.amalgam, 1, JBItems.sergium, 2));
                consumeLiquids(LiquidStack.with(JBLiquids.argon, 1f));
            }
        };

        aerialConverter = new MultiBlockCrafter("aerial-converter") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.chronite, 200,
                        JBItems.cryostal, 240,
                        JBItems.titanium, 300,
                        JBItems.metaglass, 265));
                size = 3;
                itemCapacity = 20;
                scaledHealth = 35;
                hasLiquids = true;
                liquidCapacity = 50;

                updateEffect = JBFx.etherDenseFog;

                outputLiquids = LiquidStack.with(JBLiquids.aerial, 0.3f);

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawLiquidTile(JBLiquids.aerial),
                        new DrawRegion("-base"),
                        new DrawGlowRegion("-glow") {
                            {
                                color = JBColor.xenGamma;
                                alpha = 1f;
                            }
                        });

                consumePower(30f);
                consumeItems(ItemStack.with(JBItems.cryostal, 1, JBItems.metaglass, 2));
                consumeLiquids(LiquidStack.with(JBLiquids.water, 1.4f));
            }
        };

        mercurySplitter = new MultiBlockCrafter("mercury-splitter") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.pulsarite, 180,
                        JBItems.surgeAlloy, 300,
                        JBItems.cryostal, 265,
                        JBItems.phaseFabric, 300));
                size = 3;
                itemCapacity = 20;
                scaledHealth = 50;
                hasLiquids = true;
                liquidCapacity = 50;

                updateEffect = JBFx.mercurySplitter;
                updateEffect.lifetime *= 2f;
                updateEffectChance = 0.02f;

                outputLiquids = LiquidStack.with(JBLiquids.mercury, 0.2f);

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawLiquidTile(JBLiquids.mercury),
                        new DrawRegion("-base"),
                        new DrawGlowRegion("-glow") {
                            {
                                color = Color.valueOf("9ea3a6");
                                alpha = 0.5f;
                            }
                        });

                consumePower(35f);
                consumeItems(ItemStack.with(JBItems.chronite, 1, JBItems.tungsten, 2));
                consumeLiquids(LiquidStack.with(JBLiquids.aerial, 0.3f));
            }
        };

        argonMelter = new MultiBlockCrafter("argon-melter") {
            {
                requirements(Category.crafting, ItemStack.with(
                        JBItems.sergium, 200,
                        JBItems.pulsarite, 230,
                        JBItems.cryostal, 400,
                        JBItems.surgeAlloy, 500));
                size = 4;
                itemCapacity = 20;
                scaledHealth = 35;
                hasLiquids = true;
                liquidCapacity = 100;

                updateEffect = JBFx.cloudPuff(40f, JBColor.darkEnrColor);

                outputLiquids = LiquidStack.with(JBLiquids.argon, 0.4f);

                drawer = new DrawMulti(
                        
                        new DrawLiquidTile(JBLiquids.argon),
                        new DrawRegion("-base"),
                        new DrawGlowRegion("-glow") {
                            {
                                color = Color.valueOf("8e6cff");
                                alpha = 0.5f;
                            }
                        });

                consumePower(45f);
                consumeLiquids(
                        LiquidStack.with(JBLiquids.nectron, 0.3f, JBLiquids.mercury, 0.2f, JBLiquids.aerial, 0.4f));
            }
        };
    }
}
