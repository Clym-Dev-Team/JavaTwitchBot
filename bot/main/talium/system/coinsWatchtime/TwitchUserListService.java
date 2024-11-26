package talium.system.coinsWatchtime;

import com.github.twitch4j.helix.domain.Chatter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import talium.inputs.Twitch4J.TwitchApi;
import talium.system.coinsWatchtime.chatter.ChatterService;

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

    private static ChatterService chatterService;
    private static final int POLLING_INTERVAL_SECONDS = 60;
    private static final int COIN_PAYOUT_INTERVAL_SECONDS = 600;
    private static final int COIN_PAYOUT_AMOUNT = 1;

    @Autowired
    public void setChatterService(ChatterService chatterService) {
        TwitchUserListService.chatterService = chatterService;
    }

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("CHATTER_UPDATE_EXECUTOR").build();
        CHATTER_UPDATE_SERVICE = Executors.newSingleThreadScheduledExecutor(namedThreadFactory);
        CHATTER_UPDATE_SERVICE.scheduleAtFixedRate(TwitchUserListService::update, 1, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private static List<String> getUserList() {
        return TwitchApi.getUserList().stream().map(Chatter::getUserId).toList();
    }

    private static void update() {
        // catch because otherwise the scheduler would end on any runtime error
        try {
            logger.info("Refreshing user list");
            if (!TwitchApi.isOnline()) {
                logger.debug("Channel online: false");
                return;
            }
            logger.debug("Channel online: true");
            var chatters = chatterService.getChattersOrDefault(getUserList());
            for (var user : chatters) {
                user.watchtimeSeconds += POLLING_INTERVAL_SECONDS;
                user.secondsSinceLastCoinsGain += POLLING_INTERVAL_SECONDS;

                if (user.secondsSinceLastCoinsGain >= COIN_PAYOUT_INTERVAL_SECONDS) {
                    user.secondsSinceLastCoinsGain = 0;
                    user.coins += COIN_PAYOUT_AMOUNT;
                }
            }
            chatterService.saveAll(chatters);
        } catch (Exception e) {
            logger.error("Failed to refresh user list", e);
        }
    }
}
