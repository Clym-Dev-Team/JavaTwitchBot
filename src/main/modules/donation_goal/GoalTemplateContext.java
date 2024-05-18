package main.modules.donation_goal;

public record GoalTemplateContext(
        String targetAmount,
        String currentAmount,
        String currentPercent,
        String displayName,
        String progressBar
) {}
