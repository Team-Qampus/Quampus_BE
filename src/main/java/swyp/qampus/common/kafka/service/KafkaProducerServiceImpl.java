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
    /**
     * Producer 메서드
     *  @parm 질문 혹은 답변의 Id
     *  @param universityName 학교 이름
     *  @param deptName 힉과
     *  @param type 질문 작성/답변 작성/질문 나도 궁금해요/답변 좋아요/답변 채택
     */
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
