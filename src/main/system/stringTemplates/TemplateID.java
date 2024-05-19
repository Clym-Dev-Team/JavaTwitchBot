package main.system.stringTemplates;

import java.io.Serializable;

public class TemplateID implements Serializable {
    private String type;
    private String name;
    private String objectName;

    public TemplateID() {
    }

    public TemplateID(String type, String name, String objectName) {
        this.type = type;
        this.name = name;
        this.objectName = objectName;
    }
}
