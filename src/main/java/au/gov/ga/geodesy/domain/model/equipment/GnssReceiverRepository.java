package au.gov.ga.geodesy.domain.model.equipment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface GnssReceiverRepository extends JpaRepository<GnssReceiver, Integer>,
    QueryDslPredicateExecutor<GnssReceiver> {

    @Override
    List<GnssReceiver> findAll();
}
