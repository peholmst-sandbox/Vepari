package net.pkhapps.vepari.server.domain;

import net.pkhapps.vepari.server.domain.base.AggregateRoot;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

/**
 * Entity representing an alert received from the emergency dispatch center. The information received depends only on
 * what they are willing to give out and in which format, and if the alert is received as a text message, it is
 * probably cut off if everything does not fit into a single message. Therefore, all the fields can be null and
 * the application must be able to work even when some fields are missing.
 */
@Entity
@Table(name = "alerts")
public class Alert extends AggregateRoot<Alert> {

    private String assignmentNumber;

    private WGS84Latitude latitude;

    private WGS84Longitude longitude;

    private String assignmentCode;

    private String municipality;

    private String street;

    private String streetNumber;

    private String crossingStreet;

    private String objectInformation;

    private String additionalInformation;

    @Column(nullable = false)
    private Instant alertDate;

    // TODO Units

    /**
     * The assignment number from the emergency dispatch center's internal system.
     */
    @Nullable
    public String getAssignmentNumber() {
        return assignmentNumber;
    }

    /**
     * The latitude coordinate of the incident.
     */
    @Nullable
    public WGS84Latitude getLatitude() {
        return latitude;
    }

    /**
     * The longitude coordinate of the incident.
     */
    @Nullable
    public WGS84Longitude getLongitude() {
        return longitude;
    }

    /**
     * The municipality where the incident happened.
     */
    @Nullable
    public String getMunicipality() {
        return municipality;
    }

    /**
     * The assignment code as given by the emergency dispatch center.
     */
    @Nullable
    public String getAssignmentCode() {
        return assignmentCode;
    }

    /**
     * The name of the street of the incident.
     */
    @Nullable
    public String getStreet() {
        return street;
    }

    /**
     * The street number of the incident.
     */
    @Nullable
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * The name of the crossing street, in case the incident is at or near an intersection (e.g. traffic accidents).
     */
    @Nullable
    public String getCrossingStreet() {
        return crossingStreet;
    }

    /**
     * Any additional information about the object that the emergency dispatch center has in their system.
     * <p>
     * TODO Is object ("kohde" in Finnish) really the correct English term here?
     */
    @Nullable
    public String getObjectInformation() {
        return objectInformation;
    }

    /**
     * Any additional information about the incident given by the caller to the emergency dispatch center.
     */
    @Nullable
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * The date when the alert was sent out from the emergency dispatch center.
     */
    @NonNull
    public Instant getAlertDate() {
        return alertDate;
    }
}
