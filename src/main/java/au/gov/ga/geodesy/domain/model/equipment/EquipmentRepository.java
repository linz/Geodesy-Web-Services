package au.gov.ga.geodesy.domain.model.equipment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "equipment")
public interface EquipmentRepository extends JpaRepository<Equipment, Integer>, EquipmentRepositoryCustom {
    @Override
    List<Equipment> findAll();
}
