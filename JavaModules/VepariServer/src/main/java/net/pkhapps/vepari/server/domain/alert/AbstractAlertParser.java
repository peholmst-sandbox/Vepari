package net.pkhapps.vepari.server.domain.alert;

import net.pkhapps.vepari.server.domain.Alert;
import net.pkhapps.vepari.server.domain.TextMessage;
import net.pkhapps.vepari.server.domain.event.AlertReceivedEvent;
import net.pkhapps.vepari.server.domain.event.TextMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Base class for alert parsers that react to incoming {@link TextMessage}s, check if they are alert messages and if so,
 * publishes them as {@link AlertReceivedEvent}s.
 */
abstract class AbstractAlertParser {

    final Logger logger = LoggerFactory.getLogger(getClass());
    private final ApplicationEventPublisher applicationEventPublisher;

    AbstractAlertParser(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Attempts to parse the given {@link TextMessage} into an {@link Alert}. If the
     * message is not an alert, an empty {@code Optional} is returned. This method must never throw any exceptions.
     */
    @NonNull
    abstract Optional<Alert> parseAlert(@NonNull TextMessage textMessage);

    @EventListener
    public void onTextMessageReceivedEvent(@NonNull TextMessageReceivedEvent textMessageReceivedEvent) {
        // TODO Add sender ID/number whitelist check (to prevent anybody from sending alerts)
        parseAlert(textMessageReceivedEvent.getTextMessage()).ifPresent(this::publishAlert);
    }

    private void publishAlert(@NonNull Alert alert) {
        logger.info("Publishing alert {}", alert);
        applicationEventPublisher.publishEvent(new AlertReceivedEvent(alert));
    }
}
