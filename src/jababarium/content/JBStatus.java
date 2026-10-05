package jababarium.content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;

public class JBStatus {
    public static StatusEffect ionizedStatus, chronosStop, intercepted;

    public static void load() {
        ionizedStatus = new StatusEffect("ionized-status") {
            {
                color = Color.valueOf("72d4ff");
                damage = 2f;
                effect = Fx.chainLightning;
                speedMultiplier = 0.6f;
                reloadMultiplier = 0.8f;
            }
        };

        chronosStop = new StatusEffect("chronos-stop") {
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

        intercepted = new StatusEffect("intercepted") {
            {
                damage = 0;

                speedMultiplier = 0.55f;
                healthMultiplier = 0.75f;
                damageMultiplier = 0.75f;

                effectChance = 0.05f;
                effect = JBFx.square45_4_45;
                color = Pal.accent;
            }
        };
    }
}
