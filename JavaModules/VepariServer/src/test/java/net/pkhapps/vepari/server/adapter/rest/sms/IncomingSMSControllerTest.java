package net.pkhapps.vepari.server.adapter.rest.sms;

import net.pkhapps.vepari.server.application.TextMessageService;
import net.pkhapps.vepari.server.domain.TextMessage;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
        var service = mock(TextMessageService.class);
        var controller = new IncomingSMSController(service);

        TextMessageDTO dto = new TextMessageDTO();
        dto.sender = "sender";
        dto.timestamp = Instant.now();
        dto.body = "body";

        controller.receiveSMS(dto);

        var message = ArgumentCaptor.forClass(TextMessage.class);
        verify(service).receiveTextMessage(message.capture());

        assertThat(message.getValue()).isNotNull();

        var textMessage = message.getValue();

        assertThat(textMessage.getSender()).isEqualTo(dto.sender);
        assertThat(textMessage.getTimestamp()).isEqualTo(dto.timestamp);
        assertThat(textMessage.getBody()).isEqualTo(dto.body);
    }
}
