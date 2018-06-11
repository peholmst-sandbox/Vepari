package net.pkhapps.vepari.server.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Objects;

/**
 * Authentication provider that accepts an unauthenticated {@link AccessTokenAuthentication} and returns an
 * authenticated {@link AccessTokenAuthentication} if the token was {@link AccessToken#validate() valid}.
 */
final class AccessTokenAuthenticationProvider implements AuthenticationProvider {

    private final AccessTokenRepository accessTokenRepository;

    AccessTokenAuthenticationProvider(@NonNull AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = Objects.requireNonNull(accessTokenRepository, "accessTokenRepository must not be null");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof AccessTokenAuthentication) {
            var token = accessTokenRepository.findByToken(((AccessTokenAuthentication) authentication).getToken())
                    .orElseThrow(() -> new BadCredentialsException("Access token does not exist"));
            token.validate();
            return new AccessTokenAuthentication(token);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AccessTokenAuthentication.class);
    }
}
