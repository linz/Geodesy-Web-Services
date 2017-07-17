package au.gov.ga.geodesy.port.adapter.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;

import org.geotools.metadata.iso.citation.AddressImpl;
import org.geotools.metadata.iso.citation.ContactImpl;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.metadata.iso.citation.TelephoneImpl;
import org.geotools.util.SimpleInternationalString;
import org.hamcrest.Matcher;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.citation.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.jcabi.matchers.XhtmlMatchers;
import com.jcabi.xml.XPathContext;

import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.mapper.orika.ResponsiblePartyOrikaMapper;
import au.gov.ga.geodesy.support.spring.IntegrationTest;
import au.gov.xml.icsm.geodesyml.v_0_4.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLogType;

import net.opengis.iso19139.gco.v_20070417.CodeListValueType;
import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;


public class ResponsiblePartyMarshallingITest extends IntegrationTest {

    @Autowired
    private GeodesyMLMarshaller marshaller;

    private ResponsiblePartyOrikaMapper mapper = new ResponsiblePartyOrikaMapper();

    private static final NamespaceContext namespaces = new XPathContext()
        .add("gco", "http://www.isotc211.org/2005/gco")
        .add("gmd", "http://www.isotc211.org/2005/gmd");

    @Test
    public void testUnmarshalling() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("MOBS"), GeodesyMLType.class)
            .getValue();

        SiteLogType siteLog =
            GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
            .findFirst()
            .get();

        // TODO: check all fields
        CodeListValueType roleDto = siteLog.getSiteMetadataCustodian().getCIResponsibleParty().getRole().getCIRoleCode();
        Role role = mapper.mapFromDto(siteLog.getSiteMetadataCustodian().getCIResponsibleParty()).getRole();
        assertThat(roleDto.getCodeListValue(), is("pointOfContact"));
        assertThat(role, is(Role.POINT_OF_CONTACT));
    }

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

        OnLineResourceImpl onlineResource = new OnLineResourceImpl();
        onlineResource.setLinkage(new URI("http://google.com"));
        contact.setOnLineResource(onlineResource);

        StringWriter buffer = new StringWriter();
        CIResponsiblePartyType partyDto = mapper.mapToDto(party);
        marshaller.marshal(partyDto, buffer);
        Source xml = XhtmlMatchers.xhtml(buffer);

        // TODO: complete asserts

        assertThat(xml, hasElementWithText("//gmd:CI_ResponsibleParty/gmd:individualName/gco:CharacterString", "Lazar Bodor"));

        assertThat(xml, hasContactElementWithText("gmd:onlineResource/gmd:CI_OnlineResource/gmd:linkage/gmd:URL", "http://google.com"));
        assertThat(xml, hasContactElementWithText("gmd:onlineResource/gmd:CI_OnlineResource/gmd:protocol/gco:CharacterString", "http"));

        ResponsibleParty p = mapper.mapFromDto(partyDto);
        assertThat(p.getContactInfo().getOnLineResource().getLinkage().toURL().toExternalForm(), is("http://google.com"));
        assertThat(p.getContactInfo().getOnLineResource().getProtocol(), is("http"));
    }

    private <T> Matcher<T> hasElementWithText(String elementXPath, String elementValue) {
        return XhtmlMatchers.hasXPath(elementXPath + "[text()='" + elementValue + "']", namespaces);
    }

    private <T> Matcher<T> hasContactElementWithText(String contactElementXPath, String elementValue) {
        String elementXPath = "//gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/" + contactElementXPath;
        return hasElementWithText(elementXPath, elementValue);
    }
}
