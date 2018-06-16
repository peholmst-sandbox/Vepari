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

    /**
     * Deletes all {@link AccessToken}s of the given user.
     */
    void deleteByUser(@NonNull User user);

    /**
     * Deletes the {@link AccessToken} with the given string token.
     */
    void deleteByToken(@NonNull String token);
}
