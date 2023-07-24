package dungeonmania.entities.logicalentities;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;

public class LogicBombActivator extends LogicalEntity {
    private List<Bomb> bombs = new ArrayList<>();

    public LogicBombActivator(Position position, String rule) {
        super(position, rule);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        return;
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void unsubscribe(Bomb bomb) {
        bombs.remove(bomb);
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }
}
