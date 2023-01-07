package hooks;

import java.util.NoSuchElementException;

public class HookOptional {
    HookHolder hookHolder;
    String returnValue;

    public HookOptional(HookHolder hookHolder) {
        this.hookHolder = hookHolder;
    }

    public HookOptional(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnValuet() {
        if (returnValue != null)
            return returnValue;
        throw new NoSuchElementException("No value present");
    }

    public HookHolder getHookHolder() {
        if (hookHolder != null)
            return hookHolder;
        throw new NoSuchElementException("No value present");
    }

    public boolean isString() {
        return returnValue != null;
    }
}
