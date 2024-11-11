package talium.system.stringTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {
    TemplateRepo repo;

    @Autowired
    public TemplateService(TemplateRepo repo) {
        this.repo = repo;
    }

    public void updateTemplateStringById(String id, String template) {
        var t = repo.findById(id);
        if (t.isPresent()) {
            t.get().template = template;
            repo.save(t.get());
        } else {
            // TODO make error nicer, new type or smt
            throw new RuntimeException("templates does not exist");
        }
    }

    public Optional<Template> getTemplateById(String id) {
        return repo.findById(id);
    }

    public List<Template> getAllTemplates() {
        return (List<Template>) repo.findAll();
    }

    public void save(Template template) {
        repo.save(template);
    }

    public void saveIfAbsent(List<Template> templates) {
        for (Template template : templates) {
            if (repo.existsById(template.id)) {
                return;
            }
            save(template);
        }
    }
}
