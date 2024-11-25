package talium.system.coinsWatchtime;

import com.github.twitch4j.helix.domain.Chatter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import talium.inputs.Twitch4J.TwitchApi;
import talium.system.coinsWatchtime.chatter.ChatterRepo;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Requests the twitch user list (list of active chatters) and computes the difference between the two sets.
 * These differences are then used by coins and watchtime services to add coins and watchtime for each user
 */
@Component
public class TwitchUserListService {
    private static final Logger logger = LoggerFactory.getLogger(TwitchUserListService.class);

    private static final ScheduledExecutorService CHATTER_UPDATE_SERVICE;

    private static ChatterRepo chatterRepo;
    private static final int POLLING_INTERVAL_SECONDS = 60;
    private static final int COIN_PAYOUT_INTERVAL_SECONDS = 600;
    private static final int COIN_PAYOUT_AMOUNT = 1;

    @Autowired
    public void setChatterService(ChatterRepo chatterRepo) {
        TwitchUserListService.chatterRepo = chatterRepo;
    }

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("CHATTER_UPDATE_EXECUTOR").build();
        CHATTER_UPDATE_SERVICE = Executors.newSingleThreadScheduledExecutor(namedThreadFactory);
        CHATTER_UPDATE_SERVICE.scheduleAtFixedRate(TwitchUserListService::refreshUserList, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private static List<String> getUserList() {
        return TwitchApi.getUserList().stream().map(Chatter::getUserId).toList();
    }

    private static void refreshUserList() {
        // catch because otherwise the scheduler would end on any runtime error
        try {
            logger.info("Refreshing user list");
            if (!TwitchApi.isOnline()) {
                logger.debug("Channel online: false");
                return;
            }
            logger.debug("Channel online: true");
            var viewerList = getUserList();
            for (var user : viewerList) {
                // check if an ID exist in the DB and insert a default value item in a single operation
                // alternative would be checking and then making a separate insert
                chatterRepo.addDefaultIfNotExist(user);

                chatterRepo.incrementLastCoinsGain(user, POLLING_INTERVAL_SECONDS);
                // increment the coin amount by the payout amount, and reset the lastPayoutTime if the amount of seconds since last payout exceeds the interval
                chatterRepo.incrementCoinsIfTimeSincePayout(user, COIN_PAYOUT_INTERVAL_SECONDS, COIN_PAYOUT_AMOUNT);

            }
            // adds [seconds] amount onto the coins of each user. Alternative would be to get the amount for each user, and send a separate update
            chatterRepo.incrementWatchtimeBySeconds(viewerList, POLLING_INTERVAL_SECONDS);
        } catch (Exception e) {
            logger.error("Failed to refresh user list", e);
        }
    }
}
