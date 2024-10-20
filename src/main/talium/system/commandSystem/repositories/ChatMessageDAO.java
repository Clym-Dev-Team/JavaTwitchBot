package talium.system.commandSystem.repositories;

import jakarta.persistence.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Chatmessageobject for DB saving and Runtime
 */
@Component
@Entity
@Table(name = "Core-Twitch_Chat_Messages")
public class ChatMessageDAO {
    //Annotate all Columns explicitly
    @Id private String messageID;
    private String message;
    @ManyToOne() @JoinColumn(name = "Twitch_ChatUser_ID")
    private TwitchUser user;
    //Remove user from here, only save ID to DB and get the User Object by Caching it here, and if it is missing, get it from Twitch
    private boolean isHighlightedMessage;
    private boolean isSkipSubsModeMessage;
    private boolean isDesignatedFirstMessage;
    private boolean isUserIntroduction;
    @Nullable private String getCustomRewardId;
    @Nullable private String replyToMessageID;
    private String channelID;
    private Instant sendAT;

    public static MessageRepo repo;

    public TwitchUser getUser() {
        return user;
    }

    public void setUser(TwitchUser user) {
        this.user = user;
    }

    @Autowired
    public void setRepo(MessageRepo messageRepo) {
        repo = messageRepo;
    }

    public ChatMessageDAO() {
    }

    public ChatMessageDAO(String messageID, String message, TwitchUser user, boolean isHighlightedMessage, boolean isSkipSubsModeMessage, boolean isDesignatedFirstMessage, boolean isUserIntroduction, @Nullable String getCustomRewardId, @Nullable String replyToMessageID, String channelID, Instant sendAT) {
        this.messageID = messageID;
        this.message = message;
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

    public static ChatMessageDAO simulatedMessage(String message, boolean isHighlightedMessage, boolean isSkipSubsModeMessage, boolean isDesignatedFirstMessage, boolean isUserIntroduction, String getCustomRewardId) {
        //TODO getChannel ID aus config
        String channelIdclym = "21045643";
        String channelIDOrciu = "427320589";
        return new ChatMessageDAO("-SYSTEM-", message, TwitchUser.getSystemUser(), isHighlightedMessage, isSkipSubsModeMessage, isDesignatedFirstMessage, isUserIntroduction, getCustomRewardId, null, channelIdclym, Instant.now());
    }

    public static ChatMessageDAO simulatedMessage(String message) {
        return ChatMessageDAO.simulatedMessage(message, false, false, false, false, null);
    }

    public String messageID() {
        return messageID;
    }

    public String message() {
        return message;
    }

    public TwitchUser user() {
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

    public String getCustomRewardId() {
        return getCustomRewardId;
    }

    public String replyToMessageID() {
        return replyToMessageID;
    }

    public String channelID() {
        return channelID;
    }

    public Instant sendAT() {
        return sendAT;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user=" + user.name() +
                "userP =" + user.permission() +
                ", message='" + message + '\'' +
                ", sendAT=" + sendAT +
                '}';
    }
}
