package jababarium.expand.units;

import arc.struct.Seq;
import jababarium.content.JBItems;
import jababarium.content.JBLiquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

import static jababarium.content.JBUnits.*;

public class UnitConstructors {

        public static Block groundForge, TideForge, SkyForge, kolomuiskiyReconstructor, rapsodiumReconstructor,
                        spectralReconstructor, omegaReconstructor, betaReconstructor;

        public static void load() {

                SkyForge = new UnitFactory("sky-forge") {
                        {
                                requirements(Category.units, ItemStack.with(
                                                JBItems.feronium, 300,
                                                JBItems.plastanium, 240,
                                                JBItems.silicon, 330,
                                                JBItems.titanium, 400));
                                size = 3;
                                health = 300;
                                consumePower(8f);

                                plans = Seq.with(
                                                new UnitPlan(
                                                                scout,
                                                                60f * 13,
                                                                ItemStack.with(
                                                                                JBItems.silicon, 25,
                                                                                JBItems.lead, 20)),
                                                new UnitPlan(
                                                                zanuka,
                                                                60f * 15,
                                                                ItemStack.with(
                                                                                JBItems.graphite, 25,
                                                                                JBItems.metaglass, 20)));

                        }
                };

            groundForge = new UnitFactory("ground-forge") {
                {
                    requirements(Category.units, ItemStack.with(
                            JBItems.feronium, 300,
                            JBItems.lead, 160,
                            JBItems.plastanium, 240,
                            JBItems.silicon, 330));
                    size = 3;
                    health = 300;
                    consumePower(6f);

                    plans = Seq.with(
                            new UnitPlan(
                                    tiny,
                                    60f * 13,
                                    ItemStack.with(
                                            JBItems.silicon, 25,
                                            JBItems.copper, 20)));

                }
            };

            TideForge = new UnitFactory("tide-forge") {
                {
                    requirements(Category.units, ItemStack.with(
                            JBItems.copper, 500,
                            JBItems.silicon, 400,
                            JBItems.plastanium, 140));

                    size = 3;
                    health = 500;
                    consumePower(6f);
                    plans = Seq.with(
                            new UnitPlan(
                                    undertow,
                                    60f * 15,
                                    ItemStack.with(
                                            JBItems.feronium, 40,
                                            JBItems.lead, 55)),
                            new UnitPlan(
                                    pelagis,
                                    60f * 15,
                                    ItemStack.with(
                                            JBItems.copper, 50,
                                            JBItems.lead, 55)));

                }
            };

                kolomuiskiyReconstructor = new Reconstructor("kolomuiskiy-reconstructor") {
                        {
                                requirements(Category.units, ItemStack.with(
                                                JBItems.feronium, 1000,
                                                JBItems.plastanium, 400,
                                                JBItems.silicon, 650,
                                                JBItems.thorium, 1200));

                                size = 3;
                                health = 420;
                                consumePower(12f);

                                constructTime = 60f * 18;

                                consumeItems(ItemStack.with(
                                                JBItems.feronium, 45,
                                                JBItems.plastanium, 30));

                                upgrades.addAll(
                                                new UnitType[] { scout, fray },
                                                new UnitType[] { undertow, ripjaw },
                                                new UnitType[] { pelagis, aquarail },
                                                new UnitType[] { zanuka, blip },
                                                new UnitType[] { tiny, ariel });

                        }
                };

                rapsodiumReconstructor = new Reconstructor("rapsodium-reconstructor") {
                        {
                                requirements(Category.units, ItemStack.with(
                                                JBItems.cryostal, 450,
                                                JBItems.surgeAlloy, 700,
                                                JBItems.plastanium, 1000,
                                                JBItems.silicon, 1200));

                                size = 5;
                                health = 700;
                                consumePower(20f);
                                constructTime = 60f * 25;

                                consumeItems(ItemStack.with(
                                                JBItems.feronium, 60,
                                                JBItems.thorium, 70,
                                                JBItems.silicon, 100));

                                upgrades.addAll(
                                                new UnitType[] { blip, geran },
                                                new UnitType[] { fray, omniq },
                                                new UnitType[] { ripjaw, brinneclaw },
                                                new UnitType[] { aquarail, vector },
                                                new UnitType[] { ariel, widow });
                        }
                };

                spectralReconstructor = new Reconstructor("spectral-reconstructor") {
                        {
                                requirements(Category.units, ItemStack.with(
                                                JBItems.adamantium, 700,
                                                JBItems.cryostal, 700,
                                                JBItems.feronium, 1500,
                                                JBItems.surgeAlloy, 1200));

                                size = 7;
                                health = 2000;
                                consumePower(35f);
                                constructTime = 60f * 35;

                                consumeItems(ItemStack.with(
                                                JBItems.adamantium, 130,
                                                JBItems.cryostal, 100,
                                                JBItems.plastanium, 200,
                                                JBItems.silicon, 350));

                                upgrades.addAll(
                                                new UnitType[] { omniq, vortex },
                                                new UnitType[] { geran, spectre },
                                                new UnitType[] { brinneclaw, maelstromis },
                                                new UnitType[] { vector, glacial },
                                                new UnitType[] { widow, empress });
                        }
                };

                omegaReconstructor = new Reconstructor("omega-reconstructor") {
                        {
                                requirements(Category.units, ItemStack.with(
                                                JBItems.chronite, 650,
                                                JBItems.adamantium, 1200,
                                                JBItems.cryostal, 800,
                                                JBItems.surgeAlloy, 1200));

                                size = 9;
                                health = 3200;
                                consumePower(45f);
                                constructTime = 60f * 45;

                                consumeItems(ItemStack.with(
                                                JBItems.chronite, 195,
                                                JBItems.adamantium, 500,
                                                JBItems.surgeAlloy, 350,
                                                JBItems.feronium, 650));
                                consumeLiquids(LiquidStack.with(JBLiquids.aerial, 1f));

                                upgrades.addAll(
                                                new UnitType[] { vortex, destroyer },
                                                new UnitType[] { maelstromis, quantar },
                                                new UnitType[] { spectre, inferno },
                                                new UnitType[] { glacial, rift },
                                                new UnitType[] { empress, theridion });
                        }
                };

                betaReconstructor = new Reconstructor("beta-reconstructor") {
                        {
                                requirements(Category.units, ItemStack.with(
                                                JBItems.pulsarite, 800,
                                                JBItems.chronite, 1500,
                                                JBItems.cryostal, 2200,
                                                JBItems.surgeAlloy, 3200));

                                size = 11;
                                health = 6200;
                                consumePower(55f);
                                constructTime = 60f * 65;

                                consumeItems(ItemStack.with(
                                                JBItems.pulsarite, 200,
                                                JBItems.silicon, 2200,
                                                JBItems.cryostal, 660,
                                                JBItems.phaseFabric, 340));
                                consumeLiquids(LiquidStack.with(JBLiquids.nectron, 1.1f));

                                upgrades.addAll(
                                                new UnitType[] { destroyer, decimator },
                                                new UnitType[] { quantar, leviathan },
                                                new UnitType[] { inferno, vindicator },
                                                new UnitType[] { rift, phantom },
                                                new UnitType[] { theridion, octoclasm });
                        }
                };

        }
}
