package talium.system.chatTrigger;

import talium.system.commandSystem.repositories.ChatMessage;

public interface TriggerCallback {
    void triggerCallback(ChatMessage message);
}
