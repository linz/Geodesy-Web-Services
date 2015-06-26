package au.gov.ga.geodesy.ngcp.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GnssStationRepository extends JpaRepository<GnssStation, Integer> {

    @Override
    List<GnssStation> findAll();
}
