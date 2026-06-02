package jababarium.expand.units.UnitEntity;

import arc.audio.Sound;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Interval;
import arc.util.Time;
import mindustry.entities.Units;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.entities.pattern.ShootPattern;
import mindustry.entities.pattern.ShootSpread;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Sounds;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class WeaponBuilder {
    Weapon w;

    public static WeaponBuilder create(String name) {
        WeaponBuilder b = new WeaponBuilder();
        b.w = new Weapon(name);
        return b;
    }

    public WeaponBuilder reload(float r) {
        w.reload = r;
        return this;
    }

    public WeaponBuilder range(float range) {
        BulletType b = w.bullet;

        if (b instanceof BasicBulletType bb) {
            bb.lifetime = range / bb.speed;

        } else if (b instanceof ArtilleryBulletType ab) {
            ab.lifetime = range / ab.speed;

        } else if (b instanceof MissileBulletType mb) {
            mb.lifetime = range / mb.speed;

        } else if (b instanceof LaserBulletType lb) {
            lb.length = range;

        } else {
            throw new IllegalStateException(
                    "Range is not supported for bullet type: " + b.getClass().getSimpleName());
        }

        return this;
    }

    public WeaponBuilder bullet(BulletType b) {
        w.bullet = b;
        return this;
    }

    public WeaponBuilder shootSound(Sound s) {
        w.shootSound = s;
        return this;
    }

    public WeaponBuilder shootStatus(mindustry.type.StatusEffect status, float duration) {
        w.shootStatus = status;
        w.shootStatusDuration = duration;
        return this;
    }

    public Weapon build() {
        return w;
    }

    public WeaponBuilder rotate(boolean r) {
        w.rotate = r;
        return this;
    }

    public WeaponBuilder mirror(boolean m) {
        w.mirror = m;
        return this;
    }

    public WeaponBuilder pos(float x, float y) {
        w.x = x;
        w.y = y;
        return this;
    }

    public WeaponBuilder top(boolean t) {
        w.top = t;
        return this;
    }

    public WeaponBuilder layer(float l) {
        w.layerOffset = l;
        return this;
    }

    public WeaponBuilder burst(int count, float delay) {
        w.shoot.shots = count;
        w.shoot.shotDelay = delay;
        return this;
    }

    public WeaponBuilder continuous(boolean c) {
        w.continuous = c;
        return this;
    }

    public WeaponBuilder shootSpread(int shots, float spread) {
        w.shoot = new ShootSpread(shots, spread);
        return this;
    }

    public WeaponBuilder alternate(float spread) {
        w.shoot = new ShootAlternate(spread);
        return this;
    }

    public WeaponBuilder alternate(boolean a) {
        w.alternate = a;
        return this;
    }

    public WeaponBuilder shoot(ShootPattern pattern) {
        w.shoot = pattern;
        return this;
    }

    public static WeaponBuilder createAlwaysFire(String name) {
        WeaponBuilder b = new WeaponBuilder();
        b.w = new Weapon(name) {

            @Override
            public void update(Unit unit, WeaponMount mount) {
                mount.reload = Math.max(mount.reload - Time.delta * unit.reloadMultiplier, 0);

                float wx = unit.x + Angles.trnsx(unit.rotation - 90f, x, y);
                float wy = unit.y + Angles.trnsy(unit.rotation - 90f, x, y);

                
                float searchRange = 1250f;

                boolean playerShooting = unit.isPlayer() && unit.isShooting();
                boolean enemyNearby = Units.closestTarget(
                        unit.team, wx, wy, searchRange,
                        u -> u.checkTarget(true, true),
                        t -> true) != null;

                if (!playerShooting && !enemyNearby)
                    return;

                mount.shoot = true;
                mount.aimX = wx;
                mount.aimY = wy + 1f;

                if (mount.reload <= 0f && unit.canShoot()) {
                    mount.reload = reload;

                    bullet.create(unit, unit, unit.team, wx, wy, unit.rotation(), -1f, 1f, 1f, null, null, wx, wy,
                            null);

                    if (shootSound != Sounds.none) {
                        shootSound.at(wx, wy, Mathf.random(soundPitchMin, soundPitchMax), shootSoundVolume);
                    }
                }
            }
        };
        return b;
    }
}
