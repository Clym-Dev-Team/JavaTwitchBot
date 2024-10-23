package talium.system.chatTrigger.persistence;

import jakarta.persistence.*;
import talium.system.chatTrigger.cooldown.ChatCooldown;
import talium.system.chatTrigger.triggerEngine.TriggerEngine;
import talium.inputs.Twitch4J.TwitchUserPermission;

import java.util.List;

//TODO reference triggerId guidelines
/**
 * A Trigger is a set of conditions that a chat message needs to match. If it matches a callback will be executed by the {@link TriggerEngine}. <br/>
 * <br/>
 * ChatTrigger are about half of a Chat Command, the other half being a stringTemplate
 * @param triggerId a unique triggerId that identifies this trigger. Is not allowed to collide with other triggerIds
 * @param patterns a list of matcher Patterns that the message content is matched against. It is enough if any of these patterns match
 * @param permission a minimum permission level a user of the message needs to have
 * @param userCooldown a user specific cooldown for this trigger
 * @param globalCooldown a global (for all users) cooldown for this trigger
 */
@Entity
@Table(name = "sys-chatTrigger-trigger")
public record TriggerEntity(
        @Id String triggerId,
        String description,
        @OneToMany(mappedBy = "parentTrigger")
        List<MessagePattern> patterns,
        TwitchUserPermission permission,
        @Embedded @AttributeOverrides({
                @AttributeOverride(name = "cooldownType", column = @Column(name = "userCooldown_type")),
                @AttributeOverride(name = "amount", column = @Column(name = "userCooldown_amount"))
        })
        ChatCooldown userCooldown,
        @Embedded @AttributeOverrides({
                @AttributeOverride(name = "cooldownType", column = @Column(name = "globalCooldown_type")),
                @AttributeOverride(name = "amount", column = @Column(name = "globalCooldown_amount"))
        })
        ChatCooldown globalCooldown,
        boolean isUserTrigger
) {
}
