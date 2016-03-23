package au.gov.ga.geodesy.port.adapter.rest;

import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.gov.ga.geodesy.domain.service.IgsSiteLogService;
import au.gov.ga.geodesy.port.SiteLogSource;
import au.gov.ga.geodesy.port.adapter.sopac.SiteLogSopacReader;

@Controller
@RequestMapping("/siteLog")
public class SiteLogEndpoint {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(SiteLogEndpoint.class);

    @Autowired
    private IgsSiteLogService service;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest req, HttpServletResponse rsp) throws Exception {
        SiteLogSource input = new SiteLogSopacReader(new InputStreamReader(req.getInputStream()));
        service.upload(input.getSiteLog());
    }
}
