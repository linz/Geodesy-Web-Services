package au.gov.ga.geodesy.support.mapper.dozer.converter;

import au.gov.ga.geodesy.igssitelog.domain.model.Agency;
import au.gov.ga.geodesy.igssitelog.domain.model.Contact;
import au.gov.xml.icsm.geodesyml.v_0_3.AgencyPropertyType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Test Convert: au.gov.ga.geodesy.igssitelog.domain.model.Agency <-->
 * au.gov.xml.icsm.geodesyml.v_0_3.AgencyPropertyType
 */
public class AgencyAgencyPropertyTypeConverterTest {
    private static final int YES_PRIMARY_SECONDARY = 0;
    private static final int NO_PRIMARY_SECONDARY = 1;
    private static final AgencyAgencyPropertyTypeConverter AAPTC = new AgencyAgencyPropertyTypeConverter();

    @Test
    public void testPrimarySecondary() {
        AgencyPropertyType destination = null;
        Agency source = buildAgency();
        AgencyPropertyType c = (AgencyPropertyType) AAPTC.convert(destination, source, AgencyPropertyType.class, Agency.class);

        assertThat(c.getCIResponsibleParty().getOrganisationName().getCharacterString().getValue(), is(source.getName()));
        assertThat(c.getCIResponsibleParty().getIndividualName().getCharacterString().getValue(), is(source.getPrimaryContact().getName()));
        assertThat(c.getCIResponsibleParty().getContactInfo().getCIContact().getAddress().getCIAddress().getElectronicMailAddress().get
            (0).getCharacterString().getValue(), is(source.getPrimaryContact().getEmail()));
        assertThat(c.getCIResponsibleParty().getContactInfo().getCIContact().getPhone().getCITelephone().getVoice().get(0)
            .getCharacterString().getValue(), is(source.getPrimaryContact().getTelephonePrimary()));
        assertThat(c.getCIResponsibleParty().getContactInfo().getCIContact().getPhone().getCITelephone().getFacsimile().get(0)
            .getCharacterString().getValue(), is(source.getPrimaryContact().getFax()));

        assertThat(c.getCIResponsibleParty().getContactInfo().getCIContact().getAddress().getCIAddress().getDeliveryPoint().get(0)
            .getCharacterString().getValue(), is(source.getMailingAddress()));
    }

    @Test
    public void testNoPrimarySecondary() {
        AgencyPropertyType destination = null;
        Agency source = buildAgency(NO_PRIMARY_SECONDARY);
        AgencyPropertyType c = (AgencyPropertyType) AAPTC.convert(destination, source, AgencyPropertyType.class, Agency.class);

        assertThat(c.getCIResponsibleParty().getOrganisationName().getCharacterString().getValue(), is(source.getName()));
        assertThat(c.getCIResponsibleParty().getIndividualName(), nullValue());
        assertThat(c.getCIResponsibleParty().getContactInfo().getCIContact().getPhone(), nullValue());
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

    private Contact buildContact(String name, String email, String telephonePrimary, String telephoneSecondary, String fax) {
        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setFax(fax);
        contact.setName(name);
        contact.setTelephonePrimary(telephonePrimary);
        contact.setTelephoneSecondary(telephoneSecondary);
        return contact;
    }
}
