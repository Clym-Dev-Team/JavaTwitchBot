package talium.system.stringTemplates;

import jakarta.persistence.Id;
import org.jetbrains.annotations.Nullable;

public record TemplateDTO (
        String id,
        String template,
        String messageColor
) {
    public TemplateDTO(Template template) {
        this(template.id, template.template, template.messageColor);
    }

    public Template toTemplate() {
        return new Template(this);
    }
}
