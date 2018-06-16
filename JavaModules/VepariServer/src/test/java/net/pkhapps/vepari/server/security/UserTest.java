package net.pkhapps.vepari.server.security;

import net.pkhapps.vepari.server.common.ClockHolder;
import net.pkhapps.vepari.server.common.ClockHolderTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link User}.
 */
public class UserTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static SimpleGrantedAuthority authority(String name) {
        return new SimpleGrantedAuthority(name);
    }

    @Before
    public void setUp() {
        ClockHolderTestUtils.initClockHolder();
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void defaultStateAfterConstruction() {
        ClockHolder.setClock(Clock.fixed(ZonedDateTime.of(2018, 2, 3, 12, 15, 30, 0,
                ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));

        var user = createUser();
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isFalse(); // No password
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.getUsername()).isEqualTo("joecool");
        assertThat(user.getPassword()).isNull();
        assertThat(user.getValidFrom()).isEqualTo(ClockHolder.now());
        assertThat(user.getValidTo()).isEqualTo(ZonedDateTime.of(2019, 2, 3, 12, 15, 30, 0,
                ZoneId.systemDefault()).toInstant());
        assertThat(user.getFailedLoginAttempts()).isEqualTo(0);
    }

    @Test
    public void isAccountNonExpired_validFromInTheFuture_accountExpired() {
        var user = createUser();
        user.setValidFrom(ClockHolder.now().plusSeconds(10));
        assertThat(user.isAccountNonExpired()).isFalse();
    }

    @Test
    public void isAccountNonExpired_validFromIsInThePast_accountNotExpired() {
        var user = createUser();
        user.setValidFrom(ClockHolder.now().minusSeconds(10));
        assertThat(user.isAccountNonExpired()).isTrue();
    }

    @Test
    public void isAccountNonExpired_validToIsInThePast_accountExpired() {
        var user = createUser();
        user.setValidTo(ClockHolder.now().minusSeconds(10));
        assertThat(user.isAccountNonExpired()).isFalse();
    }

    @Test
    public void isAccountNonExpired_validToIsNow_accountExpired() {
        var user = createUser();
        user.setValidTo(ClockHolder.now());
        assertThat(user.isAccountNonExpired()).isFalse();
    }

    @Test
    public void setValidFrom_afterValidTo_validToIsAdjusted() {
        var user = createUser();
        user.setValidFrom(user.getValidTo().plusSeconds(10));
        assertThat(user.getValidTo()).isEqualTo(user.getValidFrom().atZone(ZoneId.systemDefault())
                .plusYears(1).toInstant());
    }

    @Test
    public void setValidFrom_beforeValidTo_validToIsLeftUntouched() {
        var user = createUser();
        var oldValidTo = user.getValidTo();
        user.setValidFrom(oldValidTo.minusSeconds(10));
        assertThat(user.getValidTo()).isEqualTo(oldValidTo);
    }

    @Test
    public void setValidTo_beforeValidFrom_validFromIsAdjusted() {
        var user = createUser();
        user.setValidTo(user.getValidFrom().minusSeconds(10));
        assertThat(user.getValidFrom()).isEqualTo(user.getValidTo().atZone(ZoneId.systemDefault())
                .minusYears(1).toInstant());
    }

    @Test
    public void setValidTo_afterValidFrom_validFromIsLeftUntouched() {
        var user = createUser();
        var oldValidFrom = user.getValidFrom();
        user.setValidTo(oldValidFrom.plusSeconds(10));
        assertThat(user.getValidFrom()).isEqualTo(oldValidFrom);
    }

    @Test
    public void lock_lockedFor15Minutes() {
        var user = createUser().lock();
        assertThat(user.isAccountNonLocked()).isFalse();

        ClockHolderTestUtils.plus(Duration.ofMinutes(15));
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void unlock() {
        var user = createUser().lock();
        assertThat(user.isAccountNonLocked()).isFalse();

        ClockHolderTestUtils.plus(Duration.ofMinutes(1));
        assertThat(user.isAccountNonLocked()).isFalse();
        user.unlock();
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void changePassword_passwordNotUsedBefore_accepted() throws Exception {
        var user = createUser().changePassword("myPassword", passwordEncoder);
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(passwordEncoder.matches("myPassword", user.getPassword())).isTrue();
    }

    @Test(expected = User.PasswordUsedBeforeException.class)
    public void changePassword_useExistingPassword_rejected() throws Exception {
        createUser()
                .changePassword("myPassword", passwordEncoder)
                .changePassword("myPassword", passwordEncoder);
    }

    @Test(expected = User.PasswordUsedBeforeException.class)
    public void changePassword_passwordUsedBefore_rejected() throws Exception {
        var user = createUser();
        user.changePassword("myPassword", passwordEncoder);
        user.changePassword("myOtherPassword", passwordEncoder);
        user.changePassword("myPassword", passwordEncoder);
    }

    @Test
    public void changePassword_passwordUsedWayBack_accepted() throws Exception {
        var user = createUser();
        for (int i = 0; i <= User.PASSWORD_HISTORY_LENGTH; ++i) {
            user.changePassword("password" + i, passwordEncoder);
        }
        user.changePassword("password0", passwordEncoder);
    }

    @Test
    public void enableAndDisable() {
        var user = createUser();
        user.disable();
        assertThat(user.isEnabled()).isFalse();
        user.enable();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void addRole_oneRole_authoritiesAreTakenFromRole() {
        var user = createUser();
        var role = new Role("role").addAuthority(authority("auth1")).addAuthority(authority("auth2"));
        user.addRole(role);

        assertThat(user.getAuthorities()).containsOnlyOnce(authority("auth1"))
                .containsOnlyOnce(authority("auth2"));
    }

    @Test
    public void addRole_twoRoles_authoritiesAreTakenFromBothRoles() {
        var role1 = new Role("role1").addAuthority(authority("auth1")).addAuthority(authority("auth2"));
        var role2 = new Role("role2").addAuthority(authority("auth1")).addAuthority(authority("auth3"));
        var user = createUser().addRole(role1).addRole(role2);

        assertThat(user.getAuthorities()).containsOnlyOnce(authority("auth1"))
                .containsOnlyOnce(authority("auth2"))
                .containsOnlyOnce(authority("auth3"));
    }

    @Test
    public void removeRole() {
        var role1 = new Role("role1").addAuthority(authority("auth1")).addAuthority(authority("auth2"));
        var role2 = new Role("role2").addAuthority(authority("auth1")).addAuthority(authority("auth3"));
        var user = createUser().addRole(role1).addRole(role2).removeRole(role1);

        assertThat(user.getAuthorities()).containsOnlyOnce(authority("auth1"))
                .containsOnlyOnce(authority("auth3"));
    }

    @Test
    public void copy_allFieldsAreEqual() {
        var role1 = new Role("role1").addAuthority(authority("auth1")).addAuthority(authority("auth2"));
        var role2 = new Role("role2").addAuthority(authority("auth1")).addAuthority(authority("auth3"));
        var user = createUser().addRole(role1).addRole(role2);
        var copy = user.copy();
        assertThat(copy).isEqualToIgnoringGivenFields(user, "domainEvents");
    }

    @Test
    public void notifyOfFailedLoginAttempts_lockedAfterMaxAttempts() {
        var user = createUser();
        assertThat(user.isAccountNonLocked()).isTrue();
        for (int i = 0; i < User.MAX_FAILED_LOGIN_ATTEMPTS; ++i) {
            user.notifyOfFailedLogin();
        }
        assertThat(user.isAccountNonLocked()).isFalse();
        assertThat(user.getFailedLoginAttempts()).isEqualTo(3);
    }

    @Test
    public void notifyOfSuccessfulLoginAttempt_failedAttemptsIsReset() {
        var user = createUser().notifyOfFailedLogin();
        assertThat(user.getFailedLoginAttempts()).isEqualTo(1);

        user.notifyOfSuccessfulLogin();
        assertThat(user.getFailedLoginAttempts()).isEqualTo(0);
    }

    @Test
    public void eraseCredentials() throws Exception {
        var user = createUser()
                .changePassword("password", passwordEncoder)
                .changePassword("password2", passwordEncoder);
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.isPasswordUsedBefore("password", passwordEncoder)).isTrue();

        user.eraseCredentials();

        assertThat(user.getPassword()).isNull();
        assertThat(user.isPasswordUsedBefore("password", passwordEncoder)).isFalse();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private User createUser() {
        return new User("joecool");
    }
}
