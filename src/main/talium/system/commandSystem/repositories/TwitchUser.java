package talium.system.commandSystem.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Disabled;
import org.springframework.stereotype.Component;

/**
 * TwitchUser, lediglich dazu gedacht, um einen TwitchUser in einer Chatnachricht zu repräsentieren,
 * und ist nicht dazu gedacht in der Datenbank gespeichert zu werden, denn die Twitch Permissions und Badges könnten
 * sich während der Laufzeit ändern
 * <p>
 * Wenn eine Nachricht Systemintern entsteht/simuliert wird, werden beim Erstellen standard werte eingefügt:
 * name = -SYSTEM-
 * id = -SYSTEM-
 * permission = OWNER
 * subscriberMonths = Integer.MAX_VALUE
 * subscriptionTier = Integer.MAX_VALUE
 */
public record TwitchUser(
        String id,
        String name,
        TwitchUserPermission permission,
        int subscriberMonths,
        int subscriptionTier
) {
}
