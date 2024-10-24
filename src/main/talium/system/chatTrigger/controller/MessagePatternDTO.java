package talium.system.chatTrigger.controller;

import talium.system.chatTrigger.persistence.MessagePattern;

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
