package net.pkhapps.vepari.server.application;

import net.pkhapps.vepari.server.domain.Alert;
import net.pkhapps.vepari.server.domain.AlertRecord;

import java.util.List;
import java.util.Optional;

/**
 * TODO document me
 */
public interface AlertService {

    List<AlertRecord> getUnseenAlertRecords();

    Optional<Alert> getAlert(long alertRecordId);

    void acknowledgeAlert(long alertRecordId);
}
