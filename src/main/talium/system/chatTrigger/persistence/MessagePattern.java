package talium.system.chatTrigger.persistence;

import jakarta.persistence.*;

/**
 * A pattern that a message needs to match against in a {@link TriggerEntity}
 * @param pattern the String pattern, can either be a ! command at the start of the message, or a complex regex pattern
 * @param isRegex true if the pattern is regex rather than a literal ! command
 * @param isVisible true if the pattern should be listed in die public command list
 * @param isEnabled true if the pattern should be enabled or disabled
 */
@Entity
@Table(name = "sys-chatTrigger-patterns")
public record MessagePattern(
        @Id long id,
        @ManyToOne(fetch = FetchType.EAGER) @PrimaryKeyJoinColumn
        TriggerEntity parentTrigger,
        String pattern,
        boolean isRegex,
        boolean isVisible,
        boolean isEnabled
) {
}
