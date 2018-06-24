package net.pkhapps.vepari.server.application;

import net.pkhapps.vepari.server.domain.*;
import net.pkhapps.vepari.server.security.Permissions;
import net.pkhapps.vepari.server.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Optional<Alert> getAlert(long alertRecordId) {
        return Optional.empty();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void acknowledgeAlert(long alertRecordId) {

    }
}
