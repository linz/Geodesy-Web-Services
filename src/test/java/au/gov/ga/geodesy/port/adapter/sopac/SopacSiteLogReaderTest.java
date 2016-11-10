package au.gov.ga.geodesy.port.adapter.sopac;

import java.io.StringReader;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.support.spring.UnitTest;

public class SopacSiteLogReaderTest extends UnitTest {

    @Test(expectedExceptions = {InvalidSiteLogException.class})
    public void invalidSiteLog() throws InvalidSiteLogException {
        SopacSiteLogReader reader = new SopacSiteLogReader(new StringReader("foo"));
        reader.getSiteLog();
    }
}
