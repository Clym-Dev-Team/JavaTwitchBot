package talium.system.hookSystem;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Hook {
    //TODO Docs for Error handling
    //TODO Docs for no overriding Hooks, first exact name Match
}
