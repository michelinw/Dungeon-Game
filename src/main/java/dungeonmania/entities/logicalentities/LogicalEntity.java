package dungeonmania.entities.logicalentities;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import dungeonmania.entities.conductable.*;

public class LogicalEntity extends Entity {
    private int activatedTick = -1; // -1 means it is not activated by default
    private int lastActionTick = -1;
    private int lastBombCheckTick = -1;
    private String rule;
    private List<LogicalEntity> adjacentLogicalEntities = new ArrayList<>();

    public LogicalEntity(Position position, String rule) {
        super(position.asLayer(Entity.ITEM_LAYER));
        this.rule = rule;
        if (rule == Rule.NONE) { // no rule, means activated immediately
            activatedTick = 0;
        }
    }

    public void subscribeLogicalEntities(LogicalEntity e) {
        adjacentLogicalEntities.add(e);
    }

    public void unsubscribeLogicalEntities(LogicalEntity e) {
        adjacentLogicalEntities.remove(e);
    }

    private boolean isAndActivated() {
        boolean isAct = true;

        for (LogicalEntity e : adjacentLogicalEntities) {
            if (!e.isActivatedTick()) {
                isAct = false;
                break;
            }
        }
        return isAct;
    }

    private boolean isOrActivated() {
        boolean isAct = false;
        for (LogicalEntity e : adjacentLogicalEntities) {
            if (e.isActivatedTick()) {
                isAct = true;
                break;
            }
        }
        return isAct;
    }

    private boolean isXorActivated() {
        int actCount = 0;
        for (LogicalEntity e : adjacentLogicalEntities) {
            if (e.isActivatedTick()) {
                actCount++;
            }
        }
        return actCount == 1;
    }

    private boolean isCoAndActivated(int tick) {
        boolean isAct = true;
        int coAndTick = adjacentLogicalEntities.get(0).getActivatedTick();
        for (LogicalEntity e : adjacentLogicalEntities) {
            // not activated or not activated in the same tick
            if (!e.isActivatedTick() || (e.getActivatedTick() != coAndTick)) {
                isAct = false;
                break;
            }
        }
        return isAct;
    }

    public boolean isActivatedLogicalEntity(int tick, boolean activatedAction) {
        boolean isAct = false;
        if (tick != -1) {
            switch (rule) {
            case Rule.NONE:
            case Rule.TRUE:
                isAct = activatedAction;
                break;
            case Rule.AND:
                isAct = isAndActivated();
                break;
            case Rule.OR:
                isAct = isOrActivated();
                break;
            case Rule.XOR:
                isAct = isXorActivated();
                break;
            case Rule.CO_AND:
                isAct = isCoAndActivated(tick);
                break;
            default:
                isAct = false;
            }
        }

        return isAct;
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        return;
    }

    public boolean isActivatedTick() {
        return activatedTick != -1;
    }

    public int getActivatedTick() {
        return activatedTick;
    }

    public int getLastActionTick() {
        return lastActionTick;
    }

    public boolean setActivatedTick(int tick, boolean activatedAction) {
        boolean isActOld = this.isActivatedTick();
        // get current tick active status
        boolean isAct = this.isActivatedLogicalEntity(tick, activatedAction);

        if (isActOld != isAct) { // status change
            activatedTick = isAct ? tick : -1;
        }
        lastActionTick = tick;
        return isAct;
    }

    public void activateAdjacent(int tick, boolean activatedAction, GameMap map) {
        for (LogicalEntity c : adjacentLogicalEntities) {
            if (c instanceof Conductor) {
                // Conductor like Swith and Wire will check once in the same tick,
                // it will proceed further chain activate
                if (tick > c.getLastActionTick()) {
                    c.setActivatedTick(tick, activatedAction);
                    c.activateAdjacent(tick, activatedAction, map);
                }
            } else {
                // other logical entity can be check multiple time in the same tick,
                // there is no more further chain activation
                if (tick >= c.getLastActionTick()) {
                    c.setActivatedTick(tick, activatedAction);
                }
            }
        }
    }

    public boolean setActivated(int tick, boolean activatedAction, GameMap map) {
        boolean isAct = this.setActivatedTick(tick, activatedAction);
        this.activateAdjacent(tick, activatedAction, map);
        this.detonateAdjacent(tick, map);
        return isAct;
    }

    public void detonateAdjacent(int tick, GameMap map) {
        for (LogicalEntity c : adjacentLogicalEntities) {
            if (tick > c.lastBombCheckTick) {
                c.lastBombCheckTick = tick;
                if (c instanceof Bomb && ((Bomb) c).getState() == Bomb.State.PLACED && c.getRule() != Rule.NONE) {
                    ((Bomb) c).notify(map);
                } else if (c instanceof Conductor) {
                    c.detonateAdjacent(tick, map);
                }
            }
        }
    }

    public String getRule() {
        return rule;
    }
}
