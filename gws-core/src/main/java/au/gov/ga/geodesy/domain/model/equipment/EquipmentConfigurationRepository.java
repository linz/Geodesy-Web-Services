package au.gov.ga.geodesy.domain.model.equipment;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface EquipmentConfigurationRepository
    extends AggregateRepository<EquipmentConfiguration>,
            EquipmentConfigurationRepositoryCustom {

    @Override
    List<EquipmentConfiguration> findAll();

    @Query("select c from EquipmentConfiguration c" +
           " where c.equipmentId.id = :equipmentId and c.configurationTime = :configurationTime")
    EquipmentConfiguration findOne(@Param("equipmentId") Integer id, @Param("configurationTime") Instant time);
}
