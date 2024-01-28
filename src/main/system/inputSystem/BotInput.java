package main.system.inputSystem;

public interface BotInput extends Runnable {
    @Override
    void run();

    boolean shutdown();

    InputStatus getHealth();

    String threadName();

}
