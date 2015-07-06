package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SetupRepository extends JpaRepository<Setup, Integer> {

    List<Setup> findBySiteId(@Param("siteId") Integer id);

    /* @Query("select setup from Setup setup where setup.siteId in (select siteId from GnssCorsSite site where site.fourCharacterId = :id)") */
    /* List<Setup> findByFourCharacterId(@Param("id") String id); */
}
