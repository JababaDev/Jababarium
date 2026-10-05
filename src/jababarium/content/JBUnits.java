package jababarium.content;

import arc.graphics.Color;
import arc.math.Mathf;
import jababarium.expand.bullets.AdaptedShootHelix;
import jababarium.expand.units.UnitEntity.*;
import jababarium.expand.units.ai.InterceptorAI;
import mindustry.ai.types.CommandAI;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.abilities.MoveEffectAbility;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.entities.bullet.MissileBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.pattern.ShootHelix;
import mindustry.entities.pattern.ShootSpread;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;
import mindustry.world.meta.Env;
import jababarium.expand.units.abilities.*;

public class JBUnits {

        public static UnitType scout, zanuka, fray, blip, geran, omniq, vortex, spectre, destroyer, inferno, decimator,
                        vindicator, revenant,
                        nemesis, oblivion, tiny, ariel, widow, empress,
                        undertow, ripjaw, brinneclaw, maelstromis, quantar, leviathan, tidebreaker, ocelexis, pelagis,
                        aquarail, vector, glacial, rift, theridion, phantom, octoclasm, oraxia, broodmother;

        public static void load() {

                scout = UnitBuilder.create("scout")
                                .flying()
                                .health(640f)
                                .armor(1.5f)
                                .speed(2.0f)
                                .acceleration(0.2f)
                                .inertia(0.010f)
                                .outlineRadius(0)
                                .engine(0f, -3f, 1f, 270f)
                                .weapon(
                                                WeaponBuilder.create("scout-gun")
                                                                .reload(30f)
                                                                .bullet(new BasicBulletType(4f, 21) {
                                                                        {
                                                                                backColor = JBColor.nemesisMid;
                                                                                frontColor = JBColor.nemesisGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.nemesisLight;
                                                                                width = 6f;
                                                                                height = 8f;
                                                                                lifetime = 40f;
                                                                                hitEffect = Fx.hitBulletSmall;
                                                                                despawnEffect = Fx.none;
                                                                        }
                                                                })
                                                                .range(160f)
                                                                .build())
                                .build();
                scout.constructor = UnitEntity::create;

                zanuka = UnitBuilder.create("zanuka")
                                .flying()
                                .health(320f)
                                .armor(0.7f)
                                .speed(3.3f)
                                .acceleration(0.2f)
                                .inertia(0.1f)
                                .outlineRadius(0)
                                .engine(0, -3f, 3f, 270f)
                                .weapon(
                                                WeaponBuilder.create("zanuka-gun")
                                                                .reload(30f)
                                                                .bullet(new BasicBulletType(4f, 30) {
                                                                        {
                                                                                backColor = JBColor.oblivionMid;
                                                                                frontColor = JBColor.oblivionGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.oblivionLight;
                                                                                width = 6f;
                                                                                height = 8f;
                                                                                lifetime = 40f;
                                                                                hitEffect = Fx.hitBulletSmall;
                                                                                despawnEffect = Fx.none;
                                                                        }
                                                                })
                                                                .range(145f)
                                                                .build())
                                .build();
                zanuka.constructor = UnitEntity::create;

                fray = UnitBuilder.create("fray")
                                .flying()
                                .health(1600f)
                                .armor(6f)
                                .speed(2.2f)
                                .acceleration(0.8f)
                                .inertia(0.09f)
                                
                                .engines(2, 3f, -7f, 5f, 270f)
                                .hitSize(8)
                                .lockRotation()
                                .weapon(
                                                WeaponBuilder.create("jababarium-fray-gun")
                                                                .reload(30f)
                                                                .rotate(true)
                                                                .rotateSpeed(10f)
                                                                .mirror(true)
                                                                .top(true)
                                                                .pos(4f, -1f)
                                                                .bullet(new LaserBoltBulletType(6f, 35) {
                                                                        {
                                                                                backColor = JBColor.nemesisMid;
                                                                                frontColor = JBColor.nemesisGlow;
                                                                                hitColor = lightColor = JBColor.nemesisLight;
                                                                        }
                                                                })
                                                                .range(220f)
                                                                .build())
                                .build();
                fray.constructor = UnitEntity::create;

                blip = UnitBuilder.create("blip")
                                .flying()
                                .health(800f)
                                .armor(2.8f)
                                .speed(3.6f)
                                .hitSize(15f)
                                .inertia(0.1f)
                                .acceleration(0.06f)
                                .outlineRadius(0)
                                .noCell()
                                .engine(0f, -13f, 7f, 270f)
                                .weapon(
                                                WeaponBuilder.create("blip-lightning")
                                                                .reload(20f)
                                                                .rotate(true)
                                                                .rotateSpeed(13f)
                                                                .mirror(true)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .top(true)
                                                                .pos(0f, 2f)
                                                                .range(16)
                                                                .bullet(new LightningBulletType() {
                                                                        {
                                                                                damage = 27f;

                                                                                lightningLength = 10;
                                                                                lightningLengthRand = 6;
                                                                                lightningCone = 22f;

                                                                                lightningColor = JBColor.oblivionLight;
                                                                                hitColor = lightningColor;
                                                                                lightColor = lightningColor;
                                                                                lightOpacity = 0.7f;

                                                                                hitEffect = new MultiEffect(
                                                                                                Fx.hitLancer,
                                                                                                Fx.lightningShoot,
                                                                                                Fx.sparkShoot);

                                                                                lightningDamage = damage * 0.25f;

                                                                                despawnEffect = Fx.none;

                                                                        }
                                                                })
                                                                .build()

                                )
                                .build();

                blip.constructor = UnitEntity::create;

                geran = UnitBuilder.create("geran")
                                .flying()
                                .health(2800f)
                                .armor(5.6f)
                                .speed(4.5f)
                                .acceleration(0.1f)
                                .inertia(0.20f)
                                .rotateSpeed(3f)
                                .hitSize(24f)
                                .outlineRadius(0)
                                .engines(3, 10f, -6f, 9f, 270f)
                                .build();

                geran.aiController = CommandAI::new;
                geran.envEnabled = Env.any;
                geran.constructor = KamikazeUnitEntity::new;

                omniq = UnitBuilder.create("omniq")
                                .flying()
                                .health(5600f)
                                .armor(12f)
                                .speed(2.2f)
                                .outlineRadius(0)
                                .shield(55f, 3000f, 5f, 100f)
                                .engines(2, 10f, -18f, 6f, 270f)
                                .hitSize(30f)
                                .acceleration(0.06f)
                                .inertia(0.015f)
                                .rotateSpeed(5f)
                                .ability(new RepairFieldAbility(
                                                300f, 
                                                360f, 
                                                60f 
                                ))
                                .ability(new EnergyCoreAbility(
                                                0f,
                                                12f,
                                                6f,
                                                Color.valueOf("83e6d1")))
                                .build();

                omniq.constructor = UnitEntity::create;

                mindustry.entities.bullet.BulletType vortexOrb = JBBullets.Orb.copy();
                vortexOrb.damage = 117f;
                vortexOrb.lifetime = 52f;

                vortex = UnitBuilder.create("vortex")
                                .flying()
                                .health(16000f)
                                .armor(21f)
                                .speed(2.2f)
                                .outlineRadius(0)
                                .engines(2, 10f, -20f, 10f, 270f)
                                .hitSize(30f)
                                .shield(65f, 6000f, 6f, 150f)
                                .acceleration(0.09f)
                                .rotateSpeed(4f)
                                .inertia(0.01f)
                                .weapon(
                                                WeaponBuilder.create("vortex-gun")
                                                                .reload(40f)
                                                                .mirror(true)
                                                                .pos(19.5f, 19.5f)
                                                                .bullet(vortexOrb)
                                                                .burst(3, 5f)
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())
                                .build();

                vortex.aiController = InterceptorAI::new;
                vortex.constructor = UnitEntity::create;

                mindustry.entities.bullet.BulletType spectreLaser = JBBullets.laserBeam.copy();
                spectreLaser.damage = 188f;
                ((mindustry.entities.bullet.RailBulletType) spectreLaser).length = 325f;

                spectre = UnitBuilder.create("spectre")
                                .flying()
                                .health(8000f)
                                .armor(9.8f)
                                .speed(3.6f)
                                .outlineRadius(0)
                                .engines(2, 20f, -25f, 10f, 270f)
                                .hitSize(60f)
                                .acceleration(0.06f)
                                .lockRotation()
                                .inertia(0.015f)
                                .rotateSpeed(2f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-spectre-cannon")
                                                                .reload(30f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(3.2f)
                                                                .pos(38f, 5.5f)
                                                                .bullet(spectreLaser)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-spectre-cannon")
                                                                .reload(30f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(3.2f)
                                                                .pos(28f, -10f)
                                                                .bullet(spectreLaser)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .build();

                spectre.constructor = UnitEntity::create;

                destroyer = UnitBuilder.create("destroyer")
                                .flying()
                                .health(40000f)
                                .speed(1.4f)
                                .outlineRadius(0)
                                .engines(2, 20f, -50f, 10f, 270f)
                                .hitSize(60f)
                                .acceleration(0.06f)
                                .inertia(0.015f)
                                .armor(37.5f)
                                .rotateSpeed(0.8f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-destroyer-cannon")
                                                                .reload(30f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(2.2f)
                                                                .pos(20f, 10f)
                                                                .bullet(JBBullets.plasma)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-destroyer-transgression")
                                                                .reload(200f)
                                                                .pos(0f, 25f)
                                                                .bullet(JBBullets.plasmaBeam)
                                                                .shootSound(JBSounds.beam)
                                                                .continuous(true)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-destroyer-support-cannon")
                                                                .reload(20f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(8f)
                                                                .burst(3, 5f)
                                                                .pos(25f, -15f)
                                                                .bullet(JBBullets.lightSupport)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .build();

                destroyer.constructor = UnitEntity::create;

                mindustry.entities.bullet.BulletType infernoLance = JBBullets.crimsonLanceHeavy.copy();
                infernoLance.damage = 222f;
                infernoLance.splashDamage = 222f * 0.75f;
                infernoLance.lifetime = 54f;
                mindustry.entities.bullet.BulletType infernoSupportLaser = JBBullets.laserBeam.copy();
                infernoSupportLaser.damage = 78f;
                ((mindustry.entities.bullet.RailBulletType) infernoSupportLaser).length = 380f;

                inferno = UnitBuilder.create("inferno")
                                .flying()
                                .health(20000f)
                                .speed(2.3f)
                                .outlineRadius(0)
                                .engines(3, 20f, -55f, 10f, 270f)
                                .hitSize(65f)
                                .acceleration(0.06f)
                                .inertia(0.015f)
                                .rotateSpeed(0.8f)
                                .armor(17.5f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-inferno-cannon")
                                                                .reload(50f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(3.9f)
                                                                .pos(21.5f, -22.5f)
                                                                .bullet(JBBullets.crimson)
                                                                .shootSound(JBSounds.blast)
                                                                .build())

                                .weapon(
                                                WeaponBuilder.create("jababarium-inferno-lance")
                                                                .reload(100f)
                                                                .pos(0f, 40f)
                                                                .bullet(infernoLance)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-inferno-lance")
                                                                .reload(100f)
                                                                .pos(15f, 40f)
                                                                .bullet(infernoLance)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-inferno-lance")
                                                                .reload(100f)
                                                                .pos(-15f, 40f)
                                                                .bullet(infernoLance)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-inferno-support-cannon")
                                                                .reload(20f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(3.2f)
                                                                .burst(3, 5f)
                                                                .pos(20f, 12f)
                                                                .bullet(infernoSupportLaser)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .build();

                inferno.constructor = UnitEntity::create;

                mindustry.entities.bullet.BulletType decimatorSupport = JBBullets.lightSupport2.copy();
                decimatorSupport.damage = 52f;
                decimatorSupport.splashDamage = 52f * 0.4f;
                decimatorSupport.lifetime = 69f;

                decimator = UnitBuilder.create("decimator")
                                .flying()
                                .health(96000f)
                                .speed(1f)
                                .outlineRadius(0)
                                .engines(3, 25f, -30f, 12f, 270f)
                                .hitSize(90f)
                                .acceleration(0.06f)
                                .inertia(0.015f)
                                .rotateSpeed(0.6f)
                                .lockRotation()
                                .armor(75f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-decimator-cannon")
                                                                .reload(100f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.8f)
                                                                .pos(45f, 0f)
                                                                .bullet(JBBullets.voidPlasma)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-decimator-missile-launcher")
                                                                .reload(50f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(3.6f)
                                                                .pos(25f, 5f)
                                                                .bullet(JBBullets.guidedVoidMissile)
                                                                .shootSound(JBSounds.missile2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-decimator-support-cannon")
                                                                .reload(40f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(6f)
                                                                .pos(29f, 33f)
                                                                .burst(5, 5f)
                                                                .bullet(decimatorSupport)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-decimator-cannon2")
                                                                .reload(110f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.6f)
                                                                .pos(90f, -27f)
                                                                .bullet(JBBullets.voidCollapse)
                                                                .shootSound(JBSounds.shock)
                                                                .build())
                                .build();

                decimator.constructor = UnitEntity::create;

                mindustry.entities.bullet.BulletType vindicatorRail = JBBullets.laserBeam2.copy();
                vindicatorRail.damage = 155f;
                ((mindustry.entities.bullet.RailBulletType) vindicatorRail).length = 500f;

                vindicator = UnitBuilder.create("vindicator")
                                .flying()
                                .health(48000f)
                                .speed(1.7f)
                                .outlineRadius(0)
                                .engine(0f, -50f, 10f, 270f)
                                .hitSize(70f)
                                .acceleration(0.06f)
                                .inertia(0.015f)
                                .rotateSpeed(0.6f)
                                .armor(35f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-vindicator-cannon")
                                                                .reload(100f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(2.6f)
                                                                .pos(45f, -1.5f)
                                                                .bullet(JBBullets.crimsonChain)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-vindicator-laser")
                                                                .reload(140f)
                                                                .pos(0f, 35f)
                                                                .continuous(true)
                                                                .bullet(JBBullets.crimsonBeam)
                                                                .shootSound(JBSounds.beam)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-vindicator-support-laser")
                                                                .reload(50f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(2.9f)
                                                                .pos(16.5f, 42f)
                                                                .burst(7, 3f)
                                                                .bullet(vindicatorRail)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-vindicator-cannon2")
                                                                .reload(110f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(2.6f)
                                                                .pos(18f, 17.5f)
                                                                .bullet(JBBullets.crimsonNova)
                                                                .shootSound(JBSounds.shock)
                                                                .build())
                                .build();

                vindicator.constructor = UnitEntity::create;

                revenant = UnitBuilder.create("revenant")
                                .flying()
                                .health(240000f)
                                .speed(0.6f)
                                .outlineRadius(0)
                                .enginesCustom(new float[][] {
                                                { 0f, -205f, 35f, 270f },
                                                { 85f, -180f, 20f, 270f },
                                                { -85f, -180f, 20f, 270f }
                                })
                                .hitSize(220f)
                                .noCell()
                                .acceleration(0.06f)
                                .inertia(0.015f)
                                .rotateSpeed(0.1f)
                                .lockRotation()
                                .armor(150f)
                                .ability(new AuraCircleAbility(
                                                270f,
                                                JBColor.nemesisLight))
                                .weapon(
                                                WeaponBuilder.create("jababarium-revenant-cannon")
                                                                .reload(130f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(0.9f)
                                                                .pos(70f, 0f)
                                                                .bullet(JBBullets.absoluteZero)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-revenant-missile-launcher")
                                                                .reload(90f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(3.2f)
                                                                .burst(3, 5f)
                                                                .pos(65f, 50f)
                                                                .bullet(JBBullets.guidedZeroMissile)
                                                                .shootSound(JBSounds.missile2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-revenant-cannon2")
                                                                .reload(80f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.1f)
                                                                .pos(100f, -45f)
                                                                .bullet(JBBullets.azureStream)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-revenant-cannon3")
                                                                .reload(100f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.1f)
                                                                .pos(68f, -110f)
                                                                .bullet(JBBullets.frostCascade)
                                                                .shootSound(JBSounds.shock)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-revenant-shotgun")
                                                                .reload(80f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.4f)
                                                                .pos(45f, 110f)
                                                                .shootSpread(8, 10f)
                                                                .bullet(JBBullets.frostShotgun)
                                                                .shootSound(Sounds.shootFuse)
                                                                .build())
                                .build();

                revenant.constructor = UnitEntity::create;

                mindustry.entities.bullet.BulletType oblivionRail = JBBullets.laserBeam2.copy();
                oblivionRail.damage = 156f;
                ((mindustry.entities.bullet.RailBulletType) oblivionRail).length = 630f;

                oblivion = UnitBuilder.create("oblivion")
                                .flying()
                                .health(120000f)
                                .hitSize(190f)
                                .speed(1.0f)
                                .outlineRadius(0)
                                .engines(2, 43f, -140f, 12f, 270f)
                                .acceleration(0.06f)
                                .inertia(0.015f)
                                .rotateSpeed(0.1f)
                                .lockRotation()
                                .armor(70f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-oblivion-cannon")
                                                                .reload(90f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.3f)
                                                                .pos(40f, 0f)
                                                                .bullet(JBBullets.crimsonVortex)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-oblivion-cannon2")
                                                                .reload(110f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.3f)
                                                                .pos(80f, -40f)
                                                                .bullet(JBBullets.infernoCore)
                                                                .shootSound(JBSounds.blastShockwave)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-oblivion-missile-launcher")
                                                                .reload(80f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(4.5f)
                                                                .pos(20f, 10f)
                                                                .burst(3, 5f)
                                                                .bullet(JBBullets.guidedCrimsonLance)
                                                                .shootSound(JBSounds.missile2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-oblivion-laser")
                                                                .reload(70f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.6f)
                                                                .pos(15f, 60f)
                                                                .burst(9, 3f)
                                                                .bullet(oblivionRail)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-oblivion-cannon3")
                                                                .reload(120f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.3f)
                                                                .pos(90f, -65f)
                                                                .bullet(JBBullets.absoluteInferno)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .build();

                oblivion.constructor = UnitEntity::create;
                
                // nemesis-only bullets: scale the apex shard chain
                JBBullets.apexShardNemesis.damage = 190f;
                JBBullets.apexMicroNemesis.damage = 100f;

                nemesis = UnitBuilder.create("nemesis")
                                .flying()
                                .health(640000f)
                                .speed(0.3f)
                                .outlineRadius(0)
                                .engine(0f, -410f, 60f, 270f)
                                .hitSize(480f)
                                .noCell()
                                .acceleration(0.06f)
                                .inertia(0.015f)
                                .rotateSpeed(0.07f)
                                .armor(200f)
                                .lockRotation()
                                .ability(new AuraCircleAbility(530f, Color.valueOf("8be9d4")))
                                .ability(new RotatingCoreAbility(Color.valueOf("8be9d4"), 35f, 0f, 0f))
                                .ability(new ShockWaveAbility(50f, 510f, 1000f, Color.valueOf("8be9d4")))
                                .weapon(
                                                WeaponBuilder.create("jababarium-nemesis-cannon")
                                                                .reload(50f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(0.9f)
                                                                .pos(100f, 50f)
                                                                .bullet(JBBullets.nemesisBullet)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-nemesis-collapse")
                                                                .reload(800f)
                                                                .rotate(true)
                                                                .rotateSpeed(0.5f)
                                                                .pos(0f, 0f)
                                                                .bullet(JBBullets.verdantCollapse)
                                                                .shootSound(JBSounds.hugeShoot)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-nemesis-cannon2")
                                                                .reload(60f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.1f)
                                                                .pos(150f, 60f)
                                                                .bullet(JBBullets.verdantBeamBurst)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-nemesis-photosynthesis")
                                                                .reload(9f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(5.4f)
                                                                .pos(155f, 140f)
                                                                .alternate(37f)
                                                                .bullet(JBBullets.photosynthesisBullet)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-nemesis-web")
                                                                .reload(15f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(4.5f)
                                                                .pos(155f, 280f)
                                                                .bullet(JBBullets.verdantLightningWeb)
                                                                .shootSound(JBSounds.hugeShoot)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-verdant-beam")
                                                                .reload(10f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.0f)
                                                                .pos(140f, -330f)
                                                                .bullet(JBBullets.verdantBeam)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.createAlwaysFire("jababarium-verdant-cross-spin-laser")
                                                                .reload(600f)
                                                                .pos(0f, 0f)
                                                                .range(900f)
                                                                .bullet(JBBullets.crossSpinLaser)
                                                                .shootSound(JBSounds.beam)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-verdant-flame")
                                                                .reload(200f)
                                                                .pos(285f, -245f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.1f)
                                                                .alternate(false)
                                                                .bullet(JBBullets.verdantApex)
                                                                .shootSound(JBSounds.blastShockwave)
                                                                .build())

                                .build();
                nemesis.constructor = UnitEntity::create;
                
                tiny = UnitBuilder.create("tiny")
                                
                                .spider()

                                .legCount(6)
                                .legLength(9f)
                                .legBaseOffset(4f)
                                .legExtension(2f)
                                .legMoveSpace(1f)
                                .legGroupSize(2)
                                .legPairOffset(3f)
                                .legSpeed(0.12f)
                                .legLengthScl(0.9f)
                                .ripple(0.5f)

                                .health(320f)
                                .speed(1.1f)
                                .armor(1f)
                                .hitSize(8f)
                                .rotateSpeed(12f)
                                .acceleration(0.08f)
                                .inertia(0.06f)
                                .outlineRadius(3)

                                .weapon(
                                                WeaponBuilder.create("") 
                                                                .bullet(JBBullets.tinyShell)
                                                                .reload(30f)
                                                                .pos(0f, 6f) 
                                                                             
                                                                .rotate(false)
                                                                .mirror(false)
                                                                .build())

                                .build();

                ariel = UnitBuilder.create("ariel")
                                .spider()
                                .legCount(6)
                                .legLength(28f)
                                .legBaseOffset(6f)
                                .legExtension(3f)
                                .legMoveSpace(0.8f)
                                .legGroupSize(2)
                                .legPairOffset(4f)
                                .legSpeed(0.14f)
                                .legLengthScl(0.95f)
                                .legStraightness(0.4f)
                                .ripple(0.6f)

                                .health(800f)
                                .speed(1.3f)
                                .armor(4f)
                                .hitSize(12f)
                                .rotateSpeed(10f)
                                .acceleration(0.09f)
                                .inertia(0.07f)
                                .outlineRadius(3)

                                .weapon(
                                                WeaponBuilder.create("widow-cannon")
                                                                .reload(25f)
                                                                .pos(4f, 4f)
                                                                .rotate(true)
                                                                .rotateSpeed(12f)
                                                                .mirror(true)
                                                                .top(false)
                                                                .bullet(new BasicBulletType(5.5f, 58) {
                                                                        {
                                                                                backColor = JBColor.broodmotherMid;
                                                                                frontColor = JBColor.broodmotherGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.broodmotherLight;
                                                                                width = 8f;
                                                                                height = 11f;
                                                                                lifetime = 40f;
                                                                                hitEffect = Fx.hitBulletBig;
                                                                                despawnEffect = Fx.hitBulletBig;
                                                                                shootEffect = Fx.shootBig;
                                                                                smokeEffect = Fx.shootBigSmoke;
                                                                                pierce = true;
                                                                                pierceCap = 2; 
                                                                        }
                                                                })
                                                                .shootSound(Sounds.shoot)
                                                                .build())

                                .build();

                widow = UnitBuilder.create("widow")
                                .spider()

                                .legCount(6)
                                .legLength(40f)
                                .legBaseOffset(8f)
                                .legExtension(0f)
                                .legMoveSpace(2f)
                                .legGroupSize(2)
                                .legPairOffset(6f)
                                .legSpeed(0.16f)
                                .legLengthScl(1.0f)
                                .legStraightness(0.3f)
                                .legSplashDamage(140f)
                                .legSplashRange(20f)
                                .stepShake(1.5f)
                                .stepSound(Sounds.explosion)
                                .ripple(0.8f)

                                .health(2800f)
                                .speed(1.5f)
                                .armor(8f)
                                .hitSize(18f)
                                .rotateSpeed(9f)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .outlineRadius(4)

                                .weapon(
                                                WeaponBuilder.create("widow-assault-cannon")
                                                                .reload(20f) 
                                                                .pos(6f, 5f)
                                                                .rotate(true)
                                                                .rotateSpeed(5f)
                                                                .mirror(true)
                                                                .bullet(new BasicBulletType(7f, 30) {
                                                                        {
                                                                                backColor = JBColor.broodmotherMid;
                                                                                frontColor = JBColor.broodmotherGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.broodmotherLight;
                                                                                width = 9f;
                                                                                height = 12f;
                                                                                lifetime = 40f;
                                                                                pierce = true;
                                                                                pierceCap = 3;
                                                                                splashDamage = 20f;
                                                                                splashDamageRadius = 25f;
                                                                                status = StatusEffects.slow;
                                                                                statusDuration = 120f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.missile)
                                                                .build())

                                .weapon(
                                                WeaponBuilder.create("widow-minigun")
                                                                .reload(8f) 
                                                                .pos(8f, -2f)
                                                                .rotate(true)
                                                                .rotateSpeed(10f)
                                                                .mirror(true)
                                                                .bullet(new BasicBulletType(8f, 12) {
                                                                        {
                                                                                backColor = JBColor.broodmotherMid;
                                                                                frontColor = JBColor.broodmotherGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.broodmotherLight;
                                                                                width = 6f;
                                                                                height = 8f;
                                                                                lifetime = 35f;
                                                                                pierce = true;
                                                                                pierceCap = 2;
                                                                        }
                                                                })
                                                                .shootSound(Sounds.shoot)
                                                                .build())

                                .weapon(
                                                WeaponBuilder.create("widow-missile")
                                                                .reload(45f)
                                                                .pos(0f, 3f)
                                                                .rotate(true)
                                                                .rotateSpeed(5f)
                                                                .mirror(false)
                                                                .burst(3, 8f) 
                                                                .bullet(new MissileBulletType(4f, 35) {
                                                                        {
                                                                                backColor = JBColor.broodmotherMid;
                                                                                frontColor = JBColor.broodmotherGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.broodmotherLight;
                                                                                width = 8f;
                                                                                height = 10f;
                                                                                lifetime = 70f;
                                                                                homingPower = 0.1f;
                                                                                homingRange = 120f;
                                                                                splashDamage = 25f;
                                                                                splashDamageRadius = 35f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.missile)
                                                                .build())

                                .weapon(
                                                WeaponBuilder.create("widow-lightning")
                                                                .reload(25f)
                                                                .pos(0f, -5f)
                                                                .rotate(true)
                                                                .rotateSpeed(8f)
                                                                .mirror(false)
                                                                .bullet(new LightningBulletType() {
                                                                        {
                                                                                lightningColor = hitColor = lightColor = JBColor.broodmotherLight;
                                                                                damage = 50f;
                                                                                lightningLength = 15;
                                                                                lightningLengthRand = 10;
                                                                                status = StatusEffects.shocked;
                                                                                statusDuration = 120f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())

                                .build();

                empress = UnitBuilder.create("empress")
                                .spider()

                                .legCount(6)
                                .legLength(100f)
                                .legBaseOffset(8f)
                                .legExtension(0f)
                                .legMoveSpace(1f)
                                .legGroupSize(2)
                                .legPairOffset(6f)
                                .legSpeed(0.16f)
                                .legLengthScl(1.0f)
                                .legStraightness(0.3f)
                                .legSplashDamage(400f)
                                .legSplashRange(20f)
                                .stepShake(1.5f)
                                .stepSound(Sounds.explosion)
                                .ripple(0.8f)

                                .health(8000f)
                                .speed(1.5f)
                                .armor(14f)
                                .hitSize(60f)
                                .rotateSpeed(9f)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .outlineRadius(4)

                                .weapon(
                                                WeaponBuilder.create("jababarium-empress-cannon")
                                                                .reload(30f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(2.5f)
                                                                .pos(20f, 10f)
                                                                .bullet(JBBullets.laserBurn)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("empress-destroyer")
                                                                .reload(200f)
                                                                .pos(0f, 25f)
                                                                .bullet(JBBullets.pulseWave)
                                                                .shootSound(JBSounds.beam)
                                                                .continuous(true)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-empress-fang")
                                                                .reload(60f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(4.5f)
                                                                .burst(1, 5f)
                                                                .pos(25f, -15f)
                                                                .bullet(JBBullets.thunderShot)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .build();

                theridion = UnitBuilder.create("theridion")
                                .spider()

                                .legCount(6)
                                .legLength(100f)
                                .legBaseOffset(8f)
                                .legExtension(0f)
                                .legMoveSpace(1f)
                                .legGroupSize(2)
                                .legPairOffset(6f)
                                .legSpeed(0.16f)
                                .legLengthScl(1.0f)
                                .legStraightness(0.3f)
                                .legSplashDamage(1000f)
                                .legSplashRange(20f)
                                .stepShake(1.5f)
                                .stepSound(Sounds.explosion)
                                .ripple(0.8f)

                                .health(20000f)
                                .speed(0.7f)
                                .armor(25f)
                                .hitSize(45f)
                                .rotateSpeed(4f)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .outlineRadius(4)

                                .weapon(
                                                WeaponBuilder.create("jababarium-theridion-cannon")
                                                                .reload(30f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(5f)
                                                                .pos(15f, -15f)
                                                                .bullet(JBBullets.theridionBolt)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("theridion-destroyer")
                                                                .reload(200f)
                                                                .pos(0f, 25f)
                                                                .bullet(JBBullets.arcBolt)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-theridion-cannon")
                                                                .reload(60f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(5f)
                                                                .burst(1, 5f)
                                                                .pos(15f, 19f)
                                                                .bullet(JBBullets.theridionBolt)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .build();

                BulletType octoclasmBolt = JBBullets.theridionBolt.copy();
                octoclasmBolt.lifetime = 92f; // speed 6, no drag -> ~550 range

                octoclasm = UnitBuilder.create("octoclasm")
                                .spider()

                                .legCount(6)
                                .legLength(200f)
                                .legBaseOffset(40f)
                                .legExtension(-40f)
                                .legMoveSpace(0.4f)
                                .legGroupSize(4)
                                .legPairOffset(3f)
                                .legSpeed(0.08f)
                                .legLengthScl(1.0f)
                                .legStraightness(0.3f)
                                .legSplashDamage(2400f)
                                .legSplashRange(70f)
                                .stepShake(1.5f)
                                .stepSound(Sounds.shootNavanax)
                                .ripple(0.8f)
                                .landShake(5f)

                                .health(48000f)
                                .speed(0.7f)
                                .armor(50f)
                                .hitSize(70f)
                                .rotateSpeed(1f)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .outlineRadius(4)

                                .weapon(
                                                WeaponBuilder.create("jababarium-octoclasm-cannon")
                                                                .reload(50f)
                                                                .rotate(false)
                                                                .pos(23f, 8f)
                                                                .burst(3, 5f)
                                                                .rotate(true)
                                                                .rotateSpeed(4f)
                                                                .mirror(true)
                                                                .bullet(octoclasmBolt)
                                                                .shootSound(JBSounds.missile)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-octoclasm-vortex")
                                                                .reload(300f)
                                                                .rotate(false)
                                                                .pos(0f, -15f)
                                                                .bullet(JBBullets.chargedCannonBolt)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .build())
                                .build();

                oraxia = UnitBuilder.create("oraxia")
                                .spider()

                                .legCount(8)
                                .legLength(200f)
                                .legBaseOffset(80f)
                                .legExtension(-40f)
                                .legMoveSpace(0.4f)
                                .legGroupSize(2)
                                .legPairOffset(3f)
                                .legSpeed(0.08f)
                                .legLengthScl(1.0f)
                                .legStraightness(0.3f)
                                .legSplashDamage(6000f)
                                .legSplashRange(70f)
                                .stepShake(3f)
                                .stepSound(Sounds.shootNavanax)
                                .ripple(0.8f)
                                .landShake(5f)

                                .health(120000f)
                                .speed(0.7f)
                                .armor(100f)
                                .hitSize(170f)
                                .rotateSpeed(1f)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .outlineRadius(4)

                                .weapon(
                                                WeaponBuilder.create("jababarium-oraxia-vortex")
                                                                .reload(300f)
                                                                .rotate(false)
                                                                .continuous(true)
                                                                .pos(0f, -25f)
                                                                .bullet(JBBullets.deathBeam)
                                                                .shootSound(JBSounds.hugeBeam)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-oraxia-cannon")
                                                                .reload(100f)
                                                                .rotate(true)
                                                                .rotateSpeed(1.2f)
                                                                .pos(73.5f, 49f)

                                                                .mirror(true)
                                                                .bullet(JBBullets.ancientBall)
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-oraxia-laser")
                                                                .reload(50f)
                                                                .rotate(true)
                                                                .rotateSpeed(1.2f)
                                                                .pos(-106f, -50f)
                                                                .mirror(true)
                                                                .bullet(JBBullets.oraxiaBullet)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-oraxia-gun")
                                                                .reload(10f)
                                                                .rotate(true)
                                                                .rotateSpeed(2f)
                                                                .pos(109f, 12f)
                                                                .rotate(true)
                                                                .mirror(true)
                                                                .bullet(JBBullets.tideLaserBroodmother)
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())
                                .build();

                broodmother = UnitBuilder.create("broodmother")
                                .spider()

                                .legCount(8)
                                .legLength(400f)
                                .legBaseOffset(100f)
                                .legExtension(-40f)
                                .legMoveSpace(0.4f)
                                .legGroupSize(2)
                                .legPairOffset(3f)
                                .legSpeed(0.08f)
                                .legLengthScl(1.0f)
                                .legStraightness(0.3f)
                                .legSplashDamage(16000f)
                                .legSplashRange(100f)
                                .stepShake(10f)
                                .stepSound(Sounds.explosionNavanax)
                                .ripple(0.8f)
                                .landShake(5f)
                                .noCell()

                                .health(320000f)
                                .speed(0.7f)
                                .armor(170f)
                                .hitSize(280f)
                                .rotateSpeed(0.3f)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .outlineRadius(4)

                                .weapon(
                                                WeaponBuilder.create("jababarium-broodmother-vortex")
                                                                .reload(700f)
                                                                .rotate(false)
                                                                .continuous(true)
                                                                .pos(0f, -80f)
                                                                .bullet(JBBullets.broodmotherDeathBeam)
                                                                .shootSound(JBSounds.megaHugeBeam)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-broodmother-cannon")
                                                                .reload(100f)
                                                                .rotate(true)
                                                                .rotateSpeed(0.8f)
                                                                .pos(60f, 60f)
                                                                .mirror(true)
                                                                .bullet(JBBullets.tideBallBroodmother)
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-broodmother-laser")
                                                                .reload(5f)
                                                                .rotate(true)
                                                                .rotateSpeed(1.5f)
                                                                .pos(70f, 0f)
                                                                .mirror(true)
                                                                .bullet(JBBullets.repeater)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-broodmother-gun")
                                                                .reload(200f)
                                                                .rotate(true)
                                                                .rotateSpeed(0.6f)
                                                                .pos(80f, -100f)
                                                                .rotate(true)
                                                                .mirror(true)
                                                                .bullet(JBBullets.supernovaArtillery)
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())
                                .build();

                undertow = UnitBuilder.create("undertow")
                                .naval()
                                .health(440)
                                .speed(2f)
                                .armor(1.2f)
                                .outlineRadius(0)
                                .hitSize(12f)
                                .waterTrail(10)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-undertow-gun")
                                                                .rotate(true)
                                                                .rotateSpeed(10f)
                                                                .reload(40f)
                                                                .mirror(true)
                                                                .pos(5f, 0f)
                                                                .bullet(new BasicBulletType(7f, 18) {
                                                                        {
                                                                                backColor = JBColor.tidebreakerMid;
                                                                                frontColor = JBColor.tidebreakerGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.tidebreakerLight;
                                                                                width = 6f;
                                                                                height = 8f;
                                                                                lifetime = 33f;
                                                                                hitEffect = Fx.hitBulletSmall;
                                                                                despawnEffect = Fx.none;
                                                                        }
                                                                })
                                                                .build())
                                .build();

                pelagis = UnitBuilder.create("pelagis")
                                .naval()
                                .health(320)
                                .speed(1.7f)
                                .armor(0.8f)
                                .inertia(0.07f)
                                .waterTrail(10)
                                .acceleration(0.12f)
                                .outlineRadius(0)
                                .weapon(
                                                WeaponBuilder.create("pelagis-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(7f)
                                                                .reload(90f)
                                                                .mirror(true)
                                                                .pos(5f, 0f)
                                                                .bullet(new BasicBulletType(7f, 5) {
                                                                        {
                                                                                backColor = JBColor.ocelexisMid;
                                                                                frontColor = JBColor.ocelexisGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.ocelexisLight;
                                                                                width = 6f;
                                                                                height = 8f;
                                                                                lifetime = 42f;
                                                                                hitEffect = Fx.hitBulletSmall;
                                                                                despawnEffect = Fx.none;
                                                                                despawnHit = true;
                                                                                splashDamage = 33f;
                                                                                splashDamageRadius = 40f;
                                                                        }
                                                                })
                                                                .build())
                                .build();

                ripjaw = UnitBuilder.create("ripjaw")
                                .naval()
                                .health(1100)
                                .speed(1.8f)
                                .armor(4.8f)
                                .outlineRadius(0)
                                .hitSize(20f)
                                .waterTrail(15)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-ripjaw-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(6f)
                                                                .reload(20f)
                                                                .mirror(true)
                                                                .pos(8f, 2f)
                                                                .bullet(new MissileBulletType(5f, 18) {
                                                                        {
                                                                                backColor = JBColor.tidebreakerMid;
                                                                                frontColor = JBColor.tidebreakerGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.tidebreakerLight;
                                                                                width = 8f;
                                                                                height = 10f;
                                                                                lifetime = 63f;
                                                                                homingPower = 0.1f;
                                                                                homingRange = 120f;
                                                                                splashDamage = 12f;
                                                                                splashDamageRadius = 35f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.missile)
                                                                .build())
                                .build();

                aquarail = UnitBuilder.create("aquarail")
                                .naval()
                                .health(800f)
                                .speed(1.6f)
                                .armor(3.2f)
                                .outlineRadius(0)
                                .hitSize(15f)
                                .waterTrail(10)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .rotateSpeed(1.3f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-aquarail-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(4.2f)
                                                                .reload(30f)
                                                                .mirror(true)
                                                                .pos(5f, 0f)
                                                                .bullet(new MissileBulletType(6f, 8) {
                                                                        {
                                                                                backColor = JBColor.ocelexisMid;
                                                                                frontColor = JBColor.ocelexisGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.ocelexisLight;
                                                                                width = 8f;
                                                                                height = 10f;
                                                                                lifetime = 67f;
                                                                                homingPower = 0.1f;
                                                                                homingRange = 120f;
                                                                                splashDamage = 32f;
                                                                                splashDamageRadius = 45f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.missile)
                                                                .build())
                                .build();

                brinneclaw = UnitBuilder.create("brinneclaw")
                                .naval()
                                .health(3900)
                                .speed(1.7f)
                                .armor(9.6f)
                                .outlineRadius(0)
                                .hitSize(26f)
                                .waterTrail(20)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-brinneclaw-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(5f)
                                                                .reload(20f)
                                                                .mirror(true)
                                                                .pos(12f, 2f)
                                                                .bullet(new MissileBulletType(6f, 45) {
                                                                        {
                                                                                backColor = JBColor.tidebreakerMid;
                                                                                frontColor = JBColor.tidebreakerGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.tidebreakerLight;
                                                                                width = 8f;
                                                                                height = 10f;
                                                                                lifetime = 66f;
                                                                                homingPower = 0.1f;
                                                                                homingRange = 120f;
                                                                                splashDamage = 30f;
                                                                                splashDamageRadius = 35f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.missile)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-brinneclaw-lightning")
                                                                .rotate(true)
                                                                .rotateSpeed(8f)
                                                                .reload(40f)
                                                                .mirror(true)
                                                                .pos(10f, 0f)
                                                                .bullet(new LightningBulletType() {
                                                                        {
                                                                                lightningColor = hitColor = lightColor = JBColor.tidebreakerLight;
                                                                                damage = 30f;
                                                                                lightningLength = 15;
                                                                                lightningLengthRand = 10;
                                                                                status = StatusEffects.shocked;
                                                                                statusDuration = 120f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())
                                .build();

                vector = UnitBuilder.create("vector")
                                .naval()
                                .health(2800)
                                .speed(1.4f)
                                .armor(6.4f)
                                .outlineRadius(0)
                                .hitSize(26f)
                                .waterTrail(20)
                                .trailOffset(10f, 0f)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-vector-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(3.5f)
                                                                .reload(24f)
                                                                .mirror(true)
                                                                .pos(12f, 2f)
                                                                .bullet(new MissileBulletType(6.5f, 10) {
                                                                        {
                                                                                backColor = JBColor.ocelexisMid;
                                                                                frontColor = JBColor.ocelexisGlow;
                                                                                hitColor = trailColor = lightColor = JBColor.ocelexisLight;
                                                                                width = 8f;
                                                                                height = 10f;
                                                                                lifetime = 78f;
                                                                                homingPower = 0.1f;
                                                                                homingRange = 120f;
                                                                                splashDamage = 70f;
                                                                                splashDamageRadius = 55f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.missile)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-vector-lightning")
                                                                .rotate(true)
                                                                .rotateSpeed(5.6f)
                                                                .reload(45f)
                                                                .mirror(true)
                                                                .pos(10f, 0f)
                                                                .bullet(new LightningBulletType() {
                                                                        {
                                                                                lightningColor = hitColor = lightColor = JBColor.ocelexisLight;
                                                                                damage = 30f;
                                                                                lightningLength = 15;
                                                                                lightningLengthRand = 10;
                                                                                status = StatusEffects.shocked;
                                                                                statusDuration = 120f;
                                                                        }
                                                                })
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())
                                .build();

                mindustry.entities.bullet.BulletType maelstromisOrb = JBBullets.OrbTidebreaker.copy();
                maelstromisOrb.damage = 130f;
                maelstromisOrb.lifetime = 60f;
                mindustry.entities.bullet.BulletType maelstromisSupport = JBBullets.lightSupport2Tidebreaker.copy();
                maelstromisSupport.damage = 54f;
                maelstromisSupport.splashDamage = 21.6f;
                maelstromisSupport.lifetime = 63f;

                maelstromis = UnitBuilder.create("maelstromis")
                                .naval()
                                .health(11000)
                                .speed(1.5f)
                                .armor(16.8f)
                                .outlineRadius(0)
                                .hitSize(40f)
                                .waterTrail(50)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .trailOffset(15f, -10f)
                                .rotateSpeed(1f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-maelstromis-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(4f)
                                                                .reload(70f)
                                                                .mirror(true)
                                                                .burst(4, 4f)
                                                                .pos(15f, 0f)
                                                                .bullet(maelstromisOrb)
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-maelstromis-support-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(8f)
                                                                .mirror(true)
                                                                .burst(3, 5f)
                                                                .reload(60)
                                                                .pos(10f, 20f)
                                                                .bullet(maelstromisSupport)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .build();

                mindustry.entities.bullet.BulletType glacialBeam = JBBullets.laserBeamOcelexis.copy();
                glacialBeam.damage = 90f;
                ((mindustry.entities.bullet.RailBulletType) glacialBeam).length = 400f;
                mindustry.entities.bullet.BulletType glacialLance = JBBullets.crimsonLanceHeavyOcelexis.copy();
                glacialLance.damage = 430f;
                glacialLance.splashDamage = 322f;
                glacialLance.splashDamageRadius = 60f;
                glacialLance.lifetime = 87f;

                glacial = UnitBuilder.create("glacial")
                                .naval()
                                .health(8000)
                                .speed(1.2f)
                                .armor(11.2f)
                                .outlineRadius(0)
                                .hitSize(40f)
                                .waterTrail(50)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .trailOffset(15f, -10f)
                                .rotateSpeed(1f)
                                .noCell()
                                .weapon(
                                                WeaponBuilder.create("jababarium-glacial-cannon")
                                                                .reload(70f)
                                                                .mirror(true)
                                                                .rotate(true)
                                                                .rotateSpeed(1.75f)
                                                                .burst(2, 8f)
                                                                .pos(10f, 12f)
                                                                .bullet(glacialBeam)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-glacial-lance")
                                                                .rotate(true)
                                                                .rotateSpeed(2.1f)
                                                                .mirror(true)
                                                                .reload(100f)
                                                                .pos(15f, -5f)
                                                                .bullet(glacialLance)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .build())
                                .build();

                quantar = UnitBuilder.create("quantar")
                                .naval()
                                .health(27000f)
                                .speed(1.3f)
                                .armor(30f)
                                .outlineRadius(0)
                                .hitSize(70f)
                                .waterTrail(50)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .trailOffset(15f, -10f)
                                .rotateSpeed(1f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-quantar-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(3f)
                                                                .mirror(true)
                                                                .reload(80f)
                                                                .pos(20f, -10f)
                                                                .bullet(JBBullets.quantarBullet)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-quantar-cannon2")
                                                                .rotate(true)
                                                                .rotateSpeed(5f)
                                                                .mirror(true)
                                                                .reload(40f)
                                                                .burst(2, 4f)
                                                                .pos(10f, 15f)
                                                                .bullet(JBBullets.ionPulseBullet)
                                                                .shootSound(JBSounds.shootGauss2)
                                                                .build())
                                .build();

                mindustry.entities.bullet.BulletType riftBolt = JBBullets.rift.copy();
                riftBolt.damage = 560f;
                riftBolt.splashDamage = 520f;
                riftBolt.splashDamageRadius = 70f;
                riftBolt.lightningDamage = 150f;
                riftBolt.speed = 27f;
                mindustry.entities.bullet.BulletType riftLightning = JBBullets.LightningRed.copy();
                riftLightning.damage = riftLightning.lightningDamage = riftLightning.splashDamage = 200f;
                riftLightning.speed = 5f;

                rift = UnitBuilder.create("rift")
                                .naval()
                                .health(20000f)
                                .speed(1.0f)
                                .armor(20f)
                                .outlineRadius(0)
                                .hitSize(70f)
                                .waterTrail(50)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .trailOffset(15f, -10f)
                                .rotateSpeed(1f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-rift-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(1.75f)
                                                                .mirror(true)
                                                                .reload(160f)
                                                                .pos(30f, -15f)
                                                                .shoot(new ShootHelix() {
                                                                        {
                                                                                shots = 2;
                                                                                shotDelay = 18f;
                                                                                mag = 2.2f;
                                                                                scl = 2.2f;
                                                                        }
                                                                })
                                                                .bullet(riftBolt)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-rift-lightning")
                                                                .rotate(true)
                                                                .rotateSpeed(3.5f)
                                                                .mirror(true)
                                                                .reload(90f)
                                                                .pos(25f, 20)
                                                                .bullet(riftLightning)
                                                                .shootSound(Sounds.shootCollaris)
                                                                .build())
                                .build();

                mindustry.entities.bullet.BulletType leviathanShell = JBBullets.quantarBullet.copy();
                leviathanShell.damage = leviathanShell.lightningDamage = 376f;
                leviathanShell.splashDamage = 282f;
                leviathanShell.rangeOverride = 700f;
                leviathanShell.lifetime = 88f;

                leviathan = UnitBuilder.create("leviathan")
                                .naval()
                                .health(66000f)
                                .speed(1.2f)
                                .armor(60f)
                                .outlineRadius(0)
                                .hitSize(70f)
                                .waterTrail(50)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .trailOffset(15f, -10f)
                                .rotateSpeed(1f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-leviathan-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(2.5f)
                                                                .reload(110f)
                                                                .burst(3, 20f)
                                                                .pos(0f, 43f)
                                                                .bullet(leviathanShell)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-leviathan-cannon2")
                                                                .rotate(true)
                                                                .rotateSpeed(3f)
                                                                .mirror(true)
                                                                .reload(70f)
                                                                .pos(18.5f, -32f)
                                                                .bullet(JBBullets.plasmaBoltBullet)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-leviathan-beam")
                                                                .rotate(true)
                                                                .rotateSpeed(2f)
                                                                .mirror(true)
                                                                .reload(80f)
                                                                .pos(26.5f, -6f)
                                                                .bullet(JBBullets.hyperBeamBullet)
                                                                .shootSound(JBSounds.shootGauss3)
                                                                .build())
                                .build();

                mindustry.entities.bullet.BulletType phantomBolt = JBBullets.rift.copy();
                phantomBolt.damage = 480f;
                phantomBolt.splashDamage = 430f;
                phantomBolt.splashDamageRadius = 80f;
                phantomBolt.lightningDamage = 140f;
                phantomBolt.speed = 33f;
                mindustry.entities.bullet.BulletType phantomLightning = JBBullets.LightningRed.copy();
                phantomLightning.damage = phantomLightning.lightningDamage = phantomLightning.splashDamage = 125f;
                phantomLightning.speed = 6.2f;

                phantom = UnitBuilder.create("phantom")
                                .naval()
                                .health(48000f)
                                .speed(0.9f)
                                .armor(40f)
                                .outlineRadius(0)
                                .hitSize(130f)
                                .waterTrail(50)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .trailOffset(15f, -10f)
                                .rotateSpeed(0.8f)
                                .weapon(
                                                WeaponBuilder.create("jababarium-phantom-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(1.4f)
                                                                .mirror(true)
                                                                .reload(180f)
                                                                .pos(75f, -16.5f)
                                                                .shoot(new ShootHelix() {
                                                                        {
                                                                                shots = 5;
                                                                                shotDelay = 18f;
                                                                                mag = 2.2f;
                                                                                scl = 2.2f;
                                                                        }
                                                                })
                                                                .bullet(phantomBolt)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-phantom-lightning")
                                                                .rotate(true)
                                                                .rotateSpeed(2.8f)
                                                                .mirror(true)
                                                                .reload(100f)
                                                                .burst(4, 8f)
                                                                .pos(99.5f, 17f)
                                                                .bullet(phantomLightning)
                                                                .shootSound(Sounds.shootCollaris)
                                                                .build())
                                .build();

                tidebreaker = UnitBuilder.create("tidebreaker")
                                .naval()
                                .health(165000f)
                                .speed(0.8f)
                                .armor(120f)
                                .outlineRadius(0)
                                .hitSize(200f)
                                .waterTrail(90)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .trailOffset(60f, -50f)
                                .rotateSpeed(0.3f)
                                .noCell()
                                .weapon(
                                                WeaponBuilder.create("jababarium-tidebreaker-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(1.0f)
                                                                .mirror(true)
                                                                .reload(100f)
                                                                .pos(30f, 30f)
                                                                .bullet(JBBullets.voidLanceBullet)
                                                                .shootSound(JBSounds.blast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-tidebreaker-missile-launcher")
                                                                .rotate(true)
                                                                .rotateSpeed(3.5f)
                                                                .mirror(true)
                                                                .reload(110f)
                                                                .burst(5, 7f)
                                                                .pos(35f, 70f)
                                                                .bullet(JBBullets.missileStrike)
                                                                .shootSound(JBSounds.missile2)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-tidebreaker-laser-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(1.5f)
                                                                .mirror(true)
                                                                .reload(30f)
                                                                .pos(40f, -15f)
                                                                .bullet(JBBullets.tidebreakerLaser)
                                                                .shootSound(JBSounds.hugeShoot)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-tidebreaker-laser-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(1.5f)
                                                                .mirror(true)
                                                                .reload(30f)
                                                                .pos(70f, -15f)
                                                                .bullet(JBBullets.tidebreakerLaser)
                                                                .shootSound(JBSounds.hugeShoot)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-tidebreaker-cannon2")
                                                                .rotate(true)
                                                                .rotateSpeed(1.2f)
                                                                .mirror(true)
                                                                .reload(80f)
                                                                .pos(60f, 80f)
                                                                .bullet(JBBullets.condensedBolt)
                                                                .shootSound(JBSounds.gunBlast)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-tidebreaker-Std")
                                                                .rotate(true)
                                                                .rotateSpeed(4f)
                                                                .mirror(true)
                                                                .reload(60f)
                                                                .burst(15, 4f)
                                                                .pos(60f, 120f)
                                                                .shoot(new AdaptedShootHelix() {
                                                                        {
                                                                                flip = true;
                                                                                shots = 5;
                                                                                mag = 1.15f;
                                                                                scl = 6f;
                                                                                shotDelay = 3.3f;
                                                                                offset = Mathf.PI2 * 12;
                                                                                firstShotDelay = 10f;
                                                                        }
                                                                })
                                                                .bullet(JBBullets.tidebreakerStd)
                                                                .shootSound(JBSounds.shock)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-tidebreaker-lightning")
                                                                .rotate(true)
                                                                .rotateSpeed(3f)
                                                                .mirror(true)
                                                                .reload(130f)
                                                                .pos(60f, -70f)
                                                                .bullet(JBBullets.tideLightning)
                                                                .shootSound(JBSounds.blastShockwave)
                                                                .build())
                                .build();

                mindustry.entities.bullet.BulletType ocelexisLightning = JBBullets.LightningRed.copy();
                ocelexisLightning.damage = ocelexisLightning.lightningDamage = ocelexisLightning.splashDamage = 220f;
                ocelexisLightning.speed = 6f;
                mindustry.entities.bullet.BulletType ocelexisLaser = JBBullets.tideLaser.copy();
                ocelexisLaser.damage = 280f;
                ((mindustry.entities.bullet.LaserBulletType) ocelexisLaser).length = 800f;
                mindustry.entities.bullet.BulletType ocelexisBall = JBBullets.tideBall.copy();
                ocelexisBall.damage = 150f;
                ocelexisBall.splashDamage = 500f;
                ocelexisBall.lightningDamage = 127.5f;
                ocelexisBall.fragBullet = ocelexisBall.fragBullet.copy();
                ocelexisBall.fragBullet.damage = 2500f;
                ocelexisBall.fragBullet.splashDamage = 500f;
                ocelexisBall.fragBullet.lightningDamage = 280f;

                ocelexis = UnitBuilder.create("ocelexis")
                                .naval()
                                .health(120000f)
                                .speed(0.5f)
                                .armor(80f)
                                .outlineRadius(0)
                                .hitSize(150f)
                                .waterTrail(90)
                                .acceleration(0.12f)
                                .inertia(0.07f)
                                .trailOffset(60f, -50f)
                                .rotateSpeed(0.3f)
                                .noCell()
                                .weapon(
                                                WeaponBuilder.create("jababarium-ocelexis-cannon")
                                                                .rotate(true)
                                                                .rotateSpeed(2.1f)
                                                                .reload(110f)
                                                                .mirror(true)
                                                                .pos(50f, 40f)
                                                                .burst(4, 8)
                                                                .bullet(ocelexisLightning)
                                                                .shootSound(Sounds.shootCollaris)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-ocelexis-laser")
                                                                .rotate(true)
                                                                .rotateSpeed(1.0f)
                                                                .reload(50f)
                                                                .mirror(true)
                                                                .pos(40f, -10f)
                                                                .shoot(new ShootSpread() {
                                                                        {
                                                                                shots = 3;
                                                                                shotDelay = 2.25f;
                                                                                spread = 1f;
                                                                        }
                                                                })
                                                                .bullet(ocelexisLaser)
                                                                .shootSound(JBSounds.laser)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-ocelexis-cannon2")
                                                                .rotate(true)
                                                                .rotateSpeed(0.7f)
                                                                .reload(180f)
                                                                .pos(0f, 30f)
                                                                .bullet(ocelexisBall)
                                                                .shootSound(JBSounds.shootGauss1)
                                                                .build())

                                .weapon(
                                                WeaponBuilder.create("jababarium-ocelexis-lightning")
                                                                .reload(100f)
                                                                .rotate(true)
                                                                .rotateSpeed(2.1f)
                                                                .mirror(true)
                                                                .burst(7, 7f)
                                                                .pos(35f, 80f)
                                                                .bullet(JBBullets.tideLightningRed)
                                                                .shootSound(JBSounds.shock)
                                                                .build())
                                .weapon(
                                                WeaponBuilder.create("jababarium-ocelexis-collapse")
                                                                .rotate(true)
                                                                .rotateSpeed(0.7f)
                                                                .mirror(true)
                                                                .reload(160f)
                                                                .pos(30f, -50f)
                                                                .bullet(JBBullets.collapseShell)
                                                                .shootSound(Sounds.shootCorvus)
                                                                .build())
                                .build();

        }
}
