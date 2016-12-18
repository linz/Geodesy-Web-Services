package au.gov.ga.geodesy.gws.systemtest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * A collection of static methods to access regression test data.
 */
public class SystemTestResources {

    private static final PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    /**
     * Production sitelogs GeodesyML files.
     */
    private static String siteLogsDirectory = "/sitelogs/2016-12-18/geodesyml/";

    /**
     * Don't instantiate, use the static methods.
     */
    private SystemTestResources() {
    }

    /**
     * Production sitelogs GeodesyML files.
     */
    public static List<File> siteLogs() throws IOException {
        return siteLogs("*");
    }

    /**
     * Return all site log GeodesyML documents whose file name matches the given pattern, excluding the ".xml" extenstion.
     */
    public static List<File> siteLogs(String fileNamePattern) throws IOException {
        Resource[] resources = resourceResolver.getResources("classpath:" + siteLogsDirectory + fileNamePattern + ".xml");
        List<File> files = new ArrayList<>(resources.length);
        for (Resource r : resources) {
            files.add(r.getFile());
        }
        return files;
    }
}
