package talium.inputs.Twitch4J;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.TwitchEvent;
import com.github.twitch4j.helix.TwitchHelix;
import talium.inputs.shared.oauth.OAuthEndpoint;
import talium.inputs.shared.oauth.OauthAccount;
import talium.system.eventSystem.EventDispatcher;
import talium.system.inputSystem.BotInput;
import talium.system.inputSystem.HealthManager;
import talium.system.inputSystem.Input;
import talium.system.inputSystem.InputStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Optional;

@Input
public class Twitch4JInput implements BotInput {

    private static final String channelName = "clym";
    private static final String chatAccountName = "orciument";
    private static final String sendTo = "orciument";

    private static volatile TwitchChat chat;

    //"CONFIG"
    private static String app_clientID = "";

    @Value("${twitchApp_ID}")
    public void setApp_clientID(String app_clientID) {
        Twitch4JInput.app_clientID = app_clientID;
    }

    private static String app_clientSecret = "";

    @Value("${twitchApp_Secret}")
    public void setApp_clientSecret(String app_clientSecret) {
        Twitch4JInput.app_clientSecret = app_clientSecret;
    }

    private static final Logger logger = LoggerFactory.getLogger(Twitch4JInput.class);

    private static TwitchIdentityProvider iProvider;
    private OAuth2Credential oAuth2Credential;
    private TwitchClient twitchClient;
    public static TwitchHelix broadCasterHelix;
    private InputStatus health;
    //GraphQL is disabled until we need it, because the module is not yet finished
    //public static TwitchGraphQL broadCasterGraphQL;

//    @Override
//    public boolean checkConfiguration() {
//        //TODO fehlen einige Config Tests
//        if (OauthAccount.repo == null) {
//            logger.error("TwitchAccountRepository is null!");
//            return false;
//        }
////        if (!OauthAccount.repo.existsByAccName("primary")) {
////            logger.error("No OAuth Credentials found!");
////            return false;
////        }
//        //TODO iProvider.isCredentialValid() is valid
//        logger.info("Configuration and Credentials found and configured correctly");
//        return true;
//    }

    @Override
    public void run() {
        logger.debug("Starting... ");
        report(InputStatus.STARTING);
        iProvider = new TwitchIdentityProvider(app_clientID, app_clientSecret, OAuthEndpoint.getRedirectUrl("twitch"));

        var creds = getRefreshedOauthFromDB(iProvider);
        if (creds.isEmpty()) {
            logger.warn("Twitch credentials could not be found or refreshed, waiting for new Oauth Token to be created!");
            logger.warn("Head to " + OAuthEndpoint.getOauthSetupUrl() + " to Setup a new Oauth connection");
            report(InputStatus.INJURED);
            creds = createNewOauth();
            if (creds.isEmpty()) {
                logger.error("Could neither load old credentials, nor create new once, aborting startup!");
                report(InputStatus.DEAD);
                return;
            }
            logger.warn("Created new Oauth. Warning resolved!");
            report(InputStatus.STARTING);
        }
        oAuth2Credential = creds.get();

        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                //GraphQL is disabled until we need it, because the module is not yet finished
                //.withEnableGraphQL(true)
                .withEnablePubSub(true)
                .withDefaultAuthToken(oAuth2Credential)
                .withChatAccount(oAuth2Credential)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();
        twitchClient.getClientHelper().enableStreamEventListener(channelName);
        twitchClient.getChat().joinChannel(channelName);
//        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, EventDispatcher::dispatch);
        twitchClient.getEventManager().onEvent(TwitchEvent.class, EventDispatcher::dispatch);

        this.twitchClient = twitchClient;
        chat = twitchClient.getChat();

        //GraphQL is disabled until we need it, because the module is not yet finished
        //broadCasterGraphQL = twitchClient.getGraphQL();
        broadCasterHelix = twitchClient.getHelix();
        logger.debug("Start successful!");
        report(InputStatus.HEALTHY);
    }

//    @Override
//    public boolean running() {
//        return running;
//    }

    @Override
    public InputStatus getHealth() {
        return health;
    }


    @Override
    public void shutdown() {
        if (oAuth2Credential != null) {
            OauthAccount account = new OauthAccount(chatAccountName, "twitch", oAuth2Credential.getRefreshToken());
            OauthAccount.repo.save(account);
        }
        if (twitchClient != null) {
            twitchClient.close();
        }
        logger.debug("Shutdown successful!");
    }

    @Override
    public String threadName() {
        return "TwitchReading";
    }

    private Optional<OAuth2Credential> getRefreshedOauthFromDB(TwitchIdentityProvider iProvider) {
        Optional<OauthAccount> dbCreds = OauthAccount.repo.getByAccNameAndService(chatAccountName, "twitch");
        if (dbCreds.isEmpty()) {
            return Optional.empty();
        }

        // We can leave all the other once emphty because refreshCredential will fill them in for us
        return iProvider.refreshCredential(new OAuth2Credential(
                "",
                "",
                dbCreds.get().refreshToken,
                "",
                "",
                0,
                null
        ));
    }

    private Optional<OAuth2Credential> createNewOauth() {
        var scopes = new ArrayList<>();
        scopes.add("chat:edit");
        scopes.add("chat:read");
        Optional<String> code = OAuthEndpoint.newOAuthGrantFlow(chatAccountName, "twitch", iProvider, scopes);
        if (code.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(iProvider.getCredentialByCode(code.get()));
    }

    private void report(InputStatus health) {
        HealthManager.reportStatus(this, health);
        this.health = health;
    }

    public static void sendMessage(String message) {
        while (chat == null)
            Thread.onSpinWait();
        chat.sendMessage(sendTo, message);
    }

}
