package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class CollectibleEntity extends Entity {
    public CollectibleEntity(Position position) {
        super(position);
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}
