package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.MoreInformation;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.MoreInformationType;

/**
 * Reversible mapping between GeodesyML MoreInformationType DTO and
 * MoreInformation site log entity.
 */
@Component
public class MoreInformationMapper implements Iso<MoreInformationType, MoreInformation> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public MoreInformation to(MoreInformationType moreInformationType) {
        return mapper.map(moreInformationType, MoreInformation.class);
    }

    /**
     * {@inheritDoc}
     */
    public MoreInformationType from(MoreInformation moreInformation) {
        return mapper.map(moreInformation, MoreInformationType.class);
    }
}
