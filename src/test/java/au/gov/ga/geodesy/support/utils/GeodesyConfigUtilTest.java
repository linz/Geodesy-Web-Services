package au.gov.ga.geodesy.support.utils;

import org.springframework.context.annotation.PropertySource;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;

public class GeodesyConfigUtilTest {
    @Test
    public void testgetPropertySource01() {
        List<String> propSources = GeodesyConfigUtil.getPropertySource(testCase1.class);
        assertThat(propSources.size(), is(2));
        assertThat("propSources list", propSources, hasItems(valuesStraight1.toArray(new String[0])));
    }

    @Test
    public void testgetPropertySource02() {
        List<String> propSources = GeodesyConfigUtil.getPropertySourceDropPrefix(testCase1.class);
        assertThat(propSources.size(), is(2));
        assertThat("propSources list", propSources, hasItems(valuesNoPrefix1.toArray(new String[0])));
    }

    private static List<String> valuesStraight1 = Arrays.asList("classpath:a", "classpath:y");
    private static List<String> valuesNoPrefix1 = Arrays.asList("a", "y");

    @PropertySource({"classpath:a", "classpath:y"})
    class testCase1 {

    }

    @Test
    public void testgetPropertySource01_2() {
        List<String> propSources = GeodesyConfigUtil.getPropertySource(testCase2.class);
        assertThat(propSources.size(), is(5));
        assertThat("propSources list", propSources, hasItems(valuesStraight2.toArray(new String[0])));
    }

    @Test
    public void testgetPropertySource02_2() {
        List<String> propSources = GeodesyConfigUtil.getPropertySourceDropPrefix(testCase2.class);
        assertThat(propSources.size(), is(5));
        assertThat("propSources list", propSources, hasItems(valuesNoPrefix2.toArray(new String[0])));
    }

    private static List<String> valuesStraight2 = Arrays.asList("classpath:a", "classpath:b", "classpath:y", "classpath:z", "classpath:c");
    private static List<String> valuesNoPrefix2 = Arrays.asList("a", "b", "y", "z", "c");

    @PropertySource({"classpath:a", "classpath:y"})
    @PropertySource({"classpath:b", "classpath:z"})
    @PropertySource({"classpath:c"})
    class testCase2 {

    }

}
