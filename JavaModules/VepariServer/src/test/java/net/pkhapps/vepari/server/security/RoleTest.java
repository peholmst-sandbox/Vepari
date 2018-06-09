package net.pkhapps.vepari.server.security;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Role}.
 */
public class RoleTest {

    // TODO Add tests for the domain events

    @Test
    public void defaultStateAfterConstruction() {
        var role = new Role("My Role");
        assertThat(role.getName()).isEqualTo("My Role");
        assertThat(role.getAuthorities()).isEmpty();
    }

    @Test
    public void rename() {
        var role = new Role("My Role");
        role.rename("My New Role");
        assertThat(role.getName()).isEqualTo("My New Role");
    }

    @Test
    public void addAuthority() {
        var role = new Role("My Role");
        role.addAuthority(new SimpleGrantedAuthority("AUTH"));
        assertThat(role.getAuthorities()).containsOnly(new SimpleGrantedAuthority("AUTH"));
    }

    @Test
    public void removeAuthority() {
        var role = new Role("My Role");
        role.addAuthority(new SimpleGrantedAuthority("AUTH"));
        role.removeAuthority(new SimpleGrantedAuthority("AUTH"));
        assertThat(role.getAuthorities()).isEmpty();
    }
}
