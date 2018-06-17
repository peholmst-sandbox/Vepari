package net.pkhapps.vepari.server.security;

import net.pkhapps.vepari.server.util.ClockHolder;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

/**
 * Base class for domain events fired by subclasses of {@link SecurityEntity}.
 */
@SuppressWarnings("unused")
abstract class SecurityEntityEvent<SE extends SecurityEntity<SE>> {

    private final SE sender;
    private final Instant timestamp;

    SecurityEntityEvent(@NonNull SE sender) {
        this.sender = Objects.requireNonNull(sender, "sender must not be null");
        this.timestamp = ClockHolder.now();
    }

    /**
     * Returns the entity that fired the event.
     */
    @NonNull
    public SE getSender() {
        return sender;
    }

    /**
     * Returns the instant when the event was created (not necessarily when the event was fired).
     */
    @NonNull
    public Instant getTimestamp() {
        return timestamp;
    }
}
