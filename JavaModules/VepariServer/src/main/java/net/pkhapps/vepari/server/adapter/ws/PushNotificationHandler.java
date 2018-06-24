package net.pkhapps.vepari.server.adapter.ws;

import net.pkhapps.vepari.server.domain.event.PushNotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket handler that listens for {@link PushNotificationEvent}s and broadcasts them to all currently open
 * WebSocket sessions. This handler does not respond to any messages received by the clients.
 */
@Controller
class PushNotificationHandler extends AbstractWebSocketHandler {

    static final String PATH = "/notifications/1.0";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Set<WebSocketSession> activeSessions = ConcurrentHashMap.newKeySet();
    private final TaskExecutor taskExecutor;

    PushNotificationHandler(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.debug("Principal {} established new connection with session ID {}", session.getPrincipal(), session.getId());
        activeSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        activeSessions.remove(session);
        logger.debug("Connection was closed for session with ID {}", session.getId());
    }

    @EventListener
    public void onPushNotificationEvent(@NonNull PushNotificationEvent pushNotificationEvent) {
        logger.info("Pushing notification {} to all active sessions", pushNotificationEvent);
        activeSessions.forEach(session -> pushNotification(session, pushNotificationEvent));
    }

    private void pushNotification(@NonNull WebSocketSession session,
                                  @NonNull PushNotificationEvent pushNotificationEvent) {
        taskExecutor.execute(() -> {
            logger.debug("Pushing notification {} to session with ID {}", pushNotificationEvent, session.getId());
            try {
                session.sendMessage(new TextMessage(pushNotificationEvent.toMessageString()));
            } catch (IOException ex) {
                logger.warn("Could not push notification {} to session with ID {}", pushNotificationEvent,
                        session.getId());
                logger.debug("Exception thrown was:", ex);
            }
        });
    }
}
