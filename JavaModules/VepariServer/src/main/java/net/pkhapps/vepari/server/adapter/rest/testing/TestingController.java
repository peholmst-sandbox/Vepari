package net.pkhapps.vepari.server.adapter.rest.testing;

import net.pkhapps.vepari.server.Profiles;
import net.pkhapps.vepari.server.domain.event.PushNotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller used by integration tests to try out different parts of the application without going through
 * the entire stack. This controller is only available in the {@link Profiles#TESTING} profile.
 */
@RestController
@Profile(Profiles.TESTING)
@RequestMapping(path = TestingController.PATH)
public class TestingController {

    public static final String PATH = "/testing/1.0";
    public static final String PUSH_NOTIFICATION_PATH = "/push";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ApplicationEventPublisher applicationEventPublisher;

    TestingController(ApplicationEventPublisher applicationEventPublisher) {
        logger.warn("Setting up TestingController intended for integration testing only! You should not see this message in production!");
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping(path = PUSH_NOTIFICATION_PATH)
    public void sendPushNotification(@RequestBody String payload) {
        logger.info("Sending test push notification with payload \"{}\"", payload);
        applicationEventPublisher.publishEvent((PushNotificationEvent) () -> "TEST-EVENT=" + payload);
    }
}
