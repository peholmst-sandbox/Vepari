package net.pkhapps.vepari.server.adapter.rest.sms;

import net.pkhapps.vepari.server.domain.event.TextMessageReceivedEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link IncomingSMSController}.
 */
public class IncomingSMSControllerTest {

    @Test
    public void receiveSMS_eventIsPublished() {
        var publisher = mock(ApplicationEventPublisher.class);
        var controller = new IncomingSMSController(publisher);

        TextMessageDTO dto = new TextMessageDTO();
        dto.sender = "sender";
        dto.timestamp = Instant.now();
        dto.body = "body";

        controller.receiveSMS(dto);

        var event = ArgumentCaptor.forClass(TextMessageReceivedEvent.class);
        verify(publisher).publishEvent(event.capture());

        assertThat(event.getValue()).isNotNull();

        var textMessage = event.getValue().getTextMessage();

        assertThat(textMessage.getSender()).isEqualTo(dto.sender);
        assertThat(textMessage.getTimestamp()).isEqualTo(dto.timestamp);
        assertThat(textMessage.getBody()).isEqualTo(dto.body);
    }
}
