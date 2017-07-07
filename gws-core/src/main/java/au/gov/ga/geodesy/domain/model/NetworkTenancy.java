package au.gov.ga.geodesy.domain.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;

@Entity
@Table(name = "CORS_SITE_IN_NETWORK")
public class NetworkTenancy {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private Integer id;

    @Column(name = "CORS_SITE_NETWORK_ID", nullable = false)
    private Integer corsNetworkId;

    @Embedded
    private EffectiveDates period;

    @SuppressWarnings("unused") // used by hibernate
    private NetworkTenancy() {
    }

    public NetworkTenancy(Integer corsNetworkId, EffectiveDates period) {
        this.corsNetworkId = corsNetworkId;
        this.period = period;
    }

    @Override
    public boolean equals(Object x) {
        if (x == null) {
            return false;
        }
        if (x == this) {
            return true;
        }
        if (x.getClass() != getClass()) {
            return false;
        }
        NetworkTenancy other = (NetworkTenancy) x;
        return new EqualsBuilder()
            .append(id, other.getId())
            .append(corsNetworkId, other.getCorsNetworkId())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
            append(id).
            append(corsNetworkId).
            toHashCode();
    }

    protected Integer getId() {
        return id;
    }

    public Integer getCorsNetworkId() {
        return corsNetworkId;
    }

    public EffectiveDates getPeriod() {
        return period;
    }

    @Transient
    public boolean inEffect() {
        return getPeriod() == null || getPeriod().inRange(Instant.now());
    }
}
