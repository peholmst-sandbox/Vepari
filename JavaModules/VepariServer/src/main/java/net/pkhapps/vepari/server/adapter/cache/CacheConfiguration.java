package net.pkhapps.vepari.server.adapter.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import net.pkhapps.vepari.server.domain.AlertProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * TODO document me
 */
@Configuration
@EnableCaching
@ComponentScan
public class CacheConfiguration {

    private final AlertProperties alertProperties;

    CacheConfiguration(AlertProperties alertProperties) {
        this.alertProperties = alertProperties;
    }

    @Bean
    Config hazelcastConfig() {
        // Add a few seconds to make sure the alert is not evicted before the records in the database are timed out.
        return new Config().addMapConfig(new MapConfig("alerts")
                .setTimeToLiveSeconds((int) alertProperties.getAlertTimeout().toSeconds() + 5));
    }
}
