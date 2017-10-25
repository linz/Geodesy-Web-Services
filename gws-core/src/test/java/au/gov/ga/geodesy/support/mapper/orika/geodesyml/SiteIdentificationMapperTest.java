package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.spring.UnitTest;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_5.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_5.SiteIdentificationType;
import au.gov.xml.icsm.geodesyml.v_0_5.SiteLogType;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.io.Reader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SiteIdentificationMapperTest extends UnitTest {

    @Autowired
    private SiteIdentificationMapper mapper;

    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from SiteIdentificationType to SiteIdentification and back
     * to SiteIdentificationType.
     */
    @Test
    public void testMapping() throws Exception {
        try (Reader mobs = TestResources.customGeodesyMLSiteLogReader("MOBS")) {
            GeodesyMLType geodesyML = marshaller.unmarshal(mobs, GeodesyMLType.class).getValue();
            SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(geodesyML.getElements(), SiteLogType.class)
                .findFirst()
                .get();

            SiteIdentificationType siteIdTypeA = siteLogType.getSiteIdentification();
            SiteIdentification siteId = mapper.to(siteIdTypeA);

            assertThat(siteId.getSiteName(), equalTo(siteIdTypeA.getSiteName()));
            assertThat(siteId.getFourCharacterId(), equalTo(siteIdTypeA.getFourCharacterID()));
            assertThat(siteId.getMonumentInscription(), equalTo(siteIdTypeA.getMonumentInscription()));
            assertThat(siteId.getIersDOMESNumber(), equalTo(siteIdTypeA.getIersDOMESNumber()));
            assertThat(siteId.getCdpNumber(), equalTo(siteIdTypeA.getCdpNumber()));
            assertThat(siteId.getMonumentDescription(), equalTo(siteIdTypeA.getMonumentDescription().getValue()));
            assertThat(siteId.getHeightOfMonument(), equalTo(String.valueOf(siteIdTypeA.getHeightOfTheMonument())));
            assertThat(siteId.getMonumentFoundation(), equalTo(siteIdTypeA.getMonumentFoundation()));
            assertThat(siteId.getFoundationDepth(), equalTo(String.valueOf(siteIdTypeA.getFoundationDepth())));
            assertThat(siteId.getMarkerDescription(), equalTo(siteIdTypeA.getMarkerDescription()));
            assertThat(siteId.getDateInstalled(), equalTo(GMLDateUtils.stringToDateMultiParsers(siteIdTypeA.getDateInstalled().getValue().get(0))));
            assertThat(siteId.getGeologicCharacteristic(), equalTo(siteIdTypeA.getGeologicCharacteristic().getValue()));
            assertThat(siteId.getBedrockType(), equalTo(siteIdTypeA.getBedrockType()));
            assertThat(siteId.getBedrockCondition(), equalTo(siteIdTypeA.getBedrockCondition()));
            assertThat(siteId.getFractureSpacing(), equalTo(siteIdTypeA.getFractureSpacing()));
            assertThat(siteId.getFaultZonesNearby(), equalTo(siteIdTypeA.getFaultZonesNearby().getValue()));
            assertThat(siteId.getDistanceActivity(), equalTo(siteIdTypeA.getDistanceActivity()));
            assertThat(siteId.getNotes(), equalTo(siteIdTypeA.getNotes()));

            SiteIdentificationType siteIdTypeB = mapper.from(siteId);

            assertThat(siteIdTypeB.getSiteName(), equalTo(siteId.getSiteName()));
            assertThat(siteIdTypeB.getFourCharacterID(),equalTo( siteId.getFourCharacterId()));
            assertThat(siteIdTypeB.getMonumentInscription(), equalTo(siteId.getMonumentInscription()));
            assertThat(siteIdTypeB.getIersDOMESNumber(), equalTo(siteId.getIersDOMESNumber()));
            assertThat(siteIdTypeB.getCdpNumber(), equalTo(siteId.getCdpNumber()));
            assertThat(siteIdTypeB.getMonumentDescription().getValue(), equalTo(siteId.getMonumentDescription()));
            assertThat(siteIdTypeB.getMonumentDescription().getCodeSpace(), equalTo("eGeodesy/monumentDescription"));
            assertThat(String.valueOf(siteIdTypeB.getHeightOfTheMonument()), equalTo(siteId.getHeightOfMonument()));
            assertThat(siteIdTypeB.getMonumentFoundation(), equalTo(siteId.getMonumentFoundation()));
            assertThat(String.valueOf(siteIdTypeB.getFoundationDepth()), equalTo(siteId.getFoundationDepth()));
            assertThat(siteIdTypeB.getMarkerDescription(), equalTo(siteId.getMarkerDescription()));
            assertThat(GMLDateUtils.stringToDateMultiParsers(siteIdTypeB.getDateInstalled().getValue().get(0)), equalTo(siteId.getDateInstalled()));
            assertThat(siteIdTypeB.getGeologicCharacteristic().getValue(), equalTo(siteId.getGeologicCharacteristic()));
            assertThat(siteIdTypeB.getGeologicCharacteristic().getCodeSpace(), equalTo("eGeodesy/geologicCharacteristic"));
            assertThat(siteIdTypeB.getBedrockType(), equalTo(siteId.getBedrockType()));
            assertThat(siteIdTypeB.getBedrockCondition(), equalTo(siteId.getBedrockCondition()));
            assertThat(siteIdTypeB.getFractureSpacing(), equalTo(siteId.getFractureSpacing()));
            assertThat(siteIdTypeB.getFaultZonesNearby().getValue(), equalTo(siteId.getFaultZonesNearby()));
            assertThat(siteIdTypeB.getFaultZonesNearby().getCodeSpace(), equalTo("eGeodesy/faultZonesNearby"));
            assertThat(siteIdTypeB.getDistanceActivity(), equalTo(siteId.getDistanceActivity()));
            assertThat(siteIdTypeB.getNotes(), equalTo(siteId.getNotes()));
        }
    }
}
