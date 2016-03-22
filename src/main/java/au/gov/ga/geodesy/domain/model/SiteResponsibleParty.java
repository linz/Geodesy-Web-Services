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

@Entity
@Table(name = "RESPONSIBLE_PARTY")
public class SiteResponsibleParty {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private Integer id;

    @Convert(converter = ResponsiblePartyJpaConverter.class)
    @Column(name = "ISO_19115", length=4096)
    private ResponsibleParty party;

    public ResponsibleParty getParty() {
        return party;
    }

    public void setParty(ResponsibleParty party) {
        this.party = party;
    }
}
