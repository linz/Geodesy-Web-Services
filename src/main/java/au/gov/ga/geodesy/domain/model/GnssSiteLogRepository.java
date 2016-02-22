package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GnssSiteLogRepository extends JpaRepository<GnssSiteLog, Integer> {

    @Override
    List<GnssSiteLog> findAll();

    GnssSiteLog findByFourCharacterId(@Param("id") String id);
}
