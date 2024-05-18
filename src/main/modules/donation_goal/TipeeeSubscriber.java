package main.modules.donation_goal;

import main.inputs.TipeeeStream.DonationEvent;
import main.system.eventSystem.Subscriber;

import java.util.List;

public class TipeeeSubscriber {

    @Subscriber
    public static void handleTipeeeEvent(DonationEvent event) {
        for (DonationGoal goal: DonationGoalCache.activeGoals) {
            goal.amountInGoal += event.amount;
            DonationGoal.repo.save(goal);
        }

        //TODO print new goal values
    }
}
