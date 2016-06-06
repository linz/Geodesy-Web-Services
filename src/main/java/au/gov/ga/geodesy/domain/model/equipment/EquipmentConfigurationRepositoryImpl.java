package au.gov.ga.geodesy.domain.model.equipment;

import java.time.Instant;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class EquipmentConfigurationRepositoryImpl implements EquipmentConfigurationRepositoryCustom {

    @PersistenceContext(unitName = "geodesy")
    private EntityManager entityManager;

    public <T extends EquipmentConfiguration> T findOne(Class<T> configClass, Integer equipId, Instant configurationTime) {
        String entityName = configClass.getName();

        String queryString = "select e from " + entityName + " e " +
            "where e.equipmentId = :equipId and e.configurationTime = :configurationTime";

        TypedQuery<T> query = entityManager.createQuery(queryString, configClass);
        query.setParameter("equipId", equipId);
        query.setParameter("configurationTime", configurationTime);
        List<T> results = query.getResultList();
        return results.size() == 0 ? null : query.getResultList().get(0);
    }
}

