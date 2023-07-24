package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.logicalentities.LogicBombActivator;
import dungeonmania.entities.logicalentities.LogicalEntity;
import dungeonmania.map.GameMap;

public class Bomb extends LogicalEntity implements InventoryItem {
    public enum State {
        SPAWNED, INVENTORY, PLACED
    }

    public static final int DEFAULT_RADIUS = 1;
    private State state;
    private int radius;

    private List<LogicBombActivator> subs = new ArrayList<>();

    public Bomb(Position position, int radius, String rule) {
        super(position, rule);
        state = State.SPAWNED;
        this.radius = radius;
    }

    public void subscribe(LogicBombActivator e) {
        this.subs.add(e);
    }

    public void notify(GameMap map) {
        if (this.isActivatedTick()) {
            explode(map);
        }
    }

    public void pickedUp() {
        if (state == State.SPAWNED) {
            this.state = State.INVENTORY;
            subs.stream().forEach(a -> a.unsubscribe(this));
            subs.stream().forEach(a -> a.unsubscribeLogicalEntities(this));
            subs.stream().forEach(a -> this.unsubscribeLogicalEntities(a));
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public void onPutDown(GameMap map, Position p) {
        translate(Position.calculatePositionBetween(getPosition(), p));
        map.addEntity(this);
        this.state = State.PLACED;
        List<Position> adjPosList = getPosition().getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node).stream().filter(e -> (e instanceof LogicalEntity))
                    .collect(Collectors.toList());
            entities.stream().map(LogicalEntity.class::cast).forEach(e -> this.subscribeLogicalEntities(e));
            entities.stream().map(LogicalEntity.class::cast).forEach(e -> e.subscribeLogicalEntities(this));
        });
        adjPosList.stream().forEach(node -> {
            List<Entity> logicActivator = map.getEntities(node).stream().filter(a -> (a instanceof LogicBombActivator))
                    .collect(Collectors.toList());
            logicActivator.stream().map(LogicBombActivator.class::cast).forEach(a -> a.subscribe(this, map));
            logicActivator.stream().map(LogicBombActivator.class::cast).forEach(a -> this.subscribe(a));
        });
    }

    public void explode(GameMap map) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                List<Entity> entities = map.getEntities(new Position(i, j));
                entities = entities.stream().filter(e -> !(e instanceof Player)).collect(Collectors.toList());
                for (Entity e : entities)
                    map.destroyEntity(e);
            }
        }
    }

    public State getState() {
        return state;
    }
}
