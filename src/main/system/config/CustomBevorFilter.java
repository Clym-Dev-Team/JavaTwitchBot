package main.system.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;


public class CustomBevorFilter extends AbstractAuthenticationProcessingFilter {

    public CustomBevorFilter(AuthenticationManager authenticationManager) {
        super("/**", authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        var token = request.getHeader("token");
        var auth = new PreAuthenticatedAuthenticationToken(token, null);
        return this.getAuthenticationManager().authenticate(auth);
    }
}
