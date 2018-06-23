package net.pkhapps.vepari.server.domain.alert;

import net.pkhapps.vepari.server.domain.TextMessage;
import net.pkhapps.vepari.server.domain.WGS84Latitude;
import net.pkhapps.vepari.server.domain.WGS84Longitude;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit test for {@link Halyea1AlertParser}.
 */
@SuppressWarnings("SpellCheckingInspection")
public class Halyea1AlertParserTest {

    private Halyea1AlertParser parser;

    @Before
    public void setUp() {
        parser = new Halyea1AlertParser(mock(ApplicationEventPublisher.class));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void parseAlert_alertMessage_alertReturned() {
        var textMessage = new TextMessage("HÄKE", Instant.now(), "1|1800056645|1|N 60°26.806 E 22°15.212|H17 |TURKU|PUISTOKATU|5 B|TESTIKATU|TURUN HÄTÄKESKUS|Testi testi testi.|RVST11,RVST13|10:10_08.02.2018");
        var result = parser.parseAlert(textMessage);
        assertThat(result).isPresent();

        var alert = result.get();
        assertThat(alert.getAlertDate()).isEqualTo(textMessage.getTimestamp());
        assertThat(alert.getAssignmentNumber()).isEqualTo("1800056645");
        assertThat(alert.getLatitude()).isEqualTo(WGS84Latitude.fromDegreesDecimalMinutes("N 60°26.806"));
        assertThat(alert.getLongitude()).isEqualTo(WGS84Longitude.fromDegreesDecimalMinutes("E 22°15.212"));
        assertThat(alert.getAssignmentCode()).isEqualTo("H17");
        assertThat(alert.getMunicipality()).isEqualTo("TURKU");
        assertThat(alert.getStreet()).isEqualTo("PUISTOKATU");
        assertThat(alert.getStreetNumber()).isEqualTo("5 B");
        assertThat(alert.getCrossingStreet()).isEqualTo("TESTIKATU");
        assertThat(alert.getObjectInformation()).isEqualTo("TURUN HÄTÄKESKUS");
        assertThat(alert.getAdditionalInformation()).isEqualTo("Testi testi testi.");
        assertThat(alert.getUnits()).contains("RVST11", "RVST13");
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void parseAlert_truncatedAlertMessage_alertReturned() {
        var textMessage = new TextMessage("HÄKE", Instant.now(), "1|1800056645|1|N 60°26.806 E 22°15.212|H17 |TURKU|PUISTOKATU|5 B||TURUN HÄTÄKESKUS|Testi");
        var result = parser.parseAlert(textMessage);
        assertThat(result).isPresent();
        var alert = result.get();
        assertThat(alert.getAlertDate()).isEqualTo(textMessage.getTimestamp());
        assertThat(alert.getAssignmentNumber()).isEqualTo("1800056645");
        assertThat(alert.getLatitude()).isEqualTo(WGS84Latitude.fromDegreesDecimalMinutes("N 60°26.806"));
        assertThat(alert.getLongitude()).isEqualTo(WGS84Longitude.fromDegreesDecimalMinutes("E 22°15.212"));
        assertThat(alert.getAssignmentCode()).isEqualTo("H17");
        assertThat(alert.getMunicipality()).isEqualTo("TURKU");
        assertThat(alert.getStreet()).isEqualTo("PUISTOKATU");
        assertThat(alert.getStreetNumber()).isEqualTo("5 B");
        assertThat(alert.getCrossingStreet()).isNull();
        assertThat(alert.getObjectInformation()).isEqualTo("TURUN HÄTÄKESKUS");
        assertThat(alert.getAdditionalInformation()).isEqualTo("Testi");
        assertThat(alert.getUnits()).isEmpty();
    }

    @Test
    public void parseAlert_nonAlertMessage_emptyOptionalReturned() {
        var textMessage = new TextMessage("JoeCool", Instant.now(), "This is just an ordinary text message that contains a few ||s");
        var result = parser.parseAlert(textMessage);
        assertThat(result).isEmpty();
    }
}
