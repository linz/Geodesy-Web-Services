package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "NEW_CORS_SITE_REQUEST")
public class NewCorsSiteRequest {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private Integer id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "ORGANISATION", nullable = false)
    private String organisation;

    @Column(name = "POSITION", nullable = false)
    private String position;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PHONE", nullable = false)
    private String phone;

    @Column(name = "SITELOG_DATA", nullable = false)
    private String siteLogData;

    @SuppressWarnings("unused") // used by hibernate
    private NewCorsSiteRequest() {
    }

    public NewCorsSiteRequest(
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("organisation") String organisation,
        @JsonProperty("position") String position,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone,
        @JsonProperty("siteLogData") String siteLogData) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.siteLogData = siteLogData;
    }

    Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getSiteLogData() {
        return siteLogData;
    }
}
