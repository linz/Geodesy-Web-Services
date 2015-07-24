package au.gov.ga.geodesy.domain.service;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.GnssCorsSite;
import au.gov.ga.geodesy.domain.model.GnssCorsSiteRepository;
import au.gov.ga.geodesy.domain.model.NodeRepository;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SetupRepository;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.interfaces.rest.RestTest;

/* @ContextConfiguration( */
/*         classes = {GeodesyServiceTestConfig.class, PersistenceJpaConfig.class}, */
/*         loader = AnnotationConfigContextLoader.class) */

/* @Transactional("geodesyTransactionManager") */
/* public class UploadABRKTest extends AbstractTransactionalTestNGSpringContextTests { */
public class UploadABRKTest extends RestTest {

    private static final String siteLogsDir = "src/test/resources/sitelog/";

    private static final String fourCharId = "ABRK";

    @Autowired
    private GnssCorsSiteService siteService;

    @Autowired
    private GnssCorsSiteRepository sites;

    @Autowired
    private IgsSiteLogService siteLogService;

    @Autowired
    private SetupRepository setupRepo;

    @Autowired
    private NodeRepository nodeRepo;

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

    private Integer setupId1;
    private Integer setupId2;

    private InTransaction uploadABRK = new InTransaction() {
        public void f() throws Exception {
            File sitelog = new File(siteLogsDir + fourCharId + ".xml");
            siteLogService.upload(marshaller.unmarshal(new FileReader(sitelog)));
        }
    };
    private InTransaction[] scenario = {
        uploadABRK,
        new InTransaction() {
            /**
             * Save setupId.
             */
            public void f() throws Exception {
                GnssCorsSite site = sites.findByFourCharacterId(fourCharId);
                List<Setup> setups = setupRepo.findBySiteId(site.getId());
                assertEquals(setups.size(), 1);
                setupId1 = setups.get(0).getId();
            }
        },
        uploadABRK,
        new InTransaction() {
            /**
             * Save setupId.
             */
            public void f() throws Exception {
                GnssCorsSite site = sites.findByFourCharacterId(fourCharId);
                List<Setup> setups = setupRepo.findBySiteId(site.getId());
                assertEquals(setups.size(), 1);
                setupId2 = setups.get(0).getId();
            }
        }

    };

    private void execute(InTransaction... scenario) throws Exception {
        for (InTransaction s : scenario) {
            s.f();
        }
    }

    @Test
    private void checkSetupId() throws Exception {
        execute(scenario);
        System.out.println(setupId1);
        System.out.println(setupId2);
        assertEquals(setupId1, setupId2);
    }
}
