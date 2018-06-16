package net.pkhapps.vepari.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Default implementation of {@link AccessTokenService}.
 */
@Service
@Validated
class AccessTokenServiceImpl implements AccessTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenServiceImpl.class);

    private final AccessTokenRepository accessTokenRepository;
    private final SecureRandom secureRandom;
    private final SecurityProperties securityProperties;

    AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository,
                           SecureRandom secureRandom,
                           SecurityProperties securityProperties) {
        this.accessTokenRepository = accessTokenRepository;
        this.secureRandom = secureRandom;
        this.securityProperties = securityProperties;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @NonNull
    public AccessToken generateToken(@NonNull User user) {
        Objects.requireNonNull(user, "user must not be null");
        var accessToken = new AccessToken(user, secureRandom, securityProperties.getAccessTokens().getDefaultDuration());
        LOGGER.trace("Generated token {} for user {}", accessToken.getToken(), user);
        return accessTokenRepository.save(accessToken);
    }

    @Override
    @Secured({Permissions.INVALIDATE_ACCESS_TOKEN, Permissions.RUN_AS_SYSTEM})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void invalidateToken(@NotNull String token) {
        Objects.requireNonNull(token, "token must not be null");
        LOGGER.trace("Invalidating token {}", token);
        accessTokenRepository.deleteByToken(token);
    }

    @Override
    @Secured({Permissions.INVALIDATE_ACCESS_TOKEN, Permissions.RUN_AS_SYSTEM})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void invalidateAllTokens(@NotNull User user) {
        Objects.requireNonNull(user, "user must not be null");
        LOGGER.trace("Invalidating all tokens of user {}", user);
        accessTokenRepository.deleteByUser(user);
    }
}
