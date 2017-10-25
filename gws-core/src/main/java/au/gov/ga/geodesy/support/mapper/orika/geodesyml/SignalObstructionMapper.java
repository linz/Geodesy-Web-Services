package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.SignalObstructionLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_5.SignalObstructionType;

/**
 * Reversible mapping between GeodesyML BasePossibleProblemSourcesType DTO and
 * SignalObstructionLogItem entity.
 */
@Component
public class SignalObstructionMapper implements Iso<SignalObstructionType, SignalObstructionLogItem> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public SignalObstructionLogItem to(SignalObstructionType signalObstructionType) {
        return mapper.map(signalObstructionType, SignalObstructionLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public SignalObstructionType from(SignalObstructionLogItem signalObstructionLogItem) {
        return mapper.map(signalObstructionLogItem, SignalObstructionType.class);
    }
}
