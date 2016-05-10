package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import java.io.FileReader;
import java.io.Reader;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteIdentificationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;

public class SiteIdentificationMapperTest {

    private SiteIdentificationMapper mapper = new SiteIdentificationMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from SiteIdentificationType to SiteIdentification and back
     * to SiteIdentificationType.
     **/
    @Test
    public void testMapping() throws Exception {
        try (Reader mobs = new FileReader(ResourceUtils.getFile("classpath:sitelog/geodesyml/MOBS.xml"))) {
            GeodesyMLType geodesyML = marshaller.unmarshal(mobs, GeodesyMLType.class).getValue();
            SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(geodesyML.getElements(), SiteLogType.class)
                .findFirst()
                .get();

            SiteIdentificationType siteIdTypeA = siteLogType.getSiteIdentification();
            SiteIdentification siteId = mapper.to(siteIdTypeA);

            assertEquals(siteId.getSiteName(), siteIdTypeA.getSiteName());
            assertEquals(siteId.getFourCharacterId(), siteIdTypeA.getFourCharacterID());
            assertEquals(siteId.getMonumentInscription(), siteIdTypeA.getMonumentInscription());
            assertEquals(siteId.getIersDOMESNumber(), siteIdTypeA.getIersDOMESNumber());
            assertEquals(siteId.getCdpNumber(), siteIdTypeA.getCdpNumber());
            assertEquals(siteId.getMonumentDescription(), siteIdTypeA.getMonumentDescription().getValue());
            assertEquals(siteId.getHeightOfMonument(), String.valueOf(siteIdTypeA.getHeightOfTheMonument()));
            assertEquals(siteId.getMonumentFoundation(), siteIdTypeA.getMonumentFoundation());
            assertEquals(siteId.getFoundationDepth(), String.valueOf(siteIdTypeA.getFoundationDepth()));
            assertEquals(siteId.getMarkerDescription(), siteIdTypeA.getMarkerDescription());
            assertEquals(siteId.getDateInstalled(), actualDateFormat().parse(siteIdTypeA.getDateInstalled().getValue().get(0)));
            assertEquals(siteId.getGeologicCharacteristic(), siteIdTypeA.getGeologicCharacteristic().getValue());
            assertEquals(siteId.getBedrockType(), siteIdTypeA.getBedrockType());
            assertEquals(siteId.getBedrockCondition(), siteIdTypeA.getBedrockCondition());
            assertEquals(siteId.getFractureSpacing(), siteIdTypeA.getFractureSpacing());
            assertEquals(siteId.getFaultZonesNearby(), siteIdTypeA.getFaultZonesNearby().getValue());
            assertEquals(siteId.getDistanceActivity(), siteIdTypeA.getDistanceActivity());
            assertEquals(siteId.getNotes(), siteIdTypeA.getNotes());

            SiteIdentificationType siteIdTypeB = mapper.from(siteId);

            assertEquals(siteIdTypeB.getSiteName(), siteId.getSiteName());
            assertEquals(siteIdTypeB.getFourCharacterID(), siteId.getFourCharacterId());
            assertEquals(siteIdTypeB.getMonumentInscription(), siteId.getMonumentInscription());
            assertEquals(siteIdTypeB.getIersDOMESNumber(), siteId.getIersDOMESNumber());
            assertEquals(siteIdTypeB.getCdpNumber(), siteId.getCdpNumber());
            assertEquals(siteIdTypeB.getMonumentDescription().getValue(), siteId.getMonumentDescription());
            assertEquals(siteIdTypeB.getMonumentDescription().getCodeSpace(), "eGeodesy/monumentDescription");
            assertEquals(String.valueOf(siteIdTypeB.getHeightOfTheMonument()), siteId.getHeightOfMonument());
            assertEquals(siteIdTypeB.getMonumentFoundation(), siteId.getMonumentFoundation());
            assertEquals(String.valueOf(siteIdTypeB.getFoundationDepth()), siteId.getFoundationDepth());
            assertEquals(siteIdTypeB.getMarkerDescription(), siteId.getMarkerDescription());
            assertEquals(defaultDateFormat().parse(siteIdTypeB.getDateInstalled().getValue().get(0)), siteId.getDateInstalled());
            assertEquals(siteIdTypeB.getGeologicCharacteristic().getValue(), siteId.getGeologicCharacteristic());
            assertEquals(siteIdTypeB.getGeologicCharacteristic().getCodeSpace(), "eGeodesy/geologicCharacteristic");
            assertEquals(siteIdTypeB.getBedrockType(), siteId.getBedrockType());
            assertEquals(siteIdTypeB.getBedrockCondition(), siteId.getBedrockCondition());
            assertEquals(siteIdTypeB.getFractureSpacing(), siteId.getFractureSpacing());
            assertEquals(siteIdTypeB.getFaultZonesNearby().getValue(), siteId.getFaultZonesNearby());
            assertEquals(siteIdTypeB.getFaultZonesNearby().getCodeSpace(), "eGeodesy/faultZonesNearby");
            assertEquals(siteIdTypeB.getDistanceActivity(), siteId.getDistanceActivity());
            assertEquals(siteIdTypeB.getNotes(), siteId.getNotes());
        }
    }

    private FastDateFormat actualDateFormat() {
        return FastDateFormat.getInstance("yyyy-MM-ddX", TimeZone.getTimeZone("UTC"));
    }

    private FastDateFormat defaultDateFormat() {
        return FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("UTC"));
    }
}
