package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.namespace.NamespaceContext;

import org.geotools.metadata.iso.citation.AddressImpl;
import org.geotools.metadata.iso.citation.ContactImpl;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.metadata.iso.citation.TelephoneImpl;
import org.geotools.util.SimpleInternationalString;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.opengis.metadata.citation.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.jcabi.matchers.XhtmlMatchers;
import com.jcabi.xml.XPathContext;

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

    private static final NamespaceContext namespaces = new XPathContext()
        .add("gco", "http://www.isotc211.org/2005/gco")
        .add("gmd", "http://www.isotc211.org/2005/gmd");

    @Test
    public void testMarshalling() throws Exception {
        ResponsiblePartyImpl party = new ResponsiblePartyImpl();

        party.setRole(Role.POINT_OF_CONTACT);
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

        StringWriter xml = new StringWriter();
        marshaller.marshal(mapper.mapToDto(party), xml);

        System.out.println(xml.toString());

        // TODO: complete asserts
        MatcherAssert.assertThat(
                XhtmlMatchers.xhtml(xml),
                hasElementWithText("//gmd:CI_ResponsibleParty/gmd:individualName/gco:CharacterString", "Lazar Bodor")
            );
    }

    private <T> Matcher<T> hasElementWithText(String elementXPath, String elementValue) {
        return XhtmlMatchers.hasXPath(elementXPath + "[text()='" + elementValue + "']", namespaces);
    }
}
