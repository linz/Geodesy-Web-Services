package au.gov.ga.geodesy.port.adapter.rest;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.MarshallingException;
import au.gov.ga.geodesy.port.adapter.geodesyml.SopacFileTranslator;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.mapper.dozer.GeodesyMLSiteLogDozerTranslator;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBElement;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static au.gov.ga.geodesy.port.adapter.rest.ResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ZIMJ was randomly chosen as a test case for the RadioInterference's EffectiveDates fixes I recently made.  There are various SOPAC test inputs.
 * This sopac -> geodesyml (dozer mapping) converts these and then uploads them.
 */
public class UploadZIMJGeodesyMLSiteLogRestTest extends RestTest {

    @Autowired
    private SopacFileTranslator sopacFileTranslator;

    /**
     * Translate Sopace to GeodesyML file in resources sopac testData directory
     *
     * @param file in resources sopac testData directory
     * @return the output (translated) file path
     * @throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException
     * @throws MarshallingException
     * @throws IOException
     */
    private String translateFile(String file) throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException, IOException {
        return sopacFileTranslator.translateFileToTempDirectory(TestResources.sopacSiteLogTestData(file));
    }

    private void uploadCheckFile(String file) throws Exception {
        String content = IOUtils.toString(new FileReader(file));
        mvc.perform(post("/siteLog/upload").contentType(MediaType.APPLICATION_XML).content(content))
                .andExpect(status().isCreated());
    }

    private void checkUpload(String fourCharacterID) throws Exception {
        mvc.perform(get("/corsSites/search/findByFourCharacterId?id=" + fourCharacterID))
                .andExpect(status().isOk())
                .andDo(print);
    }

    @Test
    public void checkZIMJ_withEffectiveDates() throws Exception {
        String outfile = translateFile("ZIMJ-radioInterference-withEffectiveDates");
        uploadCheckFile(outfile);
        checkUpload("ZIMJ");
    }

    @Test
    public void checkZIMJ_noEffectiveDates() throws Exception {
        String outfile = translateFile("ZIMJ-radioInterference-noEffectiveDates");
        uploadCheckFile(outfile);
        checkUpload("ZIMJ");
    }

    @Test
    public void checkZIMJ_withEffectiveDatesNoToForm1() throws Exception {
        String outfile = translateFile("ZIMJ-radioInterference-withEffectiveDatesNoToForm1");
        uploadCheckFile(outfile);
        checkUpload("ZIMJ");
    }

    @Test
    public void checkZIMJ_withEffectiveDatesNoToForm2() throws Exception {
        String outfile = translateFile("ZIMJ-radioInterference-withEffectiveDatesNoToForm2");
        uploadCheckFile(outfile);
        checkUpload("ZIMJ");
    }
}
