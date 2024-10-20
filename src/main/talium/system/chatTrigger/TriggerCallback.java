package talium.system.chatTrigger;

import talium.system.commandSystem.repositories.ChatMessage;

/**
 * Defies the functions that are valid as triggerCallbacks. <br/>
 * Used in {@link TriggerManager}
 */
public interface TriggerCallback {
    /**
     * The Callback function
     * @param message the message object that satisfied the trigger conditions
     */
    void triggerCallback(ChatMessage message);
}
