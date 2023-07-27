package dungeonmania.goals;

import dungeonmania.Game;

public class Or implements Goal {
    private Goal goal2;
    private Goal goal1;

    public Or(Goal goal2, Goal goal1) {
        this.goal2 = goal2;
        this.goal1 = goal1;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        return goal1.achieved(game) || goal2.achieved(game);
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game)) return "";
        if (achieved(game))
                return "";
            else
                return "(" + goal1.toString(game) + " OR " + goal2.toString(game) + ")";
    }
}
