package dungeonmania.entities;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    public static final int DEFAULT_MOVEMENT_FACTOR = 0;
    private int movFac;

    public SwampTile(Position position, int movFac) {
        super(position);
        this.movFac = movFac;
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if ((entity instanceof Mercenary && ((Mercenary) entity).isAllied()
                && ((Mercenary) entity).isAdjacentToPlayer()) || entity instanceof Player) {
            return;
        }
        if (entity instanceof Enemy && !((Enemy) entity).isStuck()) {
            ((Enemy) entity).setTicksStuckInSwamp(movFac);
        }
    }
}
