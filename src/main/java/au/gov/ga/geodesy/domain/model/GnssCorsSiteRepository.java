package au.gov.ga.geodesy.domain.model;

import java.util.List;

import au.gov.ga.geodesy.support.spring.EntityId;
import au.gov.ga.geodesy.support.spring.EntityRepository;

public interface GnssCorsSiteRepository extends EntityRepository<GnssCorsSite, Integer> {

    EntityId<GnssCorsSite> nextId();

    @Override
    List<GnssCorsSite> findAll();
}
