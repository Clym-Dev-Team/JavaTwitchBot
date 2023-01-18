package main.modules.core;

import com.github.twitch4j.chat.events.channel.ChannelJoinEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import main.system.eventSystem.EventDispatcher;
import main.system.eventSystem.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/**
 * Handelt alle Events, die der Core braucht und Twitch4J sind.
 * Eine Liste dazu findet sich in den Unterklassen vom TwitchEvent
 *
 * @see com.github.twitch4j.chat.events.TwitchEvent
 */
public class Twitch4JHandler {
    private static final Logger logger = LoggerFactory.getLogger(Twitch4JHandler.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD-HH:mm:ss.SSS");

    @Subscribe(EventClass = ChannelMessageEvent.class)
    public static void MessageEvent(ChannelMessageEvent messageEvent) {
        logger.info("{}: {}", messageEvent.getUser().getName(), messageEvent.getMessage());
        System.out.println(simpleDateFormat.format(new Date()) + " |CHAT | " + messageEvent.getUser().getName() + ": " + messageEvent.getMessage());
    }

    @Subscribe(EventClass = CheerEvent.class)
    @Deprecated(forRemoval = true)
    public static void CheerEvent(CheerEvent cheerEvent) {
        //Do Database Stuff
        HashSet<CommandPermission> permissions = new HashSet<>();
        permissions.add(CommandPermission.MODERATOR);
        permissions.add(CommandPermission.BROADCASTER);

        EventDispatcher.dispatch(new ChannelMessageEvent(cheerEvent.getChannel(), cheerEvent.getMessageEvent(), cheerEvent.getUser(), "!testCommand", permissions));
    }

    @Subscribe(EventClass = FollowEvent.class)
    @Deprecated(forRemoval = true)
    public static void FollowEvent(FollowEvent followEvent) {
        System.out.println("Twitch4JHandler.FollowEvent");
//        InputManager.receiveChatMessage(new ChannelMessageEvent(
//                followEvent.getChannel(),
//                null,
//                followEvent.getUser(),
//                "!testCommand",
//                null));
    }

    @Subscribe(EventClass = ChannelJoinEvent.class)
    @Deprecated(forRemoval = true)
    public static void ChannelJoinEvent(ChannelJoinEvent channelJoinEvent) {
        logger.debug("{} joined! ", channelJoinEvent.getUser());
    }


}
