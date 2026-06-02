package jababarium.expand.units.UnitEntity;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.entities.Damage;
import mindustry.gen.UnitEntity;
import mindustry.gen.Teamc;
import mindustry.gen.Groups;
import mindustry.entities.units.UnitController;
import mindustry.ai.types.CommandAI;
import jababarium.content.JBFx;

public class KamikazeUnitEntity extends UnitEntity {

    public float targetX = Float.NaN;
    public float targetY = Float.NaN;

    public float explodeRadius = 180f;
    public float explodeDamage = 230f;
    public float autoTargetRange = 200f;

    private static final Vec2 tmp = new Vec2();

    @Override
    public void controller(UnitController next){
        super.controller(next);

        if(next instanceof CommandAI){
        }
    }

    public void commandPosition(Vec2 pos){
        targetX = pos.x;
        targetY = pos.y;
    }

    public void moveTo(float x, float y){
        targetX = x;
        targetY = y;
    }

    @Override
    public void update(){
        super.update();

        UnitController ctrl = controller();
        if(ctrl instanceof CommandAI){
            CommandAI ai = (CommandAI)ctrl;

            if(ai.attackTarget != null){
                targetX = ai.attackTarget.x();
                targetY = ai.attackTarget.y();
            } else {
                targetX = Float.NaN;
                targetY = Float.NaN;
            }
        }

        if(Float.isNaN(targetX)){
            Teamc nearestEnemy = null;
            float nearestDst = autoTargetRange;

            for(var enemy : Groups.unit){
                if(enemy.team != team && !enemy.dead){
                    float dst = dst(enemy);
                    if(dst < nearestDst){
                        nearestDst = dst;
                        nearestEnemy = enemy;
                    }
                }
            }

            for(var building : Groups.build){
                if(building.team != team){
                    float dst = dst(building);
                    if(dst < nearestDst){
                        nearestDst = dst;
                        nearestEnemy = building;
                    }
                }
            }

            if(nearestEnemy != null){
                targetX = nearestEnemy.x();
                targetY = nearestEnemy.y();
            }
        }

        if(Float.isNaN(targetX)){
            return;
        }

        tmp.set(targetX, targetY).sub(x, y);
        float dst = tmp.len();

        if(dst <= 15f){
            explode();
            return;
        }

        tmp.nor().scl(type.speed);

        vel.set(tmp);
        rotation = Mathf.angle(tmp.x, tmp.y);
    }

    void explode(){
        JBFx.kamikazeExplosion.at(x, y);

        Damage.damage(
                x, y,
                explodeRadius,
                explodeDamage
        );

        kill();
    }
}