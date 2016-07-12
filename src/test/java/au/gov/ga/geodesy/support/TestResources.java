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
     * Original production SOPAC site logs directory relative to classpath root.
     */
    public static String originalSopacSiteLogsDirectory() {
        return "/sitelog/sopac/original/";
    }

    /**
     * GeodesyML site logs directory relative to classpath root.
     */
    private static String geodesyMLSiteLogsDirectory() {
        return "/sitelog/geodesyml/";
    }

    /**
     * Custom SOPAC site logs directory relative to classpath root.
     */
    public static String customSopacSiteLogsDirectory() {
        return "/sitelog/sopac/custom/";
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
        return "/sitelogToGeodesyML/";
    }

    /**
     * Given a site id, return an original SOPAC site log test file.
     */
    public static File originalSopacSiteLog(String siteId) throws IOException {
        return resourceAsFile(originalSopacSiteLogResourceName(siteId));
    }

    /**
     * Given a site id, return a custom SOPAC site log test file.
     */
    public static File customSopacSiteLog(String siteId) throws IOException {
        return resourceAsFile(customSiteLogResourceName(siteId));
    }

    /**
     * Given a site id, return an original SOPAC site log test file reader.
     */
    public static Reader originalSopacSiteLogReader(String siteId) throws IOException {
        return new FileReader(resourceAsFile(originalSopacSiteLogResourceName(siteId)));
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
     * Return all original SOPAC site log test files.
     */
    public static List<File> originalSopacSiteLogs() throws IOException {
        return originalSopacSiteLogs("*");
    }

    /**
     * Given a site id pattern return all matching original SOPAC site log test files.
     */
    public static List<File> originalSopacSiteLogs(String siteIdPattern) throws IOException {
        Resource[] resources = resourceResolver.getResources("classpath:" + originalSopacSiteLogsDirectory() + siteIdPattern + ".xml");
        List<File> files = new ArrayList<>(resources.length);
        for (Resource r : resources) {
            files.add(r.getFile());
        }
        return files;
    }

    /**
     * Original production SOPAC site log resource name relative to classpath root.
     */
    private static String originalSopacSiteLogResourceName(String id) {
        return originalSopacSiteLogsDirectory() + id + ".xml";
    }

    /**
     * GeodesyML site log resource name relative to classpath root.
     */
    private static String geodesyMLSiteLogResourceName(String id) {
        return geodesyMLSiteLogsDirectory() + id + ".xml";
    }

    private static String customSiteLogResourceName(String id) {
        return customSopacSiteLogsDirectory() + id + ".xml";
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
