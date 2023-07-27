package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.movementStrategy.DefaultRandomMovementStrategy;
import dungeonmania.entities.enemies.movementStrategy.InvisibilityMovementStrategy;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;

import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;
    private boolean underControl = false;
    private int controlLength;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
        this.controlLength = 0;
    }

    public boolean isAllied() {
        if (underControl) {
            return true;
        }
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (isAllied())
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {

        int treasureAmount = player.countEntityOfType(Treasure.class) - player.countEntityOfType(SunStone.class);
        return bribeRadius >= 0 && treasureAmount >= bribeAmount;
    }

    public void setControlLength(int controlLength) {
        this.controlLength = controlLength;
    }

    public int getControlLength() {
        return controlLength;
    }

    public boolean isUnderControl() {
        return underControl;
    }

    public void setUnderControl(boolean underControl) {
        this.underControl = underControl;
        if (underControl) {
            allied = true;
        } else {
            allied = false;
        }
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        int j = 0;
        InventoryItem nextTreasure;
        for (int i = 0; i < bribeAmount; i++) {
            while (j < player.getInventory().getItems().size()) {
                nextTreasure = player.getInventory().getItems().get(j);
                if (nextTreasure instanceof Treasure && !(nextTreasure instanceof SunStone)) {
                    player.getInventory().remove(nextTreasure);
                    break;
                }
                j++;
            }
        }
    }

    public boolean isAdjacentToPlayer() {
        return isAdjacentToPlayer;
    }

    public void setAdjacentToPlayer(boolean isAdjacentToPlayer) {
        this.isAdjacentToPlayer = isAdjacentToPlayer;
    }

    @Override
    public void interact(Player player, Game game) {
        if (!underControl) {
            allied = true;
            bribe(player);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
                isAdjacentToPlayer = true;
        }
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        if (isAllied()) {
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                    : map.dijkstraPathFind(getPosition(), player.getPosition(), this);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
                isAdjacentToPlayer = true;
            else if (map.getPlayer().getEffectivePotion() instanceof InvisibilityPotion) {
                setNextPositionStrategy(new DefaultRandomMovementStrategy());
                nextPos = getNextPosition(game);
            }
        } else {
            if (player.getEffectivePotion() instanceof InvisibilityPotion) {
                setNextPositionStrategy(new InvisibilityMovementStrategy());
                nextPos = getNextPosition(game);
            } else {
                nextPos = map.dijkstraPathFind(getPosition(), player.getPosition(), this);
            }
        }
        map.moveTo(this, nextPos);
        if (this.isUnderControl()) {
            System.out.println("a");
            int length = this.getControlLength() - 1;
            this.setControlLength(length);
            if (length == 0) {
                this.setUnderControl(false);
            }
        }
    }

    @Override
    public boolean isInteractable(Player player) {
        int numSceptres = player.getInventory().getEntities(Sceptre.class).size();
        if (numSceptres > 0)
            return true;
        return !isAllied() && canBeBribed(player);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!isAllied())
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }
}
