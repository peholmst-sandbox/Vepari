package net.pkhapps.vepari.server.domain.converter;

import net.pkhapps.vepari.server.domain.WGS84Latitude;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

/**
 * Attribute converter for converting between {@link WGS84Latitude} and {@link BigDecimal}.
 */
@Converter(autoApply = true)
public class WGS84LatitudeAttributeConverter implements AttributeConverter<WGS84Latitude, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(WGS84Latitude attribute) {
        return attribute == null ? null : attribute.toDecimalDegrees();
    }

    @Override
    public WGS84Latitude convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : WGS84Latitude.fromDecimalDegrees(dbData);
    }
}
