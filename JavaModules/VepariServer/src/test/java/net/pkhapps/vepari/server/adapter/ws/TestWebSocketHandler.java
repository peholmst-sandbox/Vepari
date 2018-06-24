package net.pkhapps.vepari.server.adapter.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.fail;

/**
 * Simple {@link WebSocketHandler} that can be used for testing web socket connections.
 *
 * @see #expectMessageOfType(Class, Consumer)
 * @see #getReceivedMessages()
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class TestWebSocketHandler implements WebSocketHandler {

    private static final int DEFAULT_TIMEOUT_MS = 1000;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<WebSocketMessage<?>> receivedMessages = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection established for session {}", session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("Received message {} inside session {}", message, session);
        synchronized (this) {
            receivedMessages.add(message);
            notify();
        }
    }

    /**
     * Returns all messages that have been received by the handler, starting with the oldest one.
     */
    @NonNull
    public List<WebSocketMessage<?>> getReceivedMessages() {
        synchronized (this) {
            return List.copyOf(receivedMessages);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.info("A transport error {} occurred inside session {}", exception, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("Connection closed for session {} ({})", session, closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * Expects the next message to arrive to be of the specified type and passes it on to the given consumer for
     * additional analysis.
     *
     * @param webSocketMessageType the type of message that should be received next.
     * @param consumer             the consumer that should analyze the received message.
     * @throws Exception if no message arrives in time or it is of the wrong type.
     */
    public <T extends WebSocketMessage<?>> void expectMessageOfType(@NonNull Class<T> webSocketMessageType,
                                                                    @NonNull Consumer<T> consumer) throws Exception {
        WebSocketMessage<?> lastReceivedMessage = null;
        synchronized (this) {
            // There is a chance that the message has been received already at this time. Therefore, the timeout
            // should be short enough.
            wait(DEFAULT_TIMEOUT_MS);
            if (receivedMessages.size() > 0) {
                lastReceivedMessage = receivedMessages.get(receivedMessages.size() - 1);
            }
        }
        if (lastReceivedMessage == null) {
            fail("No received messages at all");
        }
        if (!webSocketMessageType.isInstance(lastReceivedMessage)) {
            fail("Last received message was not of type " + webSocketMessageType);
        }
        consumer.accept(webSocketMessageType.cast(lastReceivedMessage));
    }
}
