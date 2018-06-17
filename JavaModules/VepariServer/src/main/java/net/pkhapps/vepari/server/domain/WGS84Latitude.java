package net.pkhapps.vepari.server.domain;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * Value object representing a latitude coordinate in the WGS-84 coordinate system.
 *
 * @see WGS84Longitude
 */
public class WGS84Latitude extends WGS84Coordinate {

    private static final char POSITIVE_AXIS = 'N';
    private static final char NEGATIVE_AXIS = 'S';

    private WGS84Latitude(@NonNull BigDecimal decimalDegrees) {
        super(decimalDegrees, POSITIVE_AXIS, NEGATIVE_AXIS);
        if (decimalDegrees.abs().compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
    }

    /**
     * Creates a new {@code WGS84Latitude} from the given decimal degrees (DD).
     *
     * @throws IllegalArgumentException if the decimal degrees are invalid.
     */
    @NonNull
    public static WGS84Latitude fromDecimalDegrees(@NonNull BigDecimal decimalDegrees) {
        return new WGS84Latitude(decimalDegrees);
    }

    /**
     * Creates a new {@code WGS84Latitude} from the given degrees decimal minutes string (e.g. {@code N 60°26.806}).
     *
     * @throws IllegalArgumentException if the string is invalid.
     */
    @NonNull
    public static WGS84Latitude fromDegreesDecimalMinutes(@NonNull String degreesDecimalMinutes) {
        return fromDecimalDegrees(parseDegreesDecimalMinutes(degreesDecimalMinutes, POSITIVE_AXIS, NEGATIVE_AXIS));
    }
}
