package jababarium.expand.block.power;

import arc.math.Mathf;
import mindustry.entities.Effect;
import mindustry.world.blocks.power.ConsumeGenerator;

public class EffectPowerGenerator extends ConsumeGenerator {
    public Effect updateEffect;
    public float updateEffectChance = 0.04f;

    public EffectPowerGenerator(String name){
        super(name);
        hasItems = true;
        hasLiquids = true;
    }

    public class EffectConsumeGeneratorBuild extends ConsumeGeneratorBuild {
        @Override
        public void updateTile(){
            super.updateTile();

            if(efficiency > 0 && Mathf.chanceDelta(updateEffectChance)){
                updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4f));
            }
        }
    }
}