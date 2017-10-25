package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.LocalEpisodicEffectLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.spring.UnitTest;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_5.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_5.LocalEpisodicEffectType;
import au.gov.xml.icsm.geodesyml.v_0_5.SiteLogType;

import net.opengis.gml.v_3_2_1.TimePeriodType;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the mapping of a GeodesyML LocalEpisodicEffectPropertyType element to
 * and from an LocalEpisodicEffectLogItem domain object.
 */
public class LocalEpisodicEffectMapperTest extends UnitTest {

    @Autowired
	private LocalEpisodicEffectMapper mapper;

	private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

	@Test
	public void testMapping() throws Exception {

		GeodesyMLType mobs = marshaller
				.unmarshal(TestResources.customGeodesyMLSiteLogReader("WGTN-localEpisodicEffects"), GeodesyMLType.class)
				.getValue();

		SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
				.findFirst().get();

		LocalEpisodicEffectType localEpisodicEffectTypeA = siteLog.getLocalEpisodicEffects().get(0)
				.getLocalEpisodicEffect();

		LocalEpisodicEffectLogItem logItem = mapper.to(localEpisodicEffectTypeA);
		assertThat(logItem.getEvent(), equalTo(localEpisodicEffectTypeA.getEvent()));
		String xmlEffectiveDateFrom = ((TimePeriodType) localEpisodicEffectTypeA.getValidTime()
				.getAbstractTimePrimitive().getValue()).getBeginPosition().getValue().get(0);
		assertThat(
				GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(),
						GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
				equalTo(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom)));
		String xmlEffectiveDateTo = ((TimePeriodType) localEpisodicEffectTypeA.getValidTime().getAbstractTimePrimitive()
				.getValue()).getEndPosition().getValue().get(0);
		assertThat(
				GMLDateUtils.dateToString(logItem.getEffectiveDates().getTo(),
						GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
				equalTo(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateTo)));

		LocalEpisodicEffectType localEpisodicEffectTypeB = mapper.from(logItem);
		assertThat(localEpisodicEffectTypeB.getEvent(), equalTo(logItem.getEvent()));
		xmlEffectiveDateFrom = ((TimePeriodType) localEpisodicEffectTypeB.getValidTime().getAbstractTimePrimitive()
				.getValue()).getBeginPosition().getValue().get(0);
		assertThat(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom),
				equalTo(GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(),
						GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC)));
		xmlEffectiveDateTo = ((TimePeriodType) localEpisodicEffectTypeB.getValidTime().getAbstractTimePrimitive()
				.getValue()).getEndPosition().getValue().get(0);
		assertThat(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateTo), equalTo(GMLDateUtils
				.dateToString(logItem.getEffectiveDates().getTo(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC)));
	}

}
