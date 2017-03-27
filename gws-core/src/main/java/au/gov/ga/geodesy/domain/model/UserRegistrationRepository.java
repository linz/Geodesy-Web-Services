package au.gov.ga.geodesy.domain.model;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface UserRegistrationRepository extends AggregateRepository<UserRegistration>, UserRegistrationRepositoryCustom {
}
