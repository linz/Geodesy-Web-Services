package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GnssReceiverRepository extends JpaRepository<GnssReceiver, Integer> {

    @Override
    List<GnssReceiver> findAll();

    @Query("select r from geodesy.GnssReceiver r where r.type = :type and r.serialNumber = :serialNumber")
    GnssReceiver findOne(@Param("type") String type, @Param("serialNumber") String serialNumber);
}
