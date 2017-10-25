package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.RadioInterferenceLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.spring.UnitTest;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_5.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_5.RadioInterferenceType;
import au.gov.xml.icsm.geodesyml.v_0_5.SiteLogType;

import net.opengis.gml.v_3_2_1.TimeIndeterminateValueType;
import net.opengis.gml.v_3_2_1.TimePeriodType;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * Tests the mapping of a GeodesyML RadioInterference element to and from a RadioInterference domain object.
 */
public class RadioInterferenceMapperTest extends UnitTest {

    @Autowired
    private RadioInterferenceMapper mapper;

    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();
    private DateTimeFormatter format = dateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    /**
     * Test when the RadioInterference.effectiveDates has a from AND to date
     *
     * @throws Exception
     */
    @Test
    public void testMappingWithoutToDate() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("IRKJ_RadioInterference_NoToDate"),
            GeodesyMLType.class).getValue();

        SiteLogType logItem = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class).findFirst().get();

        RadioInterferenceType radioInterferenceDTO = logItem.getRadioInterferences().get(0).getRadioInterference();

        RadioInterferenceLogItem radioInterferenceEntity = mapper.to(radioInterferenceDTO);

        assertCommonFields(radioInterferenceDTO, radioInterferenceEntity, MappingDirection.FROM_DTO_TO_ENTITY);

        // IRKJ_RadioInterference_NoToDate has no end/to date
        assertThat(((TimePeriodType) radioInterferenceDTO.getValidTime().getAbstractTimePrimitive().getValue()).getEndPosition()
            .getValue().size(), is(0));
        assertThat(radioInterferenceEntity.getEffectiveDates().getTo(), nullValue());

        // <----> Test after mapping back the other way
        RadioInterferenceType radioInterferenceDTO2 = mapper.from(radioInterferenceEntity);

        assertCommonFields(radioInterferenceDTO, radioInterferenceEntity, MappingDirection.FROM_ENTITY_TO_DTO);

        // IRKJ_RadioInterference_NoToDate has indeterminate end date 
        TimePositionType endDate = ((TimePeriodType) radioInterferenceDTO2.getValidTime().getAbstractTimePrimitive().getValue()).getEndPosition();
        assertThat(endDate.getIndeterminatePosition(), is(TimeIndeterminateValueType.UNKNOWN));
        assertThat(endDate.getValue().size(), is(0));
    }

    /**
     * Test when the RadioInterference.effectiveDates has a from AND to date
     *
     * @throws Exception
     */
    @Test
    public void testMappingWithToDate() throws Exception {
        DateTimeFormatter format = dateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("IRKJ_RadioInterference_WithToDate"),
            GeodesyMLType.class).getValue();

        SiteLogType logItem = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class).findFirst().get();

        RadioInterferenceType radioInterferenceDTO = logItem.getRadioInterferences().get(0).getRadioInterference();

        RadioInterferenceLogItem radioInterferenceEntity = mapper.to(radioInterferenceDTO);

        assertCommonFields(radioInterferenceDTO, radioInterferenceEntity, MappingDirection.FROM_DTO_TO_ENTITY);

        // IRKJ_RadioInterference_WithToDate has an end/to date
        String fromDateFormatted = format(getTimePeriodType(radioInterferenceDTO).getBeginPosition());
        assertThat(format.format(radioInterferenceEntity.getEffectiveDates().getFrom()), is(fromDateFormatted));

        // <----> Test after mapping back the other way
        RadioInterferenceType radioInterferenceDTO2 = mapper.from(radioInterferenceEntity);

        assertCommonFields(radioInterferenceDTO, radioInterferenceEntity, MappingDirection.FROM_ENTITY_TO_DTO);

        // IRKJ_RadioInterference_WithToDate has an end/to date

        String actualToDateFormatted = format(getTimePeriodType(radioInterferenceDTO).getEndPosition());
        String expectedToDateFormatted = format.format(radioInterferenceEntity.getEffectiveDates().getTo());
        assertThat(actualToDateFormatted, is(expectedToDateFormatted));
    }

    private TimePeriodType getTimePeriodType(RadioInterferenceType value) {
        return ((TimePeriodType) value.getValidTime().getAbstractTimePrimitive().getValue());
    }

    private void assertCommonFields(RadioInterferenceType radioInterferenceDTO, RadioInterferenceLogItem radioInterferenceEntity,
                                    MappingDirection mappingDirection) {
        if (mappingDirection == MappingDirection.FROM_DTO_TO_ENTITY) {
            assertThat(radioInterferenceEntity.getNotes(), is(radioInterferenceDTO.getNotes()));
            assertThat(radioInterferenceEntity.getObservedDegradation(), is(radioInterferenceDTO.getObservedDegradation()));
            assertThat(radioInterferenceEntity.getPossibleProblemSource(), is(radioInterferenceDTO.getPossibleProblemSource()));

            // From Date
            String expectedFromDateFormatted = format(getTimePeriodType(radioInterferenceDTO).getBeginPosition());
            String actualFromDateFormatted = format.format(radioInterferenceEntity.getEffectiveDates().getFrom());
            assertThat(actualFromDateFormatted, is(expectedFromDateFormatted));
        } else {
            assertThat(radioInterferenceDTO.getNotes(), is(radioInterferenceEntity.getNotes()));
            assertThat(radioInterferenceDTO.getObservedDegradation(), is(radioInterferenceEntity.getObservedDegradation()));
            assertThat(radioInterferenceDTO.getPossibleProblemSource(), is(radioInterferenceEntity.getPossibleProblemSource()));

            // From Date
            String expectedFromDateFormatted = format.format(radioInterferenceEntity.getEffectiveDates().getFrom());
            String actualFromDateString = format(getTimePeriodType(radioInterferenceDTO).getBeginPosition());

            String actualFromDateFormatted = GMLDateUtils.stringToDateToString(actualFromDateString, format);
            assertThat(expectedFromDateFormatted, is(actualFromDateFormatted));
        }
    }

    private String format(TimePositionType timePositionType) {
        Instant intermediateDate = GMLDateUtils.stringToDateMultiParsers(timePositionType.getValue().get(0));
        return GMLDateUtils.dateToString(intermediateDate, format);
    }

    private DateTimeFormatter dateFormat(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));
    }

}
