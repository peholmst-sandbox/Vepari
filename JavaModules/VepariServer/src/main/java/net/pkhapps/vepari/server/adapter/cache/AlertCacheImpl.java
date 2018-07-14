package net.pkhapps.vepari.server.adapter.cache;

import net.pkhapps.vepari.server.domain.Alert;
import net.pkhapps.vepari.server.domain.AlertCache;
import net.pkhapps.vepari.server.domain.AlertProperties;
import net.pkhapps.vepari.server.domain.AlertRecord;
import net.pkhapps.vepari.server.util.ClockHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default implementation of {@link AlertCache} that uses a {@link java.util.concurrent.ConcurrentHashMap} for storing
 * and a background thread for cleaning up old entries. This implementation does not scale, so if Vepari Server needs
 * to be deployed in a scalable or high-availability environment, a more advanced cache will be needed. This is good
 * enough for the initial needs.
 */
@Component
class AlertCacheImpl implements AlertCache {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ConcurrentMap<Long, Alert> alertMap = new ConcurrentHashMap<>();
    private final AlertProperties alertProperties;

    AlertCacheImpl(AlertProperties alertProperties) {
        this.alertProperties = alertProperties;
    }

    @Override
    public void put(@NonNull AlertRecord alertRecord, @NonNull Alert alert) {
        Objects.requireNonNull(alertRecord, "alertRecord must not be null");
        Objects.requireNonNull(alert, "alert must not be null");
        logger.debug("Storing alert {} under record {}", alert, alertRecord);
        alertMap.putIfAbsent(alertRecord.getId(), alert);
    }

    @Override
    @NonNull
    public Optional<Alert> get(@NonNull AlertRecord alertRecord) {
        Objects.requireNonNull(alertRecord, "alertRecord must not be null");
        return Optional.ofNullable(alertMap.get(alertRecord.getId()));
    }

    @Scheduled(fixedDelay = 1000)
    void evictOldAlerts() {
        alertMap.forEach((id, alert) -> {
            if (isDueForEviction(alert)) {
                logger.debug("Evicting alert {} with alert record ID {}", alert, id);
                alertMap.remove(id);
            }
        });
    }

    private boolean isDueForEviction(@NonNull Alert alert) {
        return alert.getAlertDate().plus(alertProperties.getAlertTimeout()).isBefore(ClockHolder.now());
    }
}
