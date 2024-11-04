package talium.system.panelAuth;

import kotlin.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import talium.system.panelAuth.panelUser.PanelUser;
import talium.system.panelAuth.panelUser.PanelUserRepo;
import talium.system.panelAuth.exceptions.*;
import talium.system.panelAuth.session.Session;
import talium.system.panelAuth.session.SessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final Duration sessionTimeout = Duration.ofMinutes(15);
    private final SessionRepo sessionRepo;
    private final PanelUserRepo panelUserRepo;
    public static boolean byPassAllAuth;

    @Autowired
    public AuthService(SessionRepo sessionRepo, PanelUserRepo panelUserRepo, @Value("${disableAllAuth}") String disableAllAuth) {
        this.sessionRepo = sessionRepo;
        this.panelUserRepo = panelUserRepo;
        byPassAllAuth = disableAllAuth.equals("true");
    }

    public boolean authenticate(String accessToken, String userAgent) {
        Optional<Session> optSession = sessionRepo.findByAccessToken(accessToken);
        if (optSession.isPresent()) {
            // if session is not timed out, and the userAgent is the same, we shortcircuit accepting the token and granting access
            Session session = optSession.get();
            if (!session.userAgent().equals(userAgent)) {
                // TODO more specific errors (http codes) since we are now using twitch as the authorisation server
                // TODO this should maybe be a setting for testing, instead of disabling all auth all together, just allow the token taken from the browser, to be used in curl/postman
                throw new AuthenticationRejected();
            }
            if (!session.lastRefreshedAt().plusSeconds(sessionTimeout.toSeconds()).isBefore(Instant.now())) {
                return true;
            }
        }
        // if there is no session, or session is timed out, or not valid for this userAgent we try to check if the accessToken is still valid for twitch
        // if the accesstoken is valid, we create a new session, if it is not valid anymore, we reject the request
        var validation = validateToken(accessToken);
        // no validation means token invalid
        if (validation.isEmpty()) {
            // TODO return error that authentication with twitch has failed (and not that the authentication was successfully, but no auth is granted to the panel
            return false;
        }

        //TODO check if userId has access to the panel

        var butUser = panelUserRepo.findById(validation.get().component2());
        if (butUser.isEmpty()) {
            PanelUser entity = new PanelUser(validation.get().component2());
            panelUserRepo.save(entity);
            butUser = Optional.of(entity);
        }

        Session s = new Session(accessToken, userAgent, Instant.now(), butUser.get());
        sessionRepo.save(s);
        return true;
    }

    public Optional<Pair<String, String>> validateToken(String accessToken) {
        try (var client = HttpClient.newBuilder().build()) {
            URI twitchValidateToken = URI.create("https://id.twitch.tv/oauth2/validate");
            var request = HttpRequest
                    .newBuilder(twitchValidateToken)
                    .setHeader("Authorization", STR."Bearer \{accessToken}")
                    .GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 401) {
                return Optional.empty();
            }
            var json = new JSONObject(response.body());
            var userId = json.getString("user_id");
            var userName = json.getString("login");
            return Optional.of(new Pair<>(userName, userId));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void forceLogout(PanelUser panelUser) {
        sessionRepo.deleteByPanelUser(panelUser);
    }

    @Transactional
    public void proxyForceLogout(String accessToken) throws SessionCouldNotBeFound {
        Optional<Session> currentSession = sessionRepo.findByAccessToken(accessToken);
        if (currentSession.isEmpty()) {
            throw new SessionCouldNotBeFound();
        }
        sessionRepo.deleteByPanelUser(currentSession.get().botUser());
    }
}
