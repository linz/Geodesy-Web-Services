package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.MultipathSourceLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.MultipathSourceType;

/**
 * Reversible mapping between GeodesyML BasePossibleProblemSourcesType DTO and
 * MultipathSourceLogItem entity.
 */
@Component
public class MultipathSourceMapper implements Iso<MultipathSourceType, MultipathSourceLogItem> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public MultipathSourceLogItem to(MultipathSourceType multipathSourceType) {
        return mapper.map(multipathSourceType, MultipathSourceLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public MultipathSourceType from(MultipathSourceLogItem multipathSourceLogItem) {
        return mapper.map(multipathSourceLogItem, MultipathSourceType.class);
    }
}
