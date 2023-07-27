package dungeonmania.entities.logicalentities.logicStrategy;

import dungeonmania.entities.logicalentities.LogicalEntity;

public interface LogicStrategy {
    public boolean isActive(int tick, LogicalEntity logicalEntity);
}
