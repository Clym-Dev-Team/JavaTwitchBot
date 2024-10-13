package talium.system.stringTemplates;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Table(name = "sys-static_templates")
@Entity
@Component
@IdClass(TemplateID.class)
public class Template {
    public @Id String module;
    public @Id String type;
    public @Id String object;
    public String template;

    @Autowired
    public void setRepo(TemplateRepo repo) {
        Template.repo = repo;
    }
    public static TemplateRepo repo;
}
