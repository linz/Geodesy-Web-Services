package au.gov.ga.geodesy.support.aws;

import java.util.Optional;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeTagsResult;
import com.amazonaws.services.ec2.model.TagDescription;
import com.amazonaws.util.EC2MetadataUtils;

public class Aws {

    private static final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
        .withRegion(Regions.AP_SOUTHEAST_2)
        .withCredentials(new InstanceProfileCredentialsProvider(false)).build();

    public static final boolean inAmazon() {
        return EC2MetadataUtils.getInstanceId() != null;
    }

    public static Optional<String> getStackName() {
        if (inAmazon()) {
            DescribeTagsResult tags = ec2.describeTags();
            return tags.getTags().stream()
                .filter(t -> t.getKey().equals("aws:cloudformation:stack-name"))
                .findFirst()
                .map(TagDescription::getValue);
        } else {
            return Optional.empty();
        }
    }
}


