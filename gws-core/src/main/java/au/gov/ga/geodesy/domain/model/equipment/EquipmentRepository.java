package au.gov.ga.geodesy.domain.model.equipment;

import java.util.List;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

@RepositoryRestResource(path = "equipment")
public interface EquipmentRepository extends AggregateRepository<Equipment>, EquipmentRepositoryCustom {
    @Override
    List<Equipment> findAll();
}
