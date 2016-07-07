package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface NodeRepository extends AggregateRepository<Node> {

    @Query("select n from Node n where n.siteId = :siteId")
    public Page<Node> findBySiteId(@Param("siteId") Integer siteId, Pageable pageRequest);

    @RestResource(exported = false)
    @Query("select n from Node n where n.siteId = :siteId")
    public List<Node> findBySiteId(@Param("siteId") Integer siteId);

    public Node findBySetupId(@Param("setupId") Integer setupId);
}
