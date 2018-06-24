package net.pkhapps.vepari.server.application;

import net.pkhapps.vepari.server.domain.TextMessage;
import net.pkhapps.vepari.server.domain.event.TextMessageReceivedEvent;
import net.pkhapps.vepari.server.security.Permissions;

import javax.validation.constraints.NotNull;

/**
 * Application service for receiving text messages from some adapter. The {@link Permissions#RECEIVE_SMS} permission is
 * required for the service to accept the incoming SMS.
 */
public interface TextMessageService {

    /**
     * Processes the given text message and publishes a {@link TextMessageReceivedEvent} to the application event bus.
     */
    void receiveTextMessage(@NotNull TextMessage textMessage);
}
