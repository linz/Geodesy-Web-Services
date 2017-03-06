package au.gov.ga.geodesy.port.adapter.rest;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.io.File;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

import au.gov.ga.geodesy.support.spring.GeodesyRepositoryRestMvcConfig;
import au.gov.ga.geodesy.support.spring.GeodesyRestMvcConfig;
import au.gov.ga.geodesy.support.spring.IntegrationTest;
import au.gov.ga.geodesy.support.spring.ResourceServerTestConfig;

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

    /**
     * Return base64-encoded JWT token with given authority and expiry period.
     */
    protected String jwtToken(String authority, int period) {
        String signatureAlgorithm = "HS256";

        HashMap<String, String> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", signatureAlgorithm);
        header.put("exp", String.valueOf(new Date().getTime() + period));

        String claims = "{\"sub\": \"user\", \"authorities\": [\"" + authority + "\"]}";


        Jwt token = JwtHelper.encode(claims, ResourceServerTestConfig.macSigner, header);
        return token.getEncoded();
    }

    /**
     * Add the given base64-encoded JWT token to a mock HTTP servlet request.
     */
    protected RequestPostProcessor bearerToken(String jwt) {
        return mockRequest -> {
            mockRequest.addHeader("Authorization", "Bearer " + jwt);
            return mockRequest;
        };
    }

    protected RequestPostProcessor superuserToken() {
        return bearerToken(jwtToken("superuser", 60));
    }
}
