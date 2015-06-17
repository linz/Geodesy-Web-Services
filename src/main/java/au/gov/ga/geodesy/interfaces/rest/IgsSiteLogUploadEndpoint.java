package au.gov.ga.geodesy.interfaces.rest;

import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;

@Controller
@RequestMapping("/siteLog")
@Transactional("geodesyTransactionManager")
public class IgsSiteLogUploadEndpoint {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogUploadEndpoint.class);

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private IgsSiteLogXmlMarshaller siteLogMarshaller;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest req, HttpServletResponse rsp) throws Exception {
        IgsSiteLog siteLog = siteLogMarshaller.unmarshal(new InputStreamReader(req.getInputStream()));
        siteLogs.save(siteLog);
        log.info("Saving " + siteLog.getSiteIdentification().getFourCharacterId());
    }

}
