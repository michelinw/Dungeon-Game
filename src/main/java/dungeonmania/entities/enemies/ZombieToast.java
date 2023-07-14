package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
// import dungeonmania.entities.Boulder;
// import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.movementStrategy.DefaultRandomMovementStrategy;
import dungeonmania.entities.enemies.movementStrategy.InvisibilityMovementStrategy;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    private List<Position> movementTrajectory;
    private int nextPositionElement;
    private boolean forward;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
        movementTrajectory = position.getAdjacentPositions();
        nextPositionElement = 1;
        forward = true;
    }

    // private void updateNextPosition() {
    //     if (forward) {
    //         nextPositionElement++;
    //         if (nextPositionElement == movementTrajectory.size()) {
    //             nextPositionElement = 0;
    //         }
    //     } else {
    //         nextPositionElement--;
    //         if (nextPositionElement == -1) {
    //             nextPositionElement = movementTrajectory.size() - 1;
    //         }
    //     }
    // }

    @Override
    public void move(Game game) {
        if (game.getPlayer().getEffectivePotion() instanceof InvisibilityPotion) {
            setNextPositionStrategy(new InvisibilityMovementStrategy());
        } else {
            setNextPositionStrategy(new DefaultRandomMovementStrategy());
        }
        Position nextPos = getNextPosition(game);
        game.getMap().moveTo(this, nextPos);
        // List<Entity> entities = game.getMap().getEntities(nextPos);
        // if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof Boulder)) {
        //     forward = !forward;
        //     updateNextPosition();
        //     updateNextPosition();
        // }
        // nextPos = movementTrajectory.get(nextPositionElement);
        // entities = game.getMap().getEntities(nextPos);
        // if (entities == null || entities.size() == 0
        //         || entities.stream().allMatch(e -> e.canMoveOnto(game.getMap(), this))) {
        //     game.getMap().moveTo(this, nextPos);
        //     updateNextPosition();
        // }
    }

}
