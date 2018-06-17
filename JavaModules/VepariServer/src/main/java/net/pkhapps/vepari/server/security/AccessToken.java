package net.pkhapps.vepari.server.security;

import net.pkhapps.vepari.server.util.ClockHolder;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.crypto.codec.Hex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Entity representing an access token that can be used to authenticate a {@link User} without a username and password.
 * Clients should authenticate initially using a username and password (or some other means of security),
 * request an access token and then use the access token for all further security needs until the token expires.
 *
 * @see #validate()
 */
@Entity
@Table(name = "_access_tokens")
public class AccessToken extends SecurityEntity<AccessToken> implements CredentialsContainer {

    public static final int TOKEN_BYTE_LENGTH = 64;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant issueDate;

    @Column(nullable = false)
    private Instant expirationDate;

    @ManyToOne(optional = false)
    private User user;

    /**
     * Used by JPA only.
     */
    @SuppressWarnings("unused")
    private AccessToken() {
    }

    private AccessToken(@NonNull AccessToken original) {
        super(original);
        token = original.token;
        issueDate = original.issueDate;
        expirationDate = original.expirationDate;
        user = original.user;
    }

    /**
     * Creates a new access token with an {@link #getIssueDate() issue date} of the current date and time.
     *
     * @param user             the user that will be authenticated by the token.
     * @param secureRandom     the {@link SecureRandom} that will be used to generate the random {@link #getToken() string token}.
     * @param validityDuration the amount of time the token will be valid after it has been generated.
     */
    AccessToken(@NonNull User user, @NonNull SecureRandom secureRandom, @NonNull Duration validityDuration) {
        Objects.requireNonNull(secureRandom, "secureRandom must not be null");
        Objects.requireNonNull(validityDuration, "validityDuration must not be null");
        this.user = Objects.requireNonNull(user, "user must not be null");
        var tokenBytes = new byte[TOKEN_BYTE_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        token = new String(Hex.encode(tokenBytes));
        issueDate = ClockHolder.now();
        expirationDate = issueDate.plus(validityDuration);
    }

    /**
     * Returns the unique string token that identifies this {@link AccessToken}.
     */
    @NonNull
    public String getToken() {
        if (token == null) {
            throw new IllegalStateException("The credentials have been erased");
        }
        return token;
    }

    /**
     * Returns the date when this access token was issued.
     */
    @NonNull
    public Instant getIssueDate() {
        return issueDate;
    }

    /**
     * Returns the date when this access token expires.
     */
    @NonNull
    public Instant getExpirationDate() {
        return expirationDate;
    }

    /**
     * Returns the user that is authenticated by this access token.
     */
    @NonNull
    public User getUser() {
        return user;
    }

    /**
     * Validates this access token by checking that:
     * <ul>
     * <li>The user account is not locked</li>
     * <li>The user account is not expired</li>
     * <li>The user account is not disabled</li>
     * <li>The access token's issue date is <= the current date and time</li>
     * <li>The access token's expiration date is > the current date and time</li>
     * </ul>
     *
     * @throws AuthenticationException if the access token is not valid.
     */
    public void validate() throws AuthenticationException {
        if (!user.isAccountNonLocked()) {
            throw new LockedException("Account is locked");
        }
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("Account is expired");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }
        final Instant now = ClockHolder.now();
        if (issueDate.isAfter(now) || expirationDate.compareTo(now) <= 0) {
            throw new CredentialsExpiredException("Token is expired");
        }
    }

    @Override
    @NonNull
    public AccessToken copy() {
        return new AccessToken(this);
    }

    @Override
    public void eraseCredentials() {
        token = null;
    }
}
