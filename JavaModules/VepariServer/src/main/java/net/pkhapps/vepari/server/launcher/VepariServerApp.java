package net.pkhapps.vepari.server.launcher;

import net.pkhapps.vepari.server.adapter.rest.RestConfiguration;
import net.pkhapps.vepari.server.adapter.ws.WebSocketConfiguration;
import net.pkhapps.vepari.server.application.ApplicationServiceConfiguration;
import net.pkhapps.vepari.server.domain.DomainModelConfiguration;
import net.pkhapps.vepari.server.security.SecurityConfiguration;
import net.pkhapps.vepari.server.util.AsyncConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * Main entry point into the application.
 */
@SpringBootApplication
@EnableConfigurationProperties
// We have the application split up into separate configurations. It requires a little more work than just relying
// on auto-configuration for everything, but in return allows us to configure what parts of the application we want
// to integration test, for example.
@Import({AsyncConfiguration.class,
        RestConfiguration.class,
        WebSocketConfiguration.class,
        SecurityConfiguration.class,
        DomainModelConfiguration.class,
        ApplicationServiceConfiguration.class})
@SuppressWarnings("SpellCheckingInspection")
public class VepariServerApp {

    public static void main(String[] args) {
        SpringApplication.run(VepariServerApp.class, args);
    }
}
