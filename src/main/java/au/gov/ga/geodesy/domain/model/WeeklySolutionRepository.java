package au.gov.ga.geodesy.domain.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface WeeklySolutionRepository extends AggregateRepository<WeeklySolution> {

    @Query("select s from WeeklySolution s where s.epoch = :epoch")
    List<WeeklySolution> findAll(@Param("epoch") Instant epoch);
}
