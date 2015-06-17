package au.gov.ga.geodesy.igssitelog.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IgsSiteLogRepository extends JpaRepository<IgsSiteLog, Integer> {

    @Override
    List<IgsSiteLog> findAll();
}
