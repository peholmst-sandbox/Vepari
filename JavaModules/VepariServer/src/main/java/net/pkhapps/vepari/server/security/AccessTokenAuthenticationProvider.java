package net.pkhapps.vepari.server.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.Clock;

/**
 * TODO Document me!
 */
class AccessTokenAuthenticationProvider implements AuthenticationProvider {

    private final AccessTokenService accessTokenService;
    private final Clock clock;

    AccessTokenAuthenticationProvider(AccessTokenService accessTokenService, Clock clock) {
        this.accessTokenService = accessTokenService;
        this.clock = clock;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof AccessTokenAuthentication) {
            var token = ((AccessTokenAuthentication) authentication).getToken()
                    .flatMap(accessTokenService::findToken)
                    .orElseThrow(() -> new BadCredentialsException("Access token does not exist"));

        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AccessTokenAuthentication.class);
    }
}
