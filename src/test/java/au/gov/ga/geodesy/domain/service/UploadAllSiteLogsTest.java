package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import au.gov.ga.geodesy.domain.model.MockEventPublisher;
import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.UnitTestConfig;


@Transactional("geodesyTransactionManager")
public class UploadAllSiteLogsTest extends UnitTestConfig {

    private List<File> siteLogFiles = null;

    @Autowired
    private CorsSiteLogService service;

    @Autowired
    private SiteLogRepository siteLogs;

    @Autowired
    public MockEventPublisher eventPublisher;

    @BeforeClass
    private void setup() throws IOException {
        siteLogFiles = TestResources.sopacSiteLogs("A*");
    }

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        for (File f : siteLogFiles) {
            service.upload(new SopacSiteLogReader(new FileReader(f)).getSiteLog());
        }
    }

    @Test(dependsOnMethods = {"upload"})
    public void check() throws Exception {
        List<Event> events = eventPublisher.getPublishedEvents();
        assertThat(events.size(), equalTo(34));
        for (Event e : events) {
            assertThat( e, instanceOf( SiteLogReceived.class ));
        }
        assertThat(siteLogs.count(), equalTo(34L));

    }
}
