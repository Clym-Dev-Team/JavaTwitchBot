package main.modules.donation_goal;

import main.inputs.TipeeeStream.DonationEvent;
import main.system.Out;
import main.system.eventSystem.Subscriber;

import java.util.HashMap;
import java.util.List;

public class TipeeeSubscriber {

    @Subscriber
    public static void handleTipeeeEvent(DonationEvent event) {
        for (DonationGoal goal: DonationGoalCache.activeGoals) {
            goal.amountInGoal += event.amount;
            DonationGoal.repo.save(goal);
        }

        //TODO print new goal values
        DonationTemplateContext donationContext = new DonationTemplateContext(event);
        HashMap<String, Object> baseValues = new HashMap<>();
        //TODO should be done via a system to attach additional context to a template
        GoalTemplateContext goal = new GoalTemplateContext(DonationGoalCache.activeGoals.getFirst());
        Out.Twitch.sendNamedTemplate("alert", "tipeee", "donation", baseValues);
    }
}
