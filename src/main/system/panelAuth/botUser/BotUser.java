package main.system.panelAuth.botUser;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * Primary user object, holds User Preferences and other User specific, but not necessarily Security critical Account Information
 */
@Entity
@Table(name = "sys-botuser")
public class BotUser {
    @Id
    public String username;

    public Instant accountCreationTime;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "username")
//    List<Session> sessions;

    public BotUser() {
    }

    public BotUser(String username) {
        this.username = username;
        this.accountCreationTime = Instant.now();
    }
}
