package au.gov.ga.geodesy.interfaces.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import au.gov.ga.geodesy.domain.service.WeeklySolutionService;

@Controller
@RequestMapping("/solution/weekly")
public class WeeklySolutionEndpoint {

    @Autowired
    private WeeklySolutionService service;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam("sinexFileName") String sinexFilename) throws Exception {
        service.uploadSolution(sinexFilename);
    }
}
