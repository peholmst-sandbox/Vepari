package net.pkhapps.vepari.server.util;

import org.springframework.lang.NonNull;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * Utility class for getting static access to the {@link Clock} that is being used in the system. This makes it possible
 * to change the clock instance for example during testing.
 */
@SuppressWarnings("WeakerAccess")
public final class ClockHolder {

    private static Clock CLOCK = Clock.systemUTC();

    private ClockHolder() {
    }

    /**
     * Sets the clock singleton instance to use. By default, {@link Clock#systemUTC()} is used. You should normally
     * only need to change this during testing.
     */
    public static void setClock(@NonNull Clock clock) {
        CLOCK = Objects.requireNonNull(clock, "clock must not be null");
    }

    /**
     * Returns the clock instance.
     */
    @NonNull
    public static Clock clock() {
        return CLOCK;
    }

    /**
     * Returns the current instant retrieved from the clock.
     */
    @NonNull
    public static Instant now() {
        return clock().instant();
    }
}
