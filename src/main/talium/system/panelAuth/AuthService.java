package talium.system.panelAuth;

import com.google.common.hash.Hashing;
import talium.system.panelAuth.botLogin.BotLogin;
import talium.system.panelAuth.botLogin.BotLoginRepo;
import talium.system.panelAuth.botUser.BotUser;
import talium.system.panelAuth.botUser.BotUserRepo;
import talium.system.panelAuth.exceptions.*;
import talium.system.panelAuth.exceptions.*;
import talium.system.panelAuth.session.Session;
import talium.system.panelAuth.session.SessionRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final Duration sessionTimeout = Duration.ofHours(24);
    private final SessionRepo sessionRepo;
    private final BotUserRepo botUserRepo;
    private final BotLoginRepo botLoginRepo;
    public static boolean byPassAllAuth;

    @Autowired
    public AuthService(SessionRepo sessionRepo, BotUserRepo botUserRepo, BotLoginRepo botLoginRepo, @Value("${disableAllAuth}") String disableAllAuth) {
        this.sessionRepo = sessionRepo;
        this.botUserRepo = botUserRepo;
        this.botLoginRepo = botLoginRepo;
        byPassAllAuth = disableAllAuth.equals("true");
    }

    @Transactional
    public void registerUser(RegisterRequest register) throws DuplicateUserException {
        if (botUserRepo.existsById(register.username())) {
            throw new DuplicateUserException();
        }
        String password = Hashing.sha512().hashString(register.password(), StandardCharsets.UTF_8).toString();
        BotUser user = new BotUser(register.username());
        BotLogin login = new BotLogin(register.username(), register.password(), register.alg(), hashFunction(), user);
        botUserRepo.save(user);
        botLoginRepo.save(login);
    }

    @Transactional
    public String login(LoginRequest loginRequest, String userAgent) throws IncompatibleHashFunctions, InvalidAuthenticationException {
        if (byPassAllAuth) {
            return "BYPASSEDAUTHTOKEN";
        }

        String password = Hashing.sha512().hashString(loginRequest.hash(), StandardCharsets.UTF_8).toString();
        Optional<BotLogin> login = botLoginRepo.findByUsernameAndHashedPassword(loginRequest.username(), loginRequest.hash());
        if (login.isEmpty()) {
            throw new InvalidAuthenticationException("Invalid username or password");
        }
        if (!login.get().alg2().equals(hashFunction())) {
            throw new IncompatibleHashFunctions(STR."Hashfunktion used to save to DB: \{login.get().alg2()} currently supported Hashfunction: \{hashFunction()}");
        }

        String accessToken = RandomStringUtils.randomAlphanumeric(30);
        Session s = new Session(accessToken, userAgent, Instant.now(), login.get().botUser());
        sessionRepo.save(s);
        return accessToken;
    }

    @Transactional
    public void logout(String accessToken) {
        sessionRepo.deleteByAccessToken(accessToken);
    }

    @Transactional
    public void forceLogout(BotUser botUser) {
        sessionRepo.deleteByBotUser(botUser);
    }

    @Transactional
    public void proxyForceLogout(String accessToken) throws SessionCouldNotBeFound {
        Optional<Session> currentSession = sessionRepo.findByAccessToken(accessToken);
        if (currentSession.isEmpty()) {
            throw new SessionCouldNotBeFound();
        }
        sessionRepo.deleteByBotUser(currentSession.get().botUser());
    }

    @Transactional
    public boolean authenticate(String accessToken, String userAgent) throws AuthenticationRejected {
        Optional<Session> optSession = sessionRepo.findByAccessToken(accessToken);
        if (optSession.isEmpty()) {
            throw new AuthenticationRejected();
        }
        Session session = optSession.get();
        if (!session.userAgent().equals(userAgent)) {
            throw new AuthenticationRejected();
        }
        if (session.lastRefreshedAt().plusSeconds(sessionTimeout.toSeconds()).isBefore(Instant.now())) {
            throw new AuthenticationRejected();
        }
        session.setLastRefreshedAt(Instant.now());
        sessionRepo.save(session);
        return true;
    }

    public BotUser getBotUser(String accessToken) {
        Optional<Session> optSession = sessionRepo.findByAccessToken(accessToken);
        return optSession.map(Session::botUser).orElse(null);
    }

    private String hashPW(String password) {
        return Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
    }

    private String hashFunction() {
        return "sha512";
    }
}
