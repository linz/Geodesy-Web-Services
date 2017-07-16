package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.OtherInstrumentationLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.OtherInstrumentationType;

/**
 * Reversible mapping between GeodesyML OtherInstrumentationType DTO and
 * OtherInstrumentationLogItem site log entity.
 */
@Component
public class OtherInstrumentationMapper implements Iso<OtherInstrumentationType, OtherInstrumentationLogItem> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public OtherInstrumentationLogItem to(OtherInstrumentationType otherInstrumentationType) {
        return mapper.map(otherInstrumentationType, OtherInstrumentationLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public OtherInstrumentationType from(OtherInstrumentationLogItem otherInstrumentationLogItem) {
        return mapper.map(otherInstrumentationLogItem, OtherInstrumentationType.class);
    }
}
