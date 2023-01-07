package standard.bot;

import jakarta.persistence.PreRemove;
import org.springframework.stereotype.Component;
import standard.bot.inputs.Twitch4JReader;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import jakarta.annotation.PreDestroy;
import standard.repositories.TwitchAccount;
import standard.repositories.TwitchAccountRepoController;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class Core {

    //"CONFIG"
    private static final String app_clientID = "ist0n90no55q5de5skglybbaxcpksl";
    private static final String app_clientSecret = "afmb0o8z0fgpkc6tty9yz30nvmbg5s";
    private static final String panel_url = "https://localhost";
    private static final String refreshToken = "q4gin7gu6ewi1ayvndwom234rkqezdta9q31savxrswoc21xn6";

    private static final TwitchIdentityProvider twitchIdentityProvider = new TwitchIdentityProvider(app_clientID, app_clientSecret, panel_url + "/auth");
    public static TwitchClient broadCasterChannel;
    public static OAuth2Credential oAuth2Credential;
    public static String channelName = "clym";


    public Core() {

    }


    public static void newTwitch4JReader() {
//        TwitchAccount oldCreds = TwitchAccountRepoController.TwitchAccountRepo.findbyRole("primary");
        ArrayList<TwitchAccount> oldCreds = new ArrayList<TwitchAccount>( TwitchAccountRepoController.TwitchAccountRepo.findAll());
        System.out.println(oldCreds.size());
        if (oldCreds.isEmpty()) {
            System.out.println("null");
            oAuth2Credential = twitchIdentityProvider.refreshCredential(new OAuth2Credential(
                    "",
                    "",
                    "lfcgrnqekrxctaldtkfykhlbpgud7ga5a96an6rvuh6t26wkgg",
                    "",
                    "",
                    0,
                    null
            )).get();
        } else {
            System.out.println("nor null");
            oAuth2Credential = twitchIdentityProvider.refreshCredential(new OAuth2Credential(
                    "",
                    "",
                    oldCreds.get(0).refreshToken,
                    "",
                    "",
                    0,
                    null
            )).get();
        }

        new Twitch4JReader(oAuth2Credential, channelName);
    }

    @PreDestroy
    @PreRemove
    public static void saveCreds() {
        System.out.println("Core.saveCreds");
        TwitchAccount account = new TwitchAccount(oAuth2Credential.getUserId(), oAuth2Credential.getRefreshToken(), "primary");
        TwitchAccountRepoController.TwitchAccountRepo.save(account );
    }

}
