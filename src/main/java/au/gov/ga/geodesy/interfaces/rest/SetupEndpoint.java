package au.gov.ga.geodesy.interfaces.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import au.gov.ga.geodesy.domain.model.SetupRepository;

@RepositoryRestController
@RequestMapping("/setups")
public class SetupEndpoint {

    @Autowired
    private SetupRepository setups;

    @RequestMapping(value = "/search/findCurrentBySiteId", method = RequestMethod.GET, produces = "application/hal+json")
    @Transactional("geodesyTransactionManager")
    @ResponseBody
    public PersistentEntityResource findCurrentBySiteId(Integer siteId, PersistentEntityResourceAssembler assembler) {
        return assembler.toResource(setups.findCurrentBySiteId(siteId));
    }
}
