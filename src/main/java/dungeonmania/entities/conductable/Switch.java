package dungeonmania.entities.conductable;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logicalentities.Rule;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends LogicBombActivator implements Conductor {
    private boolean activated;

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER), Rule.TRUE);
    }

    @Override
    public void subscribe(Bomb bomb, GameMap map) {
        addBomb(bomb);
        if (activated) {
            setActivated(map.getGame().getTick(), true, map);
            this.getBombs().stream().forEach(b -> b.notify(map));
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
            setActivated(map.getGame().getTick(), activated, map);
            this.getBombs().stream().forEach(b -> b.notify(map));
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
            setActivated(map.getGame().getTick(), activated, map);
        }
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        return;
    }
}
