package dungeonmania.entities.logicalentities;

import dungeonmania.util.Position;

public class LightBulb extends LogicalEntity {
    public LightBulb(Position position, String rule) {
        super(position, rule);
    }

    public boolean isOn() {
        return isActivatedTick();
    }
}
