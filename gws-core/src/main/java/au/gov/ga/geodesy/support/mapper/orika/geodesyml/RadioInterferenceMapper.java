package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.RadioInterferenceLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.RadioInterferenceType;

/**
 * Reversible mapping between GeodesyML RadioInterferencesType DTO and
 * RadioInterference site log entity.
 */
@Component
public class RadioInterferenceMapper implements Iso<RadioInterferenceType, RadioInterferenceLogItem> {

    @Autowired
    private GenericMapper mapper;


    @Override
    /**
     * {@inheritDoc}
     */
    public RadioInterferenceLogItem to(RadioInterferenceType radioInterferenceType) {
        return mapper.map(radioInterferenceType, RadioInterferenceLogItem.class);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public RadioInterferenceType from(RadioInterferenceLogItem radioInterference) {
        return mapper.map(radioInterference, RadioInterferenceType.class);
    }
}
