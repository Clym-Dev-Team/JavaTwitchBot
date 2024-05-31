package main.system.panelAuth;

import com.google.gson.Gson;
import main.system.panelAuth.botUser.BotUser;
import main.system.panelAuth.botUser.BotUserRepo;
import main.system.panelAuth.session.Session;
import main.system.panelAuth.session.SessionRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class AuthController {

    private final SessionRepo sessionRepo;
    private final BotUserRepo botUserRepo;

    @Autowired
    public AuthController(SessionRepo sessionRepo, BotUserRepo botUserRepo) {
        this.sessionRepo = sessionRepo;
        this.botUserRepo = botUserRepo;
    }

    @PostMapping("/register")
    String register(@RequestBody String body, @RequestHeader(value = "User-Agent") String userAgent) {
        //TODO register user into DB
        return "nope";
    }


        @PostMapping("/login")
    String login(@RequestBody String body, @RequestHeader(value = "User-Agent") String userAgent) {
        Gson gson = new Gson();
        LoginRequest login = gson.fromJson(body, LoginRequest.class);
        //TODO search for user, create token, create session
        var user = botUserRepo.findByUsername(login.username());
        if (user == null) {
            user = new BotUser(login.username());
            botUserRepo.save(user);
        }

        String accessToken = RandomStringUtils.randomAlphanumeric(30);
        Session s = new Session(accessToken, userAgent, Instant.now(), user);
        sessionRepo.save(s);
        return accessToken;
    }

    @PostMapping("/logout")
    void logout(@RequestBody String accessToken) {
        //TODO delete active session
    }

    @PostMapping("/forceLogout")
    void sessionReset(@RequestBody String accessToken) {
        //TODO delete all active sessions
    }

}
