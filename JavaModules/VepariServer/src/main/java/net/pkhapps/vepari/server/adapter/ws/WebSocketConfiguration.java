package net.pkhapps.vepari.server.adapter.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuration for WebSocket adapters.
 */
@Configuration
@EnableWebSocket
class WebSocketConfiguration implements WebSocketConfigurer {

    private final PushNotificationHandler pushNotificationHandler;

    WebSocketConfiguration(PushNotificationHandler pushNotificationHandler) {
        this.pushNotificationHandler = pushNotificationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(pushNotificationHandler, PushNotificationHandler.PATH);
    }
}
