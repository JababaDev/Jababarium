package jababarium.content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.type.StatusEffect;

public class JBStatus {
    public static StatusEffect ionizedStatus = new StatusEffect("ionized-status") {
        {
            color = Color.valueOf("72d4ff");
            damage = 2f;
            effect = Fx.chainLightning;
            speedMultiplier = 0.6f;
            reloadMultiplier = 0.8f;
        }
    };

    public static StatusEffect chronosStop = new StatusEffect("chronos-stop") {
        {
            color = Color.valueOf("7fd6ff");
            speedMultiplier = 0f;
            reloadMultiplier = 0f;
            buildSpeedMultiplier = 0f;
            dragMultiplier = 9999f;
            disarm = true;
            effect = Fx.none;
        }
    };
}
