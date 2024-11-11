package talium.system.inputSystem;

import jakarta.annotation.Nullable;
import talium.system.inputSystem.configuration.InputConfiguration;

public interface BotInput extends Runnable {
    @Override
    void run();

    void shutdown();

    InputStatus getHealth();

    @Nullable
    InputConfiguration getConfiguration();

    String threadName();

}
