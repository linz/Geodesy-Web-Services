package au.gov.ga.geodesy.domain.model.event;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class EventRepositoryImpl implements EventRepositoryCustom {

    @PersistenceContext(unitName = "geodesy")
    private EntityManager entityManager;

    public List<Event> getPendingEvents() {

        // TODO: try to remove oneMinuteAgo
        String queryString = "select e from Event e "
            + "where e.timeHandled is null and (e.retries is null or e.retries < " + Event.MAX_RETRIES + ") and (e.timePublished is null or e.timePublished < :oneMinuteAgo)";

        TypedQuery<Event> query = entityManager.createQuery(queryString, Event.class);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -1);
        query.setParameter("oneMinuteAgo", c.getTime());
        return query.getResultList();
    }
}
