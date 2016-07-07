package au.gov.ga.geodesy.domain.model.equipment;

import java.util.List;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface GnssAntennaRepository extends AggregateRepository<GnssAntenna> {

    @Override
    List<GnssAntenna> findAll();
}
