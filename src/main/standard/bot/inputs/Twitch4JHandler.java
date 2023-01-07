package standard.bot.inputs;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.common.enums.CommandPermission;

import java.util.HashSet;

public class Twitch4JHandler {

    public static void CheerEvent(CheerEvent cheerEvent) {

        //Do Database Stuff

        HashSet<CommandPermission> permissions = new HashSet<>();
        permissions.add(CommandPermission.MODERATOR);
        permissions.add(CommandPermission.BROADCASTER);

        InputManager.receiveChatMessage(new ChannelMessageEvent(
                cheerEvent.getChannel(),
                cheerEvent.getMessageEvent(),
                cheerEvent.getUser(),
                "!testCommand",
                permissions));
    }

    public static void FollowEvent(FollowEvent followEvent) {

//        InputManager.receiveChatMessage(new ChannelMessageEvent(
//                followEvent.getChannel(),
//                ,
//                followEvent.getUser(),
//                "!testCommand",
//                followEvent));
    }
}
