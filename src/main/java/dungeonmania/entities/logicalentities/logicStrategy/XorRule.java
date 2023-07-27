package dungeonmania.entities.logicalentities.logicStrategy;

import dungeonmania.entities.logicalentities.LogicalEntity;

public class XorRule implements LogicStrategy {

    public XorRule() {
    }

    @Override
    public boolean isActive(int tick, LogicalEntity logicalEntity) {
        int actCount = 0;
        for (LogicalEntity e : logicalEntity.getAdjacentLogicalEntities()) {
            if (e.isActivatedTick()) {
                actCount++;
            }
        }
        return actCount == 1;
    }

}
