package net.pkhapps.vepari.server.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Configuration for scheduling support.
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration {

    @Bean
    TaskScheduler taskScheduler() {
        // TODO Make this scheduler configurable
        return new ThreadPoolTaskScheduler();
    }
}
