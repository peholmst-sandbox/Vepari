package net.pkhapps.vepari.server.domain.converter;

import net.pkhapps.vepari.server.domain.WGS84Longitude;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

/**
 * Attribute converter for converting between {@link WGS84Longitude} and {@link BigDecimal}.
 */
@Converter(autoApply = true)
public class WGS84LongitudeAttributeConverter implements AttributeConverter<WGS84Longitude, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(WGS84Longitude attribute) {
        return attribute == null ? null : attribute.toDecimalDegrees();
    }

    @Override
    public WGS84Longitude convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : WGS84Longitude.fromDecimalDegrees(dbData);
    }
}
