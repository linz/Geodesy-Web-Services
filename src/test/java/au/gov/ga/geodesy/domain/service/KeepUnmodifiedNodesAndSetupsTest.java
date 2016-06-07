package au.gov.ga.geodesy.domain.service;

import static org.testng.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.CorsSiteRepository;
import au.gov.ga.geodesy.domain.model.NodeRepository;
import au.gov.ga.geodesy.domain.model.SetupRepository;
import au.gov.ga.geodesy.domain.model.equipment.EquipmentRepository;
import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTestConfig;

@Transactional("geodesyTransactionManager")
public class KeepUnmodifiedNodesAndSetupsTest extends IntegrationTestConfig {

    private static final String fourCharId = "ABRK";

    @Autowired
    private CorsSiteRepository sites;

    @Autowired
    private IgsSiteLogService siteLogService;

    @Autowired
    private SetupRepository setups;

    @Autowired
    private NodeRepository nodes;

    @Autowired
    private EquipmentRepository equipment;

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
            SiteLogReader input = new SopacSiteLogReader(TestResources.sopacSiteLogReader(fourCharId));
            siteLogService.upload(input.getSiteLog());
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
