package net.pkhapps.vepari.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Security utilities for writing integration tests.
 */
@Component
public class IntegrationTestSecurityUtils {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccessTokenService accessTokenService;

    private ThreadLocal<Role> role = new ThreadLocal<>();
    private ThreadLocal<User> user = new ThreadLocal<>();
    private ThreadLocal<AccessToken> accessToken = new ThreadLocal<>();

    /**
     * Generates a new role with the given permissions, a new user that holds the role and an access token for the
     * user, and binds them all to the current thread for future use.
     */
    public void generateAccessTokenForUserWithPermissions(String... permissions) {
        var role = new Role("TEST_ROLE_" + UUID.randomUUID());
        Stream.of(permissions).forEach(role::addAuthority);
        this.role.set(roleRepository.saveAndFlush(role));

        this.user.set(userRepository.saveAndFlush(new User("TEST_USER_" + UUID.randomUUID()).addRole(role)));

        this.accessToken.set(accessTokenService.generateToken(user.get()));
    }

    /**
     * Returns the user bound to the current thread.
     */
    public Optional<User> getUser() {
        return Optional.ofNullable(user.get());
    }

    /**
     * Returns the role bound to the current thread.
     */
    public Optional<Role> getRole() {
        return Optional.ofNullable(role.get());
    }

    /**
     * Returns the access token bound to the current thread.
     */
    public Optional<AccessToken> getAccessToken() {
        return Optional.ofNullable(accessToken.get());
    }

    /**
     * Adds the access token bound to the current thread to the given HTTP headers.
     */
    public void addAccessTokenToHeader(@NonNull HttpHeaders headers) {
        getAccessToken().map(accessToken -> "Bearer " + accessToken.getToken())
                .ifPresent(headerValue -> headers.add("Authorization", headerValue));
    }
}
