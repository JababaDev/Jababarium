package jababarium.util.func;

import arc.func.Boolf;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Geometry;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Tmp;
import mindustry.core.World;
import mindustry.entities.Fires;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.world.Tile;

import static mindustry.Vars.indexer;
import static mindustry.Vars.world;

public class JBFunc {
    public static final Rand rand = new Rand(0);
    private static Building tmpBuilding;
    private static Tile tileParma;
    private static final Vec2 vec21 = new Vec2(),
            vec22 = new Vec2(),
            vec23 = new Vec2();

    public static Rand rand(long id) {
        rand.setSeed(id);
        return rand;
    }

    public static void randFadeLightningEffect(float x, float y, float range, float lightningPieceLength, Color color,
            boolean in) {
        randFadeLightningEffectScl(x, y, range, 0.55f, 1.1f, lightningPieceLength, color, in);
    }

    public static Position collideBuild(Team team, float x1, float y1, float x2, float y2, Boolf<Building> boolf) {
        tmpBuilding = null;

        boolean found = World.raycast(World.toTile(x1), World.toTile(y1), World.toTile(x2), World.toTile(y2),
                (x, y) -> (tmpBuilding = world.build(x, y)) != null && tmpBuilding.team != team && boolf.get(tmpBuilding));

        return found ? tmpBuilding : vec21.set(x2, y2);
    }

    public static Position collideBuildOnLength(Team team, float x1, float y1, float length, float ang, Boolf<Building> boolf) {
        vec22.trns(ang, length).add(x1, y1);
        return collideBuild(team, x1, y1, vec22.x, vec22.y, boolf);
    }

    public static void randFadeLightningEffectScl(float x, float y, float range, float sclMin, float sclMax,
            float lightningPieceLength, Color color, boolean in) {
        vec21.rnd(range).scl(Mathf.random(sclMin, sclMax)).add(x, y);
        
        
        
    }

    public static Seq<Boolf<Tile>> formats() {
        Seq<Boolf<Tile>> seq = new Seq<>(3);

        seq.add(
                tile -> world.getQuadBounds(Tmp.r1).contains(tile.getBounds(Tmp.r2)),
                tile -> tile.floor().isLiquid && !tile.cblock().solid && !tile.floor().solid && !tile.overlay().solid && !tile.block().solidifes,
                tile -> !tile.floor().isDeep() && !tile.cblock().solid && !tile.floor().solid && !tile.overlay().solid && !tile.block().solidifes
        );

        return seq;
    }

    public static Seq<Tile> getAcceptableTiles(int x, int y, int range, Boolf<Tile> bool) {
        Seq<Tile> tiles = new Seq<>(true, (int) (Mathf.pow(range, 2) * Mathf.pi), Tile.class);
        Geometry.circle(x, y, range, (x1, y1) -> {
            if ((tileParma = world.tile(x1, y1)) != null && bool.get(tileParma)) {
                tiles.add(world.tile(x1, y1));
            }
        });
        return tiles;
    }

    public static void extinguish(Teamc teamc, float range, float intensity) {
        indexer.eachBlock(teamc.team(), teamc.x(), teamc.y(), range, b -> true, b -> Fires.extinguish(b.tile, intensity));
    }
}
