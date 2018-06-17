package net.pkhapps.vepari.server.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link WGS84Coordinate}.
 */
public class WGS84CoordinateTest {

    @Test
    public void parseDegreesDecimalMinutes_axisFirst() {
        var input = "N 60°26.806";
        var result = WGS84Coordinate.parseDegreesDecimalMinutes(input, 'N', 'S');
        System.out.println(result);
        var coordinate = new WGS84Coordinate(result, 'N', 'S') {
        };
        assertThat(coordinate.toDegreesDecimalMinutes(WGS84Coordinate.AxisLocation.PREFIX)).isEqualTo(input);
    }

    @Test
    public void parseDegreesDecimalMinutes_axisLast() {
        var input = "22°15.212 W";
        var result = WGS84Coordinate.parseDegreesDecimalMinutes(input, 'E', 'W');
        System.out.println(result);
        var coordinate = new WGS84Coordinate(result, 'E', 'W') {
        };
        assertThat(coordinate.toDegreesDecimalMinutes(WGS84Coordinate.AxisLocation.SUFFIX)).isEqualTo(input);
    }
}
