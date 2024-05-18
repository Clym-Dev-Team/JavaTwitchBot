package main.modules.donation_goal;

import main.system.resetableCache.ResetableCache;

import java.util.List;

public class DonationGoalCache implements ResetableCache {
    static List<DonationGoal> activeGoals = DonationGoal.repo.findByActive(true);

    @Override
    public String cacheName() {
        return "DonationGoalCache";
    }

    @Override
    public String cacheDescription() {
        return "Caches the Active Donationgoals from the DB";
    }

    @Override
    public void rebuildCache() {
        activeGoals = DonationGoal.repo.findByActive(true);
    }
}
