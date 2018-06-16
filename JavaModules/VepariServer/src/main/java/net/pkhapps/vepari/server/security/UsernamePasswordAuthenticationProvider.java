package net.pkhapps.vepari.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Authentication provider that accepts and issues {@link UsernamePasswordAuthenticationToken}s and uses the
 * {@link UserRepository} to look up {@link User}s. This provider also invokes the {@link User#notifyOfFailedLogin()}
 * and {@link User#notifyOfSuccessfulLogin()} and saves the user afterwards.
 */
final class UsernamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernamePasswordAuthenticationProvider.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UsernamePasswordAuthenticationProvider(UserRepository userRepository,
                                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        LOGGER.debug("Checking password of {}", userDetails.getUsername());
        try {
            if (authentication.getCredentials() != null && passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
                if (userDetails instanceof User) {
                    userRepository.saveAndFlush(((User) userDetails).notifyOfSuccessfulLogin());
                }
            } else {
                if (userDetails instanceof User) {
                    userRepository.saveAndFlush(((User) userDetails).notifyOfFailedLogin());
                }
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
            }
        } catch (OptimisticLockingFailureException ex) {
            LOGGER.warn("Could not authenticate user {} because of an optimistic locking error", userDetails.getUsername());
            throw new AuthenticationServiceException("Simultaneous authentication attempts - try again");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        LOGGER.debug("Retrieving user details of {}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")).copy();
    }
}
