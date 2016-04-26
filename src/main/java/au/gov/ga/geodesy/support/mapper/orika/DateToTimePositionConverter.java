package au.gov.ga.geodesy.support.mapper.orika;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import net.opengis.gml.v_3_2_1.TimePositionType;

public class DateToTimePositionConverter extends BidirectionalConverter<Date, TimePositionType> {

    public TimePositionType convertTo(Date date, Type<TimePositionType> targetType, MappingContext ctx) {
        TimePositionType time = new TimePositionType();
        time.getValue().add(dateFormat().format(date));
        return time;
    }

    public Date convertFrom(TimePositionType time, Type<Date> targetType, MappingContext ctx) {
        if (time.getValue().isEmpty()) {
            return null;
        }
        try {
            return dateFormat().parse(time.getValue().get(0));
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private SimpleDateFormat dateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }
}
