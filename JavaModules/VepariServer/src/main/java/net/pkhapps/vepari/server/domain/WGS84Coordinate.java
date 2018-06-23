package net.pkhapps.vepari.server.domain;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * TODO document me
 */
@SuppressWarnings("WeakerAccess")
public abstract class WGS84Coordinate {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private final BigDecimal decimalDegrees;
    private final char positiveAxis;
    private final char negativeAxis;

    /**
     * @param decimalDegrees
     * @param positiveAxis
     * @param negativeAxis
     */
    protected WGS84Coordinate(@NonNull BigDecimal decimalDegrees, char positiveAxis, char negativeAxis) {
        Objects.requireNonNull(decimalDegrees, "decimalDegrees must not be null");
        this.decimalDegrees = decimalDegrees;
        this.positiveAxis = positiveAxis;
        this.negativeAxis = negativeAxis;
    }

    /**
     * @param input
     * @param positiveAxis
     * @param negativeAxis
     * @return
     */
    @NonNull
    protected static BigDecimal parseDegreesDecimalMinutes(@NonNull String input,
                                                           char positiveAxis,
                                                           char negativeAxis) {
        var trimmedInput = StringUtils.trimAllWhitespace(input.toUpperCase());
        if (trimmedInput.length() < 4) {
            throw new IllegalArgumentException("Input string too short: " + input);
        }

        // Determine whether the axis comes first or last in the string
        char axis;
        if (isAxis(trimmedInput.charAt(0), positiveAxis, negativeAxis)) {
            axis = trimmedInput.charAt(0);
            trimmedInput = trimmedInput.substring(1);
        } else if (isAxis(trimmedInput.charAt(trimmedInput.length() - 1), positiveAxis, negativeAxis)) {
            axis = trimmedInput.charAt(trimmedInput.length() - 1);
            trimmedInput = trimmedInput.substring(0, trimmedInput.length() - 1);
        } else {
            throw new IllegalArgumentException("Input string does not contain valid axis: " + input);
        }

        // Split the string into degrees and decimal minutes
        var parts = trimmedInput.split("°");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Input string does not contain degrees separator: " + input);
        }
        var degrees = new BigDecimal(parts[0]);
        var decimalMinutes = new BigDecimal(parts[1]);
        decimalMinutes = decimalMinutes.setScale(decimalMinutes.scale() + 2, ROUNDING_MODE);
        var decimalDegrees = degrees.add(decimalMinutes.divide(BigDecimal.valueOf(60), ROUNDING_MODE));

        if (axis == negativeAxis) {
            return decimalDegrees.negate();
        } else {
            return decimalDegrees;
        }
    }

    private static boolean isAxis(char actual, char positiveAxis, char negativeAxis) {
        return actual == positiveAxis || actual == negativeAxis;
    }

    // TODO Create parser for degreesMinutesSeconds (currently not needed)

    /**
     * @return
     */
    @NonNull
    public BigDecimal toDecimalDegrees() {
        return decimalDegrees;
    }

    /**
     * @param axisLocation
     * @return
     */
    @NonNull
    public String toDegreesDecimalMinutes(@NonNull AxisLocation axisLocation) {
        Objects.requireNonNull(axisLocation, "axisLocation must not be null");
        var sb = new StringBuilder();
        if (axisLocation == AxisLocation.PREFIX) {
            sb.append(getAxis()).append(' ');
        }
        var absoluteDecimalDegrees = decimalDegrees.abs();
        var degrees = absoluteDecimalDegrees.intValue();
        sb.append(degrees).append('°');
        var decimalMinutes = absoluteDecimalDegrees.subtract(BigDecimal.valueOf(degrees))
                .multiply(BigDecimal.valueOf(60)).setScale(absoluteDecimalDegrees.scale() - 2, ROUNDING_MODE);
        sb.append(decimalMinutes);
        if (axisLocation == AxisLocation.SUFFIX) {
            sb.append(' ').append(getAxis());
        }
        return sb.toString();
    }

    private char getAxis() {
        if (decimalDegrees.signum() >= 0) {
            return positiveAxis;
        } else {
            return negativeAxis;
        }
    }

    // TODO Create formatter for degreesMinutesSeconds (currently not needed)

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return decimalDegrees.equals(((WGS84Coordinate) obj).decimalDegrees);
    }

    @Override
    public int hashCode() {
        return decimalDegrees.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), toDegreesDecimalMinutes(AxisLocation.SUFFIX));
    }

    /**
     *
     */
    public enum AxisLocation {
        /**
         *
         */
        PREFIX,
        /**
         *
         */
        SUFFIX
    }
}
