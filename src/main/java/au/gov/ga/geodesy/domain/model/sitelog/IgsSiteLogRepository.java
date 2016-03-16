package au.gov.ga.geodesy.domain.model.sitelog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IgsSiteLogRepository extends
        JpaRepository<IgsSiteLog, Integer>,
        IgsSiteLogRepositoryCustom {

    @Override
    List<IgsSiteLog> findAll();
    
    @Query("select siteLog from IgsSiteLog siteLog where siteLog.siteIdentification.fourCharacterId = :id")
    IgsSiteLog findByFourCharacterId(@Param("id") String id);
}

