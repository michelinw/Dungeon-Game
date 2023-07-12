package dungeonmania.entities.enemies.movementStrategy;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.util.Position;

public interface MovementStrategy {
    Position getNextPosition(Enemy enemy, Game game);
}
