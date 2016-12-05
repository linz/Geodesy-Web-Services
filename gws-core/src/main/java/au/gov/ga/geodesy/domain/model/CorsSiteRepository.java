package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface CorsSiteRepository extends AggregateRepository<CorsSite> {

    @Override
    List<CorsSite> findAll();

    @Query("select site from CorsSite site where site.fourCharacterId = :id")
    CorsSite findByFourCharacterId(@Param("id") String id);
}
