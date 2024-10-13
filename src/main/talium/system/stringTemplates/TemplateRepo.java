package talium.system.stringTemplates;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TemplateRepo extends CrudRepository<Template, TemplateID> {

    Optional<Template> findByModuleAndTypeAndObject(String module, String type, String object);

    @Transactional
    @Modifying
    @Query("update Template t set t.template = ?1 where t.module = ?2 and t.type = ?3 and t.object = ?4")
    void updateTemplateByTypeAndNameAndObjectName(String template, String module, String type, String object);
}
