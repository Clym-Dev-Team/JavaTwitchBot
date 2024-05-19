package main.modules.donation_goal;

public record DonationGoalJson(
        String name,
        Double target,
        Double current,
        String alertText
) {}
