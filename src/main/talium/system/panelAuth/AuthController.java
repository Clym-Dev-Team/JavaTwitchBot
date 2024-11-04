package talium.system.panelAuth;

import com.google.gson.Gson;
import talium.system.panelAuth.botUser.BotUser;
import talium.system.panelAuth.exceptions.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/forceLogout")
    void sessionReset(Authentication authentication) {
        BotUser botUser = (BotUser) authentication.getDetails();
        if (botUser != null) {
            authService.forceLogout(botUser);
            return;
        }
        try {
            authService.proxyForceLogout((String) authentication.getPrincipal());
        } catch (SessionCouldNotBeFound e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authenticated, but User could not be found!");
        }
    }
}
