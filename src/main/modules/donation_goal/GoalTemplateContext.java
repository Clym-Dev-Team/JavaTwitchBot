package main.modules.donation_goal;

import main.system.ASCIIProgressbar;

public class GoalTemplateContext {
    public final String targetAmount;
    public final String currentAmount;
    public final String currentPercentDecimals;
    public final String currentPercentRounded;
    public final String displayName;
    public final String progressBar;


    public GoalTemplateContext(DonationGoal goal) {
        this.targetAmount = String.format("%.2f", goal.targetAmount);
        this.currentAmount = String.format("%.2f", goal.amountInGoal);
        double percent = (goal.amountInGoal / goal.targetAmount) * 100;
        this.currentPercentDecimals = String.format("%.2f", percent);
        this.currentPercentRounded = String.format("%.0f", percent);
        this.displayName = goal.displayName;
        this.progressBar = ASCIIProgressbar.bar(goal.amountInGoal, goal.targetAmount, 10, "■", " ", false, false);
    }
}
