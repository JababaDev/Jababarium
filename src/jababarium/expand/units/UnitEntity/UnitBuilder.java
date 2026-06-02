package jababarium.expand.units.UnitEntity;

import arc.audio.Sound;
import arc.struct.Seq;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.gen.LegsUnit;
import mindustry.gen.UnitWaterMove;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class UnitBuilder {

    private final UnitType unit;

    private UnitBuilder(String name) {
        unit = new UnitType(name);
        unit.engines = new Seq<>();
        unit.engineOffset = 0f;
        unit.engineSize = 0f;
        unit.trailLength = 0;
    }

    public static UnitBuilder create(String name) {
        return new UnitBuilder(name);
    }

    public UnitBuilder spider() {
        unit.constructor = LegsUnit::create;
        return this;
    }

    public UnitBuilder legCount(int count) {
        unit.legCount = count;
        return this;
    }

    public UnitBuilder legLength(float length) {
        unit.legLength = length;
        return this;
    }

    public UnitBuilder legBaseOffset(float offset) {
        unit.legBaseOffset = offset;
        return this;
    }

    public UnitBuilder legExtension(float ext) {
        unit.legExtension = ext;
        return this;
    }

    public UnitBuilder legMoveSpace(float space) {
        unit.legMoveSpace = space;
        return this;
    }

    public UnitBuilder legGroupSize(int size) {
        unit.legGroupSize = size;
        return this;
    }

    public UnitBuilder legPairOffset(float offset) {
        unit.legPairOffset = offset;
        return this;
    }

    public UnitBuilder legSpeed(float speed) {
        unit.legSpeed = speed;
        return this;
    }

    public UnitBuilder legLengthScl(float scl) {
        unit.legLengthScl = scl;
        return this;
    }

    public UnitBuilder legStraightness(float s) {
        unit.legStraightness = s;
        return this;
    }

    public UnitBuilder legSplashDamage(float damage) {
        unit.legSplashDamage = damage;
        return this;
    }

    public UnitBuilder legSplashRange(float range) {
        unit.legSplashRange = range;
        return this;
    }

    public UnitBuilder stepShake(float shake) {
        unit.stepShake = shake;
        return this;
    }

    public UnitBuilder stepSound(Sound sound) {
        return this;
    }

    public UnitBuilder ripple(float scale) {
        unit.rippleScale = scale;
        return this;
    }

    public UnitBuilder landShake(float shake) {
        unit.stepShake = shake;
        return this;
    }

    public UnitBuilder flying() {
        unit.flying = true;
        unit.lowAltitude = true;
        return this;
    }

    public UnitBuilder health(float hp) {
        unit.health = hp;
        return this;
    }

    public UnitBuilder speed(float speed) {
        unit.speed = speed;
        return this;
    }

    public UnitBuilder armor(float armor) {
        unit.armor = armor;
        return this;
    }

    public UnitBuilder outlineRadius(int radius) {
        unit.outlineRadius = radius;
        return this;
    }

    public UnitBuilder engine(float x, float y, float radius, float rotation) {
        unit.engines.clear();
        unit.engines.add(new UnitType.UnitEngine(x, y, radius, rotation));
        return this;
    }

    public UnitBuilder engines(
            int count,
            float spacing,
            float y,
            float radius,
            float rotation) {
        unit.engineSize = radius;
        unit.engines.clear();

        for (int i = 0; i < count; i++) {
            float x = (i - (count - 1) / 2f) * spacing;

            unit.engines.add(new UnitType.UnitEngine(
                    x,
                    y,
                    radius,
                    rotation));
        }
        return this;
    }

    public UnitBuilder enginesCustom(float[][] positions) {
        unit.engines.clear();

        for (float[] pos : positions) {
            unit.engines.add(new UnitType.UnitEngine(
                    pos[0],
                    pos[1],
                    pos[2],
                    pos[3]));
        }
        return this;
    }

    public UnitBuilder weapon(Weapon weapon) {
        unit.weapons.add(weapon);
        return this;
    }

    public UnitBuilder ability(Ability ability) {
        unit.abilities.add(ability);
        return this;
    }

    public UnitType build() {
        return unit;
    }

    public UnitBuilder hitSize(float size) {
        unit.hitSize = size;
        return this;
    }

    public UnitBuilder lockRotation() {
        unit.faceTarget = false;
        return this;
    }

    public UnitBuilder shield(
            float radius,
            float maxHp,
            float regen,
            float cooldown) {
        unit.abilities.add(
                new ForceFieldAbility(
                        radius,
                        regen,
                        maxHp,
                        cooldown));
        return this;
    }

    public UnitBuilder acceleration(float accel) {
        unit.accel = accel;
        return this;
    }

    public UnitBuilder inertia(float drag) {
        unit.drag = drag;
        return this;
    }

    public UnitBuilder rotateSpeed(float rotateSpeed) {
        unit.rotateSpeed = rotateSpeed;
        return this;
    }

    public UnitBuilder noCell() {
        unit.drawCell = false;
        return this;
    }

    public UnitBuilder naval() {
        unit.constructor = UnitWaterMove::create;
        unit.naval = true;
        unit.flying = false;
        unit.lowAltitude = false;
        unit.canDrown = false;
        return this;
    }

    public UnitBuilder waterTrail(int length) {
        unit.trailLength = length;
        return this;
    }

    public UnitBuilder trailOffset(float x, float y) {
        unit.waveTrailX = x;
        unit.waveTrailY = y;
        return this;
    }

}