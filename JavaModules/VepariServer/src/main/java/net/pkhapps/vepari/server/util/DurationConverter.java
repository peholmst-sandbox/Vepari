package net.pkhapps.vepari.server.util;

import org.springframework.core.convert.converter.Converter;

import java.time.Duration;

/**
 * Converter for converting from a string to {@link Duration}. The string must be in a format that
 * {@link Duration#parse(CharSequence)} understands.
 */
//@Component
//@ConfigurationPropertiesBinding
public class DurationConverter implements Converter<String, Duration> { // TODO Do we really need this converter?

    @Override
    public Duration convert(String source) {
        return Duration.parse(source);
    }
}
