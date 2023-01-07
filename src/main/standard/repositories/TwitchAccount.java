package standard.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "TwitchAccounts")
public class TwitchAccount {
    @Id
    public String twitchUserID;
    public String refreshToken;
    public String role;

    public TwitchAccount() {
    }

    public TwitchAccount(String twitchUserID, String refreshToken, String role) {
        this.twitchUserID = twitchUserID;
        this.refreshToken = refreshToken;
        this.role = role;
    }

}

