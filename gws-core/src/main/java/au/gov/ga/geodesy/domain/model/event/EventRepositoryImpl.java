package au.gov.ga.geodesy.domain.model.event;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class EventRepositoryImpl implements EventRepositoryCustom {

    @PersistenceContext(unitName = "geodesy")
    private EntityManager entityManager;

    // TODO: move out of custom
    @Override
    public List<Event> getPendingEvents() {

        String queryString = "select e from Event e "
            + "where e.timeHandled is null and (e.retries is null or e.retries < " + Event.MAX_RETRIES + ") and (e.timePublished is null)";

        TypedQuery<Event> query = entityManager.createQuery(queryString, Event.class);
        return query.getResultList();
    }
}
