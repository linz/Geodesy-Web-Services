package au.gov.ga.geodesy.support.mapper.dozer;

import org.junit.Assert;
import org.junit.Test;

public class StringDoubleConverterTest {
    StringDoubleConverter conv = new StringDoubleConverter();

    @Test
    public void test01() {
        Double dest = null;
        String source = "none";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(0.0, dest, 0.01);
    }

    @Test
    public void test02() {
        Double dest = null;
        String source = "+1mm";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(1.0, dest, 0.01);
    }

    @Test
    public void test03() {
        Double dest = null;
        String source = "-1mm";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(-1.0, dest, 0.01);
    }

    @Test
    public void test04() {
        Double dest = null;
        String source = "+-1mm";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(1.0, dest, 0.01);
    }

    @Test
    public void test05() {
        Double dest = null;
        String source = "2% rel h";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(2.0, dest, 0.01);
    }
    @Test
    public void test06() {
        Double dest = null;
        String source = "0.5 deg C";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(0.5, dest, 0.01);
    }
    @Test
    public void test07() {
        Double dest = null;
     // Yep great number this one!  Found in MAT1.xml
        String source = "20B0C +-5B0C";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(20050, dest, 0.01);
    }

    @Test
    public void test08() {
        Double dest = null;
        // Yep great number this one!  Found in MAT1.xml
        String source = "Input Frequency        ";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(0.0, dest, 0.01);
    }

}
