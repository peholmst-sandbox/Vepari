package net.pkhapps.vepari.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Filter that checks the incoming request for an {@code Authorization} header that uses the {@code Bearer}
 * security method. The bearer token is assumed to be an {@link AccessToken#getToken() access token}
 * and a {@link AccessTokenAuthentication} request is created and passed to the {@link AuthenticationManager}.
 * If security is successful, the filter populates the current
 * {@link org.springframework.security.core.context.SecurityContext} and passes the request down the chain. If
 * the security fails, an error is immediately returned to the client.
 * <p>
 * If the request does not have a supported {@code Authorization} header, the filter will pass the request down
 * the chain without further action.
 */
final class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenAuthenticationFilter.class);
    private final AuthenticationManager authenticationManager;

    AccessTokenAuthenticationFilter(@NonNull AuthenticationManager authenticationManager) {
        this.authenticationManager = Objects.requireNonNull(authenticationManager, "authenticationManager must not be null");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            LOGGER.trace("No bearer authorization header found, passing request down the filter chain");
            filterChain.doFilter(request, response);
        } else {
            LOGGER.trace("Processing bearer authentication header {}", header);
            var token = header.substring(7);
            var authenticationRequest = new AccessTokenAuthentication(token);
            try {
                var successfulAuthentication = authenticationManager.authenticate(authenticationRequest);
                LOGGER.trace("Bearer authentication was successful: {}", successfulAuthentication);
                SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
                filterChain.doFilter(request, response);
            } catch (AuthenticationException ex) {
                LOGGER.trace("Bearer authentication failed: {}", ex);
                SecurityContextHolder.clearContext();
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                // TODO Include error details as JSON
            }
        }
    }
}
