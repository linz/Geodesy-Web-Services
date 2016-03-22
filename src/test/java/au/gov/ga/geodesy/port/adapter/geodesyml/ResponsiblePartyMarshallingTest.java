package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.io.PrintWriter;

import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
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

        marshaller.marshal(mapper.mapToDto(party), new PrintWriter(System.out));
    }
}
