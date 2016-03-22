package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.geotools.metadata.iso.citation.AddressImpl;
import org.geotools.metadata.iso.citation.ContactImpl;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.metadata.iso.citation.TelephoneImpl;
import org.geotools.util.SimpleInternationalString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.interfaces.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.support.mapper.orika.ResponsiblePartyOrikaMapper;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class ResponsiblePartyMarshallingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private GeodesyMLMarshaller marshaller;

    private ResponsiblePartyOrikaMapper mapper = new ResponsiblePartyOrikaMapper();

    @Test
    public void testMarshalling() throws Exception {
        ResponsiblePartyImpl party = new ResponsiblePartyImpl();

        party.setIndividualName("Lazar Bodor");
        party.setOrganisationName(new SimpleInternationalString("Geoscience Australia"));
        party.setPositionName(new SimpleInternationalString("software developer"));

        ContactImpl contact = new ContactImpl();
        TelephoneImpl telephone = new TelephoneImpl();
        telephone.setVoices(new ArrayList<String>());
        telephone.getVoices().add("04 51989787");
        telephone.getVoices().add("04 75723066");
        contact.setPhone(telephone);
        party.setContactInfo(contact);

        AddressImpl address = new AddressImpl();
        address.setDeliveryPoints(Arrays.asList("12 Olivia Pl"));
        address.setCity(new SimpleInternationalString("Bright"));
        address.setCountry(new SimpleInternationalString("Australia"));
        address.setPostalCode("2789");
        address.setElectronicMailAddresses(Arrays.asList("a@gmail.com", "b@gmail.com"));
        contact.setAddress(address);


        marshaller.marshal(mapper.mapToDto(party), new PrintWriter(System.out));
    }
}
