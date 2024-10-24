package talium.system.stringTemplates;

import jakarta.persistence.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import talium.system.chatTrigger.persistence.TriggerEntity;

@Table(name = "sys-string_templates")
@Entity
public class Template {
    public @Id String id;
    public String template;
    public @Nullable String messageColor;

    public Template(String id, String template, @Nullable String messageColor) {
        this.id = id;
        this.template = template;
        this.messageColor = messageColor;
    }

    protected Template() {

    }
}
