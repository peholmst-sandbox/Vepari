package net.pkhapps.vepari.server.application;

import net.pkhapps.vepari.server.domain.*;
import net.pkhapps.vepari.server.security.Permissions;
import net.pkhapps.vepari.server.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static net.pkhapps.vepari.server.domain.AlertReceiptRepository.byAlertRecord;
import static net.pkhapps.vepari.server.domain.AlertReceiptRepository.byUser;

/**
 * Default implementation of {@link AlertService}.
 */
@Service
@Secured({Permissions.RECEIVE_ALERT, Permissions.RECEIVE_ALERT_FULL_DETAILS})
class AlertServiceImpl implements AlertService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ObjectProvider<User> currentUser;
    private final AlertRecordRepository alertRecordRepository;
    private final AlertReceiptRepository alertReceiptRepository;
    private final AlertCache alertCache;

    // TODO Implement me

    AlertServiceImpl(ObjectProvider<User> currentUser,
                     AlertRecordRepository alertRecordRepository,
                     AlertReceiptRepository alertReceiptRepository,
                     AlertCache alertCache) {
        this.currentUser = currentUser;
        this.alertRecordRepository = alertRecordRepository;
        this.alertReceiptRepository = alertReceiptRepository;
        this.alertCache = alertCache;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<AlertRecord> getUnseenAlertRecords() {
        return null; // TODO Implement me!
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Optional<Alert> getAlert(long alertRecordId) {
        return alertRecordRepository.findById(alertRecordId)
                .flatMap(alertCache::get)
                .map(this::stripForbiddenInformationIfNeeded);
    }

    @NonNull
    private Alert stripForbiddenInformationIfNeeded(@NonNull Alert alert) {
        var user = currentUser.getObject();
        if (user.hasAuthority(Permissions.RECEIVE_ALERT_FULL_DETAILS)) {
            return alert;
        } else {
            return new Alert.Builder(alert.getAlertDate())
                    .setAssignmentNumber(alert.getAssignmentNumber())
                    .setAssignmentCode(alert.getAssignmentCode())
                    .build();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void acknowledgeAlert(long alertRecordId) {
        alertRecordRepository.findById(alertRecordId).ifPresent(alertRecord -> {
            var user = currentUser.getObject();
            if (!hasAcknowledged(alertRecord)) {
                alertReceiptRepository.save(new AlertReceipt(alertRecord, user));
                logger.debug("User {} acknowledged {}", user, alertRecord);
            } else {
                logger.debug("User {} has already acknowledged {}", user, alertRecord);
            }
        });
    }

    private boolean hasAcknowledged(@NonNull AlertRecord alertRecord) {
        return alertReceiptRepository.count(byAlertRecord(alertRecord).and(byUser(currentUser.getObject()))) == 1;
    }
}
