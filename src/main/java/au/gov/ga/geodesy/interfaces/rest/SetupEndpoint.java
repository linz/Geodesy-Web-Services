package au.gov.ga.geodesy.interfaces.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SetupRepository;

@Controller
@RequestMapping("/setups")
public class SetupEndpoint {

    @Autowired
    private SetupRepository setups;

    @RequestMapping(value = "/search/findCurrentBySiteId", method = RequestMethod.GET, produces = "application/hal+json")
    public Setup findCurrentBySiteId(Integer siteId) {
        return setups.findCurrentBySiteId(siteId);
    }
}
