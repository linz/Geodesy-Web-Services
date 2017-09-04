package au.gov.ga.geodesy.support.persistence.jpa;

import au.gov.ga.geodesy.support.java.util.ImpreciseTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class ImpreciseTimeFieldConverter implements AttributeConverter<ImpreciseTime.Field, String> {
 
    @Override
    public String convertToDatabaseColumn(ImpreciseTime.Field field) {
        return field == null ? null : field.name();
    }
 
    @Override
    public ImpreciseTime.Field convertToEntityAttribute(String s) {
        return StringUtils.isEmpty(s) ? ImpreciseTime.Field.TIME_OF_DAY : ImpreciseTime.Field.valueOf(s);
    }
}
