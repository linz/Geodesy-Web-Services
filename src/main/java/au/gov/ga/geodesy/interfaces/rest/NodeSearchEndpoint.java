package au.gov.ga.geodesy.interfaces.rest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.gov.ga.geodesy.domain.model.CorsSite;
import au.gov.ga.geodesy.domain.model.CorsSiteRepository;
import au.gov.ga.geodesy.domain.model.Node;
import au.gov.ga.geodesy.domain.model.NodeRepository;

@Controller
@RequestMapping("/nodes/search")
public class NodeSearchEndpoint {

    @Autowired
    private NodeRepository nodes;

    @Autowired
    private CorsSiteRepository sites;

    @Autowired
    private PagedResourcesAssembler<Node> assembler;

    @RequestMapping(value = "/findByFourCharacterId", method = RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<PagedResources<Resource<Node>>> findByFourCharacterId(
            String id, Pageable pageRequest) {

        Page<Node> page = null;
        CorsSite site = sites.findByFourCharacterId(id);
        if (site != null) {
            page = nodes.findBySiteId(site.getId(), pageRequest);
        } else {
            page = new PageImpl<Node>(new ArrayList<Node>());
        }
        PagedResources<Resource<Node>> paged = assembler.toResource(page);
        return new ResponseEntity<>(paged, new HttpHeaders(), HttpStatus.OK);
    }
}
