package standard.bot.inputs;

import standard.bot.Core;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.FollowEvent;

public class Twitch4JReader extends Thread {

    public Twitch4JReader(OAuth2Credential credential, String channel) {
        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withEnableGraphQL(true)
                .withEnablePubSub(true)
                .withDefaultAuthToken(credential)
                .withChatAccount(credential)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();
        twitchClient.getClientHelper().enableStreamEventListener("orciument");
        twitchClient.getChat().joinChannel("orciument");
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, InputManager::receiveChatMessage);
        twitchClient.getEventManager().onEvent(CheerEvent.class, Twitch4JHandler::CheerEvent);
        twitchClient.getEventManager().onEvent(FollowEvent.class, Twitch4JHandler::FollowEvent);

        Core.broadCasterChannel = twitchClient;
    }
}