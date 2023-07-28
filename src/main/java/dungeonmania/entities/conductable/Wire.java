package dungeonmania.entities.conductable;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logicalentities.Rule;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends LogicBombActivator implements Conductor {
    public Wire(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER), Rule.TRUE);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void subscribe(Bomb bomb, GameMap map) {
        addBomb(bomb);
        if (this.isActivatedTick()) {
            setActivated(map.getGame().getTick(), true, map);
            this.getBombs().stream().forEach(b -> b.notify(map));
        }
    }
}
