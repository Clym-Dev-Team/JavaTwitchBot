package main.system.hookSystem;

import main.system.commandSystem.repositories.Message;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Hooks are Methods that can be named in a Command and are used to replace parts of the String to embed values in the
 * Command that are calculated at runtime. <br/>
 * This class is responsible for collecting and executing the Hooks.
 *
 */
public class HookMethodRunner {

    private static final Logger logger = LoggerFactory.getLogger(HookMethodRunner.class);

    /**
     * The Hook Methods found in the Modules directory
     */
    private static Method[] hooks = scanForHooks("main.modules");

    /**
     * This overrides the Hook package destination <b>for the entire duration of the programm</b> this needs to be done
     * because tests have a different folder structure. Only call in Unit Test initialisation!
     */
    public static void rebuildForTests() {
        hooks = scanForHooks("hooksystem");
    }

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
    public static String runHook(String name, Message message, ArrayList<String> parameter) {
        for (Method method : hooks) {
            if (method.getName().equalsIgnoreCase(name))
                return invokeHookMethod(method, message, parameter);
        }
        logger.error("Hook with name: \"{}\" could not be found!", name);
        return "{ERROR}";
    }

    private static @NotNull String invokeHookMethod(@NotNull Method method, @NotNull Message message, @NotNull ArrayList<String> parameter) {
        Object[] executionParameter;
        // Conditionally add Message at beginning of Parameter list, if it is specified in the Method
        if (method.getParameters().length >= 1 && method.getParameters()[0].getType() == Message.class) {
            executionParameter = new Object[parameter.size() + 1];
            executionParameter[0] = message;
            System.arraycopy(parameter.toArray(), 0, executionParameter, 1, parameter.size());
        } else
            executionParameter = parameter.toArray();

        try {
            return (String) method.invoke(null, executionParameter);

            //ERROR HANDLING
        } catch (IllegalArgumentException e) {
            //Skip "class " to reduce clutter in logs
            String declaringClass = method.getDeclaringClass().toString().substring(6);
            StringJoiner executionParameterTypes = new StringJoiner(", ");
            for (Object o : executionParameter) {
                executionParameterTypes.add(o.getClass().toString().substring(6));
            }

            // this \/, instead of Arrays.toString to remove the []
            StringJoiner methodParameterTypes = new StringJoiner(", ");
            for (Parameter p : method.getParameters()) {
                methodParameterTypes.add(p.toString());
            }

            logger.error("Hook invoked with invalid Argument(s)! Failed to pass: [{}] --to-> {}.{}({})",
                    executionParameterTypes,
                    declaringClass,
                    method.getName(),
                    methodParameterTypes
            );

        } catch (InvocationTargetException e) {
            String declaringClass = method.getDeclaringClass().toString().substring(6);
            logger.error("Hook threw a Exception: Source: {}.{}, E: {}", declaringClass, method.getName(), e.getMessage());

        } catch (IllegalAccessException e) {
            String declaringClass = method.getDeclaringClass().toString().substring(6);
            logger.error("Hook is Illegal to Access: Hook: {}.{}, E: {}", declaringClass, method.getName(), e.getMessage());
        }
        return "{ERROR}";
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
    private static Method[] scanForHooks(String hook_source) {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(hook_source).addScanners(Scanners.MethodsAnnotated));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Hook.class);
        //Die Methoden müssen static sein und müssen als ersten Parameter die message haben und danach eine beliebige Anzahl von Strings
        return methods.stream()
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getReturnType().equals(String.class))
                .filter(HookMethodRunner::parameterFilter)
                .toArray(Method[]::new);
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
        return switch (distinctParameter.length) {
            case 0 -> true;
            case 1 -> distinctParameter[0].equals(Message.class) || distinctParameter[0].equals(String.class);
            case 2 -> method.getParameterTypes()[0].equals(Message.class) && distinctParameter[1].equals(String.class);
            default -> false;
        };
    }
}
