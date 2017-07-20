package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.util.List;

import org.opengis.metadata.citation.ResponsibleParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

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
@Component
public class ResponsiblePartiesMapper extends CustomMapper<SiteLogType, SiteLog> {

    @Autowired
    private ContactTypeRepository contactTypes;

    private ResponsiblePartyOrikaMapper partyMapper = new ResponsiblePartyOrikaMapper();

    private void addSiteResponsibleParty(List<SiteResponsibleParty> siteResponsibleParties, AgencyPropertyType agencyProperty, ContactType contactType) {
        if (agencyProperty == null || agencyProperty.getCIResponsibleParty() == null) {
            return;
        } else {
            ResponsibleParty responsibleParty = this.partyMapper.mapFromDto(agencyProperty.getCIResponsibleParty());
            siteResponsibleParties.add(new SiteResponsibleParty(contactType.getId(), responsibleParty));
        }
    }

    @Override
    public void mapAtoB(SiteLogType siteLogType, SiteLog siteLog, MappingContext ctx) {
        List<SiteResponsibleParty> siteResponsibleParties = siteLog.getResponsibleParties();

        addSiteResponsibleParty(siteResponsibleParties, siteLogType.getSiteOwner(), contactTypes.siteOwner());

        for (AgencyPropertyType agencyProperty : siteLogType.getSiteContacts()) {
            addSiteResponsibleParty(siteResponsibleParties, agencyProperty, contactTypes.siteContact());

        }
        addSiteResponsibleParty(siteResponsibleParties, siteLogType.getSiteMetadataCustodian(), contactTypes.siteMetadataCustodian());

        for (AgencyPropertyType agencyProperty : siteLogType.getSiteDataCenters()) {
            addSiteResponsibleParty(siteResponsibleParties, agencyProperty, contactTypes.siteDataCenter());
        }
        addSiteResponsibleParty(siteResponsibleParties, siteLogType.getSiteDataSource(), contactTypes.siteDataSource());
    }

    @Override
    public void mapBtoA(SiteLog siteLog, SiteLogType siteLogType, MappingContext ctx) {
        for (SiteResponsibleParty siteParty : siteLog.getResponsibleParties()) {
            CIResponsiblePartyType partyType = this.partyMapper.mapToDto(siteParty.getParty());
            AgencyPropertyType agencyProperty = new AgencyPropertyType();
            agencyProperty.setCIResponsibleParty(partyType);

            switch (contactTypes.findOne(siteParty.getContactTypeId()).getCode()) {
                case ContactType.SITE_OWNER:
                    siteLogType.setSiteOwner(agencyProperty);
                    break;
                case ContactType.SITE_CONTACT:
                    siteLogType.getSiteContacts().add(agencyProperty);
                    break;
                case ContactType.SITE_METADATA_CUSTODIAN:
                    siteLogType.setSiteMetadataCustodian(agencyProperty);
                    break;
                case ContactType.SITE_DATA_CENTER:
                    siteLogType.getSiteDataCenters().add(agencyProperty);
                    break;
                case ContactType.SITE_DATA_SOURCE:
                    siteLogType.setSiteDataSource(agencyProperty);
                    break;
            }
        }
    }
}
