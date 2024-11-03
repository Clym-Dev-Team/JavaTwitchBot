package talium.system.twitchCommands.controller;

import talium.system.twitchCommands.persistence.MessagePattern;

public record MessagePatternDTO(
        String pattern,
        boolean isRegex,
        boolean isVisible,
        boolean isEnabled
) {
    public MessagePatternDTO(MessagePattern pattern) {
        this(pattern.pattern, pattern.isRegex, pattern.isVisible, pattern.isEnabled);
    }

    public MessagePattern toMessagePattern() {
        return new MessagePattern(this);
    }
}
