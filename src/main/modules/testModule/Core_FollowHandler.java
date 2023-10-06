package main.modules.testModule;

import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;
import main.system.eventSystem.Subscriber;

@SuppressWarnings("unused")
public class Core_FollowHandler {

    @Subscriber
    public static void handleFollow(FollowEvent followEvent) {
        System.out.println("Test follow Event");
    }

    @Subscriber
    public static void handleSub(SubscriptionEvent subscriptionEvent) {
        System.out.println("Sub follow Event");
    }
}
