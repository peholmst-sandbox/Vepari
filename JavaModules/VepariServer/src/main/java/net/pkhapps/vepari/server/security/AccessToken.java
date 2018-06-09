package net.pkhapps.vepari.server.security;

import net.pkhapps.vepari.server.common.ClockHolder;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
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
 * TODO Document me
 */
@Entity
@Table(name = "_access_tokens")
public class AccessToken extends SecurityEntity<AccessToken> {

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
    AccessToken() {

    }

    /**
     *
     * @param user
     * @param secureRandom
     * @param validityDuration
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

    @NonNull
    public String getToken() {
        return token;
    }

    @NonNull
    public Instant getIssueDate() {
        return issueDate;
    }

    @NonNull
    public Instant getExpirationDate() {
        return expirationDate;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    /**
     *
     * @throws AuthenticationException
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
}
