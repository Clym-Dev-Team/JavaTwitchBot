package talium.system.chatTrigger.triggerEngine;

import talium.system.commandSystem.repositories.ChatMessage;

/**
 * Defies the functions that are valid as triggerCallbacks. <br/>
 * Used in {@link TriggerEngine}
 */
public interface TriggerCallback {
    /**
     * The Callback function
     * @param triggerId the id of the trigger that triggered the callback
     * @param message the message object that satisfied the trigger conditions
     */
    void triggerCallback(String triggerId, ChatMessage message);
}
