package main.modules.TokioStream;

import main.inputs.TipeeeStream.DonationEvent;
import main.system.commandSystem.CommandProcessor;
import main.system.commandSystem.repositories.Message;
import main.system.eventSystem.Subscribe;

public class DonationHandler {

    @Subscribe(EventClass = DonationEvent.class)
    public static void addAmount(DonationEvent donation) {
        Goal old = Goal.repo.findByKey("primary");
        if (old == null) {
            old = new Goal(0., 0.);
            Goal.repo.save(old);
        }
        old.currentAmount += donation.amount;
//        double current = Goal.repo.getCurrentAmountByKey("primary");
        Goal.repo.updateCurrentAmountByKey(old.currentAmount, "primary");

        CommandProcessor.processMessage(Message.simulatedMessage("!donationAlert"));
    }
}
