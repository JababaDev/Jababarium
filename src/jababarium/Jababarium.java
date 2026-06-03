package jababarium;

import arc.util.*;
import jababarium.content.*;
import jababarium.expand.block.CraftingBlock;
import jababarium.expand.units.UnitConstructors;
import mindustry.mod.*;

public class Jababarium extends Mod {
    public static final String MOD_NAME = "jababarium";
    public static Mods.LoadedMod MOD;

    public Jababarium() {

    }

    @Override
    public void loadContent() {
        JBItems.load();
        JBLiquids.load();
        JBSounds.load();
        JBBullets.load();
        JBUnits.load();
        JBBlocks.load();
        JBContent.loadPriority();
        CraftingBlock.load();
        JBOres.load();
        UnitConstructors.load();
    }

    public static String name(String name) {
        return MOD_NAME + "-" + name;
    }

}
