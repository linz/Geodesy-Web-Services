package au.gov.ga.geodesy.support.moxy;

import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Assert;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GeodesyMLType;

public class GeodesyMLMoxyTest {

    private GeodesyMLMoxy marshaller;

    public GeodesyMLMoxyTest() throws Exception {
        marshaller = new GeodesyMLMoxy();
    }

    @Test
    public void unmarshal() throws Exception {
        String inputDoc = "sitelog/geodesyml/MOBS.xml";

        Reader input = new InputStreamReader(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(inputDoc));

        GeodesyMLType geodesyML = marshaller.unmarshal(input).getValue();
        
        Assert.assertEquals(23, geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance().size());
        geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance().forEach(x -> {
            System.out.println(x.getName());
        });
    }
}
