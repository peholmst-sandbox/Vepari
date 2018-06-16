package net.pkhapps.vepari.server.security;

import net.pkhapps.vepari.server.common.ClockHolder;
import net.pkhapps.vepari.server.common.ClockHolderTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.security.SecureRandom;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link AccessToken}.
 */
public class AccessTokenTest {

    private static final Duration SECONDS_TO_LIVE = Duration.ofSeconds(10);
    private final SecureRandom secureRandom = new SecureRandom();

    @Before
    public void setUp() {
        ClockHolderTestUtils.initClockHolder();
    }

    @Test
    public void defaultStateAfterConstruction() {
        var user = createUser();
        var token = createToken(user);

        assertThat(token.getUser()).isEqualTo(user);
        assertThat(token.getIssueDate()).isEqualTo(ClockHolder.now());
        assertThat(token.getExpirationDate()).isEqualTo(ClockHolder.now().plus(SECONDS_TO_LIVE));
        assertThat(token.getToken()).hasSize(AccessToken.TOKEN_BYTE_LENGTH * 2);
    }

    @Test(expected = LockedException.class)
    public void validate_lockedAccount_exceptionThrown() {
        var user = createUser().lock();
        createToken(user).validate();
    }

    @Test(expected = AccountExpiredException.class)
    public void validate_expiredAccount_exceptionThrown() {
        var user = createUser().setValidTo(ClockHolder.now().minusSeconds(10));
        createToken(user).validate();
    }

    @Test(expected = DisabledException.class)
    public void validate_disabledAccount_exceptionThrown() {
        var user = createUser().disable();
        createToken(user).validate();
    }

    @Test(expected = CredentialsExpiredException.class)
    public void validate_issueDateInFuture_exceptionThrown() {
        var token = createToken(createUser().setValidFrom(ClockHolder.now().minusSeconds(20)));
        ClockHolderTestUtils.minus(Duration.ofSeconds(10));
        token.validate();
    }

    @Test(expected = CredentialsExpiredException.class)
    public void validate_expirationDateIsNow_exceptionThrown() {
        var token = createToken(createUser());
        ClockHolderTestUtils.plus(Duration.ofSeconds(10));
        token.validate();
    }

    @Test(expected = CredentialsExpiredException.class)
    public void validate_expirationDateIsInThePast_exceptionThrown() {
        var token = createToken(createUser());
        ClockHolderTestUtils.plus(Duration.ofSeconds(15));
        token.validate();
    }

    @Test
    public void validate_everythingOk() {
        var token = createToken(createUser());
        token.validate();
    }

    @Test
    public void copy_allFieldsAreEqual() {
        var token = createToken(createUser());
        var copy = token.copy();
        assertThat(copy).isEqualToComparingFieldByField(token);
    }

    @Test(expected = IllegalStateException.class)
    public void eraseCredentials_exceptionThrownWhenAccessingToken() {
        var token = createToken(createUser());
        token.eraseCredentials();
        token.getToken();
    }

    private User createUser() {
        return new User("joecool");
    }

    private AccessToken createToken(User user) {
        return new AccessToken(user, secureRandom, SECONDS_TO_LIVE);
    }
}
