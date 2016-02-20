package au.gov.ga.geodesy.domain.model;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.interfaces.xml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.interfaces.xml.MarshallingException;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.MonumentPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.MonumentType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteType;

import net.opengis.gml.v_3_2_1.CodeType;

@Entity
@Table(name = "GNSS_SITE_LOG")
@Configurable(preConstruction = true)
public class GnssSiteLog {

    @Autowired
    @Transient
    private GeodesyMLMarshaller geodesyMLMarshaller;

    @Version
    private Integer version;

    /**
     * RDBMS surrogate key
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    protected Integer id;

    @Column(name = "FOUR_CHAR_ID", unique = true, nullable = false)
    private String fourCharacterId;

    @Column(name = "GEODESY_ML", nullable = false, length = 30000)
    private String geodesyML;

    @SuppressWarnings("unused") // used by hibernate
    private GnssSiteLog() {}

    public GnssSiteLog(String geodesyML) throws MarshallingException {
        setGeodesyML(geodesyML);
    }
    
    public String getFourCharacterId() {
        return fourCharacterId;
    }

    protected void setFourCharacterId(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }

    public String getGeodesyML() {
        return geodesyML;
    }

    private void setGeodesyML(String geodesyML) throws MarshallingException {
        Reader input = new StringReader(geodesyML);
        GeodesyMLType document = geodesyMLMarshaller.unmarshal(input).getValue();

        Optional<SiteType> siteType = getOneElement(document, SiteType.class);

        if (siteType.isPresent()) {
            Optional<MonumentType> monument = getMonument(document, siteType.get());

            if (monument.isPresent()) {
                Optional<String> id = getFourCharacterId(monument.get());
                if (id.isPresent()) {
                    setFourCharacterId(id.get());
                }
            } else {
                System.out.println("No monument!");
            }

            System.out.println("GOT ID: " + getFourCharacterId());
            this.geodesyML = geodesyML;
        } else {
            throw new RuntimeException("no site log element");
        }
    }

    private Optional<String> getFourCharacterId(MonumentType monument) {
        return monument.getName()
            .stream()
            .filter(code -> code.getCodeSpace().equals("urn:ga-gov-au:monument-fourCharacterID"))
            .findFirst()
            .map(CodeType::getValue);
    }

    @SuppressWarnings("unchecked")
    private <T> Stream<T> getElements(GeodesyMLType geodesyML, Class<T> type) {
        List<JAXBElement<?>> elements = geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance();
        return elements.stream()
            .map(JAXBElement::getValue)
            .filter(x -> type.isAssignableFrom(x.getClass()))
            .map(x -> (T) x);
    }

    private <T> Optional<T> getOneElement(GeodesyMLType geodesyML, Class<T> type) {
        return getElements(geodesyML, type).findFirst();
    }

    private Optional<MonumentType> getMonument(GeodesyMLType geodesyML, SiteType siteML) {
        MonumentPropertyType property = siteML.getMonument();
        if (property == null) {
            return Optional.empty();
        }
        if (!StringUtils.isEmpty(property.getHref())) {
            return getElements(geodesyML, MonumentType.class)
                .filter(m -> !m.getId().equals(property.getHref()))
                .findFirst();
        }
        return Optional.of(property.getMonument());
    }
}
