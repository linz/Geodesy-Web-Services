package au.gov.ga.geodesy.port.adapter.sopac;

import java.io.StringReader;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class SopacSiteLogReaderITest extends IntegrationTest {

    @Test(expectedExceptions = {InvalidSiteLogException.class})
    public void invalidSiteLog() throws InvalidSiteLogException {
        SopacSiteLogReader reader = new SopacSiteLogReader(new StringReader("foo"));
        reader.getSiteLog();
    }
}
