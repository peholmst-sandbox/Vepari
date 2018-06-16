package net.pkhapps.vepari.server.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository of {@link Role}s.
 */
interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds the role with the given name.
     */
    @NonNull
    Optional<Role> findByName(@NonNull String name);

    /**
     * Checks if a role with the given name exists.
     */
    boolean existsByName(@NonNull String name);
}
