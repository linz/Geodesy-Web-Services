package au.gov.ga.geodesy.gws.systemtest;

import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

public class SystemTestResourcesTest {

    @Test
    public void checkNumberOfSiteLogs() throws Exception {
        MatcherAssert.assertThat(SystemTestResources.siteLogs().size(), is(956));
    }

    @Test
    public void checkNumberOfSiteLogsSubset() throws Exception {
        MatcherAssert.assertThat(SystemTestResources.siteLogs("a*").size(), is(47));
    }
}
