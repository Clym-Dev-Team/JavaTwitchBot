package talium.system.chatTrigger;

public record MessagePattern(
        String pattern,
        boolean isRegex,
        boolean isVisible,
        boolean isEnabled
) {
}
