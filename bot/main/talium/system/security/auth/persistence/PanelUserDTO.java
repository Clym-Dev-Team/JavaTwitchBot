package talium.system.security.auth.persistence;

import java.time.Instant;

public record PanelUserDTO(
        String username,
        String userId,
        long accountCreationTime
) {
}
