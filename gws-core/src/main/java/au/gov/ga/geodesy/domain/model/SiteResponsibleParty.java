package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.opengis.metadata.citation.ResponsibleParty;

import au.gov.ga.geodesy.support.persistence.jpa.ResponsiblePartyJpaConverter;

/**
 * Combination of GMD responsible party and contact type (site owner, metadata custodian, etc).
 *
 * @see ContactType
 */
@Entity
@Table(name = "SITELOG_RESPONSIBLE_PARTY")
public class SiteResponsibleParty {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private Integer id;

    @Column(name = "RESPONSIBLE_ROLE_ID", nullable = false)
    private Integer contactTypeId;

    @Convert(converter = ResponsiblePartyJpaConverter.class)
    @Column(name = "RESPONSIBLE_PARTY")
    private ResponsibleParty party;

    @SuppressWarnings("unused") // used by hibernate
    private SiteResponsibleParty() {
    }

    public SiteResponsibleParty(Integer contactTypeId, ResponsibleParty party) {
        this.contactTypeId = contactTypeId;
        this.party = party;
    }

    public Integer getContactTypeId() {
        return contactTypeId;
    }

    public void setContactTypeId(Integer contactTypeId) {
        this.contactTypeId = contactTypeId;
    }

    public ResponsibleParty getParty() {
        return party;
    }

    public void setParty(ResponsibleParty party) {
        this.party = party;
    }
}
