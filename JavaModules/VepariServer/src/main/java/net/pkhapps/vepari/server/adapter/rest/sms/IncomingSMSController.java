package net.pkhapps.vepari.server.adapter.rest.sms;

import net.pkhapps.vepari.server.domain.TextMessage;
import net.pkhapps.vepari.server.domain.event.TextMessageReceivedEvent;
import net.pkhapps.vepari.server.security.Permissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * REST controller for receiving SMS:es from a GSM gateway. The {@link Permissions#RECEIVE_SMS} permission is required
 * for this REST controller to accept the incoming SMS. Incoming messages are converted to {@link TextMessage}s and
 * published to the application event publisher as {@link TextMessageReceivedEvent}s.
 */
@RestController
@Validated
@RequestMapping(path = IncomingSMSController.PATH)
class IncomingSMSController {

    static final String PATH = "/sms/1.0";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ApplicationEventPublisher applicationEventPublisher;

    IncomingSMSController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Secured(Permissions.RECEIVE_SMS)
    public void receiveSMS(@RequestBody @NotNull @Valid TextMessageDTO dto) {
        logger.info("Received text message from {}", dto.sender);
        logger.trace("Message body: \"{}\"", dto.body);

        var domainObject = new TextMessage(dto.sender, dto.timestamp, dto.body);
        applicationEventPublisher.publishEvent(new TextMessageReceivedEvent(domainObject));
    }
}
