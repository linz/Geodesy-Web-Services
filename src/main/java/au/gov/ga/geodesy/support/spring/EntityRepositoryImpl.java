package au.gov.ga.geodesy.support.spring;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class EntityRepositoryImpl<T, K extends Serializable>
        extends SimpleJpaRepository<T, K>
        implements EntityRepository<T, K> {

    public EntityRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    public EntityId<T> nextId() {
        return new EntityId<T>(UUID.randomUUID().toString().toUpperCase());
    }
}
