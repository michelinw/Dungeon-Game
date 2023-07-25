package dungeonmania.v2;

import dungeonmania.DungeonManiaController;
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
}
