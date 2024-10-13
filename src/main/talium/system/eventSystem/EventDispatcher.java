package talium.system.eventSystem;

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

/**
 * This Class is responsible for dispatching an Event to all Subscribers
 */
public class EventDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

    /**
     * Hold all valid Subscriber that are Registered with the @Subscribe Annotation
     */
    private static final Method[] subscriber = scanForSubscriber();

    /**
     * Uses Spring and Java Reflection to collect all Methods that are Annotated with @Subscriber and are Valid in the
     * main.modules folder and its subfolders.
     * A Method is Valid as a Subcriber if it: <br/>
     * - is static <br/>
     * - only has one Parameter <br/>
     * - the Parameter is of the same Class as specified in the Annotation <br/>
     * @return List of Standard Java Methods
     */
    private static Method[] scanForSubscriber() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("main.modules").addScanners(Scanners.MethodsAnnotated));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Subscriber.class);
        return methods.stream()
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 1)
                .toArray(Method[]::new);
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
        new Thread(() -> {
            StringJoiner invokedMethods = new StringJoiner(",");
            for (Method method : subscriber) {
                if (!method.getParameters()[0].getClass().equals(event.getClass()))
                    continue;

                try {
                    method.invoke(null, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not invoke Method: {} with Event {}", method, event.getClass(), e);
                }
                invokedMethods.add(method.getName());
            }
            logger.debug("Dispatched Event: {} to Methods: [{}]", event.getClass(), invokedMethods);
        }, "EventDispatcher").start();
    }
}
