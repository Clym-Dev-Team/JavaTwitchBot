package talium.system.panelAuth.session;

import talium.system.panelAuth.botUser.BotUser;

import java.io.Serializable;
import java.util.Objects;

public class SessionId implements Serializable {
    String accessToken;
    BotUser botUser;

    public SessionId(String accessToken, BotUser botUser) {
        this.accessToken = accessToken;
        this.botUser = botUser;
    }

    public SessionId() {
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionId sessionId)) return false;

        return Objects.equals(accessToken, sessionId.accessToken) && Objects.equals(botUser, sessionId.botUser);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accessToken);
        result = 31 * result + Objects.hashCode(botUser);
        return result;
    }
}
