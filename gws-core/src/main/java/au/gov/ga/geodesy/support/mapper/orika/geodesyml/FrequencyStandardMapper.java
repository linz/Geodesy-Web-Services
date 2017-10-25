package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.FrequencyStandardLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_5.FrequencyStandardType;

/**
 * Reversible mapping between GeodesyML FrequencyStandardType DTO and
 * FreqeyncyStandard site log entity.
 */
@Component
public class FrequencyStandardMapper implements Iso<FrequencyStandardType, FrequencyStandardLogItem> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public FrequencyStandardLogItem to(FrequencyStandardType frequencyStandard) {
        return mapper.map(frequencyStandard, FrequencyStandardLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public FrequencyStandardType from(FrequencyStandardLogItem frequencyStandardLogItem) {
        return mapper.map(frequencyStandardLogItem, FrequencyStandardType.class);
    }
}
