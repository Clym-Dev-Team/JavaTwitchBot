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
import org.springframework.security.web.util.matcher.*;

import java.io.IOException;


public class HeaderAuthProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public HeaderAuthProcessingFilter(AuthenticationManager authenticationManager) {
        // Not trying to auth /login and /error Solution 1 (preferred)
        super(new NegatedRequestMatcher(
                new OrRequestMatcher(
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/error")
                )
        ), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        var token = request.getHeader("token");
        var auth = new PreAuthenticatedAuthenticationToken(token, null);
        // Not trying to auth /login and /error Solution 2
        //if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/error")) {
        //    return auth;
        //}
        return this.getAuthenticationManager().authenticate(auth);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

}
