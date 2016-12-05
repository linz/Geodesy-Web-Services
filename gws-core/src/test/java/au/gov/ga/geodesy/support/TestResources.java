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
     * Original production SOPAC site logs, with errors, directory relative to classpath root.
     */
    public static String originalBadSopacSiteLogsDirectory() {
        return "/sitelog/sopac/original/bad/";
    }

    /**
     * Custom GeodesyML site logs directory relative to classpath root.
     */
    private static String customGeodesyMLSiteLogsDirectory() {
        return "/sitelog/geodesyml/custom/";
    }

    /**
     * Custom SOPAC site logs directory relative to classpath root.
     */
    public static String customSopacSiteLogsDirectory() {
        return "/sitelog/sopac/custom/";
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
     * Given a site id, return a custom GeodesyML site log test file reader.  File from resources:/sitelog/sopac/
     */
    public static Reader customGeodesyMLSiteLogReader(String siteId) throws IOException {
        return new FileReader(resourceAsFile(customGeodesyMLSiteLogResourceName(siteId)));
    }

    /**
     * Given a site id, return a sopac converted to GeodesyML site log test file reader.  File from resources:/sitelogtoGeodesyML
     */
    public static Reader geodesyMLSopacConvertedSiteLogReader(String siteId) throws IOException {
        return new FileReader(resourceAsFile(geodesyMLSopacConvertedSiteLogResourceName(siteId)));
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
     * Custom GeodesyML site log resource name relative to classpath root.
     */
    private static String customGeodesyMLSiteLogResourceName(String id) {
        return customGeodesyMLSiteLogsDirectory() + id + ".xml";
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
