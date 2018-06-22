package net.pkhapps.vepari.server.domain.base;

import net.pkhapps.vepari.server.util.ClockHolder;
import org.springframework.lang.NonNull;

import java.time.Instant;

/**
 * Base class for domain events.
 */
public abstract class DomainEvent {

    private final Instant timestamp;

    protected DomainEvent() {
        timestamp = ClockHolder.now();
    }

    /**
     * Returns the instant at which the domain event was created.
     */
    @NonNull
    public Instant getTimestamp() {
        return timestamp;
    }
}
