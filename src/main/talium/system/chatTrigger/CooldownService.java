package talium.system.chatTrigger;

import kotlin.Pair;
import org.checkerframework.dataflow.qual.Impure;
import org.checkerframework.dataflow.qual.Pure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import talium.system.commandSystem.repositories.CooldownType;
import talium.system.commandSystem.repositories.ChatMessage;
import talium.system.commandSystem.repositories.TwitchUser;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CooldownService {
    // User Cooldowns
    // ordered list of USERIDs and messageIndexes
    private static final TreeMap<Long, Integer> messageIndexes = new TreeMap<>();
    private static final HashMap<Pair<String, Long>, Integer> messageUserCooldown = new HashMap<>();
    private static final HashMap<Pair<String, Long>, Instant> secondsUserCooldown = new HashMap<>();

    // Global Cooldowns
    private static int globalMessageIndex = 0;
    private static final HashMap<String, Integer> messageGlobalCooldown = new HashMap<>();
    private static final HashMap<String, Instant> secondsGlobalCooldown = new HashMap<>();

    private static final ScheduledExecutorService cleanupService = Executors.newSingleThreadScheduledExecutor();
    private static final Logger log = LoggerFactory.getLogger(CooldownService.class);

    static {
        cleanupService.scheduleAtFixedRate(CooldownService::cleanupCooldownUser, 0, 30, TimeUnit.MINUTES);
    }

    /**
     * Returns the next index for a message of this user.
     * <bold>This function is not pure</bold>, so calling this multiple times per received message will always increase the internal counter in this service.
     * This will cause the chat cooldowns to become inaccurate or behave unexpectedly!
     * <br/> <br/>
     * If for some reason you need to get the current userMessageIndex without increasing the counter, use -> {@link CooldownService#getMessageUserIndex(TwitchUser)}
     *
     * @param user the user for which to increase and get the index
     * @return the new index to use in the received message
     * @apiNote <font color="red">This function increases the internal counter of this service!</font> Only call when you get a new Twitch Message from the specific user!
     * @see CooldownService#getMessageUserIndex(TwitchUser)
     */
    @Impure
    public static int computeMessageUserIndex(TwitchUser user) {
        var mapped = messageIndexes.getOrDefault(user.id(), 0);
        messageIndexes.put(user.id(), mapped + 1);
        return mapped + 1;
    }

    @Pure
    public static int getMessageUserIndex(TwitchUser user) {
        return messageIndexes.getOrDefault(user.id(), 0);
    }

    //TODO edit comment
    /**
     * Returns the next index for a message of this user.
     * <bold>This function is not pure</bold>, so calling this multiple times per received message will always increase the internal counter in this service.
     * This will cause the chat cooldowns to become inaccurate or behave unexpectedly!
     * <br/> <br/>
     * If for some reason you need to get the current userMessageIndex without increasing the counter, use -> {@link CooldownService#getMessageUserIndex(TwitchUser)}
     *
     * @param user the user for which to increase and get the index
     * @return the new index to use in the received message
     * @apiNote <font color="red">This function increases the internal counter of this service!</font> Only call when you get a new Twitch Message from the specific user!
     * @see CooldownService#getMessageUserIndex(TwitchUser)
     */
    @Impure
    public static int computeMessageGlobalIndex(TwitchUser user) {
        globalMessageIndex++;
        return globalMessageIndex;
    }

    @Pure
    public static int getMessageGlobalIndex(TwitchUser user) {
        return globalMessageIndex;
    }

    public static boolean inUserCooldown(ChatMessage message, String triggerId, ChatCooldown userCooldown) {
        if (userCooldown.cooldownType() == CooldownType.MESSAGES) {
            Integer lastUseMessageId = messageUserCooldown.get(new Pair<>(triggerId, message.user().id()));
            if (lastUseMessageId == null) {
                return false;
            }
            var idDelta = message.userMessageIndex() - lastUseMessageId;
            return idDelta <= userCooldown.amount();
        }
        var lastUseInstant = secondsUserCooldown.get(new Pair<>(triggerId, message.user().id()));
        if (lastUseInstant == null) {
            return false;
        }
        var secondsBetween = ChronoUnit.SECONDS.between(message.sendAT(),lastUseInstant);
        return secondsBetween <= userCooldown.amount();
    }

    public static boolean inGlobalCooldown(ChatMessage message, String triggerId, ChatCooldown globalCooldown) {
        if (globalCooldown.cooldownType() == CooldownType.MESSAGES) {
            Integer lastUseMessageId = messageGlobalCooldown.get(triggerId);
            if (lastUseMessageId == null) {
                return false;
            }
            int delta = message.globalMessageIndex() - lastUseMessageId;
            return delta <= globalCooldown.amount();
        }
        var lastUseInstant = secondsGlobalCooldown.get(triggerId);
        if (lastUseInstant == null) {
            return false;
        }
        var secondsBetween = ChronoUnit.SECONDS.between(message.sendAT(),lastUseInstant);
        return secondsBetween <= globalCooldown.amount();
    }

    public static void updateCooldownState(ChatMessage message, String triggerId, ChatCooldown globalCooldown, ChatCooldown userCooldown) {
        if (userCooldown.cooldownType() == CooldownType.MESSAGES) {
            messageUserCooldown.put(new Pair<>(triggerId, message.user().id()), message.userMessageIndex());
            return;
        } else {
            secondsUserCooldown.put(new Pair<>(triggerId, message.user().id()), Instant.now());
        }

        if (globalCooldown.cooldownType() == CooldownType.MESSAGES) {
            messageGlobalCooldown.put(triggerId, message.globalMessageIndex());
        } else {
            secondsGlobalCooldown.put(triggerId, Instant.now());
        }
    }

    private static void cleanupCooldownUser() {
        log.info("Running Cooldown cleanup task");
        // if the index of the message that executed this command last is more than 100 messages ago than the last send message of the user we remove the message
        for (var entry : messageUserCooldown.entrySet()) {
            Long userId = entry.getKey().component2();
            var messsageIndexDelta = messageIndexes.get(userId) - entry.getValue();
            if (messsageIndexDelta > 100) {
                messageUserCooldown.remove(entry.getKey());
            }
        }
        // if an entry of the time the user used this command last is more than 60 minutes old, we remove that entry
        for (var entry : secondsUserCooldown.entrySet()) {
            Long userId = entry.getKey().component2();
            var timeDelta = ChronoUnit.MINUTES.between(Instant.now(), entry.getValue());
            if (timeDelta > 60) {
                secondsUserCooldown.remove(entry.getKey());
            }
        }
    }

}
