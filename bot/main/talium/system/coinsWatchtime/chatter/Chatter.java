package talium.system.coinsWatchtime.chatter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatter_data")
public class Chatter {
    @Id
    String twitchUserId;

    long watchtimeSeconds;
    long coins;
    int secondsSinceLastCoinsGain;

    public Chatter(String twitchUserId, long watchtimeSeconds, long coins, int secondsSinceLastCoinsGain) {
        this.twitchUserId = twitchUserId;
        this.watchtimeSeconds = watchtimeSeconds;
        this.coins = coins;
        this.secondsSinceLastCoinsGain = secondsSinceLastCoinsGain;
    }

    public Chatter() {
    }
}
