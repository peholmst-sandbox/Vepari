package net.pkhapps.vepari.server.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository of {@link User}s.
 */
interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds the user with the given username.
     */
    @NonNull
    Optional<User> findByUsername(@NonNull String username);

    /**
     * Checks if a user with the given username exists.
     */
    boolean existsByUsername(@NonNull String username);
}
