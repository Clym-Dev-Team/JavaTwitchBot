package main.system.stringTemplates;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Table(name = "template_strings")
@Entity
@Component
@IdClass(TemplateID.class)
public class Template {
    public @Id String type;
    public @Id String name;
    public String template;

    @Autowired
    public void setRepo(TemplateRepo repo) {
        Template.repo = repo;
    }
    public static TemplateRepo repo;
}
