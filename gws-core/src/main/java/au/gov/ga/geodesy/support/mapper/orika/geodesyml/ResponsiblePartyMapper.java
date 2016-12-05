package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.opengis.metadata.citation.ResponsibleParty;

import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.ga.geodesy.support.mapper.orika.ResponsiblePartyOrikaMapper;

import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;

/**
 * Reversible mapping between GeodesyML CIResponsiblePartyType DTO and
 * ResponsibleParty site log entity.
 */
public class ResponsiblePartyMapper implements Iso<CIResponsiblePartyType, ResponsibleParty> {

    private ResponsiblePartyOrikaMapper mapper = new ResponsiblePartyOrikaMapper();

    /**
     * {@inheritDoc}
     */
    public ResponsibleParty to(CIResponsiblePartyType responsiblePartyType) {
        return mapper.mapFromDto(responsiblePartyType);
    }

    /**
     * {@inheritDoc}
     */
    public CIResponsiblePartyType from(ResponsibleParty responsibleParty) {
        return mapper.mapToDto(responsibleParty);
    }
}
