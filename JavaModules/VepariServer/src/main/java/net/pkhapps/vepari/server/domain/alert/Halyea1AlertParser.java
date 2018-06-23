package net.pkhapps.vepari.server.domain.alert;

import net.pkhapps.vepari.server.domain.Alert;
import net.pkhapps.vepari.server.domain.TextMessage;
import net.pkhapps.vepari.server.domain.WGS84Latitude;
import net.pkhapps.vepari.server.domain.WGS84Longitude;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Alert parser for "HÄLYEA1" messages (this is a format that the Emergency Dispatch Center of Turku can send out their
 * alert SMS:es in, optimized for applications like Vepari).
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
class Halyea1AlertParser extends AbstractAlertParser {

    Halyea1AlertParser(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }

    @Override
    @NonNull
    Optional<Alert> parseAlert(@NonNull TextMessage textMessage) {
        Objects.requireNonNull(textMessage, "textMessage must not be null");
        var parts = textMessage.getBody().split("\\|");

        if (parts.length > 3 && parts[0].equals("1") && parts[2].equals("1")) {
            // This very much appears to be an alert
            Alert.Builder builder = new Alert.Builder(textMessage.getTimestamp());
            builder.setAssignmentNumber(parts[1]);

            // Alerts will always be in the N-E quadrant since this application is only usable in Finland.
            builder.setLatitude(extractAndReturn(3, parts, coordinates -> WGS84Latitude.fromDegreesDecimalMinutes(
                    coordinates.substring(0, coordinates.indexOf('E')))));
            builder.setLongitude(extractAndReturn(3, parts, coordinates -> WGS84Longitude.fromDegreesDecimalMinutes(
                    coordinates.substring(coordinates.indexOf('E')))));

            builder.setAssignmentCode(extractAndReturn(4, parts, String::trim));
            builder.setMunicipality(extractAndReturn(5, parts, String::trim));
            builder.setStreet(extractAndReturn(6, parts, String::trim));
            builder.setStreetNumber(extractAndReturn(7, parts, String::trim));
            builder.setCrossingStreet(extractAndReturn(8, parts, String::trim));
            builder.setObjectInformation(extractAndReturn(9, parts, String::trim));
            builder.setAdditionalInformation(extractAndReturn(10, parts, String::trim));
            extract(11, parts, units ->
                    Stream.of(units.split(",")).forEach(builder::addUnit));

            logger.info("Message {} is an alert", textMessage);
            logger.trace("The message \"{}\" is an alert", textMessage.getBody());
            return Optional.of(builder.build());
        }

        logger.info("Message {} was not an alert", textMessage);
        logger.trace("The message \"{}\" does not appear to be an alert", textMessage.getBody());
        return Optional.empty();
    }

    @Nullable
    private <T> T extractAndReturn(int index, @NonNull String[] parts, @NonNull Function<String, T> parser) {
        if (parts.length > index && parts[index].length() > 0) {
            try {
                return parser.apply(parts[index]);
            } catch (Exception ex) {
                logger.trace("An error occurred while parsing: " + parts[index], ex);
            }
        }
        return null;
    }

    @SuppressWarnings("SameParameterValue")
    private void extract(int index, @NonNull String[] parts, @NonNull Consumer<String> parser) {
        extractAndReturn(index, parts, part -> {
            parser.accept(part);
            return null;
        });
    }
}
