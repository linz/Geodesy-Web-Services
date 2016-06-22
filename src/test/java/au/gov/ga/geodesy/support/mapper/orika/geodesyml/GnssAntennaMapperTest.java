package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.GnssAntennaLogItem;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssAntennaType;
import au.gov.xml.icsm.geodesyml.v_0_3.IgsAntennaModelCodeType;
import au.gov.xml.icsm.geodesyml.v_0_3.IgsRadomeModelCodeType;
import net.opengis.gml.v_3_2_1.CodeType;
import net.opengis.gml.v_3_2_1.TimePositionType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GnssAntennaMapperTest {

    private final GnssAntennaMapper mapper = new GnssAntennaMapper();
    private final DateTimeFormatter outputFormat = dateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Test
    public void testMappingToLogItem() {
        final String dateInstalled = "2012-03-24T02:03:23.000Z";
        final String dateRemoved = "2014-03-24T02:03:23.000Z";
        final String serialNumber = "CR20020709";

        IgsRadomeModelCodeType igsRadomeModelCodeType = new IgsRadomeModelCodeType();
        igsRadomeModelCodeType.withValue("SomeAntennaRadomeType").withCodeSpace("eGeodesy/antennaRadomeType");

        GnssAntennaType antenna = new GnssAntennaType()
                .withAntennaType((IgsAntennaModelCodeType) new IgsAntennaModelCodeType()
                        .withCodeSpace("https://igscb.jpl.nasa.gov/igscb/station/general/rcvr_ant.tab")
                        .withCodeList("http://xml.gov.au/icsm/geodesyml/codelists/antenna-Antenna-codelists.xml#GeodesyML_GNSSAntennaTypeCode")
                        .withCodeListValue("LEICA GRX1200GGPRO")
                        .withValue("LEICA GRX1200GGPRO"))
                .withSerialNumber(serialNumber)
                .withAntennaReferencePoint(new CodeType().withValue("BPA").withCodeSpace("eGeodesy/antennaRadomeType"))
                .withMarkerArpUpEcc(0.0)
                .withMarkerArpNorthEcc(0.0)
                .withMarkerArpEastEcc(0.0)
                .withAlignmentFromTrueNorth(0)
                .withAntennaRadomeType(igsRadomeModelCodeType)
                .withAntennaCableLength(12.0)
                .withAntennaCableType("Twine")
                .withDateInstalled(timePosition(dateInstalled))
                .withDateRemoved(timePosition(dateRemoved))
                .withManufacturerSerialNumber(serialNumber);

        GnssAntennaLogItem logItem = mapper.to(antenna);
        MatcherAssert.assertThat(logItem.getType(), Is.is(antenna.getAntennaType().getCodeListValue()));
        MatcherAssert.assertThat(logItem.getAntennaReferencePoint(), Is.is(antenna.getAntennaReferencePoint().getValue()));
        MatcherAssert.assertThat(logItem.getSerialNumber(), Is.is(antenna.getSerialNumber()));
        MatcherAssert.assertThat(logItem.getMarkerArpEastEcc(), Is.is(antenna.getMarkerArpEastEcc()));
        MatcherAssert.assertThat(logItem.getMarkerArpNorthEcc(), Is.is(antenna.getMarkerArpNorthEcc()));
        MatcherAssert.assertThat(logItem.getMarkerArpUpEcc(), Is.is(antenna.getMarkerArpUpEcc()));
        MatcherAssert.assertThat(logItem.getAlignmentFromTrueNorth(), Is.is(String.valueOf(antenna.getAlignmentFromTrueNorth())));
        MatcherAssert.assertThat(logItem.getAntennaRadomeType(), Is.is(antenna.getAntennaRadomeType().getValue()));
        MatcherAssert.assertThat(logItem.getAntennaCableLength(), Is.is(String.valueOf(antenna.getAntennaCableLength())));
        MatcherAssert.assertThat(logItem.getAntennaCableType(), Is.is(String.valueOf(antenna.getAntennaCableType())));
        Instant installed = logItem.getDateInstalled();
        MatcherAssert.assertThat(outputFormat.format(installed), Is.is(dateInstalled));
        MatcherAssert.assertThat(outputFormat.format(logItem.getDateRemoved()), Is.is(dateRemoved));

        GnssAntennaType antennaB = mapper.from(logItem);
        MatcherAssert.assertThat(antennaB.getAntennaReferencePoint(), Is.is(antenna.getAntennaReferencePoint()));
        MatcherAssert.assertThat(antennaB.getMarkerArpEastEcc(), Is.is(antenna.getMarkerArpEastEcc()));
        MatcherAssert.assertThat(antennaB.getMarkerArpNorthEcc(), Is.is(antenna.getMarkerArpNorthEcc()));
        MatcherAssert.assertThat(antennaB.getMarkerArpUpEcc(), Is.is(antenna.getMarkerArpUpEcc()));
        MatcherAssert.assertThat(antennaB.getAlignmentFromTrueNorth(), Is.is(antenna.getAlignmentFromTrueNorth()));
        MatcherAssert.assertThat(antennaB.getAntennaRadomeType(), Is.is(antenna.getAntennaRadomeType()));
        MatcherAssert.assertThat(antennaB.getAntennaCableLength(), Is.is(antenna.getAntennaCableLength()));
        MatcherAssert.assertThat(antennaB.getAntennaCableType(), Is.is(antenna.getAntennaCableType()));
        MatcherAssert.assertThat(antennaB.getDateInstalled().getValue().get(0), Is.is(dateInstalled));
        MatcherAssert.assertThat(antennaB.getDateRemoved().getValue().get(0), Is.is(dateRemoved));
    }

    private TimePositionType timePosition(String date) {
        TimePositionType timePosition = new TimePositionType();
        timePosition.getValue().add(date);
        return timePosition;
    }

    private static DateTimeFormatter dateFormat(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));
    }
}

