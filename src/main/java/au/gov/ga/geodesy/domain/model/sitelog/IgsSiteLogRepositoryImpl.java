package au.gov.ga.geodesy.domain.model.sitelog;

import org.springframework.beans.factory.annotation.Autowired;

public class IgsSiteLogRepositoryImpl implements IgsSiteLogRepositoryCustom {

    @Autowired
    private IgsSiteLogRepository siteLogs;

    public void delete(String fourCharacterId) {
        IgsSiteLog toDelete = siteLogs.findByFourCharacterId(fourCharacterId);
        if (toDelete != null) {
            siteLogs.delete(toDelete);
        }
    }
}
