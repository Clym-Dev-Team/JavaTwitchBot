package talium.system.chatTrigger;

import talium.system.commandSystem.repositories.CooldownType;

public record ChatCooldown(
        CooldownType cooldownType,
        int amount
) {
}
