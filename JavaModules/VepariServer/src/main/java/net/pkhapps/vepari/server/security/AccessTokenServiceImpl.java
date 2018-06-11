package net.pkhapps.vepari.server.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
class AccessTokenServiceImpl implements AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;

    AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public AccessToken generateTokenForCurrentUser() {
        return null;
    }

    @Override
    public void invalidateCurrentToken() {

    }

    @Override
    public void invalidateAllTokensForCurrentUser() {

    }

    @Override
    @Secured(Permissions.INVALIDATE_ACCESS_TOKEN)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void invalidateToken(@NotNull AccessToken token) {
        
    }

    @Override
    @Secured(Permissions.INVALIDATE_ACCESS_TOKEN)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void invalidateAllTokens(User user) {

    }
}
