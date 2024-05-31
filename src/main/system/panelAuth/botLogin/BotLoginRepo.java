package main.system.panelAuth.botLogin;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BotLoginRepo extends CrudRepository<BotLogin, String> {

    @Query("select b from BotLogin b where b.username = ?1 and b.hashedPassword = ?2")
    Optional<BotLogin> findByUsernameAndHashedPassword(String username, String hashedPassword);
}
