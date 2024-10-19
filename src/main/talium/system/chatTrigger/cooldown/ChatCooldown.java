package talium.system.chatTrigger.cooldown;

import talium.system.commandSystem.repositories.CooldownType;

public record ChatCooldown(
        CooldownType cooldownType,
        int amount
) {
}
