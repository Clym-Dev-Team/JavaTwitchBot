package main.standard.bot.inputs;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.FollowEvent;
import main.standard.bot.Core;

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
        twitchClient.getClientHelper().enableStreamEventListener(channel);
        twitchClient.getChat().joinChannel(channel);
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, InputManager::receiveChatMessage);
        twitchClient.getEventManager().onEvent(CheerEvent.class, Twitch4JHandler::CheerEvent);
        twitchClient.getEventManager().onEvent(FollowEvent.class, Twitch4JHandler::FollowEvent);

        Core.broadCasterChannel = twitchClient;


        //Versuch an die Zahl an gesendeten Chatnachrichten zu kommen
//        CommandFetchChatHistory commandFetchChatHistory = twitchClient.getGraphQL().fetchChatHistory(credential, "427320589", "427320589", "");
//        long wait = 0;
//        Future<FetchChatHistoryQuery.Data> queue = commandFetchChatHistory.queue();
//        while (!queue.isDone()) {
//            wait++;
//        }
//        System.out.println(queue.isDone());
//        System.out.println("wait = " + wait);
//        try {
//            System.out.println(queue.get().toString());
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
    }
}