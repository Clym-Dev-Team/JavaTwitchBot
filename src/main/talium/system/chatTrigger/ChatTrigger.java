package talium.system.chatTrigger;

import talium.system.chatTrigger.cooldown.ChatCooldown;
import talium.system.commandSystem.repositories.TwitchUserPermission;

import java.util.List;


public record ChatTrigger(
        String id,
        List<MessagePattern> patterns,
        TwitchUserPermission permission,
        ChatCooldown userCooldown,
        ChatCooldown globalCooldown
) {
}
