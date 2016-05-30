package au.gov.ga.geodesy.domain.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeeklySolutionRepository extends JpaRepository<WeeklySolution, Integer> {

    @Query("select s from WeeklySolution s where s.epoch = :epoch")
    List<WeeklySolution> findAll(@Param("epoch") Instant epoch);
}
