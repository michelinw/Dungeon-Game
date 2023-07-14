package dungeonmania.entities.enemies.movementStrategy;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class InvisibilityMovementStrategy implements MovementStrategy {
    @Override
    public Position getNextPosition(Enemy enemy, Game game) {
        Position plrDiff = Position.calculatePositionBetween(game.getMap().getPlayer().getPosition(),
                enemy.getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(enemy.getPosition(), Direction.RIGHT)
                : Position.translateBy(enemy.getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(enemy.getPosition(), Direction.UP)
                : Position.translateBy(enemy.getPosition(), Direction.DOWN);
        Position offset = enemy.getPosition();
        if (plrDiff.getY() == 0 && game.getMap().canMoveTo(enemy, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && game.getMap().canMoveTo(enemy, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (game.getMap().canMoveTo(enemy, moveX))
                offset = moveX;
            else if (game.getMap().canMoveTo(enemy, moveY))
                offset = moveY;
            else
                offset = enemy.getPosition();
        } else {
            if (game.getMap().canMoveTo(enemy, moveY))
                offset = moveY;
            else if (game.getMap().canMoveTo(enemy, moveX))
                offset = moveX;
            else
                offset = enemy.getPosition();
        }
        return offset;
    }
}
