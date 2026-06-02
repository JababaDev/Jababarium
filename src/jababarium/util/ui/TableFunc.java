package jababarium.util.ui;

import static mindustry.Vars.tilesize;

import arc.Core;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import mindustry.gen.Icon;
import arc.scene.event.InputListener;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class TableFunc {
    public static final float LEN = 60f;
    private static Table pTable = new Table(), floatTable = new Table();
    private static final Vec2 ctrlVec = new Vec2();
    public static final float OFFSET = 12f;

    public static void selectPos(Table parentT, Cons<Point2> cons) {
        Prov<Touchable> original = parentT.touchablility;
        Touchable parentTouchable = parentT.touchable;

        parentT.touchablility = () -> Touchable.disabled;

        if (!pTable.hasParent())
            ctrlVec.set(Core.camera.unproject(Core.input.mouse()));

        if (!pTable.hasParent())
            pTable = new Table(Tex.clear) {
                {
                    update(() -> {
                        if (Vars.state.isMenu()) {
                            remove();
                        } else {
                            Vec2 v = Core.camera.project(World.toTile(ctrlVec.x) * tilesize,
                                    World.toTile(ctrlVec.y) * tilesize);
                            setPosition(v.x, v.y, 0);
                        }
                    });
                }

                @Override
                public void draw() {
                    super.draw();

                    Lines.stroke(9, Pal.gray);
                    drawLines();
                    Lines.stroke(3, Pal.accent);
                    drawLines();
                    
                    
                    
                }

                private void drawLines() {
                    Lines.square(x, y, 28, 45);
                    Lines.line(x - OFFSET * 4, y, 0, y);
                    Lines.line(x + OFFSET * 4, y, Core.graphics.getWidth(), y);
                    Lines.line(x, y - OFFSET * 4, x, 0);
                    Lines.line(x, y + OFFSET * 4, x, Core.graphics.getHeight());
                }
            };

        if (!pTable.hasParent())
            floatTable = new Table(Tex.clear) {
                {
                    update(() -> {
                        if (Vars.state.isMenu())
                            remove();
                    });
                    touchable = Touchable.enabled;
                    setFillParent(true);

                    addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                            ctrlVec.set(Core.camera.unproject(x, y));
                                                                     
                                                                     
                                                                     
                            return false;
                        }
                    });
                }
            };

        
        
        

        pTable.button(Icon.cancel, Styles.emptyi, () -> {
            cons.get(Tmp.p1.set(World.toTile(ctrlVec.x), World.toTile(ctrlVec.y)));
            parentT.touchablility = original;
            parentT.touchable = parentTouchable;
            pTable.remove();
            floatTable.remove();
        }).center();

        Core.scene.root.addChildAt(Math.max(parentT.getZIndex() - 1, 0), pTable);
        Core.scene.root.addChildAt(Math.max(parentT.getZIndex() - 2, 0), floatTable);
    }
}
