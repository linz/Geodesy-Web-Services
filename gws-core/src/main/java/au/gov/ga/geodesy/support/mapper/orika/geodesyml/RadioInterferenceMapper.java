package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.RadioInterference;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.RadioInterferenceType;

/**
 * Reversible mapping between GeodesyML RadioInterferencesType DTO and
 * RadioInterference site log entity.
 */
@Component
public class RadioInterferenceMapper implements Iso<RadioInterferenceType, RadioInterference> {

    @Autowired
    private GenericMapper mapper;


    @Override
    /**
     * {@inheritDoc}
     */
    public RadioInterference to(RadioInterferenceType radioInterferenceType) {
        return mapper.map(radioInterferenceType, RadioInterference.class);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public RadioInterferenceType from(RadioInterference radioInterference) {
        return mapper.map(radioInterference, RadioInterferenceType.class);
    }
}
