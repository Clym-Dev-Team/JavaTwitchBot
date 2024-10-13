package talium.system.panelAuth.session;

import talium.system.panelAuth.botUser.BotUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepo extends CrudRepository<Session, SessionId> {

    long deleteByAccessToken(String accessToken);

    long deleteByBotUser(BotUser botUser);

    Optional<Session> findByAccessToken(String accessToken);
}
