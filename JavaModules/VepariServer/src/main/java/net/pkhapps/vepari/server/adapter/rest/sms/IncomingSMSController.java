package net.pkhapps.vepari.server.adapter.rest.sms;

import net.pkhapps.vepari.server.application.TextMessageService;
import net.pkhapps.vepari.server.domain.TextMessage;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * REST controller for receiving SMS:es from a GSM gateway and forwarding them to {@link TextMessageService}.
 */
@RestController
@Validated
@RequestMapping(path = IncomingSMSController.PATH)
class IncomingSMSController {

    static final String PATH = "/sms/1.0";
    private final TextMessageService textMessageService;

    IncomingSMSController(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void receiveSMS(@RequestBody @NotNull @Valid TextMessageDTO dto) {
        textMessageService.receiveTextMessage(new TextMessage(dto.sender, dto.timestamp, dto.body));
    }
}
