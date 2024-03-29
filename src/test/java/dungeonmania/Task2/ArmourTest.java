package dungeonmania.Task2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class ArmourTest {
    @Test
    @Tag("16-1")
    public void pickUpArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_armour", "c_armour");

        assertEquals(1, TestUtils.getEntities(res, "armour").size());
        assertEquals(0, TestUtils.getInventory(res, "armour").size());

        // pick up armour
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "armour").size());
        assertEquals(0, TestUtils.getEntities(res, "armour").size());
    }
}
