package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class DestroyEnemies implements Goal {
    private int enemyGoal;

    public DestroyEnemies(int enemyGoal) {
        this.enemyGoal = enemyGoal;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        int numSpawnersRemaining = game.getMap().getEntities(ZombieToastSpawner.class).size();
        int numEnemiesDestroyed = game.getNumEnemiesDestroyed();
        return numEnemiesDestroyed >= enemyGoal && numSpawnersRemaining == 0;
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":enemies";
    }
}
