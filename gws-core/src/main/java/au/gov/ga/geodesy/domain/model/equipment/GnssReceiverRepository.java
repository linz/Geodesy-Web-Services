package au.gov.ga.geodesy.domain.model.equipment;

import java.util.List;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface GnssReceiverRepository extends AggregateRepository<GnssReceiver> {

    @Override
    List<GnssReceiver> findAll();
}
