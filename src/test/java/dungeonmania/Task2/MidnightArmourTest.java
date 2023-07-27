package dungeonmania.Task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
// import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MidnightArmourTest {
    @Test
    @DisplayName("Test midnight craft")
    public void craftMidnightArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("midnightarmour_buildables", "simple");

        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build sceptre
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
    }

    @Test
    @DisplayName("Test create armour with zombies in the dungeon")
    public void createArmourWithZombies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("midnightarmour_zombies", "midnight");

        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // try to create midnight armour and fail
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());

        // materials in inventory remain since armour is not made
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test MidnightArmour applies buffs correctly")
    public void midnightArmourGivesExtraProtection() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("midnightarmour_buildables", "midnight");
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
    }
}
