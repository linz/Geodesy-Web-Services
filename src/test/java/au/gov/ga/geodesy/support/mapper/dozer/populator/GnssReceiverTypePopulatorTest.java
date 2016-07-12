package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class GnssReceiverTypePopulatorTest {
    private static final GnssReceiverTypePopulator gnssReceiverTypePopulator = new GnssReceiverTypePopulator();

    private static final String SERIAL_NO = "serialNo";
    private static final String MAN_SERIAL_NO = "manSerialNo";
    private static final String EMPTY = "";

    @Test
    public void testSerialYesManufacturerSerialYes() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();

        gnssReceiverType.setSerialNumber(SERIAL_NO);
        gnssReceiverType.setManufacturerSerialNumber(MAN_SERIAL_NO);

        gnssReceiverTypePopulator.checkAllRequiredElementsPopulated(gnssReceiverType);

        assertThat(gnssReceiverType.getSerialNumber(), notNullValue());
        assertThat(gnssReceiverType.getManufacturerSerialNumber(), notNullValue());
        assertThat(gnssReceiverType.getSerialNumber(), is(SERIAL_NO));
        assertThat(gnssReceiverType.getManufacturerSerialNumber(), is(MAN_SERIAL_NO));
    }

    @Test
    public void testSerialYesManufacturerSerialNo() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();

        gnssReceiverType.setSerialNumber(SERIAL_NO);
        // gnssReceiverType.setManufacturerSerialNumber(MAN_SERIAL_NO);

        gnssReceiverTypePopulator.checkAllRequiredElementsPopulated(gnssReceiverType);

        assertThat(gnssReceiverType.getSerialNumber(), notNullValue());
        assertThat(gnssReceiverType.getManufacturerSerialNumber(), notNullValue());
        assertThat(gnssReceiverType.getSerialNumber(), is(SERIAL_NO));
        assertThat(gnssReceiverType.getManufacturerSerialNumber(), is(SERIAL_NO));
    }

    @Test
    public void testSerialNoManufacturerSerialYes() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();

        // gnssReceiverType.setSerialNumber(SERIAL_NO);
        gnssReceiverType.setManufacturerSerialNumber(MAN_SERIAL_NO);

        gnssReceiverTypePopulator.checkAllRequiredElementsPopulated(gnssReceiverType);

        assertThat(gnssReceiverType.getSerialNumber(), notNullValue());
        assertThat(gnssReceiverType.getManufacturerSerialNumber(), notNullValue());
        assertThat(gnssReceiverType.getSerialNumber(), is(MAN_SERIAL_NO));
        assertThat(gnssReceiverType.getManufacturerSerialNumber(), is(MAN_SERIAL_NO));
    }

    @Test
    public void testSerialNoManufacturerSerialNo() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();

        // gnssReceiverType.setSerialNumber(SERIAL_NO);
        // gnssReceiverType.setManufacturerSerialNumber(MAN_SERIAL_NO);

        gnssReceiverTypePopulator.checkAllRequiredElementsPopulated(gnssReceiverType);

        assertThat(gnssReceiverType.getSerialNumber(), notNullValue());
        assertThat(gnssReceiverType.getManufacturerSerialNumber(), notNullValue());
        assertThat(gnssReceiverType.getSerialNumber(), is(EMPTY));
        assertThat(gnssReceiverType.getManufacturerSerialNumber(), is(EMPTY));
    }

}
