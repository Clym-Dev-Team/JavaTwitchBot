package main.system.springSecurity;

import main.system.panelAuth.botUser.BotUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class UserAuthenticationToken implements Authentication {
    String accessToken;
    boolean isAuthenticated;
    Optional<BotUser> botUser;
    List<SimpleGrantedAuthority> grantedAuthorities;

    public UserAuthenticationToken(String accessToken, boolean isAuthenticated, BotUser botUser, SimpleGrantedAuthority ...authority) {
        this.accessToken = accessToken;
        this.isAuthenticated = isAuthenticated;
        this.botUser = Optional.ofNullable(botUser);
        this.grantedAuthorities = Arrays.stream(authority).toList();
    }

    public UserAuthenticationToken(String accessToken, boolean isAuthenticated) {
        this.accessToken = accessToken;
        this.isAuthenticated = isAuthenticated;
        this.botUser = Optional.empty();
        this.grantedAuthorities = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return botUser.orElse(null);
    }

    @Override
    public Object getPrincipal() {
        return accessToken;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return botUser.isEmpty() ? accessToken : botUser.get().username;
    }
}
