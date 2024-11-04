package talium.system.panelAuth.panelUser;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * Primary user object, holds User Preferences and other User specific, but not necessarily Security critical Account Information
 */
@Entity
@Table(name = "sys-botuser")
public class PanelUser {
    @Id
    public String twitchUserId;
    public Instant accountCreationTime;

    public PanelUser() {
    }

    public PanelUser(String twitchUserId) {
        this.twitchUserId = twitchUserId;
        this.accountCreationTime = Instant.now();
    }

    @Override
    public String toString() {
        return STR."BotUser{twitchUserId='\{twitchUserId}', creationTime=\{accountCreationTime}";
    }
}
