package au.gov.ga.geodesy.support.mapper.dozer;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import au.gov.ga.geodesy.igssitelog.domain.model.Agency;
import au.gov.ga.geodesy.igssitelog.domain.model.ApproximatePosition;
import au.gov.ga.geodesy.igssitelog.domain.model.Contact;
import au.gov.xml.icsm.geodesyml.v_0_2_2.AgencyPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.ObjectFactory;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteLocationType.ApproximatePositionITRF;
import net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType;
import net.opengis.iso19139.gmd.v_20070417.CIAddressPropertyType;
import net.opengis.iso19139.gmd.v_20070417.CIAddressType;
import net.opengis.iso19139.gmd.v_20070417.CIContactPropertyType;
import net.opengis.iso19139.gmd.v_20070417.CIContactType;
import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;
import net.opengis.iso19139.gmd.v_20070417.CITelephonePropertyType;
import net.opengis.iso19139.gmd.v_20070417.CITelephoneType;
import net.opengis.iso19139.gmd.v_20070417.MDSecurityConstraintsType;

/**
 * Convert: au.gov.ga.geodesy.igssitelog.domain.model.ApproximatePosition <-->
 * au.gov.xml.icsm.geodesyml.v_0_2_2.SiteLocationType.ApproximatePositionITRF
 *
 */
public class ApproximatePositionITRFConverter implements CustomConverter {
    Logger logger = LoggerFactory.getLogger(getClass());
    ObjectFactory geoObjectFactory = new ObjectFactory();
    net.opengis.iso19139.gmd.v_20070417.ObjectFactory gmdObjectFactory = new net.opengis.iso19139.gmd.v_20070417.ObjectFactory();

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof ApproximatePosition) {
            ApproximatePosition sourceType = (ApproximatePosition) source;
            ApproximatePositionITRF dest = null;
            if (destination == null) {
                dest = geoObjectFactory.createSiteLocationTypeApproximatePositionITRF();
            } else {
                dest = (ApproximatePositionITRF) destination;
            }

            // these 4 are easily mappable with a Field Mapping
            if (sourceType.getItrfX() != null) {
                dest.setXCoordinateInMeters(sourceType.getItrfX().toString());
            }
            if (sourceType.getItrfX() != null) {
                dest.setYCoordinateInMeters(sourceType.getItrfY().toString());
            }
            if (sourceType.getItrfX() != null) {
                dest.setZCoordinateInMeters(sourceType.getItrfZ().toString());
            }
            if (sourceType.getItrfX() != null) {
                dest.setElevationMEllips(sourceType.getElevationGrs80());
            }
            // these 2 are the reason for the Converter
            if (sourceType.getGrs80() != null) {
                dest.setLatitudeNorth(new BigDecimal(sourceType.getGrs80().getCoordinate().x));
                dest.setLongitudeEast(new BigDecimal(sourceType.getGrs80().getCoordinate().y));
            }

            return dest;
        } else if (source instanceof ApproximatePositionITRF) {
            ApproximatePositionITRF sourceType = (ApproximatePositionITRF) source;
            ApproximatePosition dest = null;
            if (destination == null) {
                dest = new ApproximatePosition();
            } else {
                dest = (ApproximatePosition) destination;
            }

            Coordinate c = new Coordinate();
            c.setOrdinate(0, sourceType.getLongitudeEast().doubleValue());
            c.setOrdinate(1, sourceType.getLatitudeNorth().doubleValue());
            PrecisionModel pm = new PrecisionModel();

            Point p = null;
            if (dest.getGrs80() == null) {
                p = new Point(c, pm, 0);
            } else {
                p = (Point) dest.getGrs80();
            }

            dest.setGrs80(p);

            dest.setElevationGrs80(sourceType.getElevationMEllips());
            dest.setItrfX(Double.parseDouble(sourceType.getXCoordinateInMeters()));
            dest.setItrfY(Double.parseDouble(sourceType.getYCoordinateInMeters()));
            dest.setItrfZ(Double.parseDouble(sourceType.getZCoordinateInMeters()));
            return dest;
        } else {
            throw new MappingException("Converter TestCustomConverter " + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }

}
