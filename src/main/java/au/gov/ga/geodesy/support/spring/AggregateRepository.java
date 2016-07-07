package au.gov.ga.geodesy.support.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AggregateRepository<T> extends
    JpaRepository<T, Integer>,
    QueryDslPredicateExecutor<T> {
}
