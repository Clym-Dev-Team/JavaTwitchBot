package talium.system.chatTrigger;

import talium.system.commandSystem.repositories.CooldownType;
import talium.system.commandSystem.repositories.TwitchUserPermission;
import talium.system.stringTemplates.Template;

import java.util.List;


public record ChatTrigger(
        String id,
        List<MessagePattern> patterns,
        TwitchUserPermission permission,
        ChatCooldown userCooldown,
        ChatCooldown globalCooldown
) {
}
