package au.gov.ga.geodesy.domain.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import au.gov.ga.support.Json;

public class SetupRepositoryImpl implements SetupRepositoryCustom {

    @Autowired
    private SetupRepository setups;

    @PersistenceContext(unitName = "geodesy")
    private EntityManager entityManager;

    public Setup findCurrentBySiteId(Integer siteId) {
        String queryString =
            "select s from Setup s where s.siteId = :id and s.effectivePeriod.to is null";

        TypedQuery<Setup> query = entityManager.createQuery(queryString, Setup.class);


        System.out.println(siteId);
        System.out.println(siteId);
        System.out.println(siteId);
        System.out.println(siteId);
        System.out.println(siteId);
        System.out.println(siteId);
        System.out.println(siteId);

        for (Setup s : setups.findAll()) {
            System.out.println(Json.toString(s));
        }

        query.setParameter("id", siteId);
        return query.getSingleResult();
    }
}
