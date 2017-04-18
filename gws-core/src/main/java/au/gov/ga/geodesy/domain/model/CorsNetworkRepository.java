package au.gov.ga.geodesy.domain.model;

import org.springframework.data.repository.query.Param;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface CorsNetworkRepository extends AggregateRepository<CorsNetwork> {

    CorsNetwork findByName(@Param("name") String name);
}
