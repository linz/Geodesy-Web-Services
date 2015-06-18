package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import au.gov.ga.geodesy.support.spring.EntityId;
import au.gov.ga.geodesy.support.spring.EntityRepository;

public interface GnssCorsSiteRepository extends EntityRepository<GnssCorsSite, Integer> {

    EntityId<GnssCorsSite> nextId();

    @Override
    List<GnssCorsSite> findAll();

    @Query("select site from GnssCorsSite site where site.fourCharacterId = :id")
    GnssCorsSite findByFourCharacterId(@Param("id") String id);
}
