package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NodeRepository extends JpaRepository<Node, Integer> {

    @Query("select n from Node n where n.siteId = :siteId")
    public Page<Node> findBySiteId(@Param("siteId") Integer siteId, Pageable pageRequest);

    @Query("select n from Node n where n.siteId = :siteId")
    public List<Node> findBySiteId(@Param("siteId") Integer siteId);
}
