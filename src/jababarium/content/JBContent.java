package jababarium.content;

import arc.Core;
import arc.func.Func;
import arc.func.Prov;
import arc.graphics.g2d.TextureRegion;

import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.gen.LogicIO;
import mindustry.logic.LAssembler;
import mindustry.logic.LStatement;

public class JBContent extends Content {

    public static TextureRegion arrowRegion, pointerRegion;

    public static void loadPriority() {
        new JBContent().load();
    }

    public static void registerStatement(String name, Func<String[], LStatement> func, Prov<LStatement> prov) {
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.addUnique(prov);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.error;
    }

    public void load() {
        if (Vars.headless)
            return;

        arrowRegion = Core.atlas.find("jababarium-jump-gate-arrow");
        pointerRegion = Core.atlas.find("jababarium-jump-gate-pointer");
    }
}