package main.modules.TokioStream;

import main.system.ASCIIProgressbar;
import main.system.hookSystem.Hook;

public class GoalHook {

    @Hook
    public static String Goal() {
        Goal goal = Goal.repo.findByKey("primary");
        return ASCIIProgressbar.bar(goal.currentAmount, goal.targetAmount, 10, "■", "⠀", true, true);
    }
}
