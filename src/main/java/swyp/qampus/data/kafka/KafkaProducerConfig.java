package swyp.qampus.data.kafka;

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

    @Value("${spring.kafka.producer.retries}")
    private Integer retry;
    /*
    * ack:all
    * ack신호는 성능은 떨어지지만 event produce를 보장 가능
    * */
    @Value("${spring.kafka.producer.acks}")
    private String acksConfig;

    @Value("${spring.kafka.producer.enable-idempotence}")
    private Boolean enableIdempotence;

    @Value("${spring.kafka.producer.max-in-flight-requests-per-connection}")
    private Integer maxInFlightRequestsPerConnection;

    @Bean
    public ProducerFactory<String,String> producerFactory(){
        Map<String,Object> config=new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKER_URL);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        config.put(ProducerConfig.RETRIES_CONFIG,retry);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,enableIdempotence);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,maxInFlightRequestsPerConnection);

        return new DefaultKafkaProducerFactory<>(config);
    }

    //KafkaTemplate 생성하여 Kafka 프로듀서를 사용하여 메시지를 보냄
    @Bean
    public KafkaTemplate<String,String>kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}
