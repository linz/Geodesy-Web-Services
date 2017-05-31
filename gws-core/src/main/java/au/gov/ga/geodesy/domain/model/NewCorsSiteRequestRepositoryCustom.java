package au.gov.ga.geodesy.domain.model;

public interface NewCorsSiteRequestRepositoryCustom {
    <S extends NewCorsSiteRequest> S save(S newCorsSiteRequest);
}
