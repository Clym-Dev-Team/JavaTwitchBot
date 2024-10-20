package talium.system.chatTrigger.triggerEngine;

import talium.system.commandSystem.repositories.ChatMessage;
import talium.system.eventSystem.Subscriber;

import static talium.system.chatTrigger.triggerEngine.TriggerProvider.triggers;
import static talium.system.chatTrigger.cooldown.CooldownService.*;

//TODO reference id guidelines

/**
 * Evaluates if a message satisfies all the conditions of a trigger.
 * If a message matches, a callback function is called with this message.
 */
public class TriggerEngine {


    /**
     * Consumes {@link ChatMessage}s from the Twitch Input and checks if any triggers match this message.
     * If so, they callbacks are executed.
     *
     * @param messsage the message to check
     */
    @Subscriber
    public static void triggerFromMessage(ChatMessage messsage) {
        triggers().forEach(t -> processTrigger(t, messsage));
    }

    /**
     * Check if a message matches a specific trigger, and call this trigger
     *
     * @param trigger the trigger to check for
     * @param message the message to check against
     */
    private static void processTrigger(RuntimeTrigger trigger, ChatMessage message) {
        // if ordinal of user is smaller than the command/trigger, than the user has fewer permissions and is not allowed to execute
        if (message.user().permission().ordinal() <= trigger.permission().ordinal()) {
            return;
        }

        boolean isMatched = false;
        for (var pat : trigger.patterns()) {
            if (pat.matcher(message.message()).matches()) {
                isMatched = true;
                break;
            }
        }
        if (!isMatched) {
            return;
        }

        var cooldown = inGlobalCooldown(message, trigger.id(), trigger.userCooldown()) || inUserCooldown(message, trigger.id(), trigger.userCooldown());
        if (cooldown) {
            return;
        }
        updateCooldownState(message, trigger.id(), trigger.globalCooldown(), trigger.userCooldown());

        trigger.callback().triggerCallback(trigger.id(), message);
    }

}
