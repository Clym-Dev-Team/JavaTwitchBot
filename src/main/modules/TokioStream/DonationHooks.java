package main.modules.TokioStream;

import main.inputs.TipeeeStream.DonationEvent;
import main.system.hookSystem.Hook;

public class DonationHooks {
    //TODO add an output to twitch

    @Hook
    public static String lastDonationAmount() {
        DonationEvent donation = DonationEvent.repo.getMostRecent();
        return donation.amount + " " + donation.currency_symbol;
    }

    @Hook
    public static String lastDonationName() {
        DonationEvent donation = DonationEvent.repo.getMostRecent();
        return donation.tipeee_username;
    }

    @Hook
    public static String lastDonationMessage() {
        DonationEvent donation = DonationEvent.repo.getMostRecent();
        return donation.message;
    }

}
