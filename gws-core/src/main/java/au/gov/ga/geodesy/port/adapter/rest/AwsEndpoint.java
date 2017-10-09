package au.gov.ga.geodesy.port.adapter.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import au.gov.ga.geodesy.support.aws.Aws;

@Controller
@RequestMapping("/aws")
public class AwsEndpoint {

    @Autowired
    private Aws aws;

    @RequestMapping(
        value = "/stackName",
        method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getStackName() {
        Optional<String> stackName = aws.getStackName();

        if (stackName.isPresent()) {
            return new ResponseEntity<String>(stackName.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        }
    }
}
