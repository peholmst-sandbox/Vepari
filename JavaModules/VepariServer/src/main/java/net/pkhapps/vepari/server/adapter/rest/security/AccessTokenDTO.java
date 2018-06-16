package net.pkhapps.vepari.server.adapter.rest.security;

import net.pkhapps.vepari.server.security.AccessToken;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

/**
 * JSON DTO for an {@link AccessToken}.
 */
@SuppressWarnings("WeakerAccess")
        // Jackson wants public fields.
class AccessTokenDTO {

    public final String token;
    public final Instant issueDate;
    public final Instant expirationDate;

    AccessTokenDTO(@NonNull AccessToken accessToken) {
        Objects.requireNonNull(accessToken, "accessToken must not be null");
        token = accessToken.getToken();
        issueDate = accessToken.getIssueDate();
        expirationDate = accessToken.getExpirationDate();
    }
}
