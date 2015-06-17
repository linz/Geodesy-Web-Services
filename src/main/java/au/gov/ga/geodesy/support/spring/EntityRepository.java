package au.gov.ga.geodesy.support.spring;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EntityRepository<T, K extends Serializable>
        extends PagingAndSortingRepository<T, K>, JpaRepository<T, K> {

    EntityId<T> nextId();
}
