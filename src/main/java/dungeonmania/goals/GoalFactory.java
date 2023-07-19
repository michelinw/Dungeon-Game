package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalFactory {
    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals;
        switch (jsonGoal.getString("goal")) {
        case "AND":
            subgoals = jsonGoal.getJSONArray("subgoals");
            return new And(createGoal(subgoals.getJSONObject(0), config),
                    createGoal(subgoals.getJSONObject(1), config));
        case "OR":
            subgoals = jsonGoal.getJSONArray("subgoals");
            return new Or(createGoal(subgoals.getJSONObject(0), config), createGoal(subgoals.getJSONObject(1), config));
        case "exit":
            return new ExitG();
        case "boulders":
            return new Boulders();
        case "treasure":
            int treasureGoal = config.optInt("treasure_goal", 1);
            return new Treasure(treasureGoal);
<<<<<<< HEAD
        case "enemies":
            int enemiesGoal = config.optInt("enemy_goal", 1);
=======
        case "destroyEnemies":
            int enemiesGoal = config.optInt("enemies_goal", 1);
>>>>>>> 4f7d069 (Finished basic tests and implementation)
            return new DestroyEnemies(enemiesGoal);
        default:
            return null;
        }
    }
}
