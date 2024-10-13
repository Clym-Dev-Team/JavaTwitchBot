package talium.modules.donation_goal;

import talium.system.ASCIIProgressbar;
import talium.system.stringTemplates.Formatter;

public class GoalTemplateContext {
    public final String targetAmount;
    public final String currentAmount;
    public final String currentPercentDecimals;
    public final String currentPercentRounded;
    public final String displayName;
    public final String progressBar;

    public GoalTemplateContext(DonationGoal goal) {
        this.targetAmount = Formatter.formatDoubleComma(goal.targetAmount);
        this.currentAmount = Formatter.formatDoubleComma(goal.amountInGoal);
        double percent = (goal.amountInGoal / goal.targetAmount) * 100;
        this.currentPercentDecimals = Formatter.formatDoubleComma(percent);
        this.currentPercentRounded = Formatter.formatDoubleComma( percent);
        this.displayName = goal.displayName;
        this.progressBar = ASCIIProgressbar.bar(goal.amountInGoal, goal.targetAmount, 10, "■", " ", false, false);
    }
}
