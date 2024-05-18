package main.inputs.shared.oauth;

import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class OAuthEndpoint {

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

    //TODO Muss in Globale Config
    //    @Value("panelURL") funktioniert nicht
    private static final String panel_url = "http://localhost";

    public static String getRedirectUrl(String service) {
        return panel_url + "/auth/" + service;
    }

    public static String getOauthSetupUrl() {
        return panel_url + OauthUi.getUiLocator();
    }

    public static Optional<String> newOAuthGrantFlow(String accName, String service, OAuth2IdentityProvider iProvider, ArrayList<Object> scopes) {
        // create
        String state = RandomStringUtils.randomAlphanumeric(30);
        String auth_url = iProvider.getAuthenticationUrl(scopes, state);
        OauthRequest request = new OauthRequest(state, auth_url, accName, service);
        requests.add(request);

        // TODO add Timeout (10min)
        // wait for completion by user using web portal
        while (request.code.isEmpty()) {
            Thread.onSpinWait();
        }

        //return code, remove completed request
        String code = request.code.get();
        requests.remove(request);
        return Optional.of(code);
    }

    // TODO this should be /twitch, but dev.twitch.tv is broken for me
    @RequestMapping("/{service}")
    public String oAuthEndpoint(@PathVariable String service, @RequestParam(required = false) String code, @RequestParam(required = false) String scope, @RequestParam String state, @RequestParam(required = false) String error, @RequestParam(required = false) String error_description, Model model) {
        //If the state is not equal to the state of our request, the response is not ours
        var s = requests.stream().filter(r -> r.state.equals(state)).findFirst();
        if (s.isEmpty()) {
            model.addAttribute("error", "This oauth was never requested from the app");
            model.addAttribute("success", false);
            return "authResult";
        } else if (error != null) {
            model.addAttribute("error", error_description);
            model.addAttribute("success", false);
            return "authResult";
        }

        s.get().code = Optional.of(code);
        model.addAttribute("success", true);
        return "authResult";
    }
}
