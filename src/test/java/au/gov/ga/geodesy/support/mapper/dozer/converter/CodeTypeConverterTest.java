package au.gov.ga.geodesy.support.mapper.dozer.converter;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.mapper.dozer.converter.CodeTypeConverter;
import au.gov.xml.icsm.geodesyml.v_0_3.IgsRadomeModelCodeType;
import net.opengis.gml.v_3_2_1.CodeType;

/**
 * CodeTypeConverter is handling the CodeType super-type and these extensions (directly or indirectly via inheritance).
 * net.opengis.gml.v_3_2_1.CodeWithAuthorityType
 * au.gov.xml.icsm.geodesyml.v_0_3.IgsRadomeModelCodeType
 */
public class CodeTypeConverterTest {
    CodeTypeConverter ctv = new CodeTypeConverter();

    @Test
    public void testStringSourceDestinationCodeTypeNull() {
        CodeType destination = null;
        String source = "banana";
        CodeType c = (CodeType) ctv.convert(destination, source, CodeType.class, String.class);

        Assert.assertEquals(source, c.getValue());
    }

    @Test
    public void testStringSourceDestinationCodeTypeNotNull() {
        CodeType destination = new CodeType();
        destination.setValue("Not a banana");
        String source = "banana";
        CodeType c = (CodeType) ctv.convert(destination, source, CodeType.class, String.class);

        Assert.assertEquals(source, c.getValue());
    }

    @Test
    public void testCodeTypeSourceDestinationStringNull() {
        String destination = null;
        CodeType source = new CodeType();
        source.setValue("banana");
        String c = (String) ctv.convert(destination, source, String.class, CodeType.class);

        Assert.assertEquals(source.getValue(), c);
    }

    @Test
    public void testCodeTypeSourceDestinationStringNotNull() {
        String destination = "not a banana";
        CodeType source = new CodeType();
        source.setValue("banana");
        String c = (String) ctv.convert(destination, source, String.class, CodeType.class);

        Assert.assertEquals(source.getValue(), c);
    }

    // ========================

    @Test
    public void testStringSourceDestinationIgsRadomeModelCodeTypeNull() {
        IgsRadomeModelCodeType destination = null;
        String source = "banana";
        IgsRadomeModelCodeType c = (IgsRadomeModelCodeType) ctv.convert(destination, source,
                IgsRadomeModelCodeType.class, String.class);

        Assert.assertEquals(source, c.getValue());
    }

    @Test
    public void testStringSourceDestinationIgsRadomeModelCodeTypeNotNull() {
        IgsRadomeModelCodeType destination = new IgsRadomeModelCodeType();
        String source = "banana";
        IgsRadomeModelCodeType c = (IgsRadomeModelCodeType) ctv.convert(destination, source,
                IgsRadomeModelCodeType.class, String.class);

        Assert.assertEquals(source, c.getValue());
    }

    @Test
    public void testIgsRadomeModelCodeTypeSourceDestinationStringNull() {
        String destination = null;
        IgsRadomeModelCodeType source = new IgsRadomeModelCodeType();
        source.setValue("banana");
        String c = (String) ctv.convert(destination, source, String.class, IgsRadomeModelCodeType.class);

        Assert.assertEquals(source.getValue(), c);
    }

    @Test
    public void testIgsRadomeModelCodeTypeSourceDestinationStringNotNull() {
        String destination = "not a banana";
        IgsRadomeModelCodeType source = new IgsRadomeModelCodeType();
        source.setValue("banana");
        String c = (String) ctv.convert(destination, source, String.class, IgsRadomeModelCodeType.class);

        Assert.assertEquals(source.getValue(), c);
    }

}
