package net.pkhapps.vepari.server.security;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link Role}s.
 */
interface RoleRepository extends JpaRepository<Role, Long> {
}
