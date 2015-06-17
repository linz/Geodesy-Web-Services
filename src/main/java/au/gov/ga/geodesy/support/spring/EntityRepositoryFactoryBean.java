package au.gov.ga.geodesy.support.spring;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class EntityRepositoryFactoryBean<R extends JpaRepository<T, K>, T, K extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, K> {

    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new EntityRepositoryFactory<T, K>(em);
    }

    private static class EntityRepositoryFactory<T, K extends Serializable>
            extends JpaRepositoryFactory {

        private final EntityManager em;

        public EntityRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

        @SuppressWarnings("unchecked")
        protected Object getTargetRepository(RepositoryMetadata metadata) {
            return new EntityRepositoryImpl<T, K>((Class<T>) metadata.getDomainType(), em);
        }

        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return EntityRepositoryImpl.class;
        }
    }
}
