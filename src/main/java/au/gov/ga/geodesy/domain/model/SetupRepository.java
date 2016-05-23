package au.gov.ga.geodesy.domain.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface SetupRepository extends JpaRepository<Setup, Integer>, SetupRepositoryCustom {

    // TODO: test
    @Query("select s from Setup s where s.siteId = :siteId and s.invalidated = false")
    Page<Setup> findBySiteId(@Param("siteId") Integer id, Pageable pageRequest);

    @RestResource(exported = false)
    @Query("select s from Setup s where s.siteId = :siteId and s.invalidated = false")
    List<Setup> findBySiteId(@Param("siteId") Integer id);

    @RestResource(exported = false)
    @Query("select s from Setup s where s.siteId = :siteId and s.invalidated = true")
    List<Setup> findInvalidatedBySiteId(@Param("siteId") Integer id);

    @RestResource(exported = false)
    @Query("select s from Setup s where "
                + "s.siteId = :siteId"
                + " and ((s.effectivePeriod.from >= :effectiveFrom and s.effectivePeriod.to <= :effectiveTo)"
                + " or (s.effectivePeriod.from <= :effectiveTo and (:effectiveTo <= s.effectivePeriod.to or s.effectivePeriod.to is null))"
                + " or (s.effectivePeriod.from <= :effectiveFrom   and :effectiveFrom   <= s.effectivePeriod.to))")

    Page<Setup> findBySiteIdAndDateRange(
        @Param("siteId") Integer id,
        @Param("effectiveFrom") Date effectiveFrom,
        @Param("effectiveTo") Date effectiveTo,
        Pageable pageRequest
    );

    @RestResource(exported = false)
    @Query("select s from Setup s where "
                + "s.siteId = :siteId"
                + " and ((s.effectivePeriod.from >= :effectiveFrom and s.effectivePeriod.to <= :effectiveTo)"
                + " or (s.effectivePeriod.from <= :effectiveTo and (:effectiveTo <= s.effectivePeriod.to or s.effectivePeriod.to is null))"
                + " or (s.effectivePeriod.from <= :effectiveFrom   and :effectiveFrom   <= s.effectivePeriod.to))")

    List<Setup> findBySiteIdAndDateRange(
        @Param("siteId") Integer id,
        @Param("effectiveFrom") Date effectiveFrom,
        @Param("effectiveTo") Date effectiveTo
    );
}
