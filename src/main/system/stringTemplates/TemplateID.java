package main.system.stringTemplates;

import java.io.Serializable;

public class TemplateID implements Serializable {
    private String type;
    private String name;

    public TemplateID() {
    }

    public TemplateID(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
