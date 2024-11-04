package talium.system.panelAuth.persistence;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Holds information about the active sessions of each user.
 * Is used to authenticate incoming requests
 */
@Entity
@Table(name = "sys_paneluser_sessions")
public class Session {
    @Id
    String accessToken;

    String userAgent;

    Instant lastRefreshedAt;

    @ManyToOne(fetch = FetchType.LAZY) @NotNull
    private PanelUser panelUser;

    public Session() {
    }

    public Session(String accessToken, String userAgent, Instant lastRefreshedAt, @NotNull PanelUser panelUser) {
        this.accessToken = accessToken;
        this.userAgent = userAgent;
        this.lastRefreshedAt = lastRefreshedAt;
        this.panelUser = panelUser;
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

    public @NotNull PanelUser botUser() {
        return panelUser;
    }

    public Session setLastRefreshedAt(Instant lastRefreshedAt) {
        this.lastRefreshedAt = lastRefreshedAt;
        return this;
    }
}
