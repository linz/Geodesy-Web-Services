package au.gov.ga.geodesy.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of static methods to access regression test data.
 */
public class TestResources {

    private static final PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    /**
     * Don't instantiate, use the static methods.
     */
    private TestResources() {
    }

    /**
     * SOPAC site logs directory relative to classpath root.
     */
    private static String sopacSiteLogsDirectory() {
        return "/sitelog/sopac/";
    }

    /**
     * GeodesyML site logs directory relative to classpath root.
     */
    private static String geodesyMLSiteLogsDirectory() {
        return "/sitelog/geodesyml/";
    }

    private static String geodesyMLSiteLogsTestDataDirectory() {
        return "/sitelog/testData/";
    }

    /**
     * GeodesyML site logs Test Data directory (files with modifications) relative to classpath root.
     */
    private static String geodesyMLTestDataSiteLogsDirectory() {
        return "/sitelog/geodesyMLTestData/";
    }

    /**
     * GeodesyML site logs directory relative to classpath root.  Files that have been converted from production Sopac Sitelogs
     */
    private static String geodesyMLSopacConvertedSiteLogsDirectory() {
        return "/sitelogtoGeodesyML/";
    }

    /**
     * Given a site id, return a SOPAC site log test file.
     */
    public static File sopacSiteLog(String siteId) throws IOException {
        return resourceAsFile(sopacSiteLogResourceName(siteId));
    }

    /**
     * Given a site id, return a SOPAC site log test file.
     */
    public static File sopacSiteLogTestData(String siteId) throws IOException {
        return resourceAsFile(geodesyMLSiteLogTestDataResourceName(siteId));
    }

    /**
     * Given a site id, return a SOPAC site log test file reader.
     */
    public static Reader sopacSiteLogReader(String siteId) throws IOException {
        return new FileReader(resourceAsFile(sopacSiteLogResourceName(siteId)));
    }

    /**
     * Given a site id, return a GeodesyML site log test file reader.  File from resources:/sitelog/sopac/
     */
    public static Reader geodesyMLSiteLogReader(String siteId) throws IOException {
        return new FileReader(resourceAsFile(geodesyMLSiteLogResourceName(siteId)));
    }

    /**
     * Given a site id, return a sopac converted to GeodesyML site log test file reader.  File from resources:/sitelogtoGeodesyML
     */
    public static Reader geodesyMLSopacConvertedSiteLogReader(String siteId) throws IOException {
        return new FileReader(resourceAsFile(geodesyMLSopacConvertedSiteLogResourceName(siteId)));
    }

    /**
     * Given a site id, return a sopac converted to GeodesyML site log test file reader.  File from resources:/sitelog/testData
     */
    public static Reader geodesyMLTestDataSiteLogReader(String siteId) throws IOException {
        return new FileReader(resourceAsFile(geodesyMLTestDataSiteLogResourceName(siteId)));
    }

    /**
     * Return all SOPAC site log test files.
     */
    public static List<File> sopacSiteLogs() throws IOException {
        return sopacSiteLogs("*");
    }

    /**
     * Given a site id pattern return all matching SOPAC site log test files.
     */
    public static List<File> sopacSiteLogs(String siteIdPattern) throws IOException {
        Resource[] resources = resourceResolver.getResources("classpath:" + sopacSiteLogsDirectory() + siteIdPattern + ".xml");
        List<File> files = new ArrayList<>(resources.length);
        for (Resource r : resources) {
            files.add(r.getFile());
        }
        return files;
    }

    /**
     * SOPAC site log resource name relative to classpath root.
     */
    private static String sopacSiteLogResourceName(String id) {
        return sopacSiteLogsDirectory() + id + ".xml";
    }

    /**
     * GeodesyML site log resource name relative to classpath root.
     */
    private static String geodesyMLSiteLogResourceName(String id) {
        return geodesyMLSiteLogsDirectory() + id + ".xml";
    }

    private static String geodesyMLSiteLogTestDataResourceName(String id) {
        return geodesyMLSiteLogsTestDataDirectory() + id + ".xml";
    }

    /**
     * GeodesyML site log resource name relative to classpath root.
     */
    private static String geodesyMLSopacConvertedSiteLogResourceName(String id) {
        return geodesyMLSopacConvertedSiteLogsDirectory() + id + ".xml";
    }

    /**
     * GeodesyML site log resource name relative to classpath root.
     */
    private static String geodesyMLTestDataSiteLogResourceName(String id) {
        return geodesyMLTestDataSiteLogsDirectory() + id + ".xml";
    }

    /**
     * Find a resource relative to classpath root.
     */
    public static File resourceAsFile(String resourceName) throws IOException {
        Resource resource = new PathMatchingResourcePatternResolver().getResource("classpath:" + resourceName);
        if (resource == null) {
            throw new IllegalArgumentException("Resource " + resourceName + " must exist.");
        }
        return resource.getFile();
    }
}
