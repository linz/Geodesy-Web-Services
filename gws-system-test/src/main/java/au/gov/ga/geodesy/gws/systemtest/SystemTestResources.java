package au.gov.ga.geodesy.gws.systemtest;

import java.io.IOException;

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
    private static String siteLogsDirectory = "/sitelogs/2017-03-30/geodesyml/";

    /**
     * Don't instantiate, use the static methods.
     */
    private SystemTestResources() {
    }

    /**
     * Production sitelogs GeodesyML files.
     */
    public static Resource[] siteLogs() throws IOException {
        return siteLogs("*");
    }

    /**
     * Return all site log GeodesyML documents whose file name matches the given pattern, excluding the ".xml" extension.
     */
    public static Resource[] siteLogs(String fileNamePattern) throws IOException {
        return resourceResolver.getResources("classpath:" + siteLogsDirectory + fileNamePattern + ".xml");
    }

    /**
     * Return a single site log GeodesyML document whost file name matches the given pattern, excluding the ".xml" extension.
     */
    public static Resource siteLog(String fileNamePattern) throws IOException {
        Resource[] siteLogs = siteLogs(fileNamePattern);
        if (siteLogs.length != 1) {
            throw new IllegalArgumentException("Supplied file name pattern matched " + siteLogs.length +
                    " files, but a single match was expected.");
        }
        return siteLogs[0];
    }
}
