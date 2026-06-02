package jababarium.content;

import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;

public class JBStatusEffects {

    public static StatusEffect intercepted;

    public static void load() {

        intercepted = new StatusEffect("intercepted") {{
            damage = 0;

            speedMultiplier = 0.55f;
            healthMultiplier = 0.75f;
            damageMultiplier = 0.75f;

            effectChance = 0.05f;
            effect = JBFx.square45_4_45;
            color = Pal.accent;
        }};
    }
}
