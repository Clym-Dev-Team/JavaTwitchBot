package talium.system.panelAuth;

import talium.system.panelAuth.panelUser.PanelUser;
import talium.system.panelAuth.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/forceLogout")
    void sessionReset(Authentication authentication) {
        PanelUser panelUser = (PanelUser) authentication.getDetails();
        if (panelUser != null) {
            authService.forceLogout(panelUser);
            return;
        }
        try {
            authService.proxyForceLogout((String) authentication.getPrincipal());
        } catch (SessionCouldNotBeFound e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authenticated, but User could not be found!");
        }
    }
}
