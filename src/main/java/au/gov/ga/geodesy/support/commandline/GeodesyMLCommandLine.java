package au.gov.ga.geodesy.support.commandline;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;
import au.gov.ga.geodesy.interfaces.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.interfaces.geodesyml.MarshallingException;
import au.gov.ga.geodesy.support.mapper.dozer.GeodesyMLSiteLogDozerTranslator;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;

public class GeodesyMLCommandLine {
    private final Logger logger = LoggerFactory.getLogger(GeodesyMLCommandLine.class);

    private final static Option INPUT_FILE_OPT = Option.builder("infile").required(false).type(String.class)
            .longOpt("infile").argName("file").hasArg().desc("use given buildfile").build();
    private final static Option OUTPUT_DIR_OPT = Option.builder("outdir").required(true).type(String.class).longOpt("outdir")
            .argName("dir").hasArg().desc("output directory").build();
    private final static Option INPUT_DIR_OPT = Option.builder("indir").required(false).type(String.class)
            .longOpt("indir").argName("dir").hasArg().desc("input directory").build();

    // @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    // @Autowired
    private GeodesyMLSiteLogDozerTranslator geodesyMLSiteLogTranslator;

    // @Autowired
    private GeodesyMLMarshaller geodesyMLMarshaller;

    public GeodesyMLCommandLine(String[] args)
            throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException, IOException {
        marshaller = new IgsSiteLogMoxyMarshaller();
        geodesyMLSiteLogTranslator = new GeodesyMLSiteLogDozerTranslator();
        geodesyMLMarshaller = new GeodesyMLMoxy();

        Options options = new Options();
        options.addOption(INPUT_FILE_OPT);
        options.addOption(OUTPUT_DIR_OPT);
        options.addOption(INPUT_DIR_OPT);

        CommandLineParser parser = new DefaultParser();

        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String inputFile = line.getOptionValue("infile");
        String outputDir = line.getOptionValue("outdir");
        String inputDir = line.getOptionValue("indir");
        logger.debug("  inputFile: " + inputFile);
        logger.debug("  outdir: " + outputDir);
        logger.debug("  indir: " + inputDir);

        if (!StringUtils.isBlank(inputFile)) {
            processFileToDir(inputFile, outputDir);
        } else if (!StringUtils.isBlank(inputDir)) {
            processDir(inputDir, outputDir);
        }
    }

    /**
     * Process directory of input files. Build list of files that fail and
     * return or print ???
     * 
     * @param inputDir
     *            - directory of input files
     * @param outputDir
     *            - directory to write processing files
     * @throws IOException
     */
    private void processDir(String inputDir, String outputDir) {
        List<Path> successfulFiles = new ArrayList<>();
        List<Path> failedFiles = new ArrayList<>();
        Path currentPath = null;

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(inputDir))) {
            for (Path testFile : dirStream) {
                try {
                    currentPath = testFile;
                    Path testOutFile = returnTestFile(outputDir, testFile);
                    logger.info("process file: " + testFile.toString());
                    processFile(testFile, testOutFile);
                    successfulFiles.add(currentPath);
                } catch (au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException e) {
                    failedFiles.add(currentPath);
                    e.printStackTrace();
                } catch (MarshallingException e) {
                    failedFiles.add(currentPath);
                    e.printStackTrace();
                } catch (GeodesyRuntimeException e) {
                    System.out.println("GeodesyRuntimeException");
                    failedFiles.add(currentPath);
                    e.printStackTrace();
                    System.out.println("GeodesyRuntimeException after");
                }
            }
        } catch (IOException e) {
            failedFiles.add(currentPath);
            e.printStackTrace();
        }
        logger.info("Successfully processed this # files: " + successfulFiles.size());
        logger.error("Failed to process these files: " + failedFiles);
    }

    /**
     * Process single file - String arguments
     * 
     * @param inputFile
     * @param outputDir
     * @throws IOException
     * @throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException
     */
    private void processFileToDir(String inputFile, String outputDir)
            throws IOException, au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException {

        Path testOutFile = returnTestFile(outputDir, inputFile);
        processFile(Paths.get(inputFile), testOutFile);
    }

    /**
     * Process single file - Path arguments
     * 
     * @param testFile
     * @param testOutFile
     * @throws IOException
     * @throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException
     */
    private void processFile(Path inputFile, Path outputFile)
            throws IOException, au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException {
        logger.info("------------------------------------------------------------------------------------------------");
        logger.info("processFile - input: " + inputFile.toString() + ", write to xml file: " + outputFile.toString());

        if (Files.isRegularFile(inputFile) && inputFile.toString().endsWith("xml")) {
            Reader input = new InputStreamReader(new FileInputStream(inputFile.toFile()));

            IgsSiteLog siteLog = marshaller.unmarshal(input);

            JAXBElement<GeodesyMLType> geodesyMLJAXB = geodesyMLSiteLogTranslator.dozerTranslate(siteLog);

            GeodesyMLType geodesyML = geodesyMLJAXB.getValue();

            if (geodesyML == null) {
                throw new GeodesyRuntimeException("geodesyML == null");
            }

            FileWriter writer = new FileWriter(outputFile.toFile());
            geodesyMLMarshaller.marshal(geodesyMLJAXB, writer);
        } else {
            logger.warn("File not processed: " + inputFile.toString());
        }
    }

    /**
     * @param fileName
     * @return file with name fileName in some constant location.
     * @throws IOException
     */
    static Path returnTestFile(String outputDir, String fileName) throws IOException {
        return returnTestFile(outputDir, Paths.get(getFileName(fileName)));
    }

    static Path returnTestFile(String outputDir, Path fileNamePath) throws IOException {
        Path tempDirName = Paths.get(outputDir);
        Path tempDir = null;
        if (Files.exists(tempDirName)) {
            tempDir = tempDirName;
        } else {
            tempDir = Files.createDirectory(tempDirName);
        }
        Path tempFileName = tempDir.resolve(fileNamePath.getFileName());
        Path tempFile = null;
        if (Files.exists(tempFileName)) {
            tempFile = tempFileName;
        } else {
            tempFile = Files.createFile(tempFileName);
        }
        return tempFile;
    }

    /**
     * Given a fileName in a path, return just the filename.
     * 
     * @param fileName
     * @return just file name after final '/' or the fileName if no '/'
     */
    static String getFileName(String fileName) {
        String[] parts = fileName.split("/");
        return parts.length - 1 >= 0 ? (parts[parts.length - 1]) : fileName;
    }

    public static void main(String[] args) {
        try {
            System.out.println("GeodesyMLCommandLine - args: " + Arrays.asList(args));
            new GeodesyMLCommandLine(args);
        } catch (au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException | MarshallingException
                | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
