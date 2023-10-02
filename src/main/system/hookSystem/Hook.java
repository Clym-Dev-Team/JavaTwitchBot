package main.system.hookSystem;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Hook {

//    @AliasFor(annotation = SuppressWarnings.class, attribute = "value")
//    String[] value() default {"unused"};
    //TODO Docs for Error handling
    //TODO Docs for no overriding Hooks, first exact name Match
}
