package au.gov.ga.geodesy.port.adapter.rest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import java.lang.reflect.Method;

import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/*
 * Spring RestDoc base class.
 */
public class RestDocTest extends RestTest {

    private final ManualRestDocumentation restDocumentation = new ManualRestDocumentation("target/generated-snippets");

    @BeforeMethod
    public void setUp(Method method) {
        mvc = MockMvcBuilders.webAppContextSetup(super.webApplicationContext)
            .apply(documentationConfiguration(restDocumentation)
                .uris().withScheme("https").withHost("test.geodesy.ga.gov.au").withPort(443))
            .build();

        restDocumentation.beforeTest(getClass(), method.getName());
    }

    @AfterMethod
    public void tearDown() {
        restDocumentation.afterTest();
    }
}
