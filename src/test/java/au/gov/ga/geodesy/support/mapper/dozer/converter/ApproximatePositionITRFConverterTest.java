package au.gov.ga.geodesy.support.mapper.dozer.converter;

import au.gov.ga.geodesy.igssitelog.domain.model.ApproximatePosition;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType.ApproximatePositionITRF;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class ApproximatePositionITRFConverterTest {
    ApproximatePositionITRFConverter conv = new ApproximatePositionITRFConverter();
    private double decimalLat;
    private double decimalLong;

    @Test
    public void test01() {
        ApproximatePosition source = buildApproximatePosition();
        ApproximatePositionITRF dest = null;
        dest = (ApproximatePositionITRF) conv.convert(dest, source, ApproximatePositionITRF.class, ApproximatePosition.class);

        assertThat(Double.parseDouble(dest.getXCoordinateInMeters()), is(source.getItrfX()));
        assertThat(Double.parseDouble(dest.getYCoordinateInMeters()), is(source.getItrfY()));
        assertThat(Double.parseDouble(dest.getZCoordinateInMeters()), is(source.getItrfZ()));

        assertThat(dest.getElevationMEllips(), is(source.getElevationGrs80()));

        assertThat(dest.getLongitudeEast().doubleValue(), closeTo(this.decimalLong, 0.000001));
        assertThat(dest.getLatitudeNorth().doubleValue(), closeTo(this.decimalLat, 0.000001));
    }

    @Test
    public void test02() {
        ApproximatePositionITRF source = buildApproximatePositionITRF();
        ApproximatePosition dest = null;
        dest = (ApproximatePosition) conv.convert(dest, source, ApproximatePositionITRF.class, ApproximatePosition.class);

        assertThat(dest.getItrfX(), is(Double.parseDouble(source.getXCoordinateInMeters())));
        assertThat(dest.getItrfY(), is(Double.parseDouble(source.getYCoordinateInMeters())));
        assertThat(dest.getItrfZ(), is(Double.parseDouble(source.getZCoordinateInMeters())));

        assertThat(dest.getElevationGrs80(), is(source.getElevationMEllips()));

        assertThat(dest.getGrs80().getCoordinate().x, closeTo(source.getLongitudeEast().doubleValue(), 0.0000001));
        assertThat(dest.getGrs80().getCoordinate().y, closeTo(source.getLatitudeNorth().doubleValue(), 0.0000001));
    }

    private ApproximatePosition buildApproximatePosition() {
        ApproximatePosition source = new ApproximatePosition();

        source.setItrfX(1.0);
        source.setItrfY(2.0);
        source.setItrfZ(3.0);

        source.setElevationGrs80("20");

        Coordinate c = new Coordinate();
        c.setOrdinate(0, -234012.44594);
        this.decimalLat = -23.670124; // Expected decimalTodms mapping from this
        c.setOrdinate(1, +1335307.84759);
        this.decimalLong = 133.885513; // Expected decimalTodms mapping from this
        PrecisionModel pm = new PrecisionModel();
        Point p = new Point(c, pm, 0);

        source.setGrs80(p);
        return source;
    }

    private ApproximatePositionITRF buildApproximatePositionITRF() {
        ApproximatePositionITRF source = new ApproximatePositionITRF();

        source.setElevationMEllips("30");

        source.setXCoordinateInMeters("4.0");
        source.setYCoordinateInMeters("5.0");
        source.setZCoordinateInMeters("6.0");

        source.setLatitudeNorth(new BigDecimal(127));
        source.setLongitudeEast(new BigDecimal(144));
        return source;
    }

}
