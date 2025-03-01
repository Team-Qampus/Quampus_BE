package swyp.qampus.data.kafka.service;

import swyp.qampus.data.kafka.RecentUniversityActivityType;



public interface KafkaProducerService {
    void send(Long id,String universityName, String deptName, RecentUniversityActivityType type);
}
