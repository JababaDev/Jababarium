package jababarium.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;

public class JBOres {

    public static Block adamantiumOre;

    public static void load() {
        adamantiumOre = new OreBlock("ore-adamantium") {{
            oreDefault = true;
            variants = 3;
            oreThreshold = 0.95F;
            oreScale = 20.380953F;
            itemDrop = JBItems.adamantium;
            useColor = true;
            playerUnmineable = true;
            wallOre = false;
        }};
    }
}
