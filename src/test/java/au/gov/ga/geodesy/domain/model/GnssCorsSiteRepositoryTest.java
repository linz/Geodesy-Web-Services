package au.gov.ga.geodesy.domain.model;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.ngcp.domain.model.GnssStationRepository;
import au.gov.ga.geodesy.ngcp.domain.model.Station;
import au.gov.ga.geodesy.support.spring.NgcpPersistenceJpaConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;
import au.gov.ga.geodesy.support.spring.TraceMethodExecution;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {
        PersistenceJpaConfig.class,
        NgcpPersistenceJpaConfig.class,
        TraceMethodExecution.class
    },
    loader  = AnnotationConfigContextLoader.class
)
@Transactional
public class GnssCorsSiteRepositoryTest implements TraceMethodExecution.Intercepted {

    private static final Logger log = LoggerFactory.getLogger(GnssCorsSiteRepositoryTest.class);

    @Autowired
    private GnssCorsSiteRepository sites;

    @Autowired
    private GnssStationRepository stations;

    @Autowired
    private DataSource dataSource;

    /* @Test */
    public void test() {
        long n = sites.count();
        sites.saveAndFlush(new GnssCorsSite(sites.nextId(), "ABCD"));
        assertEquals(sites.count(), n + 1);
    }

    @Test
    @Rollback(false)
    public void testMigrateFromNgcpDatabase() {
        for (Station station : stations.findAll()) {
            sites.save(new GnssCorsSite(sites.nextId(), station.getGpsSiteId()));
        }
        sites.flush();
    }

    @AfterClass
    public static void sleepUntilInterrupted() {
        log.info("Tests are done, going to sleep.");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ok) {}
    }
}
