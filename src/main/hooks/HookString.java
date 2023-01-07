package hooks;

public class HookString {
    String name;
    String returnValue;
    HookString[] HookString;

    public HookString(String name, hooks.HookString[] hookString) {
        this.name = name;
        HookString = hookString;
    }

    public HookString(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnValue() {
        if (returnValue != null) {
            System.out.println("\"" + returnValue + "\"");
            return returnValue;
        }
        String args = "";
        for (hooks.HookString hookString : HookString) {
            args += hookString.getReturnValue();
        }
        String s = "Hook: " + name + " with Args: " + args;
        System.out.println(s);
        return s;
    }
}
