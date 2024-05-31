package main.system.springSecurity;

import jakarta.servlet.http.HttpServletRequest;
import main.system.panelAuth.AuthService;
import main.system.panelAuth.botUser.BotUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class HeaderAuthenticationProvider implements AuthenticationProvider {

    boolean byPassAuthentication;
    AuthService authService;

    @Autowired
    public HeaderAuthenticationProvider(AuthService authService, @Value(value = "${disableAllAuth:false}") boolean byPassAuthentication) {
        this.byPassAuthentication = byPassAuthentication;
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            if (byPassAuthentication) {
                return new UserAuthenticationToken("TESTUSER", true, null);
            }
            throw new AuthenticationCredentialsNotFoundException("Token header does not exist");
        }
        String userAgent = (String) authentication.getPrincipal();

        String accessToken = (String) authentication.getCredentials();
        boolean authenticated = authService.authenticate(accessToken, userAgent);


        if (byPassAuthentication) {
            authenticated = true;
        }

        return new UserAuthenticationToken(accessToken, authenticated, authService.getBotUser(accessToken));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == PreAuthenticatedAuthenticationToken.class;
    }
}
