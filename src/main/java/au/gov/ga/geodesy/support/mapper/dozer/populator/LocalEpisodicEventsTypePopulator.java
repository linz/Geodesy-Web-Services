package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.LocalEpisodicEventsType;

public class LocalEpisodicEventsTypePopulator extends GeodesyMLElementPopulator<LocalEpisodicEventsType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    @Override
    public void checkAllRequiredElementsPopulated(LocalEpisodicEventsType localEpisodicEventsType) {
        checkElementPopulated(localEpisodicEventsType, "event", GMLMiscTools.getEmptyString());
        checkElementPopulated(localEpisodicEventsType, "validTime",
                TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(GMLDateUtils.buildStartOfTime()));
    }
}
