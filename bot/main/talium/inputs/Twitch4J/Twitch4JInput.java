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
import talium.system.Out;
import talium.system.eventSystem.EventDispatcher;
import talium.system.inputSystem.BotInput;
import talium.system.inputSystem.HealthManager;
import talium.system.inputSystem.Input;
import talium.system.inputSystem.InputStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.github.twitch4j.helix.domain.User;
import talium.system.inputSystem.configuration.InputConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Input
public class Twitch4JInput implements BotInput {
    private static String channelName;
    private static String chatAccountName;
    private static String sendTo;
    private static String app_clientID;
    private static String app_clientSecret;

    @Value("${twitchChannelName}")
    public void setChannelName(String channelName) {
        Twitch4JInput.channelName = channelName;
    }

    @Value("${twitchBotAccountName:}")
    public void setChatAccountName(String chatAccountName) {
        Twitch4JInput.chatAccountName = chatAccountName;
    }

    @Value("${twitchOutputToChannel:}")
    public void setSendTo(String sendTo) {
        Twitch4JInput.sendTo = sendTo;
    }

    @Value("${twitchAppId}")
    public void setApp_clientID(String app_clientID) {
        Twitch4JInput.app_clientID = app_clientID;
    }

    @Value("${twitchAppSecret}")
    public void setApp_clientSecret(String app_clientSecret) {
        Twitch4JInput.app_clientSecret = app_clientSecret;
    }

    private static final Logger logger = LoggerFactory.getLogger(Twitch4JInput.class);

    private static volatile TwitchChat chat;
    private static volatile TwitchHelix helix;
    private TwitchIdentityProvider iProvider;
    private OAuth2Credential oAuth2Credential;
    private TwitchClient twitchClient;
    private InputStatus health;

    @Override
    public void run() {
        logger.debug("Starting... ");
        report(InputStatus.STARTING);
        if (chatAccountName == null || chatAccountName.isEmpty()) {
            logger.warn("Using twitchChannelName as botAccountName: {}", channelName);
            logger.warn("Consider setting the botAccountName explicitly to avoid accidental misconfiguration.");
            chatAccountName = channelName;
        }
        if (sendTo == null || sendTo.isEmpty()) {
            logger.warn("Using twitchChannelName as twitchOutputToChannel: {}", channelName);
            logger.warn("Consider setting the twitchOutputToChannel explicitly to avoid accidental misconfiguration.");
            sendTo = channelName;
        }

        iProvider = new TwitchIdentityProvider(app_clientID, app_clientSecret, OAuthEndpoint.getRedirectUrl("twitch"));

        var creds = getRefreshedOauthFromDB(iProvider);
        if (creds.isEmpty()) {
            logger.warn("Twitch credentials could not be found or refreshed, waiting for new Oauth Token to be created!");
            logger.warn(STR."Head to \{OAuthEndpoint.getOauthSetupUrl()} to Setup a new Oauth connection");
            report(InputStatus.INJURED);
            creds = createNewOauth();
        }
        //check if still empty after oauth
        if (creds.isEmpty()) {
            logger.error("Could neither load old credentials, nor create new once, aborting input startup!");
            report(InputStatus.DEAD);
            return;
        } else {
            logger.warn("Created new Oauth. Warning resolved!");
            report(InputStatus.STARTING);
        }
        oAuth2Credential = creds.get();

        TwitchClient twitchClient = TwitchClientBuilder.builder().withEnableHelix(true).withEnableChat(true).withDefaultAuthToken(oAuth2Credential).withChatAccount(oAuth2Credential).withDefaultEventHandler(SimpleEventHandler.class).build();
        twitchClient.getClientHelper().enableStreamEventListener(channelName);
        twitchClient.getChat().joinChannel(channelName);
        twitchClient.getEventManager().onEvent(TwitchEvent.class, EventDispatcher::dispatch);

        this.twitchClient = twitchClient;
        chat = twitchClient.getChat();
        helix = twitchClient.getHelix();

        logger.debug("Start successful!");
        report(InputStatus.HEALTHY);
    }

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
    public InputConfiguration getConfiguration() {
        return null;
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
        // We can leave all the other once empty because refreshCredential will fill them in for us
        return iProvider.refreshCredential(new OAuth2Credential("", "", dbCreds.get().refreshToken, "", "", 0, null));
    }

    private Optional<OAuth2Credential> createNewOauth() {
        var scopes = new ArrayList<>();
        scopes.add("chat:edit");
        scopes.add("chat:read");
        Optional<String> code = OAuthEndpoint.newOAuthGrantFlow(chatAccountName, "twitch", iProvider, scopes);
        return code.map(s -> iProvider.getCredentialByCode(s));
    }

    private void report(InputStatus health) {
        HealthManager.reportStatus(this, health);
        this.health = health;
    }


    /**
     * Sends a message in the chat specified by the twitchOutputToChannel Env with the bot account specified by the twitchBotAccountName env
     *
     * @param message the message text to send
     */
    public static void sendMessage(String message) {
        while (chat == null) Thread.onSpinWait();
        chat.sendMessage(sendTo, message);
    }

    public static Optional<User> getUserById(String userId) {
        if (helix == null) return Optional.empty();
        var user = helix.getUsers(null, List.of(userId), null).execute().getUsers();
        if (user.isEmpty()) return Optional.empty();
        return Optional.ofNullable(user.getFirst());
    }

    public static Optional<User> getUserByName(String userId) {
        if (helix == null) return Optional.empty();
        var user = helix.getUsers(null, null, List.of(userId)).execute().getUsers();
        if (user.isEmpty()) return Optional.empty();
        return Optional.ofNullable(user.getFirst());
    }
}
