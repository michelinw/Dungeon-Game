package dungeonmania.Task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

public class EnemyGoalTest {
    @Test
    @Tag("17-1")
    @DisplayName("Testing destroying enemies goal")
    public void destroyEnemiesBasic() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_destroyEnemiesGoalBasicTest", "c_destroyEnemiesGoalBasicTest");

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        String id = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        res = dmc.interact(id);

        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
    }

    @Test
    @Tag("17-2")
    @DisplayName("Test achieving a defeat multiple enemy goal")
    public void multipleEnemiesGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_destroyEnemiesGoalMultipleTest", "c_destroyEnemiesGoalMultipleTests");
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.UP);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("17-3")
    @DisplayName("Test destroying spawners goal")
    public void noEnemiesMultipleSpawnersGoal() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_destroyEnemiesGoalSpawnersTest", "c_destroyEnemiesGoalSpawnersTest");

        List<String> spawnerIds = TestUtils.getEntitiesStream(res, "zombie_toast_spawner").map(e -> {
            return e.getId();
        }).collect(Collectors.toList());

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.DOWN);
        res = dmc.interact(spawnerIds.get(0));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.DOWN);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.interact(spawnerIds.get(1));
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("17-4")
    @DisplayName("Test destroying spawners and enemies goal")
    public void multipleEnemiesMultipleSpawnersGoal() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_destroyEnemiesGoalComplexTest", "c_destroyEnemiesGoalComplexTest");

        // collect spawners
        List<String> spawnerIds = TestUtils.getEntitiesStream(res, "zombie_toast_spawner").map(e -> {
            return e.getId();
        }).collect(Collectors.toList());

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.DOWN);
        // destroy spawner 0
        res = dmc.interact(spawnerIds.get(0));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.DOWN);
        // destroy spawner 1
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.interact(spawnerIds.get(1));
        // kill mercenary
        res = dmc.tick(Direction.LEFT);
        // find spider
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        // kill spider
        res = dmc.tick(Direction.DOWN);
        assertEquals("", TestUtils.getGoals(res));
    }

}
