package swyp.qampus.common.kafka.service;

import swyp.qampus.common.kafka.RecentUniversityActivityType;



public interface KafkaProducerService {
    void send(Long id,String universityName, String deptName, RecentUniversityActivityType type);
}
