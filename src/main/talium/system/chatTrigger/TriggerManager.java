package talium.system.chatTrigger;

import java.util.regex.Pattern;

import talium.system.chatTrigger.cooldown.ChatCooldown;
import talium.system.commandSystem.repositories.ChatMessage;
import talium.system.commandSystem.repositories.CooldownType;
import talium.system.commandSystem.repositories.TwitchUserPermission;
import talium.system.eventSystem.Subscriber;

import java.util.List;

import static talium.system.chatTrigger.cooldown.CooldownService.*;

//TODO reference id guidelines
/**
 * A version of the {@link ChatTrigger} modified for runtime use in the {@link TriggerManager}.
 * The Patterns are unified in a list of Regex Pattern. Callback functions an added, these are executed if a message satisfies all these conditions.
 * @param id a unique id that identifies this trigger. Is not allowed to collide with other triggerIds
 * @param patterns a list of regex Patterns that the message content is matched against. It is enough if any of these patterns match
 * @param permission a minimum permission level a user of the message needs to have
 * @param userCooldown a user specific cooldown for this trigger
 * @param globalCooldown a global (for all users) cooldown for this trigger
 * @param callback the function to call after a successfull trigger
 */
record ManagedTrigger(
        String id,
        List<Pattern> patterns,
        TwitchUserPermission permission,
        ChatCooldown userCooldown,
        ChatCooldown globalCooldown,
        TriggerCallback callback
) {
}

/**
 * Evaluates if a message satisfies all the conditions of a trigger.
 * If a message matches, a callback function is called with this message.
 */
public class TriggerManager {
    // TODO:REMOVE tests
    public static void testFunction(ChatMessage messageEvent) {
        System.out.println(STR."TESTFUNCTION mesage: \{messageEvent.message()}");
    }

    //    MessagePattern pattern1 = new MessagePattern("!test", false, false, true);
    private static final Pattern pattern1 = Pattern.compile("!test");
    private static final ChatCooldown defaultCooldown = new ChatCooldown(CooldownType.MESSAGES, 2);
    private static final ManagedTrigger managedTrigger = new ManagedTrigger("test.testID", List.of(pattern1), TwitchUserPermission.EVERYONE, defaultCooldown, defaultCooldown, TriggerManager::testFunction);
    // end of tests

    private static final List<ManagedTrigger> triggers = List.of(managedTrigger);
    //TODO get list of triggers


    /**
     * Consumes {@link ChatMessage}s from the Twitch Input and checks if any triggers match this message.
     * If so, they callbacks are executed.
     * @param messsage the message to check
     */
    @Subscriber
    public static void triggerFromMessage(ChatMessage messsage) {
        triggers.forEach(t -> processTrigger(t, messsage));
    }

    /**
     * Check if a message matches a specific trigger
     * @param trigger the trigger to check for
     * @param message the message to check against
     */
    private static void processTrigger(ManagedTrigger trigger, ChatMessage message) {
        // if ordinal of user is smaller than the command/trigger, than the user has fewer permissions and is not allowed to execute
        if (message.user().permission().ordinal() <= trigger.permission().ordinal()) {
            return;
        }

        // check matcher
        //TODO matcher isEnabled
        boolean isMatched = false;
        for (var pat : trigger.patterns()) {
            if (pat.matcher(message.message()).matches()) {
                isMatched = true;
                break;
            }
        }

        // return if noting matched
        if (!isMatched) {
            return;
        }

        var cooldown = inGlobalCooldown(message, trigger.id(), trigger.userCooldown()) || inUserCooldown(message, trigger.id(), trigger.userCooldown());
        if (cooldown) {
            return;
        }

        updateCooldownState(message, trigger.id(), trigger.globalCooldown(), trigger.userCooldown());

        trigger.callback().triggerCallback(message);
    }
}
