package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;

@Entity
@Table(name = "CORS_SITE_ADDED_TO_NETWORK")
@DiscriminatorValue("cors site added to network")
@PrimaryKeyJoinColumn(foreignKey=@ForeignKey(name = "fk_domain_event_cors_site_added_to_network"))
public class CorsSiteAddedToNetwork extends Event {

    @Column(name = "SITE_ID", nullable = false)
    private @MonotonicNonNull Integer siteId;

    @Column(name = "NETWORK_ID", nullable = false)
    private @MonotonicNonNull Integer networkId;
     
    @Embedded
    private @MonotonicNonNull EffectiveDates period;

    @SuppressWarnings({"unused", "initialization.fields.uninitialized"}) // used by hibernate
    private CorsSiteAddedToNetwork() {
    }

    public CorsSiteAddedToNetwork(Integer siteId, Integer networkId, EffectiveDates period) {
        this.siteId = siteId;
        this.networkId = networkId;
        this.period = period;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public Integer getNetworkId() {
        return networkId;
    }

    public EffectiveDates getPeriod() {
        return period;
    }
}

