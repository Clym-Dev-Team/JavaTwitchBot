package talium.system.coinsWatchtime.chatter;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatterRepo extends CrudRepository<Chatter, String> {

    @Modifying
    @Query("UPDATE Chatter SET watchtimeSeconds = watchtimeSeconds + ?2 WHERE twitchUserId in ?1")
    @Transactional
    void incrementChatterWatchtimeBySeconds(List<String> chatters, int seconds);

}
