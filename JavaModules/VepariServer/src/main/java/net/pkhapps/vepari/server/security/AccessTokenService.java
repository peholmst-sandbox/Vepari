package net.pkhapps.vepari.server.security;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;

/**
 * Service for issuing and invalidating access tokens.
 */
public interface AccessTokenService {

    /**
     * Generates and returns a new access token for the given user.
     *
     * @param user the user for whom the token should be generated.
     * @return the access token.
     */
    @NonNull
    AccessToken generateToken(@NonNull User user);

    /**
     * Invalidates the given token. If the token does not exist, this method does nothing.
     *
     * @param token the token to invalidate.
     */
    void invalidateToken(@NotNull String token);

    /**
     * Invalidates all access tokens of the given user. If the user does not exist or has no tokens, this method does
     * nothing.
     *
     * @param user the user whose access tokens should be invalidated.
     */
    void invalidateAllTokens(@NotNull User user);
}
