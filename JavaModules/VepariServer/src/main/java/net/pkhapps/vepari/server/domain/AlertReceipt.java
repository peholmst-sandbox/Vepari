package net.pkhapps.vepari.server.domain;

import net.pkhapps.vepari.server.domain.base.AggregateRoot;
import net.pkhapps.vepari.server.security.User;
import net.pkhapps.vepari.server.util.ClockHolder;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root representing an alert receipt, i.e. the event of a user receiving a specific alert.
 */
@Entity
@Table(name = "alert_receipts", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "alert_record_id"}))
public class AlertReceipt extends AggregateRoot<AlertReceipt> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "delivery_date", nullable = false)
    private Instant deliveryDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "alert_record_id", nullable = false)
    private AlertRecord alertRecord;

    /**
     * Used by JPA only.
     */
    @SuppressWarnings("unused")
    private AlertReceipt() {
    }

    /**
     * Creates a new {@code AlertReceipt} for the given {@link AlertRecord} and {@link User}.
     */
    public AlertReceipt(@NonNull AlertRecord alertRecord, @NonNull User user) {
        Objects.requireNonNull(alertRecord, "alertRecord must not be null");
        Objects.requireNonNull(user, "user must not be null");
        this.user = user;
        this.alertRecord = alertRecord;
        this.deliveryDate = ClockHolder.now();
    }

    /**
     * The user that has received the alert.
     */
    @NonNull
    public User getUser() {
        return user;
    }

    /**
     * The date when the user received the alert.
     */
    @NonNull
    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * The alert that the user received.
     */
    @NonNull
    public AlertRecord getAlertRecord() {
        return alertRecord;
    }
}
