package dungeonmania.entities.logicalentities;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import dungeonmania.entities.conductable.*;
import dungeonmania.entities.logicalentities.logicStrategy.*;

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

    public List<LogicalEntity> getAdjacentLogicalEntities() {
        return adjacentLogicalEntities;
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
                AndRule andRule = new AndRule();
                isAct = andRule.isActive(tick, this);
                break;
            case Rule.OR:
                OrRule orRule = new OrRule();
                isAct = orRule.isActive(tick, this);
                break;
            case Rule.XOR:
                XorRule xorRule = new XorRule();
                isAct = xorRule.isActive(tick, this);
                break;
            case Rule.CO_AND:
                CoAndRule coAndRule = new CoAndRule();
                isAct = coAndRule.isActive(tick, this);
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
