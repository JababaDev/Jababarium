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

    static {
        Colors.put("heal", Pal.heal);
        Colors.put("ancient", ancient);
        Colors.put("reddust", Pal.redderDust);
        Colors.put("ammo", Pal.ammo);
    }
}