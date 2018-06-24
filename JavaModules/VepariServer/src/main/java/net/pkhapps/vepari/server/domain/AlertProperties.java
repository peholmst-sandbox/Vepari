package net.pkhapps.vepari.server.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Alert configuration properties.
 */
@Component
@ConfigurationProperties("app.alerts")
@SuppressWarnings({"unused", "SameParameterValue"})
public class AlertProperties {

    private Duration alertTimeout = Duration.ofMinutes(5);

    /**
     * The duration for which an {@link Alert} remains in the cache and can be retrieved by clients.
     */
    public Duration getAlertTimeout() {
        return alertTimeout;
    }

    public void setAlertTimeout(Duration alertTimeout) {
        this.alertTimeout = alertTimeout;
    }
}
