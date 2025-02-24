package swyp.qampus.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.answer.domain.*;
import swyp.qampus.answer.exception.AnswerErrorCode;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.image.domain.Image;
import swyp.qampus.image.repository.ImageRepository;
import swyp.qampus.image.service.ImageService;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.domain.QuestionDetailResponseDto;
import swyp.qampus.question.domain.QuestionListResponseDto;
import swyp.qampus.question.domain.QuestionResponseDto;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.login.entity.User;
import swyp.qampus.exception.CustomException;
import swyp.qampus.question.repository.QuestionRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    @Override
    public void createAnswer(AnswerRequestDto requestDto, List<MultipartFile> images) {

        User user = userRepository.findById(Long.valueOf(requestDto.getUser_id()))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        Question question = questionRepository.findById(requestDto.getQuestion_id())
                .orElseThrow(() -> new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION));

        Answer answer = Answer.builder()
                .question(question)
                .content(requestDto.getContent())
                .build();


        answerRepository.save(answer);

        question.incrementUnreadAnswerCount();
        question.incrementAnswerCount();

        //사진을 올린 경우 -> 사진업로드
        if (images != null) {
            List<String> urls = imageService.putFileToBucket(images, "ANSWER");
            for (String url : urls) {
                Image newImage = Image.builder()
                        .pictureUrl(url)
                        .answer(answer)

                        .build();
                imageRepository.save(newImage);
            }
        }

    }

    @Transactional
    @Override
    public void updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new RestApiException(AnswerErrorCode.NOT_EXIST_ANSWER));

        answer.update(requestDto.getContent());
    }

    @Transactional
    @Override
    public void deleteAnswer(Long answer_id) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new RestApiException(AnswerErrorCode.NOT_EXIST_ANSWER));

        answerRepository.delete(answer);

        Question question = answer.getQuestion();
        if (question != null) {
            question.decrementAnswerCount();
        }
    }

    @Override
    @Transactional
    public void choice(ChoiceRequestDto choiceRequestDto, String token) {
        //TODO:JWT로 교체
        String userId = token;
        Answer answer = answerRepository.findById(choiceRequestDto.getAnswer_id()).orElseThrow(
                () -> new RestApiException(AnswerErrorCode.NOT_EXIST_ANSWER));
        Question question = questionRepository.findById(choiceRequestDto.getQuestion_id()).orElseThrow(
                () -> new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION)
        );
        Boolean type = choiceRequestDto.getIs_chosen();
        //사용자 권한 검사 -> 해당 질문을 올린 유저와 일치하는가?
        if (!userId.equals(question.getUser().getUserId())) {
            throw new RestApiException(CommonErrorCode.FORBIDDEN);
        }
        validateAndSetChoiceSet(choiceRequestDto.getQuestion_id(), answer, type);

        answerRepository.save(answer);
    }

    private void validateAndSetChoiceSet(Long questId, Answer answer,Boolean type) {
        //채택하는 경우
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
        }
        //채택 취소하는 경우
        else{
            //이미 채택 취소된 답변에 대한 요청 시
            if(!answer.getIsChosen()){
                throw new RestApiException(AnswerErrorCode.DUPLICATED_NO_CHOSEN);
            }
        }
        answer.setIsChosen(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionListResponseDto> getQuestions(String sort, Long categoryId, int page, int size) {
        List<Question> questions;

        //특정 카테고리의 질문 조회
        if (categoryId != null) {
            questions = questionRepository.findByCategoryId(categoryId, page, size, sort);
            if (questions.isEmpty()) {
                throw new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION);
            }
        }
        //전체 질문 조회
        else {
            questions = questionRepository.findAllPaged(page, size, sort);
            if (questions.isEmpty()) {
                throw new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION);
            }
        }

        return questions.stream()
                .map(QuestionListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuestionDetailResponseDto getQuestionDetail(Long questionId, String token) {
        //TODO:JWT로 교체
        String userId = token;
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION));

        question.increseViewCount();    //조회수 증가

        //사용자 권한 검사 -> 해당 질문을 올린 유저와 일치하는가?
        if (question.getUser().getUserId().equals(userId)) {
            question.updateLastViewedDate();
        } else {
            throw new RestApiException(CommonErrorCode.FORBIDDEN);
        }

        List<AnswerResponseDto> answers = answerRepository.findByQuestionId(questionId)
                .stream()
                .map(AnswerResponseDto::new)
                .collect(Collectors.toList());

        return new QuestionDetailResponseDto(question, answers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponseDto> searchQuestions(String value, String sort, int page, int size) {
        List<Question> questions = questionRepository.searchByKeyword(value, sort, page, size);

        return questions.stream()
                .map(QuestionResponseDto::new)
                .collect(Collectors.toList());
    }
}
