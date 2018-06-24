package net.pkhapps.vepari.server.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration for the domain model.
 */
@Configuration
@EntityScan
@EnableJpaRepositories
@ComponentScan
public class DomainModelConfiguration {
}
