package main.system.panelAuth;

public record RegisterRequest(
        String username,
        String password,
        String alg
) {}
