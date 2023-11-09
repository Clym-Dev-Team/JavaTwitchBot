package main.system.eventSystem;

import com.github.twitch4j.chat.events.channel.FollowEvent;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * This Class is responsible for dispatching an Event to all Subscribers
 */
public class EventDispatcher {

    //Local Test, NOT MAIN ENTRY POINT
    //Could actually be deleted now, I think
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
//        for (Method method:eventHandler) {
//            System.out.println(method.getName());
//        }
        dispatch(new FollowEvent(null, null));
    }

    private static final Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

    /**
     * Hold all valid Subscriber that are Registered with the @Subscribe Annotation
     */
    private static final Set<Method> eventHandler = scanForSubscriber();

    /**
     * Uses Spring and Java Reflection to collect all Methods that are Annotated with @Subscriber and are Valid in the
     * main.modules folder and its subfolders.
     * A Method is Valid as a Subcriber if it: <br/>
     * - is static <br/>
     * - only has one Parameter <br/>
     * - the Parameter is of the same Class as specified in the Annotation <br/>
     * @return List of Standard Java Methods
     */
    private static Set<Method> scanForSubscriber() {
        //TODO Add requirement for the Method to be returntype void, don't think it would cause errors on our side, because we can ignore the return type, but it would go against the idea of subscriber, so an error should be given out
        //TODO Errors, this should ideally print out what Methods failed in which way. ideally in one single message, as to not spam the log
//        Reflections reflections = new Reflections("main.modules", Scanners.MethodsAnnotated);
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("main.modules").addScanners(Scanners.MethodsAnnotated));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Subscribe.class);
        //Die Methoden müssen static sein und dürfen nur einen Parameter haben,
        //und dieser Parameter muss gleich mit der Klasse in der Annotation sein
        return methods.stream()
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> method.getParameterTypes()[0].equals(method.getAnnotation(Subscribe.class).EventClass()))
                .collect(Collectors.toSet());
    }

    /**
     * This Method is for Injection/Dispatching a new Event into the EventStream
     * It calls all Subscriber to this Event, in a new Thread, and with their own Event Instance.
     * There is no control about the order of execution between the Subscriber.
     * <br/><br/>
     * If you want to call only your specific Subscriber with an Event that other Subscriber could also consume
     * then you should create a new Class that either hold all the same values or just inherits all of them. <br/>
     *
     * The Matching is done via the Class Name of the Subscriber and the Class specified in the @Subscriber Annotation
     * @param event
     */
    public static void dispatch(Object event) {
        //TODO Maybe Thread pool, damit die innit zeiten wegfallen, und der bot schneller antwortet
        new Thread(() -> {
            StringJoiner invokedMethods = new StringJoiner(",");
            for (Method method : eventHandler) {
                if (method.getAnnotation(Subscribe.class).EventClass().equals(event.getClass())) {
                    //Die Methoden müssen static sein und dürfen nur einen Parameter haben,
                    //und dieser Parameter muss gleich mit der Klasse in der Annotation sein
                    //Das ist aber durch scanForSubscriber() gesichert
//                    new Thread(() -> {
                        try {
                            method.invoke(null, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            logger.error("Could not invoke Method: {} with Event {}", method, event.getClass(), e);
                            throw new RuntimeException(e);
                        }
//                    }, method.getName()).start();
                    invokedMethods.add(method.getName());
                }
            }
            if (invokedMethods.toString().length() == 0)
                logger.debug("Dispatched Event: {} to No Methods", event.getClass());
            else
                logger.debug("Dispatched Event: {} to Methods: {}", event.getClass(), invokedMethods);
        }, "EventDispatcher").start();
    }
}
