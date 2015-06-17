package au.gov.ga.geodesy.ngcp.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Integer> {

    @Override
    List<Station> findAll();
}
