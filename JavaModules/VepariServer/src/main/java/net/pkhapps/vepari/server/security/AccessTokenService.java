package net.pkhapps.vepari.server.security;

/**
 * TODO Document me
 */
public interface AccessTokenService {

    AccessToken generateTokenForCurrentUser();

    void invalidateCurrentToken();

    void invalidateAllTokensForCurrentUser();

    void invalidateToken(AccessToken token);

    void invalidateAllTokens(User user);
}
