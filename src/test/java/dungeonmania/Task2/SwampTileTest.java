package dungeonmania.Task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwampTileTest {
    @Test
    @Tag("19-1")
    @DisplayName("Mercenary walks into swamp, slowed down")
    public void mercenarySlowed() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTestMerc", "simple");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    @Tag("5-4")
    @DisplayName("Mercenary is an ally of the player so is not slowed down")
    public void allyMercNotSlowed() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTestAllyMerc", "simple");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // go to swamp tile
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // move one ahead of swamp tile
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), getMercPos(res));
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }
}
