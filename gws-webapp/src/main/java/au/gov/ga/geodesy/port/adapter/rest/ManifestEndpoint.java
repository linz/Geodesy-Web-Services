package au.gov.ga.geodesy.port.adapter.rest;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.java.util.service.ManifestService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Endpoint to retrieve information for selected Manifests in all dependant (and this) Jars.
 */
@RestController
@RequestMapping("/manifests")
public class ManifestEndpoint implements ResourceProcessor<RepositoryLinksResource> {
    private static final Logger logger = LoggerFactory.logger(ManifestEndpoint.class);
    @Autowired
    private ManifestService manifestService;

    @Autowired
    private ServletContext servletContext;

    @PostConstruct
    public void initService() {
        // We need to use the Manifest from this web app.
        URL thisAppsManifestURL;
        try {
            thisAppsManifestURL = servletContext.getResource("/META-INF/MANIFEST.MF");
        } catch (MalformedURLException e) {
            throw new GeodesyRuntimeException("Error retrieving META-INF/MANIFEST.MF resource from webapp", e);
        }
        manifestService.buildManifest(thisAppsManifestURL);
    }

    @Override
    /**
     * Define Top-Level / Index / Document-Root HATEAOS links when you hit /
     */
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.add(ControllerLinkBuilder.linkTo(methodOn(ManifestEndpoint.class).getGeodesyInternalManifest()).withRel("manifests/internal"));
        resource.add(ControllerLinkBuilder.linkTo(methodOn(ManifestEndpoint.class).getGeodesyExternalManifest()).withRel("manifests/external"));

        return resource;
    }

    @RequestMapping(value = "/internal", method = RequestMethod.GET,
            produces = "application/hal+json")
    public HttpEntity<Resource<Map<String, ManifestService.GAManifestData>>> getGeodesyInternalManifest() {
        final Map<String, ManifestService.GAManifestData> internalManifests = manifestService.getInternalManifests();

        // HATEOS enabler
        Link link = linkTo(methodOn(ManifestEndpoint.class).getGeodesyInternalManifest()).withSelfRel();
        Resource<Map<String, ManifestService.GAManifestData>> resource = new Resource<>(internalManifests, link);

        System.out.println("internalManifests: " + internalManifests);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "/external", method = RequestMethod.GET,
            produces = "application/hal+json")
    public HttpEntity<Resource<Map<String, ManifestService.GAManifestData>>> getGeodesyExternalManifest() {
        final Map<String, ManifestService.GAManifestData> externalManifests = manifestService.getExternalManifests();

        // HATEOS enabler
        Link link = linkTo(methodOn(ManifestEndpoint.class).getGeodesyExternalManifest()).withSelfRel();
        Resource<Map<String, ManifestService.GAManifestData>> resource = new Resource<>(externalManifests, link);

        System.out.println("externalManifests: " + externalManifests);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
