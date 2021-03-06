package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.FrequencyStandardLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.spring.UnitTest;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_4.FrequencyStandardType;
import au.gov.xml.icsm.geodesyml.v_0_4.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLogType;

import net.opengis.gml.v_3_2_1.TimePeriodType;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.springframework.beans.factory.annotation.Autowired;

public class FrequencyStandardMapperTest extends UnitTest {

    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Autowired
    private FrequencyStandardMapper mapper;

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("MOBS"), GeodesyMLType.class)
            .getValue();

        SiteLogType siteLog =
            GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
            .findFirst()
            .get();

        FrequencyStandardType frequencyStandardTypeA = siteLog.getFrequencyStandards().get(0).getFrequencyStandard();
        FrequencyStandardLogItem logItem = mapper.to(frequencyStandardTypeA);

        assertThat(logItem.getType(), equalTo(frequencyStandardTypeA.getStandardType().getValue()));
        assertThat(logItem.getInputFrequency(), equalTo(String.valueOf(frequencyStandardTypeA.getInputFrequency())));
        assertThat(logItem.getNotes(), equalTo(frequencyStandardTypeA.getNotes()));
        
        assertThat(logItem.getEffectiveDates().getFrom(), equalTo(GMLDateUtils.stringToDateMultiParsers(
                ((TimePeriodType) frequencyStandardTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0))
            ));

        FrequencyStandardType frequencyStandardTypeB = mapper.from(logItem);

        assertThat(frequencyStandardTypeB.getStandardType().getValue(), equalTo(logItem.getType()));
        assertThat(frequencyStandardTypeB.getStandardType().getCodeSpace(), equalTo("eGeodesy/frequencyStandardType"));
        assertThat(frequencyStandardTypeB.getNotes(), equalTo(logItem.getNotes()));

        assertThat(GMLDateUtils.stringToDateMultiParsers(
                ((TimePeriodType) frequencyStandardTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0)), equalTo(logItem.getEffectiveDates().getFrom()));
    }

}
