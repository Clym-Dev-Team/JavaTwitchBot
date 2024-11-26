package talium.system.coinsWatchtime.chatter;

public record ChatterDTO(String twitchUserId, long watchtimeSeconds, long coins) {

    public ChatterDTO(Chatter chatter) {
        this(chatter.twitchUserId, chatter.coins, chatter.watchtimeSeconds);
    }

    public Chatter toChatter(int secondsSinceLastCoinsGain) {
        return new Chatter(twitchUserId, coins, watchtimeSeconds, secondsSinceLastCoinsGain);
    }
}
