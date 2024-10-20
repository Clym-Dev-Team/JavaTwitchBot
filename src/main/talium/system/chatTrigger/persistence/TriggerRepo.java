package talium.system.chatTrigger.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TriggerRepo extends CrudRepository<TriggerEntity, String> {

    List<TriggerEntity> getAllByisUserTrigger(boolean isUserTrigger);
}
