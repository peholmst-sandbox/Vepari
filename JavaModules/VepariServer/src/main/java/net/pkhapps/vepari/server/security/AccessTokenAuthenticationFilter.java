package net.pkhapps.vepari.server.security;

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
 * authentication method. The bearer token is assumed to be an {@link AccessToken#getToken() access token}
 * and a {@link AccessTokenAuthentication} request is created and passed to the {@link AuthenticationManager}.
 * If authentication is successful, the filter populates the current
 * {@link org.springframework.security.core.context.SecurityContext} and passes the request down the chain. If
 * the authentication fails, an error is immediately returned to the client.
 * <p>
 * If the request does not have a supported {@code Authorization} header, the filter will pass the request down
 * the chain without further action.
 */
final class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    AccessTokenAuthenticationFilter(@NonNull AuthenticationManager authenticationManager) {
        this.authenticationManager = Objects.requireNonNull(authenticationManager, "authenticationManager must not be null");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
        } else {
            var token = header.substring(7);
            var authenticationRequest = new AccessTokenAuthentication(token);
            try {
                var successfulAuthentication = authenticationManager.authenticate(authenticationRequest);
                SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
                filterChain.doFilter(request, response);
            } catch (AuthenticationException ex) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                // TODO Include error details as JSON
            }
        }
    }
}
