package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.io.FileReader;
import java.io.Reader;

import org.springframework.util.ResourceUtils;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;

public class SiteLogMapperTest {

    private SiteLogMapper mapper = new SiteLogMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from SiteLogType to SiteLog and back
     * to SiteLogType.
     **/
    @Test
    public void testMapping() throws Exception {
        try (Reader mobs = new FileReader(ResourceUtils.getFile("classpath:sitelog/geodesyml/MOBS.xml"))) {
            GeodesyMLType geodesyML = marshaller.unmarshal(mobs, GeodesyMLType.class).getValue();
            SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(geodesyML.getElements(), SiteLogType.class)
                .findFirst()
                .get();

            SiteLog siteLog = mapper.to(siteLogType);
            assertEquals(siteLog.getSiteIdentification().getSiteName(), siteLogType.getSiteIdentification().getSiteName());
            assertEquals(siteLog.getSiteLocation().getTectonicPlate(), siteLogType.getSiteLocation().getTectonicPlate().getValue());
        }
    }
}

