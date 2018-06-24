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
 * REST interface for the {@link TextMessageService}.
 */
@RestController
@Validated
@RequestMapping(path = TextMessageController.PATH)
class TextMessageController {

    static final String PATH = "/sms/1.0";
    private final TextMessageService textMessageService;

    TextMessageController(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void receiveSMS(@RequestBody @NotNull @Valid TextMessageDTO dto) {
        textMessageService.receiveTextMessage(new TextMessage(dto.sender, dto.timestamp, dto.body));
    }
}
