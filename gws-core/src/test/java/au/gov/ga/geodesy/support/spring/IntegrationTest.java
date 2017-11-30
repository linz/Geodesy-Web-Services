package au.gov.ga.geodesy.support.spring;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import au.gov.ga.geodesy.domain.model.Repositories;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@WebAppConfiguration
@Transactional("geodesyTransactionManager")

@ContextConfiguration(
    classes = {
        IntegrationTestConfig.class,
    })

public class IntegrationTest extends AbstractTransactionalTestNGSpringContextTests {

    private final ManualRestDocumentation restDocumentation = new ManualRestDocumentation("target/generated-snippets");

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected static MockMvc mvc;

    @BeforeClass
    public void setUp() {
        mvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .apply(documentationConfiguration(restDocumentation)
                .uris().withScheme("https").withHost("gws.geodesy.ga.gov.au").withPort(443))
            .build();
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @AfterClass(alwaysRun = true)
    @Rollback(false)
    protected void deleteData() {
        new Repositories().deleteAll();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        restDocumentation.beforeTest(getClass(), method.getName());
    }

    @AfterMethod
    public void tearDown() {
        restDocumentation.afterTest();
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

    protected String jwtToken(String authority) {
        return jwtToken(authority, 60);
    }

    protected String expiredJwtToken(String authority) {
        return jwtToken(authority, -1);
    }

    /**
     * Return base64-encoded JWT token with given authority and expiry period.
     */
    protected String jwtToken(String authority, int secondsToLive) {
        String signatureAlgorithm = "HS256";

        HashMap<String, String> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", signatureAlgorithm);

        String exp = String.valueOf(Instant.now().getEpochSecond() + secondsToLive);
        String claims = "{\"exp\": " + exp + ", \"sub\": \"user\", \"authorities\": [\"" + authority + "\"]}";

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

    protected RequestPostProcessor bareUserToken() {
        return bearerToken(jwtToken("none", 60));
    }
}
