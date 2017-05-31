package au.gov.ga.geodesy.domain.model;

public interface NewSiteRequestRepositoryCustom {
    <S extends NewSiteRequest> S save(S newSiteRequest);
}
