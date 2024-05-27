package main.system.stringTemplates;

import java.io.Serializable;
import java.util.Objects;

public class TemplateID implements Serializable {
    private String module;
    private String type;
    private String object;

    public TemplateID() {
    }

    public TemplateID(String module, String type, String object) {
        this.module = module;
        this.type = type;
        this.object = object;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateID that)) return false;

        return Objects.equals(module, that.module) && Objects.equals(type, that.type) && Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(module);
        result = 31 * result + Objects.hashCode(type);
        result = 31 * result + Objects.hashCode(object);
        return Objects.hashCode(result);
    }
}
