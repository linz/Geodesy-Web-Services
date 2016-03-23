package au.gov.ga.geodesy.support.mapper.dozer;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.mapper.dozer.CodeListValueTypeConverter;
import au.gov.xml.icsm.geodesyml.v_0_2_2.IgsReceiverModelCodeType;
import net.opengis.iso19139.gco.v_20070417.CodeListValueType;

/**
 * CodeListValueTypeConverter is handling the CodeListValueType super-type and these extensions (directly or indirectly via inheritance).
 * au.gov.xml.icsm.geodesyml.v_0_2_2.IgsReceiverModelCodeType
 * au.gov.xml.icsm.geodesyml.v_0_2_2.IgsAntennaModelCodeType
 *
 */
public class CodeListValueTypeConverterTest {
    CodeListValueTypeConverter ctv = new CodeListValueTypeConverter();

    @Test
    public void testStringSourceDestinationCodeListValueTypeNull() {
        CodeListValueType destination = null;
        String source = "banana";
        CodeListValueType c = (CodeListValueType) ctv.convert(destination, source, CodeListValueType.class,
                String.class);

        Assert.assertEquals(source, c.getValue());
    }

    @Test
    public void testStringSourceDestinationCodeListValueTypeNotNull() {
        CodeListValueType destination = new CodeListValueType();
        destination.setValue("Not a banana");
        String source = "banana";
        CodeListValueType c = (CodeListValueType) ctv.convert(destination, source, CodeListValueType.class,
                String.class);

        Assert.assertEquals(source, c.getValue());
    }

    @Test
    public void testCodeListValueTypeSourceDestinationStringNull() {
        String destination = null;
        CodeListValueType source = new CodeListValueType();
        source.setValue("banana");
        String c = (String) ctv.convert(destination, source, String.class, CodeListValueType.class);

        Assert.assertEquals(source.getValue(), c);
    }

    @Test
    public void testCodeListValueTypeSourceDestinationStringNotNull() {
        String destination = "not a banana";
        CodeListValueType source = new CodeListValueType();
        source.setValue("banana");
        String c = (String) ctv.convert(destination, source, String.class, CodeListValueType.class);

        Assert.assertEquals(source.getValue(), c);
    }

    // ========================

    @Test
    public void testStringSourceDestinationIgsReceiverModelCodeListValueTypeNull() {
        IgsReceiverModelCodeType destination = null;
        String source = "banana";
        IgsReceiverModelCodeType c = (IgsReceiverModelCodeType) ctv.convert(destination, source,
                IgsReceiverModelCodeType.class, String.class);

        Assert.assertEquals(source, c.getValue());
    }

    @Test
    public void testStringSourceDestinationIgsReceiverModelCodeListValueTypeNotNull() {
        IgsReceiverModelCodeType destination = new IgsReceiverModelCodeType();
        String source = "banana";
        IgsReceiverModelCodeType c = (IgsReceiverModelCodeType) ctv.convert(destination, source,
                IgsReceiverModelCodeType.class, String.class);

        Assert.assertEquals(source, c.getValue());
    }

    @Test
    public void testIgsReceiverModelCodeListValueTypeSourceDestinationStringNull() {
        String destination = null;
        IgsReceiverModelCodeType source = new IgsReceiverModelCodeType();
        source.setValue("banana");
        String c = (String) ctv.convert(destination, source, String.class, IgsReceiverModelCodeType.class);

        Assert.assertEquals(source.getValue(), c);
    }

    @Test
    public void testIgsReceiverModelCodeListValueTypeSourceDestinationStringNotNull() {
        String destination = "not a banana";
        IgsReceiverModelCodeType source = new IgsReceiverModelCodeType();
        source.setValue("banana");
        String c = (String) ctv.convert(destination, source, String.class, IgsReceiverModelCodeType.class);

        Assert.assertEquals(source.getValue(), c);
    }

}
