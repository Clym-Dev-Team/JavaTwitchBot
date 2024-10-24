package talium.system.chatTrigger.persistence;

import jakarta.persistence.Id;

public record MessagePatternDTO(
        String pattern,
        boolean isRegex,
        boolean isVisible,
        boolean isEnabled
) {
    public MessagePatternDTO(MessagePattern pattern) {
        this(pattern.pattern, pattern.isRegex, pattern.isVisible, pattern.isEnabled);
    }
}
