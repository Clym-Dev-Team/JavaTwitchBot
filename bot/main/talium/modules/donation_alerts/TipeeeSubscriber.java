package talium.modules.donation_alerts;

import talium.inputs.TipeeeStream.DonationEvent;
import talium.modules.donation_goal.DonationGoal;
import talium.modules.donation_goal.GoalTemplateContext;
import talium.system.Out;
import talium.system.eventSystem.Subscriber;

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
        baseValues.put("goal", goal);
        baseValues.put("donation", donationContext);
        Out.Twitch.sendNamedTemplate("alerts.tipeee.donation", baseValues);
    }
}
