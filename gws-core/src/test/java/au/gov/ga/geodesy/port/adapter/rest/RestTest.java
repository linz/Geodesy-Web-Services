package au.gov.ga.geodesy.port.adapter.rest;

import java.io.File;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

import au.gov.ga.geodesy.support.spring.GeodesyRepositoryRestMvcConfig;
import au.gov.ga.geodesy.support.spring.GeodesyRestMvcConfig;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ContextConfiguration(
        classes = {GeodesyRepositoryRestMvcConfig.class, GeodesyRestMvcConfig.class},
        loader = AnnotationConfigWebContextLoader.class)

@WebAppConfiguration
@Transactional("geodesyTransactionManager")
public class RestTest extends IntegrationTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected static MockMvc mvc;

    @BeforeClass
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    }

    protected String weeklyFinalSolutionsDir() {
        return "target/test-classes/solutions/final/weekly/15647-16157";
    }

    protected List<File> weeklyFinalSolutions() throws Exception {
        return files(weeklyFinalSolutionsDir(), "*.SNX");
    }

    private List<File> files(String directory, String pattern) throws Exception {
        List<File> files = new ArrayList<>();
        Path directoryPath = FileSystems.getDefault().getPath(directory);
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(directoryPath, pattern)) {
            for (Path p : paths) {
                files.add(p.toFile());
            }
        } catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
        return files;
    }
}
