package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_5.SiteLogType;

/**
 * Reversible mapping between GeodesyML SiteLogType DTO and
 * SiteLog site log entity.
 */
@Component
public class SiteLogMapper implements Iso<SiteLogType, SiteLog> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public SiteLog to(SiteLogType siteLogType) {
        return mapper.map(siteLogType, SiteLog.class);
    }

    /**
     * {@inheritDoc}
     */
    public SiteLogType from(SiteLog siteLog) {
        return mapper.map(siteLog, SiteLogType.class);
    }
}
