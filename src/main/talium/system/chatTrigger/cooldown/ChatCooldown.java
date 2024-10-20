package talium.system.chatTrigger.cooldown;

import jakarta.persistence.Embeddable;
import talium.system.commandSystem.repositories.CooldownType;

/**
 * A Cooldown with an amount of some unit. <br/>
 * E.g.: <br/>
 * - 4 MESSAGES <br/>
 * - 27 SECONDS
 * @param cooldownType the unit of the amount value
 * @param amount amount of the unit CooldownType
 */
@Embeddable
public record ChatCooldown(
        CooldownType cooldownType,
        int amount
) {
}
