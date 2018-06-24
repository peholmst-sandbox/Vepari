package net.pkhapps.vepari.server.application;

import net.pkhapps.vepari.server.domain.TextMessage;
import net.pkhapps.vepari.server.domain.event.TextMessageReceivedEvent;
import net.pkhapps.vepari.server.security.Permissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Default implementation of {@link TextMessageService}.
 */
@Service
@Validated
class TextMessageServiceImpl implements TextMessageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ApplicationEventPublisher applicationEventPublisher;

    TextMessageServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Secured(Permissions.RECEIVE_SMS)
    public void receiveTextMessage(@NotNull TextMessage textMessage) {
        logger.info("Received text message from {}", textMessage.getSender());
        logger.trace("Message body: \"{}\"", textMessage.getBody());
        applicationEventPublisher.publishEvent(new TextMessageReceivedEvent(textMessage));
    }
}
