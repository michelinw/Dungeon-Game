package dungeonmania.Task2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.*;

public class LogicalEntityTest {
    private boolean boulderAt(DungeonResponse res, int x, int y) {
        Position pos = new Position(x, y);
        return TestUtils.getEntitiesStream(res, "boulder").anyMatch(it -> it.getPosition().equals(pos));
    }

    @Test
    @Tag("18-1")
    @DisplayName("Test pushing a boulder onto switch, activating lightbulb and opening switch door")
    public void lightBulbAndSwitchDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalEntitiesTest_1", "c_logicalEntitiesTest_1");
        // Player moves boulder onto switch, activating circuit, turning on lightbulb and opening switch door
        res = dmc.tick(Direction.RIGHT);
        assertTrue(boulderAt(res, 2, 0));
        assertEquals(new Position(1, 0), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(1, TestUtils.countType(res, "light_bulb_on"));
        assertEquals(1, TestUtils.countType(res, "switch_door_open"));

        // Player moves boulder off switch, deactivating circuit, turning off lightbulb and closing switch door
        res = dmc.tick(Direction.RIGHT);
        assertTrue(boulderAt(res, 3, 0));
        assertEquals(new Position(2, 0), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(0, TestUtils.countType(res, "light_bulb_on"));
        assertEquals(0, TestUtils.countType(res, "switch_door_open"));
    }

    @Test
    @Tag("18-2")
    @DisplayName("Test and and xor bombs")
    public void bombTests() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalEntitiesTest_2", "simple");
        // Player moves boulder onto switch, activating circuit, turning on lightbulb and opening switch door
        res = dmc.tick(Direction.RIGHT);
        assertTrue(boulderAt(res, 2, 0));
        assertEquals(new Position(1, 0), TestUtils.getPlayer(res).get().getPosition());

        // Player moves over and picks up bomb
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // place down bomb
        assertEquals(1, TestUtils.getInventory(res, "bomb").size());
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());
        assertEquals(0, TestUtils.countType(res, "treasure"));
        assertEquals(8, TestUtils.countType(res, "wire"));

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getInventory(res, "bomb").size());
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        assertEquals(6, TestUtils.countType(res, "wire"));
    }

    @Test
    @Tag("18-3")
    @DisplayName("Test coand bomb")
    public void coAndTest() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalEntitiesTest_3", "simple");
        assertEquals(12, TestUtils.countType(res, "wire"));

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);

        assertEquals(1, TestUtils.getInventory(res, "bomb").size());
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        assertEquals(8, TestUtils.countType(res, "wire"));
    }
}
