package au.gov.ga.geodesy.domain.model;

public interface UserRegistrationRepositoryCustom {
    <S extends UserRegistration> S save(S userRegistration);
}
