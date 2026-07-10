package jababarium.content;

import jababarium.content.blocks.*;

import mindustry.world.Block;


public class JBBlocks {

    public static Block manualArtillery, cryostalConveyor, cryostalRouter, cryostalJunction, cryostalBridge,
            fluxReactor, helix, selfhealingConduit, singularityNeedle, selfhealingJunction, selfhealingRouter,
            entropyChain, cryostalDrill, selfhealingliquidBridge, ionizer, solarApex, chronos, antiMatterWarper, ignis,
            hastae,
            adamantiumSynthesizer, overlord, transgression, apex, avangard, omega, nyx, abbys, entropy, nokko, cascade,
            nexus, reaper, tempest, basicUnitPrinter, advancedUnitPrinter, gammaReaper, cryostalUnloader;

    public static void load() {
        DistributionBlocks.load();
        ProductionBlocks.load();
        SpecialBlocks.load();
        BasicTurrets.load();
        HeavyTurrets.load();
        AdvancedTurrets.load();
        EliteTurrets.load();
    }
}
