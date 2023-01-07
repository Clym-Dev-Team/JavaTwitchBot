package standard.repositories;

import java.util.Arrays;
import java.util.HashSet;

/**
 * TwitchUser, lediglich dazu gedacht, um einen TwitchUser in einer Chatnachricht zu repräsentieren,
 * und ist nicht dazu gedacht in der Datenbank gespeichert zu werden, denn die Twitch Permissions und Badges könnten
 * sich während der Laufzeit ändern
 */
public class TwitchUser {
    private final String id;
    private final String name;
    private final HashSet<TwitchUserPermissions> permissions;
    private final int subscriberMonths;
    private final int subscriptionTier;

    public TwitchUser(String id, String name, int subscriberMonths, int subscriptionTier, HashSet<TwitchUserPermissions> permissions) {
        this.id = id;
        this.name = name;
        this.subscriberMonths = subscriberMonths;
        this.subscriptionTier = subscriptionTier;
        this.permissions = permissions;
    }

    public TwitchUser(String id, String name, int subscriberMonths, int subscriptionTier, TwitchUserPermissions... permissions) {
        this.id = id;
        this.name = name;
        this.subscriberMonths = subscriberMonths;
        this.subscriptionTier = subscriptionTier;
        this.permissions = new HashSet<>(Arrays.asList(permissions));
    }
}
