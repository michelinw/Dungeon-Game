package dungeonmania.entities.logicalentities.logicStrategy;

import dungeonmania.entities.logicalentities.LogicalEntity;

public class OrRule implements LogicStrategy {

    public OrRule() {
    }

    @Override
    public boolean isActive(int tick, LogicalEntity logicalEntity) {
        boolean isAct = false;
        for (LogicalEntity e : logicalEntity.getAdjacentLogicalEntities()) {
            if (e.isActivatedTick()) {
                isAct = true;
                break;
            }
        }
        return isAct;
    }

}
