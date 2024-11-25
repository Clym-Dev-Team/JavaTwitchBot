package talium.system.coinsWatchtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import talium.system.coinsWatchtime.chatter.ChatterService;

import java.util.List;

@Component
public class CoinsService {

    private static ChatterService chatterService;
    private static final int COIN_POLLING_INTERVALS_NEEDED = 10;
    private static final int COIN_PAYOUT_AMOUNT = 1;

    @Autowired
    public void setChatterService(ChatterService chatterService) {
        CoinsService.chatterService = chatterService;
    }

    static void addCoins(List<String> newChatterList, List<String> leftChatters, boolean isOnline) {
        if (!isOnline) {
            return;
        }

        chatterService.addCoinsToUser(newChatterList, 60, COIN_POLLING_INTERVALS_NEEDED * 60, COIN_PAYOUT_AMOUNT);
    }
}
