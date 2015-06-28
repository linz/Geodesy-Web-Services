package au.gov.ga.geodesy.domain.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipmentConfigurationRepository
    extends JpaRepository<EquipmentConfiguration, Integer>,
            EquipmentConfigurationRepositoryCustom {

    @Override
    List<EquipmentConfiguration> findAll();

    @Query("select c from EquipmentConfiguration c" +
           " where c.equipmentId.id = :equipmentId and c.configurationTime = :configurationTime")
    EquipmentConfiguration findOne(@Param("equipmentId") Integer id, @Param("configurationTime") Date time);
}
