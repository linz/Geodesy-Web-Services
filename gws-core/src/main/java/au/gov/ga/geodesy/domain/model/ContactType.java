package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SITELOG_RESPONSIBLE_PARTY_ROLE")
public class ContactType {

    public static final String SITE_OWNER = "SiteOwner";
    public static final String SITE_CONTACT = "SiteContact";
    public static final String SITE_METADATA_CUSTODIAN = "SiteMetadataCustodian";
    public static final String SITE_DATA_CENTER = "SiteDataCenter";
    public static final String SITE_DATA_SOURCE = "SiteDataSource";

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "RESPONSIBLE_ROLE_XMLTAG", nullable = false)
    private String code;

    @Column(name = "RESPONSIBLE_ROLE_NAME", nullable = false)
    private String name;

    private ContactType() {
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    @SuppressWarnings("unused") // used by hibernate
    private void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused") // used by hibernate
    private void setName(String name) {
        this.name = name;
    }
}
