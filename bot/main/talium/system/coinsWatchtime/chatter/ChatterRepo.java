package talium.system.coinsWatchtime.chatter;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatterRepo extends CrudRepository<Chatter, String> {

    @Transactional
    @Modifying
    @Query(value = "IF NOT EXISTS(SELECT chatter_data.twitch_user_id FROM chatter_data WHERE twitch_user_id = ?1) THEN " +
            "INSERT INTO chatter_data VALUE (?1, 0, 0, 0);" +
            "END IF;", nativeQuery = true)
    void addDefaultIfNotExist(String chatterId);

    @Modifying
    @Query("UPDATE Chatter SET watchtimeSeconds = watchtimeSeconds + ?2 WHERE twitchUserId in ?1")
    @Transactional
    void incrementWatchtimeBySeconds(List<String> chatters, int seconds);

    @Modifying
    @Transactional
    @Query("UPDATE Chatter SET secondsSinceLastCoinsGain = secondsSinceLastCoinsGain + ?2 WHERE twitchUserId = ?1")
    void incrementLastCoinsGain(String chatterId, int coins);

    @Modifying
    @Transactional
    @Query("UPDATE Chatter SET coins = coins + ?3, secondsSinceLastCoinsGain = 0 WHERE twitchUserId = ?1 AND secondsSinceLastCoinsGain >= ?2")
    void incrementCoinsIfTimeSincePayout(String chatters, int payout_interval, int payout_amount);
}
