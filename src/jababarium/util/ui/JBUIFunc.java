package jababarium.util.ui;

import arc.Core;
import arc.audio.Sound;
import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.style.Drawable;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Scaling;
import arc.util.Time;
import mindustry.gen.Tex;

import static mindustry.Vars.*;

public class JBUIFunc {
    private static long lastToast, waiting;

    
    private static void scheduleToast(float time, Runnable run) {
        if (waiting > 5)
            return;
        long duration = (int) ((time + 1.25f) * 1000);
        long since = Time.timeSinceMillis(lastToast);
        if (since > duration) {
            lastToast = Time.millis();
            run.run();
        } else {
            waiting++;
            Time.runTask((duration - since) / 1000f * 60f, () -> {
                waiting--;
                run.run();
            });
            lastToast += duration;
        }
    }

    private static void scheduleToast(Runnable run) {
        long duration = (int) (3.5 * 1000);
        long since = Time.timeSinceMillis(lastToast);
        if (since > duration) {
            lastToast = Time.millis();
            run.run();
        } else {
            Time.runTask((duration - since) / 1000f * 60f, run);
            lastToast += duration;
        }
    }

    public static void showToast(Drawable icon, String text, Sound sound) {
        if (state.isMenu())
            return;
        if (headless)
            return;

        scheduleToast(() -> {
            sound.play();

            Table table = new Table(Tex.pane2);
            table.update(() -> {
                if (state.isMenu() || !ui.hudfrag.shown) {
                    table.remove();
                }
            });
            table.margin(12);
            table.image(icon).size(48).scaling(Scaling.fit).pad(-4).padLeft(12);
            table.add(text).wrap().width(280f).get().setAlignment(Align.center, Align.center);
            table.pack();

            
            Table container = Core.scene.table();
            container.top().add(table);
            container.setTranslation(0, table.getPrefHeight());
            container.actions(
                    Actions.translateBy(0, -table.getPrefHeight(), 1f, Interp.fade), Actions.delay(2.5f),
                    
                    Actions.run(() -> container.actions(Actions.translateBy(0, table.getPrefHeight(), 1f, Interp.fade),
                            Actions.remove())));
        });
    }
}
