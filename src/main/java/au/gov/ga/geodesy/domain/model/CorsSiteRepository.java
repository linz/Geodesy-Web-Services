package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CorsSiteRepository extends JpaRepository<CorsSite, Integer> {

    @Override
    List<CorsSite> findAll();

    @Query("select site from CorsSite site where site.fourCharacterId = :id")
    CorsSite findByFourCharacterId(@Param("id") String id);
}
