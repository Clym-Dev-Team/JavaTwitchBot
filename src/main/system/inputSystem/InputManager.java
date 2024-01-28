package main.system.inputSystem;

import main.system.ASCIIProgressbar;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class InputManager {

    private static final Logger logger = LoggerFactory.getLogger(InputManager.class);
    private static final List<BotInput> inputs = new ArrayList<>();

    public static void startAllInputs() {
        var scannedInputs = scanForInputs().stream().toList();
        logger.info("Starting Inputs...");
        for (BotInput input : scannedInputs) {
            startInput(input);
        }
    }

    public static void startInput(BotInput input) {
        try {
            new Thread(input, "LAUNCHER-" + input.threadName()).start();
            inputs.add(input);
        } catch (RuntimeException e) {
            logger.error("Exception starting Input: {} because: {}", input.getClass().getName(), e.getMessage());
            HealthManager.reportStatus(input, InputStatus.DEAD);
        }
    }

    public static void stopAllInputs() {
        logger.info("Stopping Inputs...");
        for (BotInput input : inputs) {
            stopInput(input);
        }
    }

    public static void stopInput(BotInput input) {
        //TODO kill any Inputs if they do not shut down after a certain time
        try {
            new Thread(() -> {
                input.shutdown();
                inputs.remove(input);
            }, "CLOSER-" + input.threadName()).start();
//            inputs.remove(input);
            HealthManager.reportStatus(input, InputStatus.STOPPED);
        } catch (Exception e) {
            logger.error("Exception stopping Input: {} because: {}", input.getClass().getName(), e.getMessage());
            HealthManager.reportStatus(input, InputStatus.DEAD);
        }
    }

    public static boolean finishedShutdown() {
        return !inputs.isEmpty();
    }

    private static HashSet<BotInput> scanForInputs() {
//        Reflections reflections = new Reflections("main.inputs", Scanners.TypesAnnotated);
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("main.inputs").addScanners(Scanners.TypesAnnotated));
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Input.class);
        HashSet<BotInput> inputSet = new HashSet<>();

        //get all Classes Annotated with Input.class, and if they implement the TwitchBotInput interface,
        //then a new instance will be created, and get added to the Set of Inputs
        for (Class<?> aClass : typesAnnotatedWith) {
            if (Arrays.stream(aClass.getInterfaces()).toList().contains(BotInput.class)) {
                //Try to create a new instance of that class
                try {
                    inputSet.add((BotInput) (aClass.getConstructor().newInstance()));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        logger.info("Scanned for Inputs, found: {}, configured correctly: {}", typesAnnotatedWith.size(), ASCIIProgressbar.consoleBar(inputSet.size(), typesAnnotatedWith.size()));
        return inputSet;
    }
}
