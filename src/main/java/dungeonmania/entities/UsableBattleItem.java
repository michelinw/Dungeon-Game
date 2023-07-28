package dungeonmania.entities;

import dungeonmania.Game;

public interface UsableBattleItem {
    public void use(Game game);

    public int getDurability();
}
