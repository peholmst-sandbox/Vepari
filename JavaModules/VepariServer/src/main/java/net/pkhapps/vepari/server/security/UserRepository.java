package net.pkhapps.vepari.server.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository of {@link User}s.
 */
interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds the user with the given username.
     */
    Optional<User> findByUsername(String username);
}
