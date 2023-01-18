package main.system.commandSystem;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepo extends CrudRepository<Command, String> {
}
