package net.pkhapps.vepari.server.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;
import java.util.Objects;

/**
 * An authentication token for {@link AccessToken}-based authentication that can be used either as an authentication
 * request ({@link #isAuthenticated()} is set to false) or as the result of a successful authentication
 * ({@link #isAuthenticated()} is set to true).
 */
final class AccessTokenAuthentication extends AbstractAuthenticationToken {

    private final UserId principal;
    private final String token;

    /**
     * Creates a new unauthenticated {@code AccessTokenAuthentication} to use as an authentication request.
     *
     * @param token the {@link AccessToken#getToken() string token}.
     */
    AccessTokenAuthentication(@NonNull String token) {
        super(Collections.emptyList());
        this.token = Objects.requireNonNull(token, "token must not be null");
        this.principal = null;
        super.setAuthenticated(false);
    }

    /**
     * Creates a new authenticated {@code AccessTokenAuthentication}.
     *
     * @param token the {@link AccessToken} corresponding.
     */
    AccessTokenAuthentication(@NonNull AccessToken token) {
        super(Objects.requireNonNull(token, "token must not be null").getUser().getAuthorities());
        this.token = token.getToken();
        this.principal = token.getUser().toUserId();
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @NonNull
    public String getToken() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Cannot explicitly set the authenticated flag to true");
        }
        super.setAuthenticated(false);
    }
}
