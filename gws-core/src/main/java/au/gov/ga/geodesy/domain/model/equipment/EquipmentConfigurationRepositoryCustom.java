package au.gov.ga.geodesy.domain.model.equipment;

import java.time.Instant;

public interface EquipmentConfigurationRepositoryCustom {
    <T extends EquipmentConfiguration> T findOne(Class<T> configClass, Integer equipId, Instant configurationTime);
}
