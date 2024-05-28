package main.system.panelAuth.session;

import org.springframework.data.repository.CrudRepository;

public interface SessionRepo extends CrudRepository<Session, SessionId> {

}
