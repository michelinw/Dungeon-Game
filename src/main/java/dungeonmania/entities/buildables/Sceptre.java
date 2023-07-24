package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Sceptre extends Buildable {
    private int durability;
    private int controlLength;

    public Sceptre(int controlLength) {
        super(null);
        this.controlLength = controlLength;
    }

    public int getControlLength() {
        return this.controlLength;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 1, 1));

    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public int getDurability() {
        return durability;
    }
}
