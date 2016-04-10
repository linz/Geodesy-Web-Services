package au.gov.ga.geodesy.support.mapper.dozer.populator;

import java.util.ArrayList;
import java.util.List;

import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * The receivers have required elements that don't all exist in the SOPAC Sitelog xml. This fills them in.
 * Note that it isn't possible to do every element as they will have complex element hierarchies.
 * 
 * @author brookes
 *
 */
public class GnssReceiverTypePopulator extends GeodesyMLElementPopulator<GnssReceiverType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    @Override
    public void checkAllRequiredElementsPopulated(GnssReceiverType gnssReceiverType) {
        // This element can be blank when receiver hasn't been removed. Some other logic in the project
        // removes empty elements from the Sopac SiteLog before it gets to this translator
        checkElementPopulated(gnssReceiverType, "dateRemoved", getBlankTimePositionType());
    }

    private TimePositionType getBlankTimePositionType() {
        TimePositionType tpt = new TimePositionType();
        tpt.setValue((List<String>) new ArrayList<String>());
        return tpt;
    }

}
