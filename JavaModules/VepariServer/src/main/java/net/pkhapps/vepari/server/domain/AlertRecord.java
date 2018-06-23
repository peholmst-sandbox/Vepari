package net.pkhapps.vepari.server.domain;

import net.pkhapps.vepari.server.domain.base.AggregateRoot;
import net.pkhapps.vepari.server.domain.event.AlertReceivedEvent;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root representing an alert record, i.e. a record of the fact that an alert has been received. For legal
 * and security reasons, the actual alert details cannot be stored in the database.
 */
@Entity
@Table(name = "alert_records")
public class AlertRecord extends AggregateRoot<AlertRecord> {

    @Column(name = "alert_date", nullable = false)
    private Instant alertDate;
    @Column(name = "reception_date", nullable = false)
    private Instant receptionDate;
    @Column(name = "assignment_code")
    private String assignmentCode;

    /**
     * Used by JPA only.
     */
    @SuppressWarnings("unused")
    private AlertRecord() {
    }

    /**
     * Creates a new {@code AlertRecord} from the given {@link AlertReceivedEvent}.
     */
    public AlertRecord(@NonNull AlertReceivedEvent alertEvent) {
        Objects.requireNonNull(alertEvent, "alertEvent must not be null");
        alertDate = alertEvent.getAlert().getAlertDate();
        receptionDate = alertEvent.getTimestamp();
        assignmentCode = alertEvent.getAlert().getAssignmentCode();
    }

    /**
     * The date when the alert was sent out from the emergency dispatch center.
     */
    @NonNull
    public Instant getAlertDate() {
        return alertDate;
    }

    /**
     * The date when the alert was received by this application.
     */
    @NonNull
    public Instant getReceptionDate() {
        return receptionDate;
    }

    /**
     * The assignment code of the alert.
     */
    @Nullable
    public String getAssignmentCode() {
        return assignmentCode;
    }
}
