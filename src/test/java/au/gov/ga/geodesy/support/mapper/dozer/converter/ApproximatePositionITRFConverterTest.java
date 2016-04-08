package au.gov.ga.geodesy.support.mapper.dozer.converter;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import au.gov.ga.geodesy.igssitelog.domain.model.ApproximatePosition;
import au.gov.ga.geodesy.support.mapper.dozer.converter.ApproximatePositionITRFConverter;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType.ApproximatePositionITRF;

public class ApproximatePositionITRFConverterTest {
    ApproximatePositionITRFConverter conv = new ApproximatePositionITRFConverter();
    private double decimalLat;
    private double decimalLong;

    @Test
    public void test01() {
        ApproximatePosition source = buildApproximatePosition();
        ApproximatePositionITRF dest = null;
        dest = (ApproximatePositionITRF) conv.convert(dest, source, ApproximatePositionITRF.class,
                ApproximatePosition.class);

        Assert.assertEquals(source.getItrfX(), (Double) Double.parseDouble(dest.getXCoordinateInMeters()));
        Assert.assertEquals(source.getItrfY(), (Double) Double.parseDouble(dest.getYCoordinateInMeters()));
        Assert.assertEquals(source.getItrfZ(), (Double) Double.parseDouble(dest.getZCoordinateInMeters()));

        Assert.assertEquals(source.getElevationGrs80(), dest.getElevationMEllips());

        Assert.assertEquals(this.decimalLong, dest.getLongitudeEast().doubleValue(), 0.000001);
        Assert.assertEquals(this.decimalLat, dest.getLatitudeNorth().doubleValue(), 0.000001);
    }

    @Test
    public void test02() {
        ApproximatePositionITRF source = buildApproximatePositionITRF();
        ApproximatePosition dest = null;
        dest = (ApproximatePosition) conv.convert(dest, source, ApproximatePositionITRF.class,
                ApproximatePosition.class);

        Assert.assertEquals((Double) Double.parseDouble(source.getXCoordinateInMeters()), dest.getItrfX());
        Assert.assertEquals((Double) Double.parseDouble(source.getYCoordinateInMeters()), dest.getItrfY());
        Assert.assertEquals((Double) Double.parseDouble(source.getZCoordinateInMeters()), dest.getItrfZ());

        Assert.assertEquals(source.getElevationMEllips(), dest.getElevationGrs80());

        Assert.assertEquals(source.getLongitudeEast().doubleValue(), dest.getGrs80().getCoordinate().x, 0.0000001);
        Assert.assertEquals(source.getLatitudeNorth().doubleValue(), dest.getGrs80().getCoordinate().y, 0.0000001);
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
