package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "datePrepared", types = { SiteLog.class })
public interface SiteLogDatePreparedProjection {

  @Value("#{target.siteIdentification.fourCharacterId}")
  String getFourCharacterId();

  @Value("#{target.formInformation.datePrepared}")
  Instant getDatePrepared();

  ZonedDateTime getLastModifiedDate();
}
