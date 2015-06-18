package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import au.gov.ga.geodesy.support.spring.EntityId;

@Entity
@Table(name = "gnss_cors_site")
public class GnssCorsSite extends Site {

    /* @Column(name = "id", length = 36, nullable = false, unique = true) */
    /* @Type(type = "au.gov.ga.geodesy.support.hibernate.EntityIdUserType") */
    /* private EntityId<GnssCorsSite> gnssCorsSiteId; */

    /**
     * Business id
     */
    @Column(name = "four_character_id", length = 4, nullable = false, unique = true)
    private String fourCharacterId;

    /* public EntityId<GnssCorsSite> gnssCorsSiteId() { */
    /*     return gnssCorsSiteId; */
    /* } */

    /* public GnssCorsSite(EntityId<GnssCorsSite> id, String fourCharacterId) { */
    public GnssCorsSite(String fourCharacterId) {
        super();
        /* gnssCorsSiteId = id; */
        setFourCharacterId(fourCharacterId);
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }

    private void setFourCharacterId(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }
}
