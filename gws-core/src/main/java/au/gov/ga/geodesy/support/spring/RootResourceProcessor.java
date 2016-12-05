package au.gov.ga.geodesy.support.spring;

import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;

/**
 * Customise repository links resource.
 */
public class RootResourceProcessor implements ResourceProcessor<RepositoryLinksResource> {

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        Link equipment = resource.getLink("equipments").withRel("equipment");
        resource.getLinks().remove(resource.getLink("equipments"));
        resource.add(equipment);
        return resource;
    }
}
