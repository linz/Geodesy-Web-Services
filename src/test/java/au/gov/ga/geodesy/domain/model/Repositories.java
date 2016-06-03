package au.gov.ga.geodesy.domain.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.domain.model.CorsSiteRepository;
import au.gov.ga.geodesy.domain.model.NodeRepository;
import au.gov.ga.geodesy.domain.model.SetupRepository;
import au.gov.ga.geodesy.domain.model.equipment.EquipmentConfigurationRepository;
import au.gov.ga.geodesy.domain.model.equipment.EquipmentRepository;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;

/**
 * Utilities that affect all repositories.
 */
@Configurable
public class Repositories {

    private static final Logger log = LoggerFactory.getLogger(Repositories.class);

    @Autowired
    private SiteLogRepository siteLogs;

    @Autowired
    private CorsSiteRepository sites;

    @Autowired
    private SetupRepository setups;

    @Autowired
    private EquipmentRepository equipment;

    @Autowired
    private EquipmentConfigurationRepository equipmentConfiguration;

    @Autowired
    private NodeRepository nodes;

    /**
     * Delete all persistent entitites.
     */
    public void deleteAll() {
        siteLogs.deleteAll();
        nodes.deleteAll();
        setups.deleteAll();
        sites.deleteAll();
        equipmentConfiguration.deleteAll();
        equipment.deleteAll();
        log.info("Deleted all data");
    }
}
