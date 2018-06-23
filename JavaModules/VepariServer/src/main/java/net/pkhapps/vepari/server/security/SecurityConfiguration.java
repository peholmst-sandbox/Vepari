package net.pkhapps.vepari.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.security.SecureRandom;
import java.util.List;

/**
 * Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
@EnableJpaAuditing
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AccessTokenRepository accessTokenRepository;

    private final UserRepository userRepository;

    @Autowired
    SecurityConfiguration(AccessTokenRepository accessTokenRepository, UserRepository userRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.userRepository = userRepository;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecureRandom secureRandom() throws Exception {
        return SecureRandom.getInstanceStrong();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(new AccessTokenAuthenticationProvider(accessTokenRepository))
                .authenticationProvider(new UsernamePasswordAuthenticationProvider(userRepository, passwordEncoder()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .logout().disable()
                .rememberMe().disable()
                .anonymous().disable()
                .sessionManagement().disable()
                .addFilterBefore(new AccessTokenAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true)
    static class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

        @Override
        protected AccessDecisionManager accessDecisionManager() {
            var permissionVoter = new RoleVoter();
            permissionVoter.setRolePrefix(Permissions.PREFIX);
            // TODO Once we add support for RUN_AS, we need to change to another access decision manager
            return new UnanimousBased(List.of(new AuthenticatedVoter(), permissionVoter));
        }
    }
}
