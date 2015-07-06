package au.gov.ga.geodesy.domain.model;

public interface SetupRepositoryCustom {

    Setup findCurrentBySiteId(Integer siteId);
}
