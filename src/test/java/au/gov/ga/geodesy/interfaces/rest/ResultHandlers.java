package au.gov.ga.geodesy.interfaces.rest;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
 
public class ResultHandlers {
    public static ResultHandler print = new ResultHandler() {
        public void handle(MvcResult result) throws Exception {
            System.out.println(result.getResponse().getContentAsString());
            Exception e = result.getResolvedException();
            if (e != null) {
                e.printStackTrace();
            }
        }
    };
}
