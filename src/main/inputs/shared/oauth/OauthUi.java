package main.inputs.shared.oauth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class OauthUi {
    @RequestMapping("/setup/auth")
    public String setup(Model model) {
        if (OAuthEndpoint.requests.isEmpty()) {
            model.addAttribute("mode", "not_needed");
            return "setup";
        }

        model.addAttribute("authList", OAuthEndpoint.requests);
        model.addAttribute("mode", "needed");
        return "setup";
    }

    public static String getUiLocator() {
        return "/setup/auth";
    }

}
