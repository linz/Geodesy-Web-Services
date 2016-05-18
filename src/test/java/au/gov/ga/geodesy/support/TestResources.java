package au.gov.ga.geodesy.support;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

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
     * Given a site id, return a SOPAC site log test file.
     */
    public static File sopacSiteLog(String siteId) throws IOException {
        return resourceAsFile(sopacSiteLogResourceName(siteId));
    }

    /**
     * Given a site id, return a SOPAC site log test file reader.
     */
    public static Reader sopacSiteLogReader(String siteId) throws IOException {
        return new FileReader(resourceAsFile(sopacSiteLogResourceName(siteId)));
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
     * SOPAC site logs directory relative to classpath root.
     */
    private static String sopacSiteLogsDirectory() {
        return "/sitelog/sopac/";
    }

    /**
     * SOPAC site log resource name relative to classpath root.
     */
    private static String sopacSiteLogResourceName(String id) {
        return sopacSiteLogsDirectory() + id + ".xml";
    }

    /**
     * Find a resource relative to classpath root.
     */
    private static File resourceAsFile(String resourceName) throws IOException {
        Resource resource = new PathMatchingResourcePatternResolver().getResource("classpath:" + resourceName);
        if (resource == null) {
            throw new IllegalArgumentException("Resource " + resourceName + " must exist.");
        }
        return resource.getFile();
    }
}
