package main.system.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;


public class HeaderAuthProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public HeaderAuthProcessingFilter(AuthenticationManager authenticationManager) {
        super("/**", authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        var token = request.getHeader("token");
        var auth = new PreAuthenticatedAuthenticationToken(token, null);
        return this.getAuthenticationManager().authenticate(auth);
    }

    @Override
    public void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
            ) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication( authResult);
        chain.doFilter(request, response);
    }

}
