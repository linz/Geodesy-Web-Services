package au.gov.ga.geodesy.support.moxy;

import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Assert;
import org.testng.annotations.Test;

import au.gov.xml.icsm.geodesyml.v_0_2_2.GeodesyMLType;

public class GeodesyMLMoxyTest {

    private GeodesyMLMoxy marshaller;

    public GeodesyMLMoxyTest() throws Exception {
        marshaller = new GeodesyMLMoxy();
    }

    @Test
    public void unmarshal() throws Exception {
        Reader input = new InputStreamReader(Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("sitelog/geodesyml/MOBS.xml"));

        GeodesyMLType geodesyML = marshaller.unmarshal(input).getValue();

        Assert.assertEquals(24, geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance().size());
        geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance().forEach(x -> {
            System.out.println(x.getName());
        });
    }
}
