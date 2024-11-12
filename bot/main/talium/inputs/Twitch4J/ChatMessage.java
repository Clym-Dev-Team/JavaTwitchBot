package talium.inputs.Twitch4J;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public record ChatMessage(
        String messageId,
        int userMessageIndex,
        int globalMessageIndex,
        String message,
        TwitchUser user,
        boolean isHighlightedMessage,
        boolean isSkipSubsModeMessage,
        boolean isDesignatedFirstMessage,
        boolean isUserIntroduction,
        @Nullable String getCustomRewardId,
        @Nullable String replyToMessageID,
        String channelID,
        Instant sendAT
) {
}
