package hooks;

import java.lang.reflect.Method;

public class HookHolder {

    String hookName;
    Method hook;
    HookOptional[] hookOptionals;

    public HookHolder() {
    }

    void findHook() {
        //sucht als der Hook liste seinen eigenen raus und initialisiert seine Argumente Liste mit der richtigen länge
    }

    void executeHook() {
        //führt hook mit den Argumenten aus
    }
}
