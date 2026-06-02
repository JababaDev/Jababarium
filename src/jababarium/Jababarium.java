package jababarium;

import arc.*;
import arc.util.*;
import jababarium.content.*;
import jababarium.expand.block.CraftingBlock;
import jababarium.expand.units.UnitConstructors;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

public class Jababarium extends Mod {
    public static final String MOD_NAME = "jababarium";
    public static Mods.LoadedMod MOD;

    public Jababarium() {

        Log.info("Loaded ExampleJavaMod constructor.");

        
        Events.on(ClientLoadEvent.class, e -> {
            
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("behold").row();
                
                
                dialog.cont.image(Core.atlas.find("jababarium-frog")).pad(20f).row();
                dialog.cont.button("I see", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });
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
