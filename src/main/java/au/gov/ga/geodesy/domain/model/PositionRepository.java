package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PositionRepository extends JpaRepository<Position, Integer> {

    @Query("select p from Position p where p.fourCharacterId = :id")
    List<Position> findByFourCharacterId(@Param("id") String id);
}
