package talium.system.coinsWatchtime.chatter;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatterService {
    ChatterRepo chatterRepo;

    public ChatterService(ChatterRepo chatterRepo) {
        this.chatterRepo = chatterRepo;
    }

    /**
     * Add Watchtime for each user in DB.
     * This method makes heavy use of complex (and native) sql queries to offload computation and data modification to the database because of the possible thousands of users that need to be processed here.
     * @param chatterIds list of userIds that should receive coins
     * @param seconds how many coins should be added to each user
     */
    @Transactional
    public void addWatchtimeSeconds(List<String> chatterIds, int seconds) {
        for (String chatterId : chatterIds) {
            // check if an ID exist in the DB and insert a default value item in a single operation
            // alternative would be checking and then making a separate insert
            chatterRepo.addDefaultIfNotExist(chatterId);
        }
        // adds [seconds] amount onto the coins of each user. Alternative would be to get the amount for each user, and send a separate update
        chatterRepo.incrementWatchtimeBySeconds(chatterIds, seconds);
    }

    @Transactional
    public void addCoinsToUser(List<String> chatters, int checkInterval, int payoutIntervalSeconds, int coinsToAdd) {
        for (String chatterId : chatters) {
            chatterRepo.addDefaultIfNotExist(chatterId);
            chatterRepo.incrementLastCoinsGain(chatterId, checkInterval);
            chatterRepo.incrementCoinsIfTimeSincePayout(chatterId, payoutIntervalSeconds, coinsToAdd);
        }
    }
}