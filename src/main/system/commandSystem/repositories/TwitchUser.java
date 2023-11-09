package main.system.commandSystem.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static main.system.commandSystem.repositories.TwitchUserPermission.*;

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
@Component
@Table(name = "Core-Twitch_ChatUser")
@Entity
public class TwitchUser {
    @Id private String id;
    private String name;
    private TwitchUserPermission permission;
    private int subscriberMonths;
    private int subscriptionTier;

//    private static TwitchUserRepo repo;

//    @Autowired
//    public void setRepo(TwitchUserRepo twitchUserRepo) {
//        repo = twitchUserRepo;
//    }

    TwitchUser() {
    }

    public TwitchUser(String id, String name, int subscriberMonths, int subscriptionTier, TwitchUserPermission permission) {
        this.id = id;
        this.name = name;
        this.subscriberMonths = subscriberMonths;
        this.subscriptionTier = subscriptionTier;
        this.permission = permission;
    }

    public static TwitchUser getSystemUser() {
        return new TwitchUser("-SYSTEM-", "-SYSTEM-",Integer.MAX_VALUE, Integer.MAX_VALUE, SYSTEM);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public TwitchUserPermission permission() {
        return permission;
    }

    public int subscriberMonths() {
        return subscriberMonths;
    }

    public int subscriptionTier() {
        return subscriptionTier;
    }
}
