package talium.system.chatTrigger;

import talium.system.chatTrigger.cooldown.ChatCooldown;
import talium.system.commandSystem.repositories.TwitchUserPermission;

import java.util.List;

//TODO reference id guidelines
/**
 * A Trigger is a set of conditions that a chat message needs to match. If it matches a callback will be executed by the {@link TriggerManager}. <br/>
 * <br/>
 * ChatTrigger are about half of a Chat Command, the other half being a stringTemplate
 * @param id a unique id that identifies this trigger. Is not allowed to collide with other triggerIds
 * @param patterns a list of matcher Patterns that the message content is matched against. It is enough if any of these patterns match
 * @param permission a minimum permission level a user of the message needs to have
 * @param userCooldown a user specific cooldown for this trigger
 * @param globalCooldown a global (for all users) cooldown for this trigger
 */
public record ChatTrigger(
        String id,
        List<MessagePattern> patterns,
        TwitchUserPermission permission,
        ChatCooldown userCooldown,
        ChatCooldown globalCooldown
) {
}
