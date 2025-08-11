package swyp.qampus.common.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Slf4j
public class NCPStorageConfig {
    @Value("${ncp.storage.region}")
    private String region;

    @Value("${ncp.storage.endpoint}")
    private String endPoint;

    @Value("${ncp.storage.accessKey}")
    private String accessKey;

    @Value("${ncp.storage.secretKey}")
    private String secretKey;

    @Bean
    public AmazonS3Client objectStorageClient() {
        log.info("NCP !!"+endPoint);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey))
                ).build();

    }

}
