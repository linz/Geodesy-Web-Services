package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SetupRepository extends JpaRepository<Setup, Integer>, SetupRepositoryCustom {

    List<Setup> findBySiteId(@Param("siteId") Integer id);
}
