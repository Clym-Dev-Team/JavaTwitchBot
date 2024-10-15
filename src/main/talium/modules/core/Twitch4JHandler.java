package talium.modules.core;

import com.github.twitch4j.chat.events.channel.ChannelJoinEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventUser;
import talium.system.commandSystem.CommandProcessor;
import talium.system.commandSystem.repositories.Message;
import talium.system.commandSystem.repositories.TwitchUser;
import talium.system.commandSystem.repositories.TwitchUserPermission;
import talium.system.eventSystem.EventDispatcher;
import talium.system.eventSystem.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static talium.system.commandSystem.repositories.TwitchUserPermission.*;

/**
 * Handels all Events from Twitch4J that are needed for the core of the bot to function
 * @see com.github.twitch4j.chat.events.TwitchEvent
 */
public class Twitch4JHandler {
    private static final Logger logger = LoggerFactory.getLogger(Twitch4JHandler.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD-HH:mm:ss.SSS");

    @Subscriber
    public static void MessageEvent(ChannelMessageEvent messageEvent) {
        if (messageEvent.getMessage().toCharArray()[0] == '!')
            System.out.println(simpleDateFormat.format(new Date()) + " |CHAT | " + messageEvent.getUser().getName() + ": " + messageEvent.getMessage());

        //Convert ChatMessage
        EventUser eUser = messageEvent.getUser();
        TwitchUser user = new TwitchUser(eUser.getId(), eUser.getName(), messageEvent.getSubscriberMonths(), messageEvent.getSubscriptionTier(), convertUserPermissions(messageEvent.getPermissions()));
        String replyToID = null;
        if (messageEvent.getReplyInfo() != null)
            replyToID = messageEvent.getReplyInfo().getMessageId();
        Message message = new Message(
                messageEvent.getMessageEvent().getEventId(),
                messageEvent.getMessage(),
                user,
                messageEvent.isHighlightedMessage(),
                messageEvent.isHighlightedMessage(),
                messageEvent.isDesignatedFirstMessage(),
                messageEvent.isUserIntroduction(),
                messageEvent.getCustomRewardId().orElse(null),
                replyToID,
                messageEvent.getChannel().getId(),
                messageEvent.getFiredAtInstant());
        CommandProcessor.processMessage(message);
    }


    //Das sind nur schnelle Beispiele um zu testen, dass es funktioniert
    @Subscriber
    @Deprecated(forRemoval = true)
    public static void CheerEvent(CheerEvent cheerEvent) {
        //Do Database Stuff
        EventDispatcher.dispatch(new ChannelMessageEvent(cheerEvent.getChannel(), cheerEvent.getMessageEvent(), cheerEvent.getUser(), "!testCommand"));
    }

    @Subscriber
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

    @Subscriber
    @Deprecated(forRemoval = true)
    public static void ChannelJoinEvent(ChannelJoinEvent channelJoinEvent) {
        logger.debug("{} joined! ", channelJoinEvent.getUser());
    }

    /**
     * Converts a set of Twitch4J permission into our equivalent Permissions, and returns the highest one.
     * @param userPermissions A set of Twitch4J Permissions
     * @return The highest equivalent ranking permission in our system from the set
     * @see CommandPermission
     * @see TwitchUserPermission
     */
    private static TwitchUserPermission convertUserPermissions(Set<CommandPermission> userPermissions) {
        HashSet<TwitchUserPermission> permissions = translatePerms(userPermissions);
        TwitchUserPermission highest_perm = EVERYONE;
        for (TwitchUserPermission perm : permissions) {
            if (perm.ordinal() > highest_perm.ordinal())
                highest_perm = perm;
        }
        return highest_perm;
    }

    /**
     * Translate the Permissions of a user from Twit4J's permission System into our.
     * @param permissions A Set if Twit4J#s permissions
     * @return A Set of our Permissions
     * @see CommandPermission
     */
    private static HashSet<TwitchUserPermission> translatePerms(Set<CommandPermission> permissions) {
        HashSet<TwitchUserPermission> newPerm = new HashSet<>();
        for (CommandPermission cp : permissions) {
            switch (cp) {
                case EVERYONE -> newPerm.add(EVERYONE);
                case SUBSCRIBER -> newPerm.add(SUBSCRIBER);
                case FOUNDER -> newPerm.add(FOUNDER);
                case PREDICTIONS_BLUE -> newPerm.add(PREDICTIONS_BLUE);
                case PREDICTIONS_PINK -> newPerm.add(PREDICTIONS_PINK);
                case ARTIST -> newPerm.add(ARTIST);
                case VIP -> newPerm.add(VIP);
                case MODERATOR -> newPerm.add(MODERATOR);
                case BROADCASTER -> newPerm.add(BROADCASTER);
                case OWNER -> newPerm.add(OWNER);
                //PRIME_TURBO, NO_VIDEO, MOMENTS, NO_AUDIO, TWITCHSTAFF, SUBGIFTER, BITS_CHEERER, PARTNER, FORMER_HYPE_TRAIN_CONDUCTOR, CURRENT_HYPE_TRAIN_CONDUCTOR -> {}
                default -> {
                }
            }
        }
        return newPerm;
    }

}
