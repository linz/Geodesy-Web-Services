package au.gov.ga.geodesy.domain.model.equipment;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class EquipmentRepositoryImpl implements EquipmentRepositoryCustom {

    @PersistenceContext(unitName = "geodesy")
    private EntityManager entityManager;

    public <T extends Equipment> T findOne(Class<T> equipmentType, String type, String serialNumber) {
        String entityName = equipmentType.getName();

        String queryString = "select e from " + entityName + " e " +
            "where e.type = :type and e.serialNumber = :serialNumber";

        TypedQuery<T> query = entityManager.createQuery(queryString, equipmentType);
        query.setParameter("type", type);
        query.setParameter("serialNumber", serialNumber);
        List<T> results = query.getResultList();
        return results.size() == 0 ? null : query.getResultList().get(0);
    }

    public <T extends Equipment> List<T> findByEquipmentType(Class<T> equipmentType) {
        String queryString = "select e from " + equipmentType.getName() + " e ";
        TypedQuery<T> query = entityManager.createQuery(queryString, equipmentType);
        return query.getResultList();
    }
}

