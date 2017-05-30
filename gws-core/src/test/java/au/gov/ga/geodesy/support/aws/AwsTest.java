package au.gov.ga.geodesy.support.aws;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Optional;

import org.testng.annotations.Test;

public class AwsTest {

    @Test
    public void testInAmazon() {
        assertThat(Aws.inAmazon(), is(false));
        assertThat(Aws.getStackName(), is(Optional.empty()));
    }
}
