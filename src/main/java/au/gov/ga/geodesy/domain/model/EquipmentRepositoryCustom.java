package au.gov.ga.geodesy.domain.model;

public interface EquipmentRepositoryCustom {
    <T extends Equipment> T findOne(Class<T> equipmentType, String type, String serialNumber);
}
