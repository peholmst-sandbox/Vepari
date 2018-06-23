package net.pkhapps.vepari.server.domain.alert;

import net.pkhapps.vepari.server.domain.Alert;
import net.pkhapps.vepari.server.domain.TextMessage;
import net.pkhapps.vepari.server.domain.event.AlertReceivedEvent;
import net.pkhapps.vepari.server.domain.event.TextMessageReceivedEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link AbstractAlertParser}.
 */
public class AbstractAlertParserTest {

    private ApplicationEventPublisher applicationEventPublisher;

    @Before
    public void setUp() {
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
    }

    @Test
    public void onTextMessageReceivedEvent_nonAlert_noEventPublished() {
        var parser = new AbstractAlertParser(applicationEventPublisher) {

            @Override
            Optional<Alert> parseAlert(TextMessage textMessage) {
                return Optional.empty();
            }
        };

        parser.onTextMessageReceivedEvent(new TextMessageReceivedEvent(
                new TextMessage("sender", Instant.now(), "body")));

        verify(applicationEventPublisher, Mockito.never()).publishEvent(Mockito.any());
    }

    @Test
    public void onTextMessageReceivedEvent_alert_eventPublished() {
        var alert = new Alert.Builder(Instant.now()).build();
        var parser = new AbstractAlertParser(applicationEventPublisher) {

            @Override
            Optional<Alert> parseAlert(TextMessage textMessage) {
                return Optional.of(alert);
            }
        };

        parser.onTextMessageReceivedEvent(new TextMessageReceivedEvent(
                new TextMessage("sender", Instant.now(), "body")));

        var event = ArgumentCaptor.forClass(AlertReceivedEvent.class);
        verify(applicationEventPublisher).publishEvent(event.capture());
        assertThat(event.getValue().getAlert()).isSameAs(alert);
    }
}
