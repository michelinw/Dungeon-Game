package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.movementStrategy.DefaultRandomMovementStrategy;
import dungeonmania.entities.enemies.movementStrategy.PlayerInvincibilityEnemyMovement;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        if (!this.isStuck()) {
            if (game.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
                setNextPositionStrategy(new PlayerInvincibilityEnemyMovement());
            } else {
                setNextPositionStrategy(new DefaultRandomMovementStrategy());
            }
            Position nextPos = getNextPosition(game);
            game.getMap().moveTo(this, nextPos);
        }
    }
}
