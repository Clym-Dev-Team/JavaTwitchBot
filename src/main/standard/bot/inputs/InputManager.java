package standard.bot.inputs;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class InputManager {

    public static void receiveChatMessage(ChannelMessageEvent ircMessageEvent) {

        System.out.println(ircMessageEvent.getUser().getName() + ": " + ircMessageEvent.getMessage());
    }
}
