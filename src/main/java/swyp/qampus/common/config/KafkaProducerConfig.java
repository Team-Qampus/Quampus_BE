package swyp.qampus.common.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    //Kafka producer를 생성하는 팩토리
    @Value("${spring.kafka.bootstrap-servers}")
    private String BROKER_URL;

    @Bean
    public ProducerFactory<String,Object> producerFactory(){
        Map<String,Object> config=new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKER_URL);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    //KafkaTemplate 생성하여 Kafka 프로듀서를 사용하여 메시지를 보냄
    @Bean
    public KafkaTemplate<String,Object>kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}
