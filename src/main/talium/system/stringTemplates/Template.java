package talium.system.stringTemplates;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Table(name = "sys-string_templates")
@Entity
@Component
public class Template {
    public @Id String id;
    public String template;
    @Nullable public String messageColor;
}
