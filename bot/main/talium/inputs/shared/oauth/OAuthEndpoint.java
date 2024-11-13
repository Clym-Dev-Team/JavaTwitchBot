package talium.inputs.shared.oauth;

import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import talium.TwitchBot;
import talium.system.UnexpectedShutdownException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class OAuthEndpoint {

    private static String BOT_BASE_URL;
    private static String PANEL_BASE_URL;

    @Autowired
    public OAuthEndpoint(@Value("${serverBaseUrl}") String botBaseUrl, @Value("${panelBaseUrl}") String panelBaseUrl) {
        OAuthEndpoint.BOT_BASE_URL = botBaseUrl;
        OAuthEndpoint.PANEL_BASE_URL = panelBaseUrl;
    }

    public static class OauthRequest {
        public String state;
        public String url;
        public String accName;
        public String service;
        Optional<String> code;

        public OauthRequest(String state, String url, String accName, String service) {
            this.state = state;
            this.url = url;
            this.accName = accName;
            this.service = service;
            this.code = Optional.empty();
        }

        public OauthRequest(String state, String url, String accName, String service, Optional<String> code) {
            this.state = state;
            this.url = url;
            this.accName = accName;
            this.service = service;
            this.code = code;
        }
    }

    public static ArrayList<OauthRequest> requests = new ArrayList<>();

    public static String getRedirectUrl(String service) {
        return BOT_BASE_URL + "/auth/" + service;
    }

    public static String getOauthSetupUrl() {
        return PANEL_BASE_URL + "/auth";
    }

    public static Optional<String> newOAuthGrantFlow(String accName, String service, OAuth2IdentityProvider iProvider, ArrayList<Object> scopes) {
        // create
        String state = RandomStringUtils.randomAlphanumeric(30);
        String auth_url = iProvider.getAuthenticationUrl(scopes, state);
        OauthRequest request = new OauthRequest(state, auth_url, accName, service);
        requests.add(request);

        // TODO add Timeout (10min)
        // wait for completion by user using web portal
        while (request.code.isEmpty() && !TwitchBot.requestedShutdown) {
            Thread.onSpinWait();
        }
        if (TwitchBot.requestedShutdown) {
            throw new UnexpectedShutdownException();
        }

        //return code, remove completed request
        String code = request.code.get();
        requests.remove(request);
        return Optional.of(code);
    }

    @RequestMapping("/auth/{service}")
    public String oAuthEndpoint(@PathVariable String service, @RequestParam(required = false) String code, @RequestParam(required = false) String scope, @RequestParam String state, @RequestParam(required = false) String error, @RequestParam(required = false) String error_description, Model model) {
        //If the state is not equal to the state of our request, the response is not ours
        var s = requests.stream().filter(r -> r.state.equals(state)).findFirst();
        if (s.isEmpty()) {
            return STR."redirect:\{PANEL_BASE_URL}?success=false&error=" + URLEncoder.encode("This oauth was never requested from the app", StandardCharsets.UTF_8);
        } else if (error != null) {
            return STR."redirect:\{PANEL_BASE_URL}?success=false&error=" + URLEncoder.encode(error_description, StandardCharsets.UTF_8);
        }

        s.get().code = Optional.of(code);
        return STR."redirect:\{PANEL_BASE_URL}?success=true";
    }

    @GetMapping("/setup/auth/list")
    @ResponseBody
    public String oauthList() {
        Gson gson = new Gson();
        record OauthDTO(String url, String accName, String service) {}
        var r = requests.stream().map(o -> new OauthDTO(o.url, o.accName, o.service)).toList();
        return gson.toJson(r);
    }
}
