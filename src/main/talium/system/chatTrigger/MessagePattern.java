package talium.system.chatTrigger;

/**
 * A pattern that a message needs to match against in a {@link ChatTrigger}
 * @param pattern the String pattern, can either be a ! command at the start of the message, or a complex regex pattern
 * @param isRegex true if the pattern is regex rather than a literal ! command
 * @param isVisible true if the pattern should be listed in die public command list
 * @param isEnabled true if the pattern should be enabled or disabled
 */
public record MessagePattern(
        String pattern,
        boolean isRegex,
        boolean isVisible,
        boolean isEnabled
) {
}
