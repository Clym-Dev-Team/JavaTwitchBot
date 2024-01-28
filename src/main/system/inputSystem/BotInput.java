package main.system.inputSystem;

public interface BotInput extends Runnable {
    @Override
    void run();

    void shutdown();

    InputStatus getHealth();

    String threadName();

}
