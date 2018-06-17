package net.pkhapps.vepari.server.security;

import net.pkhapps.vepari.server.util.ClockHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Entity representing a user account that also implements the {@link UserDetails} interface. Most important
 * features:
 * <ul>
 * <li>Authorities are specified by adding {@link Role}s to the user.</li>
 * <li>User accounts can be enabled or disabled.</li>
 * <li>User accounts can be locked for a short period of time.</li>
 * <li>User accounts have finite validity periods, i.e. you have to specify when the account becomes valid and when the validity ends.</li>
 * <li>User accounts maintain a limited password history and recently used passwords cannot be reused.</li>
 * </ul>
 */
@Entity
@Table(name = "_users")
@SuppressWarnings("UnusedReturnValue") // Method-chaining API
public class User extends SecurityEntity<User> implements UserDetails, CredentialsContainer {

    /**
     * The number of passwords that are kept in the history of used passwords. A user's password
     * cannot be changed to a password that is found in the password history.
     */
    public static final int PASSWORD_HISTORY_LENGTH = 10;
    /**
     * The default validity period of newly created accounts (1 year). The period can be adjusted by calling
     * {@link #setValidFrom(Instant)} and/or {@link #setValidTo(Instant)}.
     */
    public static final Period DEFAULT_ACCOUNT_VALIDITY_PERIOD = Period.ofYears(1);
    /**
     * The duration of an account lock (15 minutes). This duration can currently not be adjusted.
     */
    public static final Duration LOCK_DURATION = Duration.ofMinutes(15);
    /**
     * The number of failed login attempts that will lock the account.
     */
    public static final int MAX_FAILED_LOGIN_ATTEMPTS = 3;

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Column(nullable = false, unique = true, name = "username")
    private String username;

