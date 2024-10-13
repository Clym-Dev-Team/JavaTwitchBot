package talium.system.stringTemplates;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/templates")
public class MVP_TemplateController {

    private final TemplateRepo templateRepo;

    public MVP_TemplateController(TemplateRepo templateRepo) {
        this.templateRepo = templateRepo;
    }

    @GetMapping("/all")
    String all() {
        List<Template> templates = (List<Template>) Template.repo.findAll();
        Gson gson = new Gson();
        return gson.toJson(templates);
    }

    @GetMapping
    String getBy(@RequestParam String module, @RequestParam String type, @RequestParam String object) {
        Optional<Template> template = Template.repo.findByModuleAndTypeAndObject(module, type, object);
        if (template.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
            //TODO
        }
        Gson gson = new Gson();
        return gson.toJson(template.get());
    }

    @PostMapping
    void updateTemplateString(@RequestParam String module, @RequestParam String type, @RequestParam String object, @RequestBody String new_template) {
        Optional<Template> template_opt = Template.repo.findByModuleAndTypeAndObject(module, type, object);
        if (template_opt.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
            //TODO
        }
        Template template = template_opt.get();
        template.template = new_template;
        templateRepo.save(template);
    }
}
