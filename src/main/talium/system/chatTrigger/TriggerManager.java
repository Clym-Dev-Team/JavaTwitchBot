package talium.system.chatTrigger;

import java.util.regex.Pattern;

import talium.system.chatTrigger.cooldown.ChatCooldown;
import talium.system.commandSystem.repositories.ChatMessage;
import talium.system.commandSystem.repositories.CooldownType;
import talium.system.commandSystem.repositories.TwitchUserPermission;
import talium.system.eventSystem.Subscriber;

import java.util.List;

import static talium.system.chatTrigger.cooldown.CooldownService.*;

record ManagedTrigger(
        String id,
        List<Pattern> patterns,
        TwitchUserPermission permission,
        ChatCooldown userCooldown,
        ChatCooldown globalCooldown,
        TriggerCallback callback
) {
}

public class TriggerManager {
    // TODO:REMOVE tests
    public static void testFunction(ChatMessage messageEvent) {
        System.out.println(STR."TESTFUNCTION mesage: \{messageEvent.message()}");
    }

    //    MessagePattern pattern1 = new MessagePattern("!test", false, false, true);
    private static final Pattern pattern1 = Pattern.compile("!test");
    private static final ChatCooldown defaultCooldown = new ChatCooldown(CooldownType.MESSAGES, 2);
    private static final ManagedTrigger managedTrigger = new ManagedTrigger("test.testID", List.of(pattern1), TwitchUserPermission.EVERYONE, defaultCooldown, defaultCooldown, TriggerManager::testFunction);
    // end of tests

    private static final List<ManagedTrigger> triggers = List.of(managedTrigger);
    //TODO get list of triggers


    @Subscriber
    public static void triggerFromMessage(ChatMessage messsage) {
        triggers.forEach(t -> processTrigger(t, messsage));
    }

    private static void processTrigger(ManagedTrigger trigger, ChatMessage message) {
        // if ordinal of user is smaller than the command/trigger, than the user has fewer permissions and is not allowed to execute
        if (message.user().permission().ordinal() <= trigger.permission().ordinal()) {
            return;
        }

        // check matcher
        boolean isMatched = false;
        for (var pat : trigger.patterns()) {
            if (pat.matcher(message.message()).matches()) {
                isMatched = true;
                break;
            }
        }

        // return if noting matched
        if (!isMatched) {
            return;
        }

        var cooldown = inGlobalCooldown(message, trigger.id(), trigger.userCooldown()) || inUserCooldown(message, trigger.id(), trigger.userCooldown());
        if (cooldown) {
            return;
        }

        updateCooldownState(message, trigger.id(), trigger.globalCooldown(), trigger.userCooldown());

        trigger.callback().triggerCallback(message);
    }
}
