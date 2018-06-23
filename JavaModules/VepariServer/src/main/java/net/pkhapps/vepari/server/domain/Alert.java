package net.pkhapps.vepari.server.domain;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Value object representing an alert received from the emergency dispatch center. The information received depends only
 * on what they are willing to give out and in which format, and if the alert is received as a text message, it is
 * probably cut off if everything does not fit into a single message. Therefore, all the fields can be null and
 * the application must be able to work even when some fields are missing.
 */
public class Alert implements Serializable {

    private final String assignmentNumber;
    private final WGS84Latitude latitude;
    private final WGS84Longitude longitude;
    private final String assignmentCode;
    private final String municipality;
    private final String street;
    private final String streetNumber;
    private final String crossingStreet;
    private final String objectInformation;
    private final String additionalInformation;
    private final Set<String> units;
    private final Instant alertDate;

    private Alert(@Nullable String assignmentNumber,
                  @Nullable WGS84Latitude latitude,
                  @Nullable WGS84Longitude longitude,
                  @Nullable String assignmentCode,
                  @Nullable String municipality,
                  @Nullable String street,
                  @Nullable String streetNumber,
                  @Nullable String crossingStreet,
                  @Nullable String objectInformation,
                  @Nullable String additionalInformation,
                  @NonNull Collection<String> units,
                  @NonNull Instant alertDate) {
        this.assignmentNumber = assignmentNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.assignmentCode = assignmentCode;
        this.municipality = municipality;
        this.street = street;
        this.streetNumber = streetNumber;
        this.crossingStreet = crossingStreet;
        this.objectInformation = objectInformation;
        this.additionalInformation = additionalInformation;
        this.units = Set.copyOf(units);
        this.alertDate = alertDate;
    }

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
    @SuppressWarnings("SpellCheckingInspection")
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

    /**
     * The alerted units.
     */
    @NonNull
    public Set<String> getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return String.format("%s[code=%s, alertDate=%s]", getClass().getSimpleName(), getAssignmentCode(),
                getAlertDate());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        var other = (Alert) obj;
        return Objects.equals(assignmentNumber, other.assignmentNumber) &&
                Objects.equals(latitude, other.latitude) &&
                Objects.equals(longitude, other.longitude) &&
                Objects.equals(assignmentCode, other.assignmentCode) &&
                Objects.equals(municipality, other.municipality) &&
                Objects.equals(street, other.street) &&
                Objects.equals(streetNumber, other.streetNumber) &&
                Objects.equals(crossingStreet, other.crossingStreet) &&
                Objects.equals(objectInformation, other.objectInformation) &&
                Objects.equals(additionalInformation, other.additionalInformation) &&
                Objects.equals(units, other.units) &&
                Objects.equals(alertDate, other.alertDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentNumber, latitude, longitude, assignmentCode, municipality, street, streetNumber,
                crossingStreet, objectInformation, additionalInformation, units, alertDate);
    }

    /**
     * Builder for creating new {@link Alert} instances (this is easier to use than passing lots of arguments
     * to the initializing constructor).
     */
    @SuppressWarnings({"UnusedReturnValue"})
    public static class Builder {

        private final Instant alertDate;
        private final Set<String> units = new HashSet<>();
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

        public Builder(@NonNull Instant alertDate) {
            this.alertDate = Objects.requireNonNull(alertDate, "alertDate must not be null");
        }

        @NonNull
        public Builder setAssignmentNumber(@Nullable String assignmentNumber) {
            this.assignmentNumber = assignmentNumber;
            return this;
        }

        @NonNull
        public Builder setLatitude(@Nullable WGS84Latitude latitude) {
            this.latitude = latitude;
            return this;
        }

        @NonNull
        public Builder setLongitude(@Nullable WGS84Longitude longitude) {
            this.longitude = longitude;
            return this;
        }

        @NonNull
        public Builder setAssignmentCode(@Nullable String assignmentCode) {
            this.assignmentCode = assignmentCode;
            return this;
        }

        @NonNull
        public Builder setMunicipality(@Nullable String municipality) {
            this.municipality = municipality;
            return this;
        }

        @NonNull
        public Builder setStreet(@Nullable String street) {
            this.street = street;
            return this;
        }

        @NonNull
        public Builder setStreetNumber(@Nullable String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        @NonNull
        public Builder setCrossingStreet(@Nullable String crossingStreet) {
            this.crossingStreet = crossingStreet;
            return this;
        }

        @NonNull
        public Builder setObjectInformation(@Nullable String objectInformation) {
            this.objectInformation = objectInformation;
            return this;
        }

        @NonNull
        public Builder setAdditionalInformation(@Nullable String additionalInformation) {
            this.additionalInformation = additionalInformation;
            return this;
        }

        @NonNull
        public Builder addUnit(@NonNull String unit) {
            Objects.requireNonNull(unit, "unit must not be null");
            units.add(unit);
            return this;
        }

        /**
         * Creates and returns a new {@link Alert} from this builder.
         */
        @NonNull
        public Alert build() {
            return new Alert(assignmentNumber, latitude, longitude, assignmentCode, municipality, street, streetNumber,
                    crossingStreet, objectInformation, additionalInformation, units, alertDate);
        }
    }
}
