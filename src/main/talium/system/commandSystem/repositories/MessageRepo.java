package talium.system.commandSystem.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends CrudRepository<ChatMessageDAO, String> {
}
