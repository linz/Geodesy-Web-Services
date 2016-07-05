package au.gov.ga.geodesy.domain.model.equipment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface GnssReceiverRepository extends JpaRepository<GnssReceiver, Integer>,
    QueryByExampleExecutor<GnssReceiver>, QueryDslPredicateExecutor<GnssReceiver> {

    @Override
    List<GnssReceiver> findAll();
}
