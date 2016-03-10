package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CorsSiteLogRepository extends JpaRepository<CorsSiteLog, Integer> {

    @Override
    List<CorsSiteLog> findAll();

    CorsSiteLog findByFourCharacterId(@Param("id") String id);
}
