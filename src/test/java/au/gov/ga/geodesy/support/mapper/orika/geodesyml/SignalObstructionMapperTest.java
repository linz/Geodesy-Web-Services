package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import au.gov.ga.geodesy.domain.model.sitelog.SignalObstructionLogItem;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.BasePossibleProblemSourcesType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Tests the mapping of a GeodesyML SignalObstructionsPropertyType element
 * to and from an SignalObstructionLogItem domain object.
 */
public class SignalObstructionMapperTest {

    private SignalObstructionMapper mapper = new SignalObstructionMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLTestDataSiteLogReader("METZ-signalObstructionSet"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        BasePossibleProblemSourcesType signalObstructionTypeA =
                siteLog.getSignalObstructionsSet().get(0).getSignalObstructions();

        SignalObstructionLogItem logItem = mapper.to(signalObstructionTypeA);
        assertEquals(logItem.getPossibleProblemSource(), signalObstructionTypeA.getPossibleProblemSources());
        String xmlEffectiveDateFrom = ((TimePeriodType) signalObstructionTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0);
        assertEquals(GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom));
        String xmlEffectiveDateTo = ((TimePeriodType) signalObstructionTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getEndPosition().getValue().get(0);
        assertEquals(GMLDateUtils.dateToString(logItem.getEffectiveDates().getTo(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateTo));
        assertEquals(logItem.getNotes(), signalObstructionTypeA.getNotes());

        BasePossibleProblemSourcesType signalObstructionTypeB = mapper.from(logItem);
        assertEquals(signalObstructionTypeB.getPossibleProblemSources(), logItem.getPossibleProblemSource());
        xmlEffectiveDateFrom = ((TimePeriodType) signalObstructionTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0);
        assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom),
                GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC));
        xmlEffectiveDateTo = ((TimePeriodType) signalObstructionTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getEndPosition().getValue().get(0);
        assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateTo),
                GMLDateUtils.dateToString(logItem.getEffectiveDates().getTo(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC));
        assertEquals(signalObstructionTypeB.getPossibleProblemSources(), logItem.getPossibleProblemSource());

    }

}

