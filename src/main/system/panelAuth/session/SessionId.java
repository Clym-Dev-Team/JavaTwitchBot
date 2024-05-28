package main.system.panelAuth.session;

import java.io.Serializable;

public class SessionId implements Serializable {
    String accessToken;
    String username;

    public SessionId(String accessToken, String username) {
        this.accessToken = accessToken;
        this.username = username;
    }

    public SessionId() {
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionId sessionId)) return false;

        return accessToken.equals(sessionId.accessToken) && username.equals(sessionId.username);
    }

    @Override
    public int hashCode() {
        int result = accessToken.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
}
