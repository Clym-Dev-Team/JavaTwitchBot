package talium.system.panelAuth;

public record RegisterRequest(
        String username,
        String password,
        String alg
) {}
