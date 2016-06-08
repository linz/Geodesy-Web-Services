package au.gov.ga.geodesy.port.adapter.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.CorsSiteLogService;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLSiteLogReader;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLValidator;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.xmlschemer.Violation;

@Controller
@EnableEntityLinks
@RequestMapping("/siteLog")
public class SiteLogEndpoint {

    private static final Logger log = LoggerFactory.getLogger(SiteLogEndpoint.class);

    @Autowired
    private CorsSiteLogService service;

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    private GeodesyMLValidator geodesyMLValidator;

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ResponseEntity<List<Violation>> validateGeodesyMLSiteLog(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        StreamSource source = new StreamSource(req.getInputStream(), "data:");
        List<Violation> violations = geodesyMLValidator.validate(source);
        if (violations.isEmpty()) {
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.badRequest().body(violations);
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadGeodesyMLSiteLog(HttpServletRequest req, HttpServletResponse rsp) throws IOException, InvalidSiteLogException {
        SiteLogReader reader = new GeodesyMLSiteLogReader(new InputStreamReader(req.getInputStream()));
        SiteLog siteLog = reader.getSiteLog();
        service.upload(siteLog);
        try {
            String location = entityLinks.linkToSingleResource(SiteLog.class, siteLog.getId()).getHref();
            return ResponseEntity.created(new URI(location)).body("");
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/sopac/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadSopacSiteLog(
            HttpServletRequest req, HttpServletResponse rsp) throws IOException, InvalidSiteLogException {

        String siteLogText = IOUtils.toString(req.getInputStream());
        log.debug("Received SOPAC site log: " + siteLogText);
        SiteLogReader reader = new SopacSiteLogReader(new StringReader(siteLogText));
        SiteLog siteLog = reader.getSiteLog();
        service.upload(siteLog);
        try {
            String location = entityLinks.linkToSingleResource(SiteLog.class, siteLog.getId()).getHref();
            return ResponseEntity.created(new URI(location)).body("");
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidSiteLogException.class)
    public void invalidSiteLogHandler(InvalidSiteLogException e) {
        log.error("Received invalid site log", e);
    }
}
