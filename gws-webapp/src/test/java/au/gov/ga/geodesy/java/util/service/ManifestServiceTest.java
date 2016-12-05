package au.gov.ga.geodesy.java.util.service;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by brookes on 24/05/16.
 */
public class ManifestServiceTest {
    Logger logger = LoggerFactory.getLogger(ManifestService.class);

    ManifestService manifestService = Mockito.mock(ManifestService.class);

    @BeforeMethod
    public void init() {
        // Ideally I'd use a spy so all these doCallRealMethods() aren't needed
        // But to create a spy means the Constructor is called and I can't mock out any methods
        // that are called from it.  I had to choose the lesser of two evils
        Mockito.doNothing().when(manifestService).buildManifests();
        Mockito.doCallRealMethod().when(manifestService).buildManifest(Mockito.any());
        Mockito.doCallRealMethod().when(manifestService).getInternalManifests();
        Mockito.doCallRealMethod().when(manifestService).getExternalManifests();
        Mockito.doCallRealMethod().when(manifestService).init();
        manifestService.init();
    }

    @Test
    public void testGAManifestData() {
        List<URL> resources = getManifestFileResources();
        List<ManifestService.GAManifestData> gAManifestDatums = new ArrayList<>();
        resources.stream().forEach(it -> {
            ManifestService.GAManifestData gaManifestData = manifestService.new GAManifestData(it);
            gAManifestDatums.add(gaManifestData);
        });
        assertThat(gAManifestDatums.size(), is(2));
        gAManifestDatums.stream().forEach(it -> {
            logger.debug("Manifest: " + it.getManifestUrl() + ", data: " + it.getManifest());
            assertThat(it.getManifestUrl(), not(nullValue()));
            assertThat(it.getManifest(), not(nullValue()));
        });
    }

    @Test
    public void testManifestServiceInternal() {
        URL resource = getManifestResource("MANIFEST_geodesy-domain-model.MF");
        manifestService.buildManifest(resource);
        assertThat(manifestService.getExternalManifests(),not(nullValue()));
        assertThat(manifestService.getExternalManifests().size(), is(0));
        assertThat(manifestService.getInternalManifests(),not(nullValue()));
        assertThat(manifestService.getInternalManifests().size(), is(1));
        URL resource2 = getManifestResource("MANIFEST_externalJar.MF");
        manifestService.buildManifest(resource2);
        assertThat(manifestService.getExternalManifests(),not(nullValue()));
        assertThat(manifestService.getExternalManifests().size(), is(1));
        assertThat(manifestService.getInternalManifests(),not(nullValue()));
        assertThat(manifestService.getInternalManifests().size(), is(1));
    }

    @Test
    public void testManifestServiceComplete() {
        Mockito.doCallRealMethod().when(manifestService).buildManifests();
        manifestService.init();

        // The internalManifests can't be tested since the data is pulled from the dependency jars of the
        // project and one can't be sure what is 'internal' has the correct header in the manifest.
        // However there is a separate test (testManifestServiceInternal()) that uses a test MANIFEST
        // so the correct behaviour is being confirmed
        //        Assert.assertNotNull(manifestService.getInternalManifests());
        //        Assert.assertNotEquals(0, manifestService.getInternalManifests().size());
        assertThat(manifestService.getExternalManifests(),not(nullValue()));
        assertThat(manifestService.getExternalManifests().size(), not(is(0)));
    }

    // Pull the manifests from the dependant jars of this project
    private List<URL> getManifestFileResources() {
        Enumeration<URL> resources = null;
        List<URL> urlList = new ArrayList<>();
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources("manifests");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try {
                Scanner manifestResourceScanner = new Scanner((InputStream) url.getContent()).useDelimiter("\\A");
                while (manifestResourceScanner.hasNext()) {
                    String allResources = manifestResourceScanner.next();
                    Scanner linesScanner = new Scanner(allResources).useDelimiter("\n");
                    while (linesScanner.hasNext()) {
                        String resourceLine = linesScanner.next();
                        logger.debug("  Resource: " + resourceLine);
                        URL resourceUrl = new URL(url.toString() + "/" + resourceLine);
                        urlList.add(resourceUrl);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    private URL getManifestResource(String resourceFileName) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("manifests/" + resourceFileName);
        return resource;
    }

    @Test
    public void testExtractName1() {
        Mockito.doCallRealMethod().when(manifestService).extractAppName(Mockito.any());

        String path = "jar:file:/usr/local/installs/apache-tomcat-8.0.35/webapps/ROOT/WEB-INF/lib/spring-tx-4.1.6.RELEASE.jar!/META-INF/MANIFEST.MF";
        String expected = "spring-tx-4.1.6.RELEASE.jar!/META-INF/MANIFEST.MF";
        String extracted = manifestService.extractAppName(path);
        System.out.println("testExtractName - extract: " + extracted);
        assertThat(extracted, is(expected));
    }

    @Test
    public void testExtractName2() {
        Mockito.doCallRealMethod().when(manifestService).extractAppName(Mockito.any());

        String path = "spring-tx-4.1.6.RELEASE.jar!/META-INF/MANIFEST.MF";
        String expected = "spring-tx-4.1.6.RELEASE.jar!/META-INF/MANIFEST.MF";
        String extracted = manifestService.extractAppName(path);
        System.out.println("testExtractName - extract: " + extracted);
        assertThat(extracted, is(expected));
    }
}