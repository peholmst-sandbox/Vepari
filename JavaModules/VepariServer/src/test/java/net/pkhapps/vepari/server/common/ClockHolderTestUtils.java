package net.pkhapps.vepari.server.common;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

/**
 * TODO Document me!
 */
public class ClockHolderTestUtils {

    public static void initClockHolder() {
        ClockHolder.setClock(Clock.fixed(Instant.now(), ZoneId.systemDefault()));
    }

    public static void plus(Duration duration) {
        var now = ClockHolder.now();
        ClockHolder.setClock(Clock.fixed(now.plus(duration), ZoneId.systemDefault()));
    }

    public static void minus(Duration duration) {
        var now = ClockHolder.now();
        ClockHolder.setClock(Clock.fixed(now.minus(duration), ZoneId.systemDefault()));
    }
}
