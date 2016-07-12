package au.gov.ga.geodesy.support.mapper.dozer.converter;

import au.gov.xml.icsm.geodesyml.v_0_3.IgsRadomeModelCodeType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class AntennaRadomeTypeConverterTest {
    AntennaRadomeTypeConverter conv = new AntennaRadomeTypeConverter();

    @Test
    public void test01() {
        String source = "AntennaRadomeValue";
        IgsRadomeModelCodeType dest = null;
        dest = (IgsRadomeModelCodeType) conv.convert(dest, source, IgsRadomeModelCodeType.class, String.class);
        assertThat(dest, notNullValue());
        assertThat(dest.getValue(), is(source));
        assertThat(dest.getCodeSpace(), is(AntennaRadomeTypeConverter.DEFAULT_CODESPACEATTR));

    }

}
