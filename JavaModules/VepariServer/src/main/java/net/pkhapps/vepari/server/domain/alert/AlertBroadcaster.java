package net.pkhapps.vepari.server.domain.alert;

import net.pkhapps.vepari.server.domain.AlertCache;
import net.pkhapps.vepari.server.domain.AlertRecord;
import net.pkhapps.vepari.server.domain.AlertRecordRepository;
import net.pkhapps.vepari.server.domain.event.AlertReceivedEvent;
import net.pkhapps.vepari.server.domain.event.PushNotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Application event listener for {@link AlertReceivedEvent}s that stores received alerts in
 * {@link AlertRecordRepository} and {@link AlertCache}, respectively, and then fires a {@link PushNotificationEvent}
 * to notify clients that there is a new alert.
 */
@Component
class AlertBroadcaster {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AlertRecordRepository alertRecordRepository;
    private final AlertCache alertCache;
    private final ApplicationEventPublisher applicationEventPublisher;

    AlertBroadcaster(AlertRecordRepository alertRecordRepository,
                     AlertCache alertCache,
                     ApplicationEventPublisher applicationEventPublisher) {
        this.alertRecordRepository = alertRecordRepository;
        this.alertCache = alertCache;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener
    public void onAlertReceivedEvent(@NonNull AlertReceivedEvent alertReceivedEvent) {
        var record = alertRecordRepository.saveAndFlush(new AlertRecord(alertReceivedEvent));
        logger.debug("Storing alert {} and record {} in cache", alertReceivedEvent.getAlert(), record);
        alertCache.put(record, alertReceivedEvent.getAlert());
        logger.debug("Publishing push notification event for alert record {}", record);
        applicationEventPublisher.publishEvent(new PushAlertNotificationEvent(record));
    }

    static class PushAlertNotificationEvent implements PushNotificationEvent {

        private final AlertRecord alertRecord;

        PushAlertNotificationEvent(AlertRecord alertRecord) {
            this.alertRecord = alertRecord;
        }

        @Override
        public String toMessageString() {
            return String.format("ALERT:%d", alertRecord.getId());
        }

        @Override
        public String toString() {
            return String.format("%s[%s]", getClass().getSimpleName(), alertRecord);
        }
    }
}
