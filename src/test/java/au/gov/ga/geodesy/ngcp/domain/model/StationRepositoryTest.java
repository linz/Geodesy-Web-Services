package au.gov.ga.geodesy.ngcp.domain.model;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import au.gov.ga.geodesy.support.spring.NgcpPersistenceJpaConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {NgcpPersistenceJpaConfig.class},
    loader  = AnnotationConfigContextLoader.class
)
public class StationRepositoryTest {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(StationRepositoryTest.class);

    @Autowired
    private StationRepository stations;

    @Autowired
    private DataSource dataSource;

    /**
     * There are 28,632 stations in the old NGCP database.
     */
    @Test
    public void testStationCount() {
        Assert.assertEquals(stations.count(), 28632);
    }
}
