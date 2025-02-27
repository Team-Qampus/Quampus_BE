package swyp.qampus.common.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.TopicForRetryable;

@Configuration
public class KafkaTopicConfig {
    //브로커 설정
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    //토픽 이름 설정
    @Value(value = "${spring.topic.university}")
    private String universityTopicName;

    @Bean
    public NewTopic universityTopic(){
        return TopicBuilder.name(universityTopicName)
                .partitions(5)
                .replicas(1)
                .build();
    }
}
