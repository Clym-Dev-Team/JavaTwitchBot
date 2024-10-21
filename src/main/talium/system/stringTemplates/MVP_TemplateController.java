package talium.system.stringTemplates;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/templates")
public class MVP_TemplateController {

    private final TemplateService templateService;

    @Autowired
    public MVP_TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/all")
    String all() {
        List<Template> templates = templateService.getAllTemplates();
        Gson gson = new Gson();
        return gson.toJson(templates);
    }

    @GetMapping
    String getBy(@RequestParam String module, @RequestParam String type, @RequestParam String object) {
        Optional<Template> template = templateService.getTemplateById(STR."\{module}.\{type}.\{object}");
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
        try {
            String id = STR."\{module}.\{type}.\{object}";
            templateService.updateTemplateStringById(id, new_template);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }
}
