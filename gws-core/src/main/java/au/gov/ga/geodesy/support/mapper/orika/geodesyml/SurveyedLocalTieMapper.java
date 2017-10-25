package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.SurveyedLocalTieLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_5.SurveyedLocalTieType;

/**
 * Reversible mapping between GeodesyML SurveyedLocalTiesType DTO and
 * SurveyedLocalTie site log entity.
 */
@Component
public class SurveyedLocalTieMapper implements Iso<SurveyedLocalTieType, SurveyedLocalTieLogItem> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public SurveyedLocalTieLogItem to(SurveyedLocalTieType SurveyedLocalTieType) {
        return mapper.map(SurveyedLocalTieType, SurveyedLocalTieLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public SurveyedLocalTieType from(SurveyedLocalTieLogItem SurveyedLocalTie) {
        return mapper.map(SurveyedLocalTie, SurveyedLocalTieType.class);
    }
}
