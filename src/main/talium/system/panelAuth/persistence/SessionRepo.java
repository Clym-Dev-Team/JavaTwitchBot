package talium.system.panelAuth.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepo extends CrudRepository<Session, String> {

    long deleteByAccessToken(String accessToken);

    void deleteByPanelUser(PanelUser panelUser);

    Optional<Session> findByAccessToken(String accessToken);
}
