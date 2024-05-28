package main.system.panelAuth.botLogin;

import jakarta.persistence.*;
import main.system.panelAuth.botUser.BotUser;

/**
 * Object for storing the information purely needed for Logging a user in.
 * Instances of this object should be discarded as soon as they aren't needed anymore
 */
@Entity
@Table(name = "sys-botlogin")
public class BotLogin {
    @Id
    String username;

    String hashedPassword;

    String alg1;

    String alg2;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    BotUser botUser;

    public BotLogin() {}

    public BotLogin(String username, String hashedPassword, String alg1, String alg2, BotUser botUser) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.alg1 = alg1;
        this.alg2 = alg2;
    }
}
