package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteIdentificationType;

/**
 * Reversible mapping between GeodesyML SiteIdentificationType DTO and
 * SiteIdentification site log entity.
 */
@Component
public class SiteIdentificationMapper implements Iso<SiteIdentificationType, SiteIdentification> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public SiteIdentification to(SiteIdentificationType siteIdType) {
        return mapper.map(siteIdType, SiteIdentification.class);
    }

    /**
     * {@inheritDoc}
     */
    public SiteIdentificationType from(SiteIdentification siteId) {
        return mapper.map(siteId, SiteIdentificationType.class);
    }
}
