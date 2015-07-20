package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface SetupRepository extends JpaRepository<Setup, Integer>, SetupRepositoryCustom {

    Page<Setup> findBySiteId(@Param("siteId") Integer id, Pageable pageRequest);

    @RestResource(exported = false)
    List<Setup> findBySiteId(@Param("siteId") Integer id);
}
