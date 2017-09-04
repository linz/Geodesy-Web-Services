package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.java.util.ImpreciseTime;

import ma.glasnost.orika.metadata.TypeFactory;

import net.opengis.gml.v_3_2_1.TimePositionType;

public class ImpreciseTimeToTimePositionConverterTest {

    private ImpreciseTimeToTimePositionConverter converter = new ImpreciseTimeToTimePositionConverter();

    @Test
    public void parseYear() {
        TimePositionType timePosition = new TimePositionType().withValue("1997");
        ImpreciseTime time = converter.convertFrom(timePosition, TypeFactory.valueOf(ImpreciseTime.class), null);
        assertThat(time.toString(), is("1997"));
        assertThat(time.getPrecision(), is(ImpreciseTime.Field.YEAR));
    }

    @Test
    public void parseYearMonth() {
        TimePositionType timePosition = new TimePositionType().withValue("1997-12");
        ImpreciseTime time = converter.convertFrom(timePosition, TypeFactory.valueOf(ImpreciseTime.class), null);
        assertThat(time.toString(), is("1997-12"));
        assertThat(time.getPrecision(), is(ImpreciseTime.Field.MONTH));
    }

    @Test
    public void parseDate() {
        TimePositionType timePosition = new TimePositionType().withValue("1997-12-03");
        ImpreciseTime time = converter.convertFrom(timePosition, TypeFactory.valueOf(ImpreciseTime.class), null);
        assertThat(time.toString(), is("1997-12-03"));
        assertThat(time.getPrecision(), is(ImpreciseTime.Field.DAY));
    }

    @Test
    public void parseDateTime() {
        TimePositionType timePosition = new TimePositionType().withValue("1997-12-03T12:13Z");
        ImpreciseTime time = converter.convertFrom(timePosition, TypeFactory.valueOf(ImpreciseTime.class), null);
        assertThat(time.toString(), is("1997-12-03T12:13:00Z"));
        assertThat(time.getPrecision(), is(ImpreciseTime.Field.TIME_OF_DAY));
    }
}
