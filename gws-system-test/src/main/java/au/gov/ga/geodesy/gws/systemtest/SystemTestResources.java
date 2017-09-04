package au.gov.ga.geodesy.gws.systemtest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
    // private static String siteLogsDirectory = "/sitelogs/2017-06-29/geodesyml/";
    private static String siteLogsDirectory = "/sitelogs/2017-08-31/geodesyml/";

    /**
     * Don't instantiate, use the static methods.
     */
    private SystemTestResources() {
    }

    /**
     * Return a snapshot of production site log GeodesyML files.
     */
    public static Resource[] siteLogs() throws IOException {
        return siteLogs("*");
    }

    /**
     * Return n random production site log GeodesyML files.
     */
    public static Resource[] siteLogs(int n) throws IOException {
        List<Resource> list = Arrays.asList(siteLogs());
        Collections.shuffle(list);
        return list.subList(0, n).toArray(new Resource[0]);
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
