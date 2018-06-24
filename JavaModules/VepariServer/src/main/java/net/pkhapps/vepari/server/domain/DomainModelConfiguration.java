package net.pkhapps.vepari.server.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the domain model.
 */
@Configuration
@EntityScan
@ComponentScan
public class DomainModelConfiguration {
}
