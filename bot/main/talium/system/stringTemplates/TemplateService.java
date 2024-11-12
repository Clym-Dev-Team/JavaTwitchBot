package talium.system.stringTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import talium.system.twitchCommands.persistence.TriggerRepo;
import talium.system.twitchCommands.persistence.TriggerService;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {
    private final TriggerRepo triggerRepo;
    TemplateRepo repo;

    @Autowired
    public TemplateService(TemplateRepo repo, TriggerRepo triggerRepo) {
        this.repo = repo;
        this.triggerRepo = triggerRepo;
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

    public Optional<Template> getTemplateByCommandId(String commandId) {
        var command = triggerRepo.findById(commandId);
        if (command.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(command.get().template);
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
