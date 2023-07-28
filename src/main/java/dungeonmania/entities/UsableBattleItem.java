package dungeonmania.entities;

import dungeonmania.Game;

public interface UsableBattleItem extends BattleItem {
    public void use(Game game);

    public int getDurability();
}
