package jababarium.content;

import arc.Core;
import arc.func.Cons;
import arc.func.Func;
import arc.func.Prov;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;

import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.gen.LogicIO;
import mindustry.logic.LAssembler;
import mindustry.logic.LStatement;
import jababarium.Jababarium;

public class JBContent extends Content {

    public static TextureRegion arrowRegion, pointerRegion;

    public static TextureRegion 
    raid, objective, fleet, capture;

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

    Texture loadTex(String name, Cons<Texture> modifier) {
        Texture tex = new Texture(
                Jababarium.MOD.root.child("textures").child(name + (name.endsWith(".png") ? "" : ".png")));
        modifier.get(tex);

        return tex;
    }
}