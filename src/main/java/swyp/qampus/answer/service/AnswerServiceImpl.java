package swyp.qampus.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.answer.domain.*;
import swyp.qampus.answer.exception.AnswerErrorCode;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.image.domain.Image;
import swyp.qampus.image.repository.ImageRepository;
import swyp.qampus.image.service.ImageService;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.MessageResponseDto;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.exception.UniversityErrorCode;
import swyp.qampus.university.repository.UniversityRepository;
import swyp.qampus.user.domain.User;
import swyp.qampus.exception.CustomException;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final JWTUtil jwtUtil;
    private final UniversityRepository universityRepository;

    @Transactional
    @Override
    public AnswerResponseDto createAnswer(AnswerRequestDto requestDto, List<MultipartFile> images) {
        User user = userRepository.findById(requestDto.getUser_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.USER_NOT_FOUND));

        Question question = questionRepository.findById(requestDto.getQuestion_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        Answer answer = Answer.builder()
                .user(user)
                .question(question)
                .content(requestDto.getContent())
                .build();

        Answer savedAnswer = answerRepository.save(answer);

        //이미지 올린 경우
        if(images!=null){
            List<String> urls=imageService.putFileToBucket(images,"ANSWER");
            for (String url:urls){
                Image image=Image.builder()
                        .pictureUrl(url)
                        .answer(answer)
                        .build();
                imageRepository.save(image);
            }
        }
        return new AnswerResponseDto(savedAnswer.getAnswerId(), "답변 생성 성공");
    }

    @Transactional
    @Override
    public MessageResponseDto updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        answer.update(requestDto.getContent());
        return new MessageResponseDto("답변 수정 성공");
    }

    @Transactional
    @Override
    public MessageResponseDto deleteAnswer(Long answer_id) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        answerRepository.delete(answer);
        return new MessageResponseDto("답변 삭제 성공");
    }

    @Override
    @Transactional
    public void choice(ChoiceRequestDto choiceRequestDto, String token) {
        //TODO:JWT로 교체
        Long userId=jwtUtil.getUserIdFromToken(token);
        //유저 찾기
        User user=userRepository.findById(userId).orElseThrow(
                ()->new RestApiException(CommonErrorCode.USER_NOT_FOUND)
        );
        Answer answer=answerRepository.findById(choiceRequestDto.getAnswer_id()).orElseThrow(
                ()-> new RestApiException(AnswerErrorCode.NOT_EXIST_ANSWER));
        Question question=questionRepository.findById(choiceRequestDto.getQuestion_id()).orElseThrow(
                ()->new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION)
        );
        Boolean type=choiceRequestDto.getIs_chosen();
        //사용자 권한 검사 -> 해당 질문을 올린 유저와 일치하는가?
        if(!userId.equals(question.getUser().getUserId())){
            throw new RestApiException(CommonErrorCode.FORBIDDEN);
        }
        validateAndSetChoiceSet(choiceRequestDto.getQuestion_id(),answer,type);

        answerRepository.save(answer);
    }

    private void validateAndSetChoiceSet(Long questId, Answer answer,Boolean type) {

        if(type){
            //해당 질문에서 이미 채택한 답변이 존재하는 경우
            Integer exitedChosen=answerRepository.countChoiceOfAnswer(questId);
            if(exitedChosen>=1){
                throw new RestApiException(AnswerErrorCode.DUPLICATED_CHOSEN_OF_QUESTION);
            }
            //이미 채택된 답변에 대한 요청 시
            if(answer.getIsChosen()){
                throw new RestApiException(AnswerErrorCode.DUPLICATED_CHOSEN);
            }
            //채택하는 경우
            University university=universityRepository.findById(answer.getUser().getUniversity().getUniversityId()).orElseThrow(()->
                    new RestApiException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
            university.increaseChoiceCnt();
            universityRepository.save(university);
        }
        //채택 취소하는 경우
        else{
            //이미 채택 취소된 답변에 대한 요청 시
            if(!answer.getIsChosen()){
                throw new RestApiException(AnswerErrorCode.DUPLICATED_NO_CHOSEN);
            }
            //채택하는 경우
            University university=universityRepository.findById(answer.getUser().getUniversity().getUniversityId()).orElseThrow(()->
                    new RestApiException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
            university.decreaseChoiceCnt();
            universityRepository.save(university);
        }
        answer.setIsChosen(type);
    }

}
