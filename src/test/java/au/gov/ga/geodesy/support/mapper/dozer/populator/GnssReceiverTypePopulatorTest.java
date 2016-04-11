package au.gov.ga.geodesy.support.mapper.dozer.populator;

import org.junit.Assert;
import org.junit.Test;

import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;

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

        Assert.assertNotNull(gnssReceiverType.getSerialNumber());
        Assert.assertNotNull(gnssReceiverType.getManufacturerSerialNumber());
        Assert.assertEquals(SERIAL_NO, gnssReceiverType.getSerialNumber());
        Assert.assertEquals(MAN_SERIAL_NO, gnssReceiverType.getManufacturerSerialNumber());
    }

    @Test
    public void testSerialYesManufacturerSerialNo() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();

        gnssReceiverType.setSerialNumber(SERIAL_NO);
        // gnssReceiverType.setManufacturerSerialNumber(MAN_SERIAL_NO);

        gnssReceiverTypePopulator.checkAllRequiredElementsPopulated(gnssReceiverType);

        Assert.assertNotNull(gnssReceiverType.getSerialNumber());
        Assert.assertNotNull(gnssReceiverType.getManufacturerSerialNumber());
        Assert.assertEquals(SERIAL_NO, gnssReceiverType.getSerialNumber());
        Assert.assertEquals(SERIAL_NO, gnssReceiverType.getManufacturerSerialNumber());
    }

    @Test
    public void testSerialNoManufacturerSerialYes() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();

        // gnssReceiverType.setSerialNumber(SERIAL_NO);
        gnssReceiverType.setManufacturerSerialNumber(MAN_SERIAL_NO);

        gnssReceiverTypePopulator.checkAllRequiredElementsPopulated(gnssReceiverType);

        Assert.assertNotNull(gnssReceiverType.getSerialNumber());
        Assert.assertNotNull(gnssReceiverType.getManufacturerSerialNumber());
        Assert.assertEquals(MAN_SERIAL_NO, gnssReceiverType.getSerialNumber());
        Assert.assertEquals(MAN_SERIAL_NO, gnssReceiverType.getManufacturerSerialNumber());
    }

    @Test
    public void testSerialNoManufacturerSerialNo() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();

        // gnssReceiverType.setSerialNumber(SERIAL_NO);
        // gnssReceiverType.setManufacturerSerialNumber(MAN_SERIAL_NO);

        gnssReceiverTypePopulator.checkAllRequiredElementsPopulated(gnssReceiverType);

        Assert.assertNotNull(gnssReceiverType.getSerialNumber());
        Assert.assertNotNull(gnssReceiverType.getManufacturerSerialNumber());
        Assert.assertEquals(EMPTY, gnssReceiverType.getSerialNumber());
        Assert.assertEquals(EMPTY, gnssReceiverType.getManufacturerSerialNumber());
    }

}
