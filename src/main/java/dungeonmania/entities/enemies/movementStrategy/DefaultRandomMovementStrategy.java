package dungeonmania.entities.enemies.movementStrategy;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.util.Position;

public class DefaultRandomMovementStrategy implements MovementStrategy {
    @Override
    public Position getNextPosition(Enemy enemy, Game game) {
        List<Position> pos = enemy.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> game.getMap().canMoveTo(enemy, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            return enemy.getPosition();
        } else {
            Random randGen = new Random();
            return pos.get(randGen.nextInt(pos.size()));
        }
    }
}
