package talium.system.panelAuth;

import org.springframework.http.ResponseEntity;
import talium.system.panelAuth.persistence.PanelUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import talium.system.panelAuth.persistence.Session;
import talium.system.panelAuth.persistence.SessionRepo;

import java.util.Optional;

@RestController
public class AuthController {
    private final SessionRepo sessionRepo;

    @Autowired
    public AuthController(SessionRepo sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @PostMapping("/forceLogout")
    ResponseEntity<String> sessionReset(Authentication authentication) {
        PanelUser panelUser = (PanelUser) authentication.getDetails();
        if (panelUser != null) {
            sessionRepo.deleteByPanelUser(panelUser);
            return ResponseEntity.ok().build();
        }
        // delete all session with the user that owns this session
        Optional<Session> currentSession = sessionRepo.findByAccessToken((String) authentication.getPrincipal());
        if (currentSession.isEmpty()) {
            return new ResponseEntity<>("Authenticated, but User of this session could not be found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        sessionRepo.deleteByPanelUser(currentSession.get().botUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logoutAll")
    void logoutAllUsers() {
        sessionRepo.deleteAll();
    }

}
