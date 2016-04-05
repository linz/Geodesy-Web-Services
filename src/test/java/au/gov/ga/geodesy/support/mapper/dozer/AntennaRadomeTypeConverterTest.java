package au.gov.ga.geodesy.support.mapper.dozer;

import org.junit.Assert;
import org.junit.Test;

import au.gov.xml.icsm.geodesyml.v_0_3.IgsRadomeModelCodeType;

public class AntennaRadomeTypeConverterTest {
    AntennaRadomeTypeConverter conv = new AntennaRadomeTypeConverter();

    @Test
    public void test01() {
        String source = "AntennaRadomeValue";
        IgsRadomeModelCodeType dest = null;
        dest = (IgsRadomeModelCodeType) conv.convert(dest, source, IgsRadomeModelCodeType.class, String.class);
        Assert.assertNotNull(dest);
        Assert.assertEquals(source, dest.getValue());
        Assert.assertEquals(AntennaRadomeTypeConverter.DEFAULT_CODESPACEATTR, dest.getCodeSpace());

    }

}
