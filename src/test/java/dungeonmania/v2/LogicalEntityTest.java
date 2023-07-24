// package dungeonmania.v2;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Tag;
// import org.junit.jupiter.api.Test;

// import dungeonmania.DungeonManiaController;
// import dungeonmania.mvp.TestUtils;
// import dungeonmania.response.models.DungeonResponse;
// // import dungeonmania.util.Direction;
// import dungeonmania.util.Position;

// import static org.junit.jupiter.api.Assertions.*;

// public class LogicalEntityTest {

//     private boolean boulderAt(DungeonResponse res, int x, int y) {
//         Position pos = new Position(x, y);
//         return TestUtils.getEntitiesStream(res, "boulder").anyMatch(it -> it.getPosition().equals(pos));
//     }

//     @Test
//     @Tag("3-1")
//     @DisplayName("Test pushing a boulder")
//     public void pushBoulder() {
//         DungeonManiaController dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicalEntitiesTest_1", "c_logicalEntitiesTest_1");
//         System.out.println(res.getDungeonId());
//         System.out.println(res.getInventory());
//         System.out.println(res.getEntities());

//         // // Player moves boulder onto switch, activating circuit
//         // res = dmc.tick(Direction.RIGHT);
//         // assertTrue(boulderAt(res, 2, 0));
//         // assertEquals(new Position(1, 0), TestUtils.getPlayer(res).get().getPosition());
//     }
// }
