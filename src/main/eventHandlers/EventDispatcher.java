package eventHandlers;

import com.github.twitch4j.chat.events.channel.FollowEvent;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

public class EventDispatcher {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
//        for (Method method:eventHandler) {
//            System.out.println(method.getName());
//        }
        dispatchEvent(new FollowEvent(null, null));
    }

    private static final Set<Method> eventHandler = scanForSubscriber();

    private static Set<Method> scanForSubscriber() {
        Reflections reflections = new Reflections("eventHandlers.subscriber", Scanners.MethodsAnnotated);
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Subscribe.class);
        //Die Methoden müssen static sein und dürfen nur einen Parameter haben,
        // und dieser Parameter muss gleich mit der Klasse in der Annotation sein
        return methods.stream()
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> method.getParameterTypes()[0].equals(method.getAnnotation(Subscribe.class).EventClass()))
                .collect(Collectors.toSet());
    }

    public static void dispatchEvent(Object event) throws InvocationTargetException, IllegalAccessException {
        for (Method method : eventHandler) {
            if (method.getAnnotation(Subscribe.class).EventClass().equals(event.getClass())) {
                //Die Methoden müssen static sein und dürfen nur einen Parameter haben,
                //und dieser Parameter muss gleich mit der Klasse in der Annotation sein
                //Das ist aber durch scanForSubscriber() gesichert
                method.invoke(null, event);
            }
        }
    }
}
