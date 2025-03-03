package swyp.qampus.ai.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.ai.domain.Ai;
import swyp.qampus.ai.domain.request.ChatGPTRequest;
import swyp.qampus.ai.domain.response.AiResponseDto;
import swyp.qampus.ai.domain.response.ChatGPTResponse;
import swyp.qampus.ai.repository.AiRepository;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.question.repository.QuestionRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AiServiceImpl implements AiService {
    @Value("${openai.model}")
    private String apiModel;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final AiRepository aiRepository;
    private final QuestionRepository questionRepository;
    private final JWTUtil jwtUtil;

    public AiServiceImpl(
            @Qualifier("openAiRestTemplate") RestTemplate restTemplate,
            AiRepository aiRepository,
            QuestionRepository questionRepository,
            JWTUtil jwtUtil) {
        this.restTemplate = restTemplate;
        this.aiRepository = aiRepository;
        this.questionRepository = questionRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AiResponseDto getAiAnswer(String token, Long questionId) throws IOException {
        //API 요청한 유저가 작성한 질문인지 확인
        //TODO:JWT 검증으로 교체
        if (!jwtUtil.validateToken(token)){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED);
        }
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION));
        Ai ai = question.getAi();

        //이전에 생성된 AI 답변이 있는 경우
        if (ai != null) {
            return AiResponseDto.of(ai);
        } else {
            //새로운 ai생성 만들기

            /*
            * 이미지가 존재하는 경우
            * */
            if(!question.getImageList().isEmpty()){
                ChatGPTResponse chatGPTResponse=
                        requestImageAndText(question.getImageList().get(0).getPictureUrl()
                        ,question.getContent());
                Ai newAi=Ai.builder()
                        .content(chatGPTResponse.getChoices().get(0).getMessage().getContent())
                        .build();
                newAi=aiRepository.save(newAi);
                question.setAi(newAi);
                questionRepository.save(question);

                return AiResponseDto.of(newAi);
                /*
                 * 이미지가 없는 경우
                 * */
            }else{

                ChatGPTResponse chatGPTResponse = requestText(question.getContent());
                Ai newAi=Ai.builder()
                        .content(chatGPTResponse.getChoices().get(0).getMessage().getContent())
                        .build();
                newAi=aiRepository.save(newAi);
                question.setAi(newAi);
                questionRepository.save(question);

                return AiResponseDto.of(newAi);
            }


        }
    }

    //텍스트만 전송
    private ChatGPTResponse requestText(String requestText) {
        ChatGPTRequest request = ChatGPTRequest.createTextRequest(apiModel, 1500, "user", requestText);
        return restTemplate.postForObject(apiUrl, request, ChatGPTResponse.class);
    }

    //이미지랑 텍스트 전송
    private ChatGPTResponse requestImageAndText(String imageUrl, String requestText) throws IOException {
        ChatGPTRequest request = ChatGPTRequest.createImageRequest(apiModel, 1500, "user", requestText, imageUrl);
        return restTemplate.postForObject(apiUrl, request, ChatGPTResponse.class);
    }
}
