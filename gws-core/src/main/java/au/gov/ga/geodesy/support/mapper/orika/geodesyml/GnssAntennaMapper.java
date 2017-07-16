package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.GnssAntennaLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.GnssAntennaType;

/**
 * Reversible mapping between GeodesyML GnssAntennaType DTO and GnssAntennaLogItem site log entity.
 */
@Component
public class GnssAntennaMapper implements Iso<GnssAntennaType, GnssAntennaLogItem> {

    @Autowired
    private GenericMapper mapper;

    @Override
    public GnssAntennaLogItem to(GnssAntennaType antenna) {
        return mapper.map(antenna, GnssAntennaLogItem.class);
    }

    @Override
    public GnssAntennaType from(GnssAntennaLogItem antennaLogItem) {
        return mapper.map(antennaLogItem, GnssAntennaType.class);
    }
}
