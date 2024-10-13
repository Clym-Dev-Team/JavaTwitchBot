package talium.system.panelAuth.botLogin;

import jakarta.persistence.*;
import talium.system.panelAuth.botUser.BotUser;

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
        this.botUser = botUser;
    }

    public String username() {
        return username;
    }

    public String hashedPassword() {
        return hashedPassword;
    }

    public String alg1() {
        return alg1;
    }

    public String alg2() {
        return alg2;
    }

    public BotUser botUser() {
        return botUser;
    }

    @Override
    public String toString() {
        return "BotLogin{" +
                "username='" + username + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", alg1='" + alg1 + '\'' +
                ", alg2='" + alg2 + '\'' +
                ", botUser=" + botUser +
                '}';
    }
}
