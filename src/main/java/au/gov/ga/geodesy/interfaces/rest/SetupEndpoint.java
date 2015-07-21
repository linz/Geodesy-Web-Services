package au.gov.ga.geodesy.interfaces.rest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import au.gov.ga.geodesy.domain.model.GnssCorsSite;
import au.gov.ga.geodesy.domain.model.GnssCorsSiteRepository;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SetupRepository;

@RepositoryRestController
@RequestMapping("/setups")
public class SetupEndpoint {

    @Autowired
    private SetupRepository setups;
    
    @Autowired
    private GnssCorsSiteRepository sites;

    @Autowired
    private PagedResourcesAssembler<Setup> assembler;

    @RequestMapping(value = "/search/findCurrentBySiteId", method = RequestMethod.GET,
            produces = "application/hal+json")
    @Transactional("geodesyTransactionManager")
    @ResponseBody
    public PersistentEntityResource findCurrentBySiteId(Integer siteId, PersistentEntityResourceAssembler assembler) {
        return assembler.toResource(setups.findCurrentBySiteId(siteId));
    }

    @RequestMapping(value = "/search/findByFourCharacterId", method = RequestMethod.GET,
            produces = "application/hal+json")
    public ResponseEntity<PagedResources<Resource<Setup>>> findByFourCharacterId(
            String id, Pageable pageRequest) {

        Page<Setup> page = null;
        GnssCorsSite site = sites.findByFourCharacterId(id);

        if (site != null) {
            page = setups.findBySiteId(site.getId(), pageRequest);
        } else {
            page = new PageImpl<Setup>(new ArrayList<Setup>());
        }
        PagedResources<Resource<Setup>> paged = assembler.toResource(page);
        return new ResponseEntity<>(paged, new HttpHeaders(), HttpStatus.OK);
    }
}
