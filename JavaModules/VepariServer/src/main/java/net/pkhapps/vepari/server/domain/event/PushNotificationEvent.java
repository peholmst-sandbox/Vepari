package net.pkhapps.vepari.server.domain.event;

import org.springframework.lang.NonNull;

/**
 * Interface for application events that should result in a push notification being broadcast to all connected clients.
 */
public interface PushNotificationEvent {

    /**
     * The message string that will be broadcast to the clients. As a rule of thumb, the push notification should only
     * notify the client that something has happened and give an ID or similar that the client can use to retrieve
     * more detailed information about the event.
     */
    @NonNull
    String toMessageString();
}
