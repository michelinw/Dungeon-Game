package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.movementStrategy.MovementStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable {
    private BattleStatistics battleStatistics;
    private MovementStrategy nextPositionStrategy;

    private int ticksStuckInSwamp = 0;

    public void setNextPositionStrategy(MovementStrategy nextPositionStrategy) {
        this.nextPositionStrategy = nextPositionStrategy;
    }

    public Position getNextPosition(Game game) {
        return nextPositionStrategy.getNextPosition(this, game);
    }

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        // if (entity instanceof Player) {
        //     Player player = (Player) entity;
        //     map.getGame().battle(player, this);
        // }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    public abstract void move(Game game);

    public boolean isStuck() {
        if (ticksStuckInSwamp != 0) {
            ticksStuckInSwamp -= 1;
            return true;
        }
        return false;
    }

    public void setTicksStuckInSwamp(int stuck) {
        ticksStuckInSwamp = stuck;
    }
}
