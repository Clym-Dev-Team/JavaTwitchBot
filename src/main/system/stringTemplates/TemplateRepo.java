package main.system.stringTemplates;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TemplateRepo extends CrudRepository<Template, TemplateID> {

    @NotNull
    Optional<Template> findByTypeAndName(String type, String name);
}
