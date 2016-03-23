package au.gov.ga.geodesy.support.mapper.dozer;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.Agency;
import au.gov.ga.geodesy.igssitelog.domain.model.Contact;
import au.gov.ga.geodesy.support.mapper.dozer.AgencyAgencyPropertyTypeConverter;
import au.gov.xml.icsm.geodesyml.v_0_2_2.AgencyPropertyType;

/**
 * Test Convert: au.gov.ga.geodesy.igssitelog.domain.model.Agency <-->
 * au.gov.xml.icsm.geodesyml.v_0_2_2.AgencyPropertyType
 *
 */
public class AgencyAgencyPropertyTypeConverterTest {
    private static final int YES_PRIMARY_SECONDARY = 0;
    private static final int NO_PRIMARY_SECONDARY = 1;
    AgencyAgencyPropertyTypeConverter aaptc = new AgencyAgencyPropertyTypeConverter();

    @Test
    public void testPrimarySecondary() {
        AgencyPropertyType destination = null;
        Agency source = buildAgency();
        AgencyPropertyType c = (AgencyPropertyType) aaptc.convert(destination, source, AgencyPropertyType.class,
                Agency.class);

        Assert.assertEquals(source.getName(),
                c.getCIResponsibleParty().getOrganisationName().getCharacterString().getValue());
        Assert.assertEquals(source.getPrimaryContact().getName(),
                c.getCIResponsibleParty().getIndividualName().getCharacterString().getValue());
        Assert.assertEquals(source.getPrimaryContact().getEmail(),
                c.getCIResponsibleParty().getContactInfo().getCIContact().getAddress().getCIAddress()
                        .getElectronicMailAddress().get(0).getCharacterString().getValue());
        Assert.assertEquals(source.getPrimaryContact().getTelephonePrimary(), c.getCIResponsibleParty().getContactInfo()
                .getCIContact().getPhone().getCITelephone().getVoice().get(0).getCharacterString().getValue());
        Assert.assertEquals(source.getPrimaryContact().getFax(), c.getCIResponsibleParty().getContactInfo()
                .getCIContact().getPhone().getCITelephone().getFacsimile().get(0).getCharacterString().getValue());

        Assert.assertEquals(source.getMailingAddress(), c.getCIResponsibleParty().getContactInfo().getCIContact()
                .getAddress().getCIAddress().getDeliveryPoint().get(0).getCharacterString().getValue());
    }

    @Test
    public void testNoPrimarySecondary() {
        AgencyPropertyType destination = null;
        Agency source = buildAgency(NO_PRIMARY_SECONDARY);
        AgencyPropertyType c = (AgencyPropertyType) aaptc.convert(destination, source, AgencyPropertyType.class,
                Agency.class);

        Assert.assertEquals(source.getName(),
                c.getCIResponsibleParty().getOrganisationName().getCharacterString().getValue());
        Assert.assertEquals(null, c.getCIResponsibleParty().getIndividualName());
        Assert.assertEquals(null, c.getCIResponsibleParty().getContactInfo().getCIContact().getPhone());
    }

    private Agency buildAgency() {
        return buildAgency(YES_PRIMARY_SECONDARY);
    }

    private Agency buildAgency(int wantPrimarySecondary) {
        Agency agency = new Agency();

        agency.setName("Boating Corporation");
        agency.setMailingAddress("1 Wallaby Way");
        agency.setPreferredAbbreviation("WW");

        if (wantPrimarySecondary == YES_PRIMARY_SECONDARY) {
            Contact primaryContact = buildContact("nemo", "nemo@fnqld.com", "111", "222", "333");
            Contact secondaryContact = buildContact("nemosdad", "nemosdad@fnqld.com", "444", "555", "666");
            agency.setPrimaryContact(primaryContact);
            agency.setSecondaryContact(secondaryContact);
        }

        return agency;
    }

    private Contact buildContact(String name, String email, String telephonePrimary, String telephoneSecondary,
            String fax) {
        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setFax(fax);
        contact.setName(name);
        contact.setTelephonePrimary(telephonePrimary);
        contact.setTelephoneSecondary(telephoneSecondary);
        return contact;
    }
}
