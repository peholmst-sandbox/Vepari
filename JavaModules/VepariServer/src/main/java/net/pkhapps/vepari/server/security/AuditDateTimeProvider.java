package net.pkhapps.vepari.server.security;

import net.pkhapps.vepari.server.util.ClockHolder;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * {@link DateTimeProvider} for JPA auditing that delegates to {@link ClockHolder}.
 */
@Component
class AuditDateTimeProvider implements DateTimeProvider {

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(ClockHolder.now());
    }
}
