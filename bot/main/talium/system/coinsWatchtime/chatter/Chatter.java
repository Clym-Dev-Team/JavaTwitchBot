package talium.system.coinsWatchtime.chatter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatter_data")
public class Chatter {
    @Id
    public String twitchUserId;

    public long watchtimeSeconds;
    public long coins;
    public int secondsSinceLastCoinsGain;

    public Chatter(String twitchUserId) {
        this.twitchUserId = twitchUserId;
        this.watchtimeSeconds = 0;
        this.coins = 0;
        this.secondsSinceLastCoinsGain = 0;
    }

    public Chatter() {
    }
}
