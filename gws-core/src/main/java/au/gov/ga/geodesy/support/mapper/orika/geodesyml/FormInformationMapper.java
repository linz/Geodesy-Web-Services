package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.FormInformation;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_5.FormInformationType;

/**
 * Reversible mapping between GeodesyML FormInformationType DTO and
 * FormInformation site log entity.
 */
@Component
public class FormInformationMapper implements Iso<FormInformationType, FormInformation> {

    @Autowired
    private GenericMapper mapper;

    /**
     * {@inheritDoc}
     */
    public FormInformation to(FormInformationType formInfoType) {
        return mapper.map(formInfoType, FormInformation.class);
    }

    /**
     * {@inheritDoc}
     */
    public FormInformationType from(FormInformation formInfo) {
        return mapper.map(formInfo, FormInformationType.class);
    }
}
