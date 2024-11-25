package talium.system.coinsWatchtime;

import com.github.twitch4j.helix.domain.Chatter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import talium.inputs.Twitch4J.TwitchApi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static talium.system.coinsWatchtime.CoinsService.addCoins;
import static talium.system.coinsWatchtime.WatchtimeService.addMinuteOfWatchtime;

/**
 * Requests the twitch user list (list of active chatters) and computes the difference between the two sets.
 * These differences are then used by coins and watchtime services to add coins and watchtime for each user
 */
public class TwitchUserListService {
    private static List<String> viewerList = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(TwitchUserListService.class);

    private static final ScheduledExecutorService CHATTER_UPDATE_SERVICE;

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("CHATTER_UPDATE_EXECUTOR").build();
        CHATTER_UPDATE_SERVICE = Executors.newSingleThreadScheduledExecutor(namedThreadFactory);
        CHATTER_UPDATE_SERVICE.scheduleAtFixedRate(TwitchUserListService::refreshUserList, 0, 1, TimeUnit.MINUTES);
    }

    private static List<String> getUserList() {
        return TwitchApi.getUserList().stream().map(Chatter::getUserId).toList();
    }

    private static void refreshUserList() {
        // catch because otherwise the scheduler would end on any runtime error
        try {
            logger.info("Refreshing user list");
            var oldUserList = viewerList;
            viewerList = getUserList();
            var leftChatters = getLeftChatters(oldUserList, viewerList);
//            var isOnline = TwitchApi.isOnline();
            var isOnline = true;
            logger.debug("Channel online: {}", isOnline);
            addMinuteOfWatchtime(viewerList, isOnline);
            addCoins(viewerList, leftChatters.stream().toList(), isOnline);
        } catch (Exception e) {
            logger.error("Failed to refresh user list", e);
        }
    }

    private static Set<String> getLeftChatters(List<String> lastChatters, List<String> currentChatters) {
        var setCopy = new HashSet<>(lastChatters);
        currentChatters.forEach(setCopy::remove);
        return setCopy;
    }

    private static Set<String> getJoinedChatters(List<String> lastChatters, List<String> currentChatters) {
        var setCopy = new HashSet<>(currentChatters);
        lastChatters.forEach(setCopy::remove);
        return setCopy;
    }
}
