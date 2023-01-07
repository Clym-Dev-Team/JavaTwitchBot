package standard.repositories;

import java.time.Instant;
import java.util.Optional;

/**
 * Chatmessage
 */
public class Message {
    private final String MessageRAW;
    private final Optional<TwitchUser> user;
    private final boolean isHighlightedMessage;
    private final boolean isSkipSubsModeMessage;
    private final boolean isDesignatedFirstMessage;
    private final boolean isUserIntroduction;
    private final Optional<String> getCustomRewardId;
    private final Optional<String> replyToMessageID;
    private final String channelID;
    private final Instant sendAT;

    public Message(String messageRAW, Optional<TwitchUser> user, boolean isHighlightedMessage, boolean isSkipSubsModeMessage, boolean isDesignatedFirstMessage, boolean isUserIntroduction, Optional<String> getCustomRewardId, Optional<String> replyToMessageID, String channelID, Instant sendAT) {
        MessageRAW = messageRAW;
        this.user = user;
        this.isHighlightedMessage = isHighlightedMessage;
        this.isSkipSubsModeMessage = isSkipSubsModeMessage;
        this.isDesignatedFirstMessage = isDesignatedFirstMessage;
        this.isUserIntroduction = isUserIntroduction;
        this.getCustomRewardId = getCustomRewardId;
        this.replyToMessageID = replyToMessageID;
        this.channelID = channelID;
        this.sendAT = sendAT;
    }

    public String MessageRAW() {
        return MessageRAW;
    }

    public Optional<TwitchUser> user() {
        return user;
    }

    public boolean isHighlightedMessage() {
        return isHighlightedMessage;
    }

    public boolean isSkipSubsModeMessage() {
        return isSkipSubsModeMessage;
    }

    public boolean isDesignatedFirstMessage() {
        return isDesignatedFirstMessage;
    }

    public boolean isUserIntroduction() {
        return isUserIntroduction;
    }

    public Optional<String> getCustomRewardId() {
        return getCustomRewardId;
    }

    public Optional<String> replyToMessageID() {
        return replyToMessageID;
    }

    public String channelID() {
        return channelID;
    }

    public Instant sendAT() {
        return sendAT;
    }
}
