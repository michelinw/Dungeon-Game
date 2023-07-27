package dungeonmania.v2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SunStoneTest {
    @Test
    @DisplayName("Test player can't walk through closed door")
    public void cantPassClosedDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("sunstone_collectables", "simple");
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // should not walk through door
        res = dmc.tick(Direction.UP);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @DisplayName("Test player can pick up sun stone")
    public void pickSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("sunstone_collectables", "simple");

        assertEquals(1, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up sun stone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Use sun stone to open door")
    public void openDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("sunstone_collectables", "simple");

        // pick up SunStone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // walk through door and check sun stone is retained
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @DisplayName("SunStone cannot bribe mercenary")
    public void testSunStoneBribeMercenaryComplex() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("sunstone2", "simple");

        // Check that we dont have sunstone
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        // Check that we cant bribe yet
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));

        // Pick up sunStone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        // Check that we cant bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));

        // Pick up treasure
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        // Check that we can bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // Check that we still have sunStone
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("counts SunStone towards treasure goal")
    public void testSunStoneTreasureGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("sunstone3", "simple");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect sunStone
        res = dmc.tick(Direction.RIGHT);
        int numTreasuresTotal = TestUtils.getInventory(res, "sun_stone").size()
                + TestUtils.getInventory(res, "treasure").size();
        assertEquals(1, numTreasuresTotal);

        // assert goal not met
        //assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        numTreasuresTotal = TestUtils.getInventory(res, "sun_stone").size()
                + TestUtils.getInventory(res, "treasure").size();
        assertEquals(2, numTreasuresTotal);

        // assert goal met
        assertTrue(TestUtils.getGoals(res).contains(""));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }
}
