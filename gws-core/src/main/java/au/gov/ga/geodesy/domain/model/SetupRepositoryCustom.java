package au.gov.ga.geodesy.domain.model;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Ad-hoc methods for retreiving Setups.
 */
public interface SetupRepositoryCustom {

    /**
     * Find the Setup currently in effect at a Site.
     */
    Setup findCurrentBySiteId(Integer siteId);

    /**
     * Find all Setups at a Site for a period of interest.
     */
    Page<Setup> findBySiteIdAndPeriod(Integer siteId, Instant periodStart, Instant periodEnd, Pageable pageRequest);
}
