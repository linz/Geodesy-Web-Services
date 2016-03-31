package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.MonumentPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.MonumentType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import au.gov.xml.icsm.geodesyml.v_0_3.SitePropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteType;
import net.opengis.gml.v_3_2_1.CodeType;

/**
 * Create {@code GnssSiteLog} instances from GeodesyML documents. One geodesyML document can contain
 * multiple site logs.
 */
public class CorsSiteLogFactory {

    private GeodesyMLType geodesyML;
    private XLinkResolver xlinkResolver = new XLinkResolver();

    /**
     * The factory needs a reference to an entire geodesyML document, because not all site
     * log data can be reached without following xlink references.
     */
    public CorsSiteLogFactory(GeodesyMLType ml) {
        geodesyML = ml;
    }

    /**
     * Given a geodesyML document, attempt to look up an element by its gml:id.
     * TODO: Move out of GnssSiteLogFactory or replace with private methods.
     */
    public class XLinkResolver {

        private List<JAXBElement<?>> topLevelElements() {
            return geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance();
        }

        private Pair<?, Method> getIdMethod(JAXBElement<?> element) {
            Object x = element.getValue();
            return Pair.of(x, MethodUtils.getAccessibleMethod(x.getClass(), "getId", new Class[] {}));
        }

        @SuppressWarnings("unchecked")
        public <T> Optional<T> resolve(String targetHref, Class<T> type) {
            return topLevelElements()
                    .stream()
                    .map(element -> getIdMethod(element))
                    .filter(pair -> pair.getRight() != null)
                    .filter(pair -> {
                        try {
                            String candidateId = (String) pair.getRight().invoke(pair.getLeft(), new Object[] {});
                            return targetHref.substring(1).equals(candidateId); // strip # from href
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .map(pair -> (T) pair.getLeft())
                    .findFirst(); // TODO: assume uniques
        }
    }

    private <T> Optional<T> resolve(String href, Class<T> type) {
        return xlinkResolver.resolve(href, type);
    }

    public Stream<SiteLog> create() {
        return GeodesyMLUtils
                .getElementFromJAXBElements(geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance(),
                        SiteLogType.class)
                .map(this::create)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<SiteType> getSiteML(SitePropertyType sitePropertyML) {
        return resolve(sitePropertyML.getHref(), SiteType.class);
    }

    private Optional<MonumentType> getMonumentML(MonumentPropertyType monumentPropertyML) {
        return resolve(monumentPropertyML.getHref(), MonumentType.class);
    }

    private Optional<SiteLog> create(SiteLogType siteLogML) {
        return getSiteML(siteLogML.getAtSite())
            .flatMap(siteML -> getMonumentML(siteML.getMonument()))
            .flatMap(this::getFourCharacterId)
            .flatMap(id -> Optional.of(new SiteLog()));
    }

    private Optional<String> getFourCharacterId(MonumentType monument) {
        return monument.getName()
                .stream()
                .filter(code -> code.getCodeSpace().equals("urn:ga-gov-au:monument-fourCharacterID"))
                .findFirst()
                .map(CodeType::getValue);
    }
}
