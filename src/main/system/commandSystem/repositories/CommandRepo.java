package main.system.commandSystem.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommandRepo extends CrudRepository<Command, String> {

    @NotNull ArrayList<Command> findAll();
}
