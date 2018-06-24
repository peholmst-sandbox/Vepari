package net.pkhapps.vepari.server.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration for async support.
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean
    TaskExecutor taskExecutor() {
        // TODO Make this executor configurable
        return new ThreadPoolTaskExecutor();
    }
}
