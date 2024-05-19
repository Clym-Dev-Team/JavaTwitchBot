package main.modules.donation_alerts;

import main.inputs.TipeeeStream.DonationEvent;
import main.modules.donation_goal.DonationGoal;
import main.modules.donation_goal.GoalTemplateContext;
import main.system.Out;
import main.system.eventSystem.Subscriber;

import java.util.HashMap;
import java.util.List;

public class TipeeeSubscriber {

    @Subscriber
    public static void handleTipeeeEvent(DonationEvent event) {
        List<DonationGoal> activeGoals = DonationGoal.repo.findByActive(true);
        for (DonationGoal goal: activeGoals) {
            goal.amountInGoal += event.amount;
            DonationGoal.repo.save(goal);
        }

        //TODO print new goal values
        DonationTemplateContext donationContext = new DonationTemplateContext(event);
        HashMap<String, Object> baseValues = new HashMap<>();
        //TODO should be done via a system to attach additional context to a template
        GoalTemplateContext goal = new GoalTemplateContext(activeGoals.getFirst());
        Out.Twitch.sendNamedTemplate("alert", "tipeee", "donation", baseValues);
    }
}
