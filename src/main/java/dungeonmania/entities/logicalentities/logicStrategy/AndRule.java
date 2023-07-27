package dungeonmania.entities.logicalentities.logicStrategy;

import dungeonmania.entities.logicalentities.LogicalEntity;

public class AndRule implements LogicStrategy {

    public AndRule() {
    }

    @Override
    public boolean isActive(int tick, LogicalEntity logicalEntity) {
        boolean isAct = true;
        for (LogicalEntity e : logicalEntity.getAdjacentLogicalEntities()) {
            if (!e.isActivatedTick()) {
                isAct = false;
                break;
            }
        }
        return isAct;
    }

}
