package dungeonmania.entities.logicalentities.logicStrategy;

import dungeonmania.entities.logicalentities.LogicalEntity;

public class CoAndRule implements LogicStrategy {

    public CoAndRule() {
    }

    @Override
    public boolean isActive(int tick, LogicalEntity logicalEntity) {
        boolean isAct = true;
        int coAndTick = logicalEntity.getAdjacentLogicalEntities().get(0).getActivatedTick();
        for (LogicalEntity e : logicalEntity.getAdjacentLogicalEntities()) {
            // not activated or not activated in the same tick
            if (!e.isActivatedTick() || (e.getActivatedTick() != coAndTick)) {
                isAct = false;
                break;
            }
        }
        return isAct;
    }

}
