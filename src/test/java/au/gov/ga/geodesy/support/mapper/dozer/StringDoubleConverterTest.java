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
    public void test041() {
        Double dest = null;
        String source = "+-10";

        dest = (Double) conv.convert(dest, source, Double.class, String.class);

        Assert.assertEquals(10.0, dest, 0.01);
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
        // Yep great number this one! Found in MAT1.xml
        String source = "20B0C +-5B0C";

        dest = (Double) conv.convert(dest, source, Double.class, String.class);

        Assert.assertEquals(20050, dest, 0.01);
    }

    @Test
    public void test08() {
        Double dest = null;
        // Yep great number this one! Found in MAT1.xml
        String source = "Input Frequency        ";

        dest = (Double) conv.convert(dest, source, Double.class, String.class);

        Assert.assertEquals(0.0, dest, 0.01);
    }

    @Test
    public void test09() {
        Double dest = null;
        String source = "3 %";

        dest = (Double) conv.convert(dest, source, Double.class, String.class);

        Assert.assertEquals(3.0, dest, 0.01);
    }

    @Test
    public void test10() {
        Double dest = null;
        String source = "Tolerance = 5 deg C";

        dest = (Double) conv.convert(dest, source, Double.class, String.class);

        Assert.assertEquals(5.0, dest, 0.01);
    }

    @Test
    public void test11() {
        Double dest = null;
        String source = "2.0 ()";

        dest = (Double) conv.convert(dest, source, Double.class, String.class);

        Assert.assertEquals(2.0, dest, 0.01);
    }

    @Test
    public void test12() {
        Double dest = null;
        String source = "TextText!!^^&&**Text 2.0 ()&%@$@@^%^%Text";

        dest = (Double) conv.convert(dest, source, Double.class, String.class);

        Assert.assertEquals(2.0, dest, 0.01);
    }

    @Test
    public void test13() {
        Double dest = null;
        String source = "+-10...90";

        dest = (Double) conv.convert(dest, source, Double.class, String.class);

        Assert.assertEquals(10.0, dest, 0.01);
    }
    @Test
    public void test14() {
        Double dest = null;
        String source = "+/-1%(0...90%RH)";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(10.0, dest, 0.01);
    }
    @Test
    public void test15() {
        Double dest = null;
        String source = "+/-0.1 @ 20.0C";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(0.120, dest, 0.01);
    }
    @Test
    public void test16() {
        Double dest = null;
        String source = "32 m + 0.5";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(32.0, dest, 0.01);
    }
    @Test
    public void test17() {
        Double dest = null;
        String source = "APPROX. 0.3 DEG C";
        
        dest = (Double) conv.convert(dest, source, Double.class, String.class);
        
        Assert.assertEquals(0.3, dest, 0.01);
    }

}
