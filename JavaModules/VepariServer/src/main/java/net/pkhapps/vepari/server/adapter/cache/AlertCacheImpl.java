package net.pkhapps.vepari.server.adapter.cache;

import net.pkhapps.vepari.server.domain.Alert;
import net.pkhapps.vepari.server.domain.AlertCache;
import net.pkhapps.vepari.server.domain.AlertRecord;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class AlertCacheImpl implements AlertCache {

    @Override
    public void put(AlertRecord alertRecord, Alert alert) {

    }

    @Override
    public Optional<Alert> get(AlertRecord alertRecord) {
        return Optional.empty();
    }
}
