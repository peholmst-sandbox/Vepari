package net.pkhapps.vepari.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main entry point into the application.
 */
@SpringBootApplication
@EnableConfigurationProperties
@SuppressWarnings("SpellCheckingInspection")
public class VepariServerApp {

    public static void main(String[] args) {
        SpringApplication.run(VepariServerApp.class, args);
    }
}
