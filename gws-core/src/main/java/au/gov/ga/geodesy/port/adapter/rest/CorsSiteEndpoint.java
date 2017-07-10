package au.gov.ga.geodesy.port.adapter.rest;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import au.gov.ga.geodesy.domain.model.CorsNetworkRepository;
import au.gov.ga.geodesy.domain.model.CorsSite;
import au.gov.ga.geodesy.domain.model.CorsSiteRepository;
import au.gov.ga.geodesy.domain.model.command.AddCorsSiteToNetwork;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;

@RepositoryRestController
@RequestMapping("/corsSites")
public class CorsSiteEndpoint {

    @Autowired
    private CorsSiteRepository sites;

    @Autowired
    private CorsNetworkRepository networks;

    @Autowired
    private EventPublisher eventPublisher;

    @RequestMapping(
        value = "/{siteId}/addToNetwork",
        method = RequestMethod.PUT
    )
    public ResponseEntity<String> addToNetwork(
        @PathVariable("siteId") Integer siteId,
        @RequestParam("networkId") Integer networkId,
        @RequestParam(required = false) String effectiveFrom,
        @RequestParam(required = false) String effectiveTo,
        @RequestParam(defaultValue = "uuuu-MM-dd") String timeFormat) {

        CorsSite site = sites.findOne(siteId);
        if (!networks.exists(networkId)) {
            return new ResponseEntity<String>("Network not found", HttpStatus.BAD_REQUEST);
        }
        EffectiveDates period = new EffectiveDates(
            parse(effectiveFrom, timeFormat),
            parse(effectiveTo, timeFormat)
        );
        site.handle(new AddCorsSiteToNetwork(networkId, period)).forEach(eventPublisher::publish);
        sites.save(site);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    private Instant parse(String time, String pattern) {
        return GMLDateUtils.stringToDate(time, pattern);
    }
}
