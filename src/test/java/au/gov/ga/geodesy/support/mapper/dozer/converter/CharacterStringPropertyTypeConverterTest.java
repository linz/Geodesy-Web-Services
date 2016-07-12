package au.gov.ga.geodesy.support.mapper.dozer.converter;

import net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType;
import net.opengis.iso19139.gco.v_20070417.ObjectFactory;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CharacterStringPropertyTypeConverterTest {
    CharacterStringPropertyTypeConverter ctc = new CharacterStringPropertyTypeConverter();
    ObjectFactory gcoObjectFactory = new ObjectFactory();

    @Test
    public void testWay1() {
        String source = "banana";
        CharacterStringPropertyType out = (CharacterStringPropertyType) ctc.convert(null, source, CharacterStringPropertyType.class,
            String.class);
        assertThat(out.getCharacterString().getValue(), is(source));
    }

    @Test
    public void testWay2() {
        CharacterStringPropertyType source = new CharacterStringPropertyType();
        String stringSource = "banana";
        source.setCharacterString(gcoObjectFactory.createCharacterString(stringSource));

        String out = (String) ctc.convert(null, source, CharacterStringPropertyType.class, String.class);
        assertThat(out, is(stringSource));
    }

}
