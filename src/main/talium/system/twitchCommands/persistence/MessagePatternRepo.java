package talium.system.twitchCommands.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessagePatternRepo extends CrudRepository<MessagePattern, MessagePatternId> {

    List<MessagePattern> getAllByParentTrigger(TriggerEntity parentTrigger);

    @Transactional
    void deleteAllByParentTrigger(TriggerEntity parentTrigger);

}
