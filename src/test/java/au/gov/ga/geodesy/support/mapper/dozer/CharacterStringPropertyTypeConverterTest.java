package au.gov.ga.geodesy.support.mapper.dozer;

import org.junit.Test;
import org.testng.Assert;

import au.gov.ga.geodesy.support.mapper.dozer.CharacterStringPropertyTypeConverter;
import net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType;
import net.opengis.iso19139.gco.v_20070417.ObjectFactory;

public class CharacterStringPropertyTypeConverterTest {
    CharacterStringPropertyTypeConverter ctc = new CharacterStringPropertyTypeConverter();
    ObjectFactory gcoObjectFactory = new ObjectFactory();

    @Test
    public void testWay1() {
        String source = "banana";
        CharacterStringPropertyType out = (CharacterStringPropertyType) ctc.convert(null, source,
                CharacterStringPropertyType.class, String.class);
        Assert.assertEquals(source, out.getCharacterString().getValue());
    }

    @Test
    public void testWay2() {
        CharacterStringPropertyType source = new CharacterStringPropertyType(); 
        String stringSource = "banana";
        source.setCharacterString(gcoObjectFactory.createCharacterString(stringSource));

        String out = (String) ctc.convert(null, source, CharacterStringPropertyType.class, String.class);
        Assert.assertEquals(stringSource, out);
    }

}
