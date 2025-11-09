package org.zk.linkman.produces;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@ApplicationScoped
public class AwsProducer {

    @ConfigProperty(name = "aws.region")
    Region region;

    @ConfigProperty(name = "aws.endpoint")
    URI endpoint;


    @ConfigProperty(name = "aws.access-key-id", defaultValue = "test")
    String accessKeyId;

    @ConfigProperty(name = "aws.secret-access-key", defaultValue = "test")
    String secretAccessKey;

    @Produces
    public SqsClient produceSqsClient() {

        return SqsClient.builder().region(region)
                .endpointOverride(endpoint).credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                        )
                )
                .build();
    }
}
