package swyp.qampus.common.kafka.service;

import com.amazonaws.services.ec2.model.ActivityStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import swyp.qampus.common.kafka.RecentUniversityActivityType;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService{
    private static final String TOPIC_NAME="university_";
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public void send(Long id,String universityName, String deptName, RecentUniversityActivityType type){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("id",id);
        hashMap.put("departName",deptName);
        hashMap.put("type",type);
        hashMap.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

        //Json 파싱
        try {
            String message=objectMapper.writeValueAsString(hashMap);
            kafkaTemplate.send(TOPIC_NAME+universityName,message);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }
}
