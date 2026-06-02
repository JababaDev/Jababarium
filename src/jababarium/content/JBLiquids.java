package jababarium.content;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.Liquids;
import mindustry.type.Liquid;

public class JBLiquids {
    public static Seq<Liquid> streams = Seq.with();

    public static Liquid nectron, aerial, mercury, argon;

    public static Liquid water, slag, oil, cryofluid, arkycite, gallium, neoplasm, ozone, hydrogen, nitrogen, cyanogen;

    public static void load() {
        nectron = new Liquid("nectron", Color.valueOf("#46A644")) {{
            viscosity = 0.4f;
            temperature = 0.8f;
            heatCapacity = 0.4f;
            coolant = false;
            explosiveness = 4f;
            lightColor = Color.valueOf("ff990080");
            flammability = 0.5f;
        }};
        aerial = new Liquid("aerial", Color.valueOf("#27829C")) {{
            viscosity = 0.1f;
            coolant = false;
            lightColor = Color.valueOf("ff990080");
        }};
        mercury = new Liquid("mercury", Color.valueOf("#A1A1A1")){{
            flammability = 0.2f;
            coolant = false;
            lightColor = Color.valueOf("ff990080");
        }};
        argon = new Liquid("argon", Color.valueOf("#A817D1")) {{
            coolant = true;
            heatCapacity = 0.1f;
            lightColor = Color.valueOf("ff990080");
        }};

        water = Liquids.water;
        slag = Liquids.slag;
        oil = Liquids.oil;
        cryofluid = Liquids.cryofluid;
        arkycite = Liquids.arkycite;
        gallium = Liquids.gallium;
        neoplasm = Liquids.neoplasm;
        ozone = Liquids.ozone;
        hydrogen = Liquids.hydrogen;
        nitrogen = Liquids.nitrogen;
        cyanogen = Liquids.cyanogen;
    }
}
