package net.pkhapps.vepari.server.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository of {@link AccessToken}s.
 */
interface AccessTokenRepository extends JpaRepository<AccessToken, String> {

    /**
     * Finds the {@link AccessToken} corresponding to the given string token.
     */
    @NonNull
    Optional<AccessToken> findByToken(@NonNull String token);
}
