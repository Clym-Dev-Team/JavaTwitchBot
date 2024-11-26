package talium.system.coinsWatchtime;

import org.jetbrains.annotations.Nullable;
import talium.inputs.Twitch4J.ChatMessage;
import talium.system.inputSystem.BotInput;
import talium.system.inputSystem.Input;
import talium.system.inputSystem.InputStatus;
import talium.system.inputSystem.configuration.InputConfiguration;

/**
 * Does nothing, only temporarily exists to add watchtime and coins commands
 */
@Input
public class CoinsDummyInput implements BotInput {
    @Override
    public void run() {
        //noop
    }

    @Override
    public void shutdown() {
        //noop
    }

    @Override
    public InputStatus getHealth() {
        return InputStatus.HEALTHY;
    }

    @Override
    public String threadName() {
        return "CoinsDummyInput";
    }

    @Override
    public @Nullable InputConfiguration getConfiguration() {
        return new InputConfiguration.Builder()
                .addCallbackCommand("watchtime.getwatchtime", "!watchtime", CoinsDummyInput::triggerGetWatchtime)
                .addCallbackCommand("watchtime.getCoins", "!coins", CoinsDummyInput::triggerGetCoins)
                .build();
    }

    public static void triggerGetWatchtime(String triggerId, ChatMessage message) {
        System.out.println("Triggered !watchtime");
    }

    public static void triggerGetCoins(String triggerId, ChatMessage message) {
        System.out.println("Triggered !coins");
    }
}
