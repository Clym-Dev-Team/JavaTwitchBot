package talium.system.panelAuth.session;

import jakarta.persistence.*;
import talium.system.panelAuth.botUser.BotUser;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Holds information about the active sessions of each user.
 * Is used to authenticate incoming requests
 */
@Entity
@Table(name = "sys-botuser-sessions")
@IdClass(SessionId.class)
public class Session {
    @Id
    String accessToken;

    String userAgent;

    Instant lastRefreshedAt;

    @ManyToOne(fetch = FetchType.LAZY) @NotNull @Id
    private BotUser botUser;

    public Session() {
    }

    public Session(String accessToken, String userAgent, Instant lastRefreshedAt, @NotNull BotUser botUser) {
        this.accessToken = accessToken;
        this.userAgent = userAgent;
        this.lastRefreshedAt = lastRefreshedAt;
        this.botUser = botUser;
    }

    public String accessToken() {
        return accessToken;
    }

    public String userAgent() {
        return userAgent;
    }

    public Instant lastRefreshedAt() {
        return lastRefreshedAt;
    }

    public @NotNull BotUser botUser() {
        return botUser;
    }

    public Session setLastRefreshedAt(Instant lastRefreshedAt) {
        this.lastRefreshedAt = lastRefreshedAt;
        return this;
    }
}
