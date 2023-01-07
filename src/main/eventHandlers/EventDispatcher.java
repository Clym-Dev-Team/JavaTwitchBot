package eventHandlers;

import com.github.twitch4j.chat.events.channel.FollowEvent;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EventDispatcher {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        System.out.println(EventDispatcher.eventHandler.size());
        dispatchEvent(new FollowEvent(null, null));
    }

    private static final Set<Method> eventHandler = scanForSubscriber();

    private static Set<Method> scanForSubscriber() {
        Reflections reflections = new Reflections("eventHandlers.subscriber", new MethodAnnotationsScanner());
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Subscribe.class);
        //Die Methoden müssen static sein und dürfen nur einen Parameter haben,
        // und dieser Parameter muss gleich mit der Klasse in der Annotation sein
        return methods.stream()
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> method.getParameterTypes()[0].equals(method.getAnnotation(Subscribe.class).EventClass()))
                .collect(Collectors.toSet());
    }

    private static Set<Method> scanForSubscriber2() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        System.out.println("0 = " + provider.findCandidateComponents("java.subscriber").size());
        System.out.println("1 = " + provider.findCandidateComponents("eventHandlers").size());
        System.out.println("2 = " + provider.findCandidateComponents("eventHandlers.subscriber").size());
        System.out.println("3 = " + provider.findCandidateComponents("eventHandlers.subscriber.Core_FollowHandler").size());
        System.out.println("4 = " + provider.findCandidateComponents("main.eventHandlers").size());
        System.out.println("5 = " + provider.findCandidateComponents("main.eventHandlers.subscriber").size());
        System.out.println("6 = " + provider.findCandidateComponents("main.eventHandlers.subscriber.Core_FollowHandler").size());
        System.out.println("7 = " + provider.findCandidateComponents("src.main.eventHandlers").size());
        System.out.println("8 = " + provider.findCandidateComponents("src.main.eventHandlers.subscriber").size());
        System.out.println("9 = " + provider.findCandidateComponents("src.main.eventHandlers.subscriber.Core_FollowHandler").size());

        Set<BeanDefinition> beanDefs = provider.findCandidateComponents("eventHandlers.subscriber");

        System.out.println("beanDefs.size() = " + beanDefs.size());
        System.out.println("BeanDefinition: ");
        for (BeanDefinition s : beanDefs) {
            System.out.println(s);
        }
        System.out.println("---BeanDefinition---");
        System.out.println();

        List<String> annotatedBeans = new ArrayList<>();
        for (BeanDefinition bd : beanDefs) {
            if (bd instanceof AnnotatedBeanDefinition) {
                Map<String, Object> annotAttributeMap = ((AnnotatedBeanDefinition) bd).getMetadata().getAnnotationAttributes(Subscribe.class.getCanonicalName());
                annotatedBeans.add(annotAttributeMap.get("name").toString());
            }
        }
        System.out.println("annotatedBeans.size() = " + annotatedBeans.size());
        System.out.println("Beans: ");
        for (String s : annotatedBeans) {
            System.out.println(s);
        }
        System.out.println("---Beans---");
        return null;
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
