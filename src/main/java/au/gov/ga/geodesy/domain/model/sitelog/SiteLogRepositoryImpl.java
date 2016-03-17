package au.gov.ga.geodesy.domain.model.sitelog;

import org.springframework.beans.factory.annotation.Autowired;

public class SiteLogRepositoryImpl implements SiteLogRepositoryCustom {

    @Autowired
    private SiteLogRepository siteLogs;

    public void delete(String fourCharacterId) {
        SiteLog toDelete = siteLogs.findByFourCharacterId(fourCharacterId);
        if (toDelete != null) {
            siteLogs.delete(toDelete);
        }
    }
}
