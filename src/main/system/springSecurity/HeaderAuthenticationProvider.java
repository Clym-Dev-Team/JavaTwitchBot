package main.system.springSecurity;

import main.system.panelAuth.botUser.BotUser;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class HeaderAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("Token header does not exist");
        }
        String accessToken = authentication.getPrincipal().toString();
        //TODO get user from accessToken
        //TODO auth
        return new UserAuthenticationToken(accessToken, true, new BotUser("test USer "));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == PreAuthenticatedAuthenticationToken.class;
    }
}
