package main.system.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
        return new UserAuthenticationToken(accessToken, true, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == PreAuthenticatedAuthenticationToken.class;
    }
}
