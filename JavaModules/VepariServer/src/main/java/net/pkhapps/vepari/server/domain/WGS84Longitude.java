package net.pkhapps.vepari.server.domain;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * Value object representing a longitude coordinate in the WGS-84 coordinate system.
 *
 * @see WGS84Latitude
 */
public class WGS84Longitude extends WGS84Coordinate {

    private static final char POSITIVE_AXIS = 'E';
    private static final char NEGATIVE_AXIS = 'W';

    private WGS84Longitude(@NonNull BigDecimal decimalDegrees) {
        super(decimalDegrees, POSITIVE_AXIS, NEGATIVE_AXIS);
        if (decimalDegrees.abs().compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }

    /**
     * Creates a new {@code WGS84Longitude} from the given decimal degrees (DD).
     *
     * @throws IllegalArgumentException if the decimal degrees are invalid.
     */
    @NonNull
    public static WGS84Longitude fromDecimalDegrees(@NonNull BigDecimal decimalDegrees) {
        return new WGS84Longitude(decimalDegrees);
    }

    /**
     * Creates a new {@code WGS84Latitude} from the given degrees decimal minutes string (e.g. {@code W 22°15.212}).
     *
     * @throws IllegalArgumentException if the string is invalid.
     */
    @NonNull
    public static WGS84Longitude fromDegreesDecimalMinutes(@NonNull String degreesDecimalMinutes) {
        return fromDecimalDegrees(parseDegreesDecimalMinutes(degreesDecimalMinutes, POSITIVE_AXIS, NEGATIVE_AXIS));
    }
}
