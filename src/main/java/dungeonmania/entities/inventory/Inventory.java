package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        List<String> result = new ArrayList<>();

        if (wood >= 1 && arrows >= 3) {
            result.add("bow");
        }
        if (shieldCanBeBuilt()) {
            result.add("shield");
        }
        if (midnightArmourCanBeBuilt()) {
            result.add("midnight_armour");
        }
        if (sceptreCanBeBuilt()) {
            result.add("sceptre");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, boolean remove, boolean forceShield, EntityFactory factory) {

        List<Wood> wood = getEntities(Wood.class);
        List<Arrow> arrows = getEntities(Arrow.class);
        List<Treasure> treasure = getEntities(Treasure.class);
        List<Key> keys = getEntities(Key.class);
        List<Sword> sword = getEntities(Sword.class);
        List<SunStone> sunStone = getEntities(SunStone.class);

        if (bowCanBeBuilt(forceShield)) {
            if (remove) {
                removeItems(wood, 1);
                removeItems(arrows, 3);
            }
            return factory.buildBow();

        } else if (shieldCanBeBuilt()) {
            if (remove) {
                removeItems(wood, 2);
                if (treasure.size() >= 1) {
                    items.remove(treasure.get(0));
                } else {
                    items.remove(keys.get(0));
                }
            }
            return factory.buildShield();

        } else if (midnightArmourCanBeBuilt()) {
            if (remove) {
                items.remove(sword.get(0));
                items.remove(sunStone.get(0));
            }
            return factory.buildMidnightArmour();

        } else if (sceptreCanBeBuilt()) {
            if (remove) {
                if (wood.size() < 1) {
                    removeItems(arrows, 2);
                } else {
                    removeItems(wood, 1);
                }
                if (keys.size() >= 1) {
                    removeItems(keys, 1);
                } else if (treasure.size() - sunStone.size() >= 1) {
                    removeItems(treasure, 1);
                }
                removeItems(sunStone, 1);
            }
            return factory.buildSceptre();
        }
        return null;
    }

    private void removeItems(List<? extends InventoryItem> itemList, int count) {
        for (int i = 0; i < count; i++) {
            items.remove(itemList.get(i));
        }
    }

    public boolean shieldCanBeBuilt() {
        List<Wood> wood = getEntities(Wood.class);
        List<Treasure> treasure = getEntities(Treasure.class);
        List<Key> keys = getEntities(Key.class);
        List<SunStone> sunStone = getEntities(SunStone.class);

        if (wood.size() >= 2 && (treasure.size() >= 1 || keys.size() >= 1 || sunStone.size() >= 1)) {
            return true;
        }
        return false;
    }

    public boolean bowCanBeBuilt(boolean forceShield) {
        List<Wood> wood = getEntities(Wood.class);
        List<Arrow> arrows = getEntities(Arrow.class);
        if ((wood.size() >= 1 && arrows.size() >= 3 && !forceShield)) {
            return true;
        }
        return false;
    }

    public boolean midnightArmourCanBeBuilt() {
        List<SunStone> sunStone = getEntities(SunStone.class);
        List<Sword> sword = getEntities(Sword.class);
        if (sword.size() >= 1 && sunStone.size() >= 1) {
            return true;
        }
        return false;
    }

    public boolean sceptreCanBeBuilt() {
        List<Wood> wood = getEntities(Wood.class);
        List<Arrow> arrows = getEntities(Arrow.class);
        List<Treasure> treasure = getEntities(Treasure.class);
        List<Key> keys = getEntities(Key.class);
        List<SunStone> sunStone = getEntities(SunStone.class);

        if ((wood.size() >= 1 || arrows.size() >= 2) && (keys.size() >= 1 || treasure.size() >= 1)
                && sunStone.size() >= 1) {
            return true;
        }
        return false;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null || getFirst(MidnightArmour.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon1 = getFirst(Sword.class);
        BattleItem weapon2 = getFirst(Bow.class);
        BattleItem weapon3 = getFirst(MidnightArmour.class);
        if (weapon1 == null && weapon2 == null) {
            return weapon3;
        } else if (weapon2 == null && weapon3 == null) {
            return weapon1;
        }
        return weapon2;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }

    public boolean hasSceptre() {
        return getFirst(Sceptre.class) != null;
    }

    public Sceptre getSceptre() {
        return getFirst(Sceptre.class);
    }

    public boolean hasSunStone() {
        return getFirst(SunStone.class) != null;
    }

}
