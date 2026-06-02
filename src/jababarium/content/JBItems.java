package jababarium.content;

import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.type.Item;

public class JBItems {

    public static Item
            feronium, cryostal, pulsarite, singularium, chronite, sergium, amalgam, adamantium;

    public static Item
            scrap, copper, lead, graphite, coal, titanium, thorium, silicon, plastanium,
            phaseFabric, surgeAlloy, sporePod, sand, blastCompound, pyratite, metaglass,
            beryllium, tungsten, oxide, carbide, fissileMatter, dormantCyst;

    public static void load() {
        feronium = new Item("feronium", Color.valueOf("#A17F7F")){{
            cost = 2f;
            explosiveness = 3f;
        }};
        cryostal = new Item("cryostal", Color.valueOf("#63D9E0")){{
            cost = 2f;
            radioactivity = 0.03f;
        }};
        pulsarite = new Item("pulsarite", Color.valueOf("#931DBF")){{
            cost = 3f;
            explosiveness = 5f;
            radioactivity = 0.1f;
            charge = 25f;
        }};
        singularium = new Item("singularium", Color.valueOf("#8AEB8F")){{
            cost = 4f;
            explosiveness = 6f;
            charge = 30f;
        }};
        chronite = new Item("chronite", Color.valueOf("#918E9C")){{
            cost = 5f;
        }};
        sergium = new Item("sergium", Color.valueOf("#113899")){{
            cost = 3f;
            explosiveness = 11f;
            radioactivity = 1f;
            charge = 5f;
        }};
        amalgam = new Item("amalgam", Color.valueOf("#AD8A4E")){{
            cost = 3f;
            explosiveness = 4f;
            charge = 15f;
        }};
        adamantium = new Item("adamantium", Color.valueOf("#D67E7E")){{
            cost = 2f;
            hardness = 7;
        }};

        scrap = Items.scrap;
        copper = Items.copper;
        lead = Items.lead;
        graphite = Items.graphite;
        coal = Items.coal;
        titanium = Items.titanium;
        thorium = Items.thorium;
        silicon = Items.silicon;
        plastanium = Items.plastanium;
        phaseFabric = Items.phaseFabric;
        surgeAlloy = Items.surgeAlloy;
        sporePod = Items.sporePod;
        sand = Items.sand;
        blastCompound = Items.blastCompound;
        pyratite = Items.pyratite;
        metaglass = Items.metaglass;
        beryllium = Items.beryllium;
        tungsten = Items.tungsten;
        oxide = Items.oxide;
        carbide = Items.carbide;
        fissileMatter = Items.fissileMatter;
        dormantCyst = Items.dormantCyst;

    }
}