    @Column(name = "encoded_password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "_user_password_history") // TODO ordering
    private List<String> passwordHistory = new ArrayList<>();

    @Column(name = "locked")
    private Instant locked;

    @Column(nullable = false, name = "enabled")
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "_user_roles")
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false, name = "valid_from_date")
    private Instant validFrom;

    @Column(nullable = false, name = "valid_to_date")
    private Instant validTo;

    @Column(nullable = false, name = "failed_login_attempts")
    private int failedLoginAttempts;

    /**
     * Used by JPA only.
     */
    @SuppressWarnings("unused")
    private User() {
    }

    private User(@NonNull User original) {
        super(original);
        username = original.username;
        password = original.password;
        passwordHistory = new ArrayList<>(original.passwordHistory);
        locked = original.locked;
        enabled = original.enabled;
        roles = new HashSet<>(original.roles);
        validFrom = original.validFrom;
        validTo = original.validTo;
        failedLoginAttempts = original.failedLoginAttempts;
    }

    /**
     * Creates a new user account. The account is enabled, valid and unlocked but lacks a password.
     * Use {@link #changePassword(String, PasswordEncoder)} to set an initial password.
     *
     * @param username the username.
     */
    User(@NonNull String username) {
        this.username = Objects.requireNonNull(username, "username must not be null");
        setValidFrom(ClockHolder.now());
        registerEvent(new UserCreatedEvent(this));
    }

    /**
     * Changes the password of the user account.
     *
     * @param password the new password (in clear text).
     * @param encoder  the password encoder to use to encrypt the password.
     * @throws PasswordUsedBeforeException if the password has been used before.
     */
    @NonNull
    public User changePassword(@NonNull String password, @NonNull PasswordEncoder encoder) throws PasswordUsedBeforeException {
        Objects.requireNonNull(password, "password must not be null");
        Objects.requireNonNull(encoder, "encoder must not be null");
        if (isPasswordUsedBefore(password, encoder)) {
            throw new PasswordUsedBeforeException();
        }
        if (passwordHistory.size() >= PASSWORD_HISTORY_LENGTH) {
            passwordHistory.remove(0);
        }
        this.password = encoder.encode(password);
        passwordHistory.add(this.password);
        // TODO Set expiration date for the new password
        return this;
    }

    boolean isPasswordUsedBefore(@NonNull String password, @NonNull PasswordEncoder encoder) {
        return passwordHistory.stream().anyMatch(oldPassword -> encoder.matches(password, oldPassword));
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return roles.stream().flatMap(role -> role.getAuthorities().stream()).collect(Collectors.toSet());
    }

    /**
     * Checks if this user has the given authority.
     */
    public boolean hasAuthority(@NonNull GrantedAuthority authority) {
        Objects.requireNonNull(authority, "authority must not be null");
        return roles.stream().flatMap(role -> role.getAuthorities().stream()).anyMatch(auth -> Objects.equals(auth, authority));
    }

    /**
     * Checks if this user has the given authority.
     */
    public boolean hasAuthority(@NonNull String authority) {
        Objects.requireNonNull(authority, "authority must not be null");
        return hasAuthority(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        var now = ClockHolder.now();
        return validFrom.compareTo(now) <= 0 && validTo.isAfter(now);
    }

    @Override
    public boolean isAccountNonLocked() {
        var now = ClockHolder.now();
        return locked == null || !locked.plus(LOCK_DURATION).isAfter(now);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return password != null;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns the instant at which the user account becomes valid.
     */
    @NonNull
    public Instant getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the instant at which the user account becomes valid. If this instant is after
     * {@link #getValidTo() validTo}, {@code validTo} is automatically adjusted to
     * {@link #DEFAULT_ACCOUNT_VALIDITY_PERIOD} after this instant.
     */
    @NonNull
    public User setValidFrom(@NonNull Instant validFrom) {
        this.validFrom = Objects.requireNonNull(validFrom, "validFrom must not be null");
        if (validTo == null || validFrom.isAfter(validTo)) {
            validTo = ZonedDateTime.ofInstant(validFrom, ZoneId.systemDefault())
                    .plus(DEFAULT_ACCOUNT_VALIDITY_PERIOD)
                    .toInstant();
        }
        return this;
    }

    /**
     * Returns the instant at which the user account is no longer valid.
     */
    @NonNull
    public Instant getValidTo() {
        return validTo;
    }

    /**
     * Sets the instant at which the user account is no longer valid. If this instant is
     * before {@link #getValidFrom() validFrom}, {@code validFrom} is automatically adjusted to
     * {@link #DEFAULT_ACCOUNT_VALIDITY_PERIOD} before this instant.
     */
    @NonNull
    public User setValidTo(@NonNull Instant validTo) {
        this.validTo = Objects.requireNonNull(validTo, "validTo must not be null");
        if (validFrom == null || validFrom.isAfter(validTo)) {
            validFrom = ZonedDateTime.ofInstant(validTo, ZoneId.systemDefault())
                    .minus(DEFAULT_ACCOUNT_VALIDITY_PERIOD)
                    .toInstant();
        }
        return this;
    }

    /**
     * Enables the account.
     *
     * @see #disable()
     */
    @NonNull
    public User enable() {
        if (!enabled) {
            enabled = true;
            registerEvent(new UserEnabledEvent(this));
            LOGGER.trace("Enabled user {}", username);
        }
        return this;
    }

    /**
     * Disables the account.
     *
     * @see #enable()
     */
    @NonNull
    public User disable() {
        if (enabled) {
            enabled = false;
            registerEvent(new UserDisabledEvent(this));
            LOGGER.trace("Disabled user {}", username);
        }
        return this;
    }

    /**
     * Locks the account for {@link #LOCK_DURATION}.
     *
     * @see #unlock()
     */
    @NonNull
    public User lock() {
        locked = ClockHolder.now();
        registerEvent(new UserLockedEvent(this));
        LOGGER.trace("Locked user {}", username);
        return this;
    }

    /**
     * Unlocks the account.
     *
     * @see #lock()
     */
    @NonNull
    public User unlock() {
        locked = null;
        LOGGER.trace("Unlocked user {}", username);
        return this;
    }

    /**
     * Adds the given role to this user. If the role has already been added, nothing happens.
     */
    @NonNull
    public User addRole(@NonNull Role role) {
        Objects.requireNonNull(role, "role must not be null");
        if (roles.add(role)) {
            registerEvent(new UserRoleAddedEvent(this, role));
            LOGGER.trace("Added role {} to user {}", role.getName(), username);
        }
        return this;
    }

    /**
     * Removes the given role from this user. If the user did not hold the role, nothing happens.
     */
    @NonNull
    public User removeRole(@NonNull Role role) {
        Objects.requireNonNull(role, "role must not be null");
        if (roles.remove(role)) {
            registerEvent(new UserRoleRemovedEvent(this, role));
            LOGGER.trace("Removed role {} from user {}", role.getName(), username);
        }
        return this;
    }

    /**
     * Called by the authentication provider when somebody has tried to authenticate as this user but failed.
     */
    User notifyOfFailedLogin() {
        failedLoginAttempts++;
        LOGGER.trace("Login failed for user {}, number of failed attempts is {}", username, failedLoginAttempts);
        if (failedLoginAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            lock();
        }
        return this;
    }

    /**
     * Called by the authentication provider when somebody has successfully authenticated as this user.
     */
    User notifyOfSuccessfulLogin() {
        failedLoginAttempts = 0;
        LOGGER.trace("Login succeeded for user {}", username);
        return this;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    @Override
    @NonNull
    public User copy() {
        return new User(this);
    }

    @Override
    public void eraseCredentials() {
        password = null;
        passwordHistory.clear();
    }

    /**
     * Exception thrown when a user tries to re-use a password.
     */
    public static class PasswordUsedBeforeException extends Exception {
    }

    /**
     * Event fired when a {@link User} is locked.
     */
    public static final class UserLockedEvent extends SecurityEntityEvent<User> {

        UserLockedEvent(@NonNull User user) {
            super(user);
        }
    }

    /**
     * Event fired when a {@link User} is disabled.
     */
    public static final class UserDisabledEvent extends SecurityEntityEvent<User> {

        UserDisabledEvent(@NonNull User user) {
            super(user);
        }
    }

    /**
     * Event fired when a {@link User} is enabled.
     */
    public static final class UserEnabledEvent extends SecurityEntityEvent<User> {

        UserEnabledEvent(@NonNull User user) {
            super(user);
        }
    }

    /**
     * Event fired when a {@link User} is created.
     */
    public static final class UserCreatedEvent extends SecurityEntityEvent<User> {

        UserCreatedEvent(@NonNull User user) {
            super(user);
        }
    }

    /**
     * Base class for {@link User user events} that concern a specific {@link Role}.
     */
    @SuppressWarnings("unused")
    static abstract class UserRoleEvent extends SecurityEntityEvent<User> {

        private final Role role;

        UserRoleEvent(@NonNull User user, @NonNull Role role) {
            super(user);
            this.role = Objects.requireNonNull(role, "role must not be null");
        }

        /**
         * Returns the role that this event concerns.
         */
        @NonNull
        public Role getRole() {
            return role;
        }
    }

    /**
     * Event fired when a {@link Role} is added to a {@link User}.
     */
    public static final class UserRoleAddedEvent extends UserRoleEvent {

        UserRoleAddedEvent(@NonNull User user, @NonNull Role role) {
            super(user, role);
        }
    }

    /**
     * Event fired when a {@link Role} is removed from a {@link User}.
     */
    public static final class UserRoleRemovedEvent extends UserRoleEvent {

        UserRoleRemovedEvent(@NonNull User user, @NonNull Role role) {
            super(user, role);
        }
    }
}
