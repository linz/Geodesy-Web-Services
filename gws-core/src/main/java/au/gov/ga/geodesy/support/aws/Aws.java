package au.gov.ga.geodesy.support.aws;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeTagsResult;
import com.amazonaws.services.ec2.model.TagDescription;
import com.amazonaws.util.EC2MetadataUtils;

@Component
public class Aws {

    private static final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
        .withRegion(Regions.AP_SOUTHEAST_2)
        .withCredentials(new InstanceProfileCredentialsProvider(false)).build();

    private Optional<String> instanceId = Optional.empty();
    private Optional<String> stackName = Optional.empty();

    public Aws() {
        this.instanceId = Optional.ofNullable(EC2MetadataUtils.getInstanceId());

        this.stackName = this.instanceId.flatMap(instanceId -> {
            DescribeTagsResult tags = ec2.describeTags();
            return tags.getTags().stream()
                .filter(t -> instanceId.equals(t.getResourceId()) && "aws:cloudformation:stack-name".equals(t.getKey()))
                .findFirst()
                .map(TagDescription::getValue);
        });
    }

    public boolean inAmazon() {
        return getInstanceId().isPresent();
    }

    public Optional<String> getInstanceId() {
        return this.instanceId;
    }

    public Optional<String> getStackName() {
        return this.stackName;
    }
}


