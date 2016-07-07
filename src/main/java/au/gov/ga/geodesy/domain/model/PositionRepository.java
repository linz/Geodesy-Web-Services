package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface PositionRepository extends AggregateRepository<Position> {

    @Query("select p from Position p where p.fourCharacterId = :id")
    List<Position> findByFourCharacterId(@Param("id") String id);
}
