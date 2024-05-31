package main.system.panelAuth;

import com.github.twitch4j.common.exception.UnauthorizedException;
import com.google.gson.Gson;
import main.system.panelAuth.botLogin.BotLoginRepo;
import main.system.panelAuth.botUser.BotUser;
import main.system.panelAuth.botUser.BotUserRepo;
import main.system.panelAuth.exceptions.*;
import main.system.panelAuth.session.Session;
import main.system.panelAuth.session.SessionRepo;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.Instant;
import java.util.Locale;

@RestController
public class AuthController {

    private static final Faker faker = new Faker(Locale.GERMANY);
    private static final Gson gson = new Gson();
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Transactional
    void register(@RequestBody String body) {
        RegisterRequest register = gson.fromJson(body, RegisterRequest.class);
        try {
            authService.registerUser(register);
        } catch (DuplicateUserException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "BotUser with same Name already exists");
        }
    }

    @PostMapping("/login")
    String login(@RequestBody String body, @RequestHeader(value = "User-Agent") String userAgent) {
        LoginRequest login = gson.fromJson(body, LoginRequest.class);
        try {
            return authService.login(login, userAgent);
        } catch (IncompatibleHashFunctions e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed because of unexpected data format");
        } catch (InvalidAuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    @PostMapping("/logout")
    void logout(Authentication authentication) {
        authService.logout((String) authentication.getPrincipal());
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
