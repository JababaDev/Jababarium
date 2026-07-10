package jababarium.content;

import jababarium.content.bullets.*;
import mindustry.entities.bullet.BulletType;

public class JBBullets {

    public static String STRIKE = "missile";

    public static String MINE_BULLET = "mine-bullet";

    public static BulletType burst, singularityPoint, entropyBolt, transgression, chronosShell, chronosField, apexShell,
            apexShard, apexMicro, abbys, Orb, laserBeam, plasma, plasmaBeam, lightSupport, crimson, crimsonLance,
            crimsonLanceHeavy,
            voidPlasma, guidedVoidMissile, voidCollapse, lightSupport2, crimsonChain, crimsonBeam, laserBeam2,
            crimsonNova, absoluteZero, guidedZeroMissile, azureStream, frostCascade, frostShotgun, crimsonVortex,
            infernoCore,
            guidedCrimsonLance, absoluteInferno, nemesisBullet, verdantCollapse, verdantBeamBurst, tinyShell,
            empressVenom, empressWebCluster, empressFangBarrage, laserBurn, thunderShot, pulseWave,
            photosynthesisBullet,
            verdantLightningWeb, verdantBeam, crossSpinLaser, verdantApex, maelstrom, quantarBullet, ionPulseBullet,
            plasmaBoltBullet,
            hyperBeamBullet, voidLanceBullet, missileStrike, tidebreakerLaser, condensedBolt, tidebreakerStd,
            basicSkyFrag, tideLightning, LightningRed,
            rift, arcBolt, theridionBolt, chargedCannonBolt, tideLaser, tideBall, deathBeam, tideLightningRed,
            collapseShell, ancientBall, oraxiaBullet,
            oraxiaLaser, broodmotherDeathBeam, supernovaLaser, supernovaCore,
            supernovaArtillery, repeater, gammaReaper;

    public static void load() {
        MiscBullets.load();
        EnergyBullets.load();
        CrimsonBullets.load();
        FrostBullets.load();
        VoidBullets.load();
        VerdantBullets.load();
        TideBullets.load();
        BossBullets.load();
        SupernovaBullets.load();
    }
}
