package net.pkhapps.vepari.server.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Security configuration properties
 */
@Component
@ConfigurationProperties("app.security")
@SuppressWarnings({"unused", "SameParameterValue"})
class SecurityProperties {

    private AccessTokens accessTokens = new AccessTokens();

    public AccessTokens getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(AccessTokens accessTokens) {
        this.accessTokens = accessTokens;
    }

    static class AccessTokens {

        private Duration defaultDuration = Duration.ofDays(365);

        public Duration getDefaultDuration() {
            return defaultDuration;
        }

        public void setDefaultDuration(Duration defaultDuration) {
            this.defaultDuration = defaultDuration;
        }
    }
}
