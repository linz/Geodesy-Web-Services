package au.gov.ga.geodesy.domain.service;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.GnssCorsSite;
import au.gov.ga.geodesy.domain.model.GnssCorsSiteRepository;
import au.gov.ga.geodesy.domain.model.Node;
import au.gov.ga.geodesy.domain.model.NodeRepository;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SetupRepository;
import au.gov.ga.geodesy.domain.model.equipment.EquipmentRepository;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesyServiceTestConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class KeepUnmodifiedNodesAndSetupsTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String siteLogsDir = "src/test/resources/sitelog/";

    private static final String fourCharId = "ABRK";

    @Autowired
    private GnssCorsSiteService siteService;

    @Autowired
    private GnssCorsSiteRepository sites;

    @Autowired
    private IgsSiteLogService siteLogService;

    @Autowired
    private SetupRepository setups;

    @Autowired
    private NodeRepository nodes;

    @Autowired
    private EquipmentRepository equipment;

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    @Autowired
    private PlatformTransactionManager txnManager;

    private abstract class InTransaction {
        public void f() throws Exception {
            DefaultTransactionDefinition txnDef = new DefaultTransactionDefinition();
            TransactionStatus txn = txnManager.getTransaction(txnDef);
            try {
                f();
            }
            finally {
                txnManager.commit(txn);
            }
        }
    }

    private InTransaction uploadABRK = new InTransaction() {
        public void f() throws Exception {
            File sitelog = new File(siteLogsDir + fourCharId + ".xml");
            siteLogService.upload(marshaller.unmarshal(new FileReader(sitelog)));
        }
    };
    private InTransaction[] scenario = {
        uploadABRK,
        uploadABRK,
    };

    private void execute(InTransaction... scenario) throws Exception {
        for (InTransaction s : scenario) {
            s.f();
        }
    }

    @Test
    @Rollback(false)
    public void execute() throws Exception {
        execute(scenario);
    }

    @Test(dependsOnMethods = "execute")
    @Rollback(false)
    public void checkSetupId() throws Exception {
        assertEquals(sites.count(), 1);
        assertEquals(setups.count(), 1);
        assertEquals(nodes.count(), 1);
        assertEquals(equipment.count(), 3);
    }
}
