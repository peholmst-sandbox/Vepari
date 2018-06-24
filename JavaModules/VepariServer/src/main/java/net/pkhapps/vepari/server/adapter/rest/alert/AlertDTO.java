package net.pkhapps.vepari.server.adapter.rest.alert;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.pkhapps.vepari.server.domain.Alert;
import net.pkhapps.vepari.server.domain.WGS84Coordinate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * JSON DTO for an {@link Alert}.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@JsonInclude(JsonInclude.Include.NON_NULL)
class AlertDTO {

    public final String assignmentNumber;
    public final BigDecimal latitude;
    public final BigDecimal longitude;
    public final String assignmentCode;
    public final String municipality;
    public final String street;
    public final String streetNumber;
    public final String crossingStreet;
    public final String objectInformation;
    public final String additionalInformation;
    public final Collection<String> units;
    public final Instant alertDate;

    AlertDTO(@NonNull Alert alert) {
        Objects.requireNonNull(alert, "alert must not be null");
        assignmentNumber = alert.getAssignmentNumber();
        latitude = toDecimalDegrees(alert.getLatitude());
        longitude = toDecimalDegrees(alert.getLongitude());
        assignmentCode = alert.getAssignmentCode();
        municipality = alert.getMunicipality();
        street = alert.getStreet();
        streetNumber = alert.getStreetNumber();
        crossingStreet = alert.getCrossingStreet();
        objectInformation = alert.getObjectInformation();
        additionalInformation = alert.getAdditionalInformation();
        units = Set.copyOf(alert.getUnits());
        alertDate = alert.getAlertDate();
    }

    @Nullable
    private BigDecimal toDecimalDegrees(@Nullable WGS84Coordinate coordinate) {
        return coordinate == null ? null : coordinate.toDecimalDegrees();
    }
}
