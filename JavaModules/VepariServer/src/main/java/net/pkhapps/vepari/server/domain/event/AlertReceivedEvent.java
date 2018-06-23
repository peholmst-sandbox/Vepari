package net.pkhapps.vepari.server.domain.event;

import net.pkhapps.vepari.server.domain.Alert;
import net.pkhapps.vepari.server.domain.base.DomainEvent;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Domain event published when a new {@link Alert} has been received.
 */
public class AlertReceivedEvent extends DomainEvent {

    private final Alert alert;

    /**
     * Creates a new {@code AlertReceivedEvent}.
     */
    public AlertReceivedEvent(@NonNull Alert alert) {
        this.alert = Objects.requireNonNull(alert, "alert must not be null");
    }

    /**
     * Returns the alert that was received.
     */
    @NonNull
    public Alert getAlert() {
        return alert;
    }
}
