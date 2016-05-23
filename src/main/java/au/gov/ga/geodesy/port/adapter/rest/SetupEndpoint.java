package au.gov.ga.geodesy.port.adapter.rest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import au.gov.ga.geodesy.domain.model.CorsSite;
import au.gov.ga.geodesy.domain.model.CorsSiteRepository;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SetupRepository;

@RepositoryRestController
@RequestMapping("/setups")
public class SetupEndpoint {

    @Autowired
    private SetupRepository setups;

    @Autowired
    private CorsSiteRepository sites;

    @Autowired
    private PagedResourcesAssembler<Setup> assembler;

    @RequestMapping(
        value = "/search/findByFourCharacterId",
        method = RequestMethod.GET,
        produces = "application/hal+json")

    public ResponseEntity<PagedResources<Resource<Setup>>> findByFourCharacterId(
            @RequestParam("id") String fourCharId,
            String effectiveFrom,
            String effectiveTo,
            @RequestParam(defaultValue = "yyyy-MM-dd") String timeFormat,
            Pageable pageRequest) {

        Page<Setup> page = null;
        CorsSite site = sites.findByFourCharacterId(fourCharId);

        if (site != null) {
            page = setups.findBySiteIdAndDateRange(
                    site.getId(),
                    parseDate(timeFormat, effectiveFrom),
                    parseDate(timeFormat, effectiveTo),
                    pageRequest);
        } else {
            page = new PageImpl<Setup>(new ArrayList<Setup>());
        }
        PagedResources<Resource<Setup>> paged = assembler.toResource(page);
        return new ResponseEntity<>(paged, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(
        value = "/search/findCurrentByFourCharacterId",
        method = RequestMethod.GET,
        produces = "application/hal+json")

    @ResponseBody
    @Transactional("geodesyTransactionManager")
    public ResponseEntity<PersistentEntityResource> findCurrentByFourCharacterId(
            @RequestParam("id") String fourCharId,
            PersistentEntityResourceAssembler assembler) {

        CorsSite site = sites.findByFourCharacterId(fourCharId);
        if (site == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Setup setup = setups.findCurrentBySiteId(site.getId());
        if (setup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(assembler.toResource(setup));
    }

    private Date parseDate(String pattern, String str) {
        try {
            return FastDateFormat.getInstance(pattern, TimeZone.getTimeZone("UTC")).parse(str);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
