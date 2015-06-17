package au.gov.ga.geodesy.ngcp.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import static org.junit.Assert.*;
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
public class GnssStationRepositoryTest {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(GnssStationRepositoryTest.class);

    @Autowired
    private GnssStationRepository stations;

    @Autowired
    private DataSource dataSource;

    /**
     * There are 738 GnssStations.
     */
    @Test
    public void countGnssStations() {
        assertEquals(stations.count(), 738);
    }

    /**
     * There is one gnss station (id = 45143) whose igsId does not match its gpsSiteId.
     */
    @Test
    public void testMismatchedGnssStations() {
        List<GnssStation> mismatched = new ArrayList<GnssStation>();
        for (GnssStation s : stations.findAll()) {
            if (s.getIgsId() != null && !s.getGpsSiteId().equals(s.getIgsId())) {
                System.out.println(s.getId() + ": " + s.getGpsSiteId() + " and " + s.getIgsId());
                mismatched.add(s);
            }
        }
        assertEquals(1, mismatched.size());
        assertEquals((Integer) 45143, mismatched.get(0).getId());
    }

    /**
     * There are 683 gnss stations with null igsId.
     */
    @Test
    public void countGnssStationsWithNullIgsId() {
        int n = 0;
        for (GnssStation s : stations.findAll()) {
            if (s.getIgsId() == null) {
                n++;
            }
        }
        assertEquals(683, n);
    }
}
