package net.pkhapps.vepari.server.security;

import java.util.Optional;

public interface AccessTokenService {

    Optional<AccessToken> findToken(String token);
}
