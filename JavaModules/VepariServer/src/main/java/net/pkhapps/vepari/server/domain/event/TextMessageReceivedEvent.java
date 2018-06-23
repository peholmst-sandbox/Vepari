package net.pkhapps.vepari.server.domain.event;

import net.pkhapps.vepari.server.domain.TextMessage;
import net.pkhapps.vepari.server.domain.base.DomainEvent;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Domain event published when a new {@link TextMessage} has been received.
 */
public class TextMessageReceivedEvent extends DomainEvent {

    private final TextMessage textMessage;

    /**
     * Creates a new {@code TextMessageReceivedEvent}.
     */
    public TextMessageReceivedEvent(@NonNull TextMessage textMessage) {
        this.textMessage = Objects.requireNonNull(textMessage, "textMessage must not be null");
    }

    /**
     * Returns the text message that was received.
     */
    @NonNull
    public TextMessage getTextMessage() {
        return textMessage;
    }
}