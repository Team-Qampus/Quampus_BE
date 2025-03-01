package swyp.qampus.data.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.TopicForRetryable;

@Configuration
public class KafkaTopicConfig {
    //브로커 설정
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;


    private static String universityTopicName="university-events";

    @Bean
    public NewTopic universityTopic(){
        return TopicBuilder.name(universityTopicName)

                .partitions(5)
                .replicas(1)
                .build();
    }
}
