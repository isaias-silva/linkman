package org.zk.linkman.commons.produces.aws;

import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;


import java.net.URI;

public class ServicesProducer {

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

        return SqsClient.builder().region(region).endpointOverride(endpoint).credentialsProvider(getCredentialsProvider()).build();
    }

    @Produces
    public S3Client produceS3Client() {
        return S3Client.builder().region(region).endpointOverride(endpoint).forcePathStyle(true).credentialsProvider(getCredentialsProvider()).build();
    }

    @Produces
    public S3Presigner produceS3Presigner() {
        return S3Presigner.builder().region(region).endpointOverride(endpoint).credentialsProvider(getCredentialsProvider()).build();
    }

    private StaticCredentialsProvider getCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey));
    }
}

