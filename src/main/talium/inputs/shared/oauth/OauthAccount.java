package talium.inputs.shared.oauth;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "sys-auth_accounts")
@Component
public class OauthAccount {
    @Id
    public String accName;
    public String service;
    public String refreshToken;

    public OauthAccount() {
    }

    public OauthAccount(String accName, String service, String refreshToken) {
        this.accName = accName;
        this.service = service;
        this.refreshToken = refreshToken;
    }

    public static OauthAccountRepo repo;

    @Autowired
    public void setOauthAccountRepo(OauthAccountRepo oauthAccountRepo) {
        repo = oauthAccountRepo;
    }

}


