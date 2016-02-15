package au.gov.ga.geodesy.domain.model;

import java.util.List;

public interface EquipmentRepositoryCustom {
    <T extends Equipment> T findOne(Class<T> equipmentType, String type, String serialNumber);
    <T extends Equipment> List<T> findByEquipmentType(Class<T> equipmentType);
}
