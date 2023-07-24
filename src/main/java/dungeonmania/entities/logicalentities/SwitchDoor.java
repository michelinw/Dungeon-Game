package dungeonmania.entities.logicalentities;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

import dungeonmania.entities.enemies.Spider;
import dungeonmania.util.Position;

public class SwitchDoor extends LogicalEntity {

    public SwitchDoor(Position position, String rule) {
        super(position, rule);
    }

    public boolean isOpen() {
        return isActivatedTick();
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return isActivatedTick() || entity instanceof Spider;
    }

}
