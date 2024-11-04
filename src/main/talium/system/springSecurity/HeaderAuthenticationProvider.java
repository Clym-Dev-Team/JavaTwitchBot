package talium.system.springSecurity;

import talium.system.panelAuth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class HeaderAuthenticationProvider implements AuthenticationProvider {

    AuthService authService;

    @Autowired
    public HeaderAuthenticationProvider(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new AuthenticationCredentialsNotFoundException("Token header does not exist");
        }
        String userAgent = (String) authentication.getPrincipal();

        String accessToken = (String) authentication.getCredentials();
        boolean authenticated = authService.authenticate(accessToken, userAgent);

        return new UserAuthenticationToken(accessToken, authenticated);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == PreAuthenticatedAuthenticationToken.class;
    }
}
