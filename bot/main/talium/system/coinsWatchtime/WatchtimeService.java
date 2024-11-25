package talium.system.coinsWatchtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import talium.system.coinsWatchtime.chatter.ChatterService;

import java.util.List;

@Component
public class WatchtimeService {

    private static ChatterService chatterService;

    @Autowired
    public void setChatterService(ChatterService chatterService) {
        WatchtimeService.chatterService = chatterService;
    }

    /**
     * Adds one minute of watch-time to every user currently in the chat.
     * </br>
     * This approach has a maximal error of one minute (per watch session), since we would add too much time on join, and incorrectly not add time once the user left. </br>
     * (This is because we just check at one instance in time, but the users join and leave in between our polls) </br>
     * This over and under claiming of seconds watched, with a polling frequency of 60 Seconds means a maximum error of 60 Seconds, and an average error of 30 Seconds.
     *
     * @param viewerList List of viewer IDs in the Chat
     * @param isOnline
     */
    static void addMinuteOfWatchtime(List<String> viewerList, boolean isOnline) {
        if (!isOnline) {
            return;
        }
        chatterService.addWatchtimeSeconds(viewerList, 60);
    }
}
