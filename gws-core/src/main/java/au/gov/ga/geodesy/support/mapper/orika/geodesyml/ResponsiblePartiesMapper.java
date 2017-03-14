package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.opengis.metadata.citation.ResponsibleParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.domain.model.ContactType;
import au.gov.ga.geodesy.domain.model.ContactTypeRepository;
import au.gov.ga.geodesy.domain.model.SiteResponsibleParty;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.support.mapper.orika.ResponsiblePartyOrikaMapper;
import au.gov.xml.icsm.geodesyml.v_0_4.AgencyPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLogType;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;

/**
 * Map GeodesyML SiteLogType DTO to SiteLog domain entity.
 */
@Configurable(dependencyCheck = true)
public class ResponsiblePartiesMapper extends CustomMapper<SiteLogType, SiteLog> {

    @Autowired
    private ContactTypeRepository contactTypes;

    private ResponsiblePartyOrikaMapper partyMapper = new ResponsiblePartyOrikaMapper();

    @Override
    public void mapAtoB(SiteLogType siteLogType, SiteLog siteLog, MappingContext ctx) {
        for (AgencyPropertyType agencyProperty : siteLogType.getSiteContacts()) {
            ResponsibleParty party = this.partyMapper.mapFromDto(agencyProperty.getCIResponsibleParty());
            SiteResponsibleParty siteParty = new SiteResponsibleParty(
                contactTypes.siteContact().getId(),
                party);
            siteLog.getResponsibleParties().add(siteParty);
        }

        ResponsibleParty party = this.partyMapper.mapFromDto(siteLogType.getSiteMetadataCustodian().getCIResponsibleParty());
        SiteResponsibleParty siteParty = new SiteResponsibleParty(
            contactTypes.siteMetadataCustodian().getId(),
            party);
        siteLog.getResponsibleParties().add(siteParty);
    }

    @Override
    public void mapBtoA(SiteLog siteLog, SiteLogType siteLogType, MappingContext ctx) {
        for (SiteResponsibleParty siteParty : siteLog.getResponsibleParties()) {
            CIResponsiblePartyType partyType = this.partyMapper.mapToDto(siteParty.getParty());
            AgencyPropertyType agencyProperty = new AgencyPropertyType();
            agencyProperty.setCIResponsibleParty(partyType);

            switch (contactTypes.findOne(siteParty.getContactTypeId()).getCode()) {
                case ContactType.SITE_CONTACT:
                    siteLogType.getSiteContacts().add(agencyProperty);
                    break;
                case ContactType.SITE_METADATA_CUSTODIAN:
                    siteLogType.setSiteMetadataCustodian(agencyProperty);
                    break;
            }
        }
    }
}
