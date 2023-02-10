package main.system.inputSystem;

public interface TipeeStreamInput extends Runnable {

    boolean checkConfiguration();

    boolean shutdown();

    @Override
    void run();

    boolean running();
}
