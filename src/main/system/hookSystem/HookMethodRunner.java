package main.system.hookSystem;

import main.system.commandSystem.repositories.Message;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Hooks are Methods that can be named in a Command and are used to replace parts of the String to embed values in the
 * Command that are calculated at runtime. <br/>
 * This class is responsible for collecting and executing the Hooks.
 *
 */
public class HookMethodRunner {

    /**
     * The Hook Methods found in the Modules directory
     */
    private static final Set<Method> hooks = scanForHooks();

    /**
     * This Method is only for the Execution of already found Hooks. <br>
     * It find a Matching hook, and hands the rest of to an internal function responsible for the execution of its code.
     * The firsts exact Match is used, overriding a Method is currently not possible!
     *
     * @param name The name of the parsed Hook, if no exact Match (case insensitiv) is found and Error is Returned into the Command
     * @param message The Message wich the Hook Originated from (for Context, if the Hooks wants it)
     * @param parameter The already calculated arguments that should be used to call this Hook based on the Text of the Command
     * @return The String returned by the Hook, of a String containing an Error Message
     */
    //TODO figure out Error Handling for Hooks/Add it to the Hook Docs
    public static String runHook(String name, Message message, HashMap<Integer, String> parameter) {
        for (Method method : hooks) {
            if (method.getName().equalsIgnoreCase(name))
                return invokeHookMethod(method, message, parameter);
        }
        return "{ERROR cant find Hook with Name: " + name + "}";
    }

    private static String invokeHookMethod(Method method, Message message, HashMap<Integer, String> parameter) {
        // dunno why this is a hashmap, it had some reason, but I forgot and I am hungry
        // maybe something about ordering of the Arguments
        ArrayList<Object> list = new ArrayList<>();
        if (parameter != null) {
            for (int i = 0; i < parameter.size(); i++) {
                list.add(parameter.get(i));
            }
        }

        // Conditionally add Message at beginning of Parameter list, if it is specified in the Method
        if (method.getParameterCount() >= 1 && method.getParameterTypes()[0].equals(Message.class)) {
            list.add(0, message);
        }

        try {
            return (String) method.invoke(null, list.toArray());
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
            return "{ERROR cant execute Hook: " + method.getName() + " " + e.getMessage() + " args: " + list.size() + " argsN: " + method.getParameterCount() + "}";
        }
    }

    /**
     * Scan for Hooks Modules in the module Directory with Java Reflection. <br>
     * A Method is only valid as a Hook if it: <br>
     *  - is static <br>
     *  - the Return Type is of type {@code String}, void is NOT allowed, instead you can return an empty String <br>
     *  - Parameters: (only these cases are allowed) <br>
     *    1) No Parameter <br>
     *    2) AS many Parameters of Type String as you like <br>
     *    3) A Parameter of Type {@link Message} as the Message that triggered the Hook, followed by as many Parameters of Type String as you like <br>
     *
     * @return The Set of valid Hook Methods
     */
    private static Set<Method> scanForHooks() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("main.modules").addScanners(Scanners.MethodsAnnotated));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Hook.class);
        //Die Methoden müssen static sein und müssen als ersten Parameter die message haben und danach eine beliebige Anzahl von Strings
        Set<Method> collect = methods.stream()
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getReturnType().equals(String.class))
                .filter(HookMethodRunner::parameterFilter)
                .collect(Collectors.toSet());


        //Debug Logging
        //TODO switch to logger
        String output = "";
        for (Method method : collect) {
            output += method.getName() + "(";

            for (Class<?> aClass : method.getParameterTypes())
                output += aClass.getName() + " ,";


            if (method.getParameterCount() > 0)
                output = output.substring(0, output.length() - 2);

            output += ")";
//            System.out.println(output);
            output = "";
        }
        return collect;
    }

    /**
     * Checks if a Methods Parameters are Valid to be used as a Hook Method.
     * For more information as to which Parameters are Valid look at the documentation for the scanForHooks Method in the Hook Runner
     * @see HookMethodRunner
     * @param method The Method to be checked
     * @return If it is valid
     */
    private static boolean parameterFilter(Method method) {
        Object[] distinctParameter = Arrays.stream(method.getParameterTypes()).distinct().toArray();

        boolean returnValue;
        switch (distinctParameter.length) {
            case 0 -> returnValue = true;
            case 1 -> returnValue = distinctParameter[0].equals(Message.class) || distinctParameter[0].equals(String.class);
            case 2 -> returnValue = method.getParameterTypes()[0].equals(Message.class) && distinctParameter[1].equals(String.class);
            default -> returnValue = false;
        }
        //System.out.println(method.getName() + ": " + count + " " + Arrays.toString(distinctParameter) + " -> " + returnValue);
        return returnValue;
    }
}
