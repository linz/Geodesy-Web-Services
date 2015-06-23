package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GnssAntennaRepository extends JpaRepository<GnssAntenna, Integer> {

    @Override
    List<GnssAntenna> findAll();

    /* @Query("select r from geodesy.GnssAntenna r where r.antennaType = :type and r.serialNumber = :serialNumber") */
    /* GnssAntenna findOne(@Param("type") String type, @Param("serialNumber") String serialNumber); */
}
