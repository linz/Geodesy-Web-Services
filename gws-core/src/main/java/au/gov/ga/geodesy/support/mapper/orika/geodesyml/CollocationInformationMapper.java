package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.CollocationInformationLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.CollocationInformationType;

/**
 * Reversible mapping between GeodesyML CollocationInformationType DTO and
 * CollocationInformation site log entity.
 */
@Component
public class CollocationInformationMapper implements Iso<CollocationInformationType, CollocationInformationLogItem> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public CollocationInformationLogItem to(CollocationInformationType collocationInformationType) {
        return mapper.map(collocationInformationType, CollocationInformationLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public CollocationInformationType from(CollocationInformationLogItem collocationInformation) {
        return mapper.map(collocationInformation, CollocationInformationType.class);
    }
}
