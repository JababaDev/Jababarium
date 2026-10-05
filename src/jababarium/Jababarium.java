package jababarium;

import arc.*;
import jababarium.content.*;
import jababarium.expand.block.CraftingBlock;
import jababarium.expand.units.UnitConstructors;
import mindustry.game.EventType.*;
import mindustry.mod.*;

public class Jababarium extends Mod {
    public static final String MOD_NAME = "jababarium";
    public static Mods.LoadedMod MOD;

    public Jababarium() {
        Events.on(WorldLoadEvent.class, e -> JBGroups.worldInit());
        Events.on(ResetEvent.class, e -> JBGroups.clear());
    }

    @Override
    public void loadContent() {
        JBItems.load();
        JBLiquids.load();
        JBSounds.load();
        JBStatus.load();
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
