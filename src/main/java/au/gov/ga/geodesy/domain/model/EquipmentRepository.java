package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends
        JpaRepository<Equipment, Integer>,
        EquipmentRepositoryCustom {

    @Override
    List<Equipment> findAll();
}
