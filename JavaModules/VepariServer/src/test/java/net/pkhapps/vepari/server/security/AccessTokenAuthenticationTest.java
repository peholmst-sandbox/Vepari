package net.pkhapps.vepari.server.security;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link AccessTokenAuthentication}.
 */
public class AccessTokenAuthenticationTest {

    private static final GrantedAuthority AUTHORITY = new SimpleGrantedAuthority("MY_AUTHORITY");

    @Test
    public void authenticationRequest_notAuthenticated() {
        final var token = "myToken";
        var auth = new AccessTokenAuthentication(token);
        assertThat(auth.isAuthenticated()).isFalse();
        assertThat(auth.getAuthorities()).isEmpty();
        assertThat(auth.getPrincipal()).isNull();
        assertThat(auth.getCredentials()).isEqualTo(token);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void authenticationResult_authenticated() throws Exception {
        var user = createUser();
        var accessToken = createAccessToken(user);
        var auth = new AccessTokenAuthentication(accessToken);
        assertThat(auth.isAuthenticated()).isTrue();
        assertThat(auth.getAuthorities()).containsOnly(AUTHORITY);
        assertThat(auth.getPrincipal()).isNotSameAs(user).isEqualTo(user);
        assertThat(auth.getCredentials()).isEqualTo(accessToken.getToken());
    }

    @Test
    public void setAuthenticated_toFalse_accepted() throws Exception {
        var auth = new AccessTokenAuthentication(createAccessToken(createUser()));
        auth.setAuthenticated(false);
        assertThat(auth.isAuthenticated()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAuthenticated_toTrue_exceptionThrown() {
        var auth = new AccessTokenAuthentication("token");
        auth.setAuthenticated(true);
    }

    @Test
    public void eraseCredentials() throws Exception {
        var auth = new AccessTokenAuthentication(createAccessToken(createUser()));
        auth.eraseCredentials();
        assertThat(auth.getCredentials()).isNull();
        assertThat(((User) auth.getPrincipal()).getPassword()).isNull();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private User createUser() throws Exception {
        var role = new Role("myRole").addAuthority(AUTHORITY);
        return new User("joecool").addRole(role).setId(123L).changePassword("password", new BCryptPasswordEncoder());
    }

    private AccessToken createAccessToken(User user) {
        return new AccessToken(user, new SecureRandom(), Duration.ofHours(1));
    }

}
