package au.gov.ga.geodesy.port.adapter.geodesyml;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.support.mapper.dozer.GeodesyMLSiteLogDozerTranslator;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Translate Sopac file into GeodesyML
 */
@Service
public class SopacFileTranslator {

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    @Autowired
    private GeodesyMLSiteLogDozerTranslator geodesyMLSiteLogTranslator;

    @Autowired
    private GeodesyMLMarshaller geodesyMLMarshaller;

    private final Path outputTemporaryDir;

    public SopacFileTranslator() throws IOException {
        outputTemporaryDir = Files.createTempDirectory("geodesyMLOutput");
    }

    /**
     * Translate given single SOPAC Sitelog file to GeodesyML file in a temporary directory
     *
     * @param inputTestFile input file to translate
     * @return output translated file full path - Output file will be be in temp directory with same name as input file
     * @throws IOException
     * @throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException
     * @throws MarshallingException
     */
    public String translateFileToTempDirectory(File inputTestFile)
            throws IOException, au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException {
        return translateFile(inputTestFile, outputTemporaryDir);
    }

    /**
     * Translate given single SOPAC Sitelog file to GeodesyML file in given outputDirectory directory
     *
     * @param inputTestFile input file to translate
     * @return output translated file full path - Output file will be be in given directory with same name as input file
     * @throws IOException
     * @throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException
     * @throws MarshallingException
     */
    public String translateFile(File inputTestFile, Path outputDirectory)
            throws IOException, au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException {
        Logger logger = LoggerFactory.getLogger(SopacFileTranslator.class);

        Path outputFile = Paths.get(outputDirectory.toString(), getFileName(inputTestFile.getName()));
        Reader inputFileReader = new FileReader(inputTestFile);

        logger.info("Translate File - Sopac to GeodesyML");
        logger.info("  Input: " + inputTestFile.getName());
        logger.info("  Output: " + outputFile.toString());

        IgsSiteLog siteLog = marshaller.unmarshal(inputFileReader);

        JAXBElement<GeodesyMLType> geodesyMLJAXB = geodesyMLSiteLogTranslator.dozerTranslate(siteLog);

        GeodesyMLType geodesyML = geodesyMLJAXB.getValue();

        if (geodesyML == null) {
            throw new GeodesyRuntimeException("geodesyML == null");
        }

        FileWriter writer = new FileWriter(outputFile.toFile());
        geodesyMLMarshaller.marshal(geodesyMLJAXB, writer);

        return outputFile.toString();
    }

    /**
     * Given a fileName in a path, return just the filename.
     *
     * @param fileName
     * @return just file name after final '/' or the fileName if no '/'
     */
    private static String getFileName(String fileName) {
        String[] parts = fileName.split("/");
        return parts.length - 1 >= 0 ? (parts[parts.length - 1]) : fileName;
    }
}
