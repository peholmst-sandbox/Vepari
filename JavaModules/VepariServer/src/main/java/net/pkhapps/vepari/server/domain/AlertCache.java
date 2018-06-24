package net.pkhapps.vepari.server.domain;

import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Interface for a cache of received {@link Alert}s. Alerts remain in the cache for a specified amount of time, after
 * which they are automatically evicted.
 */
public interface AlertCache {

    /**
     * Stores the given alert record/alert pair in the cache.
     */
    void put(@NonNull AlertRecord alertRecord, @NonNull Alert alert);

    /**
     * Retrieves the alert corresponding to the given alert record.
     */
    @NonNull
    Optional<Alert> get(@NonNull AlertRecord alertRecord);
}
