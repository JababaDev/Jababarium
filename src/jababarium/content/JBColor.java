package jababarium.content;

import arc.graphics.Color;
import arc.graphics.Colors;
import mindustry.content.Items;
import mindustry.graphics.Pal;

public class JBColor {
    public static Color ancient = Items.surgeAlloy.color.cpy().lerp(Pal.accent, 0.115f),
            ancientLight = ancient.cpy().lerp(Color.white, 0.7f),
            ancientLightMid = ancient.cpy().lerp(Color.white, 0.4f),
            ancientDark = ancient.cpy().lerp(Color.black, 0.995f),
            ancientHeat = Color.red.cpy().mul(1.075f),
            ally = new Color(0, 0, 1, 0.15f), hostile = new Color(1, 0, 0, 0.15f),
            deeperBlue = Color.valueOf("#778ff2"),
            lightSky = Color.valueOf("#8DB0FF"),
            lightSkyBack = lightSky.cpy().lerp(Color.white, 0.2f),
            lightSkyMiddle = lightSky.cpy().lerp(Color.white, 0.6f),
            lightSkyFront = lightSky.cpy().lerp(Color.white, 0.77f),
            darkEnrColor = Pal.sapBullet.cpy().mul(1.075f).lerp(Color.white, 0.075f),
            thurmixRed = Color.valueOf("#FF9492"),
            thurmixRedLight = Color.valueOf("#FFCED0"),
            thurmixRedDark = thurmixRed.cpy().lerp(Color.black, 0.9f),
            darkEnr = darkEnrColor.cpy().lerp(Color.black, 0.85f),
            darkEnrFront = darkEnrColor.cpy().lerp(Color.white, 0.45f),
            trail = Color.lightGray.cpy().lerp(Color.gray, 0.65f),
            thermoPst = Color.valueOf("CFFF87").lerp(Color.white, 0.15f),
            powerArea = Pal.power.cpy().a(0.5f),
            xenEmpty = Color.valueOf("#a3a9ad"),
            xenAlpha = Color.valueOf("#abc8dc"),
            yellow = Color.valueOf("#f0ffce"),
            xenGamma = Color.valueOf("#78c9ff"),
            nectrone = Color.valueOf("#4EC225"),
            cryostal = Color.valueOf("#2AE4EB"),
            dark = Color.valueOf("#0f1010"),
            chroniteBase = ancient.cpy().lerp(Color.gray, 0.55f),
            chroniteDark = chroniteBase.cpy().lerp(Color.black, 0.75f),
            chroniteMid = chroniteBase.cpy().lerp(Color.white, 0.12f),
            chroniteLight = chroniteBase.cpy().lerp(Color.white, 0.35f),
            chroniteEdge = chroniteBase.cpy().lerp(Pal.accent, 0.06f),
            chroniteGlow = chroniteBase.cpy().lerp(Color.white, 0.22f).a(0.55f),
            pulsariteBase = Color.gray.cpy().lerp(JBColor.nectrone, 0.25f),
            green = Color.valueOf("##5CE65C"),
            sporeLight = Color.valueOf("ffaae0"),
            sporeMid = Color.valueOf("ff9ed5"),
            sporePink = Color.valueOf("ff6ec7"),
            sporeDark = Color.valueOf("e55f9a"),
            thurmixDeep = Color.valueOf("#1a0005"),
            thurmixCore = Color.valueOf("#ffd0d0"),
            thurmixFlare = Color.valueOf("#ff9999");

    /** Unit branch palettes, taken from the accent colors of each branch's top unit sprite. */
    public static Color
            nemesisVoid = Color.valueOf("071a16"),
            nemesisDeep = Color.valueOf("0f3a31"),
            nemesisDark = Color.valueOf("1f7a66"),
            nemesisMid = Color.valueOf("36b79b"),
            nemesisLight = Color.valueOf("69e0c7"),
            nemesisPale = Color.valueOf("c4f7ea"),
            nemesisGlow = Color.valueOf("ecfffa"),
            oblivionVoid = Color.valueOf("1a0606"),
            oblivionDeep = Color.valueOf("3a0f0f"),
            oblivionDark = Color.valueOf("7a2424"),
            oblivionMid = Color.valueOf("a63d3d"),
            oblivionLight = Color.valueOf("e65555"),
            oblivionPale = Color.valueOf("ffc4c4"),
            oblivionGlow = Color.valueOf("fff0f0"),
            tidebreakerVoid = Color.valueOf("08081c"),
            tidebreakerDeep = Color.valueOf("14143d"),
            tidebreakerDark = Color.valueOf("33338a"),
            tidebreakerMid = Color.valueOf("5757c1"),
            tidebreakerLight = Color.valueOf("8aa3f4"),
            tidebreakerPale = Color.valueOf("d2dcff"),
            tidebreakerGlow = Color.valueOf("f0f3ff"),
            ocelexisVoid = Color.valueOf("1c0808"),
            ocelexisDeep = Color.valueOf("3d1414"),
            ocelexisDark = Color.valueOf("8a3a3a"),
            ocelexisMid = Color.valueOf("c45f5f"),
            ocelexisLight = Color.valueOf("f19583"),
            ocelexisPale = Color.valueOf("ffd9cf"),
            ocelexisGlow = Color.valueOf("fff3ef"),
            broodmotherVoid = Color.valueOf("200606"),
            broodmotherDeep = Color.valueOf("4a1010"),
            broodmotherDark = Color.valueOf("a02c2c"),
            broodmotherMid = Color.valueOf("ff6363"),
            broodmotherLight = Color.valueOf("ff9f9f"),
            broodmotherPale = Color.valueOf("ffdcdc"),
            broodmotherGlow = Color.valueOf("fff2f2");

    static {
        Colors.put("heal", Pal.heal);
        Colors.put("ancient", ancient);
        Colors.put("reddust", Pal.redderDust);
        Colors.put("ammo", Pal.ammo);
    }
}