package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GnssCorsSiteRepository extends JpaRepository<GnssCorsSite, Integer> {

    @Override
    List<GnssCorsSite> findAll();

    @Query("select site from GnssCorsSite site where site.fourCharacterId = :id")
    GnssCorsSite findByFourCharacterId(@Param("id") String id);
}
