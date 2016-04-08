package au.gov.ga.geodesy.support.mapper.dozer.populator;

import java.util.Arrays;
import java.util.stream.Collectors;

import au.gov.xml.icsm.geodesyml.v_0_3.GnssAntennaType;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * The receivers have required elements that don't all exist in the SOPAC Sitelog xml. This fills them in.
 * Note that it isn't possible to do every element as they will have complex element hierarchies.
 * 
 * @author brookes
 *
 */
public class GeodesyMLDozerEventListener_GnssAntennaType extends GeodesyMLElementPopulator<GnssAntennaType> {
    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    void checkAllRequiredElementsPopulated(GnssAntennaType gnssAntennaType) {
        // This can be blank when receiver hasn't been removed. Some other logic in the project
        // removes empty elements from the Sopac SiteLog before it gets to this translator
        checkElementPopulated(gnssAntennaType, "antennaCableType", getEmptyString());
        checkElementPopulated(gnssAntennaType, "radomeSerialNumber", getEmptyString());
        checkElementPopulated(gnssAntennaType, "dateRemoved", getEmptyTimePositionType());
    }

    private TimePositionType getEmptyTimePositionType() {
        TimePositionType tpt = new TimePositionType();
        tpt.setValue(Arrays.stream(new String[] {""}).collect(Collectors.toList()));
        return tpt;
    }

    private String getEmptyString() {
        return "";
    }
}
