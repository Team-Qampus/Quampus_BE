package swyp.qampus.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.activity.Activity;
import swyp.qampus.activity.ActivityType;
import swyp.qampus.activity.repository.ActivityRepository;
import swyp.qampus.answer.domain.*;
import swyp.qampus.answer.exception.AnswerErrorCode;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.curious.repository.CuriousRepository;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.image.domain.Image;
import swyp.qampus.image.repository.ImageRepository;
import swyp.qampus.image.service.ImageService;
import swyp.qampus.login.config.data.RedisCustomService;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.domain.QuestionDetailResponseDto;
import swyp.qampus.question.domain.QuestionListResponseDto;
import swyp.qampus.question.domain.QuestionResponseDto;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.exception.UniversityErrorCode;
import swyp.qampus.university.repository.UniversityRepository;

import swyp.qampus.exception.CustomException;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.question.repository.QuestionRepository;


import java.util.HashMap;
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
    private final UniversityRepository universityRepository;
    private final CuriousRepository curiousRepository;
    private final RedisCustomService redisCustomService;
    private final ActivityRepository activityRepository;
    private final static String REDIS_PREFIX="activity ";
    private final static Long REDIS_LIMIT_TIME=12L;

    @Transactional
    @Override
    public Long createAnswer(AnswerRequestDto requestDto, List<MultipartFile> images,String token) {

        User user = userRepository.findById(jwtUtil.getUserIdFromToken(token))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        Question question = questionRepository.findById(requestDto.getQuestion_id())
                .orElseThrow(() -> new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION));

        Answer answer = Answer.builder()
                .user(user)
                .question(question)
                .content(requestDto.getContent())
                .build();

        answer=answerRepository.save(answer);

        question.incrementUnreadAnswerCount();
        question.incrementAnswerCount();

        //사진을 올린 경우 -> 사진업로드
        if (images != null&&!images.isEmpty()) {
            List<String> urls = imageService.putFileToBucket(images, "ANSWER");
            for (String url : urls) {
                Image newImage = Image.builder()
                        .pictureUrl(url)
                        .answer(answer)
                        .build();
                answer.addImage(newImage);
                answerRepository.save(answer);
                imageRepository.save(newImage);
            }
        }

        Activity activity=Activity
                .builder()
                .activityMajor(user.getMajor())
                .activityDetailId(answer.getAnswerId())
                .activityType(ActivityType.ANSWER)
                .university(user.getUniversity())
                .build();
        activityRepository.save(activity);

        return answer.getAnswerId();
    }

    @Transactional
    @Override
    public void updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto, List<MultipartFile> images, String token) {

        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new RestApiException(AnswerErrorCode.NOT_EXIST_ANSWER));
        if(!answer.getUser().getUserId()
                .equals(jwtUtil.getUserIdFromToken(token))){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED);
        }
        answer.update(requestDto.getContent());

        if (images != null && !images.isEmpty()) {
            // 기존 이미지 삭제
            List<Image> existingImages = imageRepository.findByAnswer(answer);
            imageRepository.deleteAll(existingImages);

            // 새로운 이미지 업로드
            List<String> urls = imageService.putFileToBucket(images, "ANSWER");
            for (String url : urls) {
                Image newImage = Image.builder()
                        .pictureUrl(url)
                        .answer(answer)
                        .build();
                answer.addImage(newImage);
                imageRepository.save(newImage);
            }
        }
    }

    @Transactional
    @Override
    public void deleteAnswer(Long answer_id,String token) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new RestApiException(AnswerErrorCode.NOT_EXIST_ANSWER));

        if(!answer.getUser().getUserId()
                .equals(jwtUtil.getUserIdFromToken(token))){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED);
        }
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
        Long userId=jwtUtil.getUserIdFromToken(token);
        //유저 찾기

        Answer answer=answerRepository.findById(choiceRequestDto.getAnswer_id()).orElseThrow(
                ()-> new RestApiException(AnswerErrorCode.NOT_EXIST_ANSWER));
        Question question=questionRepository.findById(choiceRequestDto.getQuestion_id()).orElseThrow(
                ()->new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION)
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
        User user = answer.getUser();
        Activity activity;

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
            //채택하는 경우
            University university=universityRepository.findById(answer.getUser().getUniversity().getUniversityId()).orElseThrow(()->
                    new RestApiException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));
            university.increaseChoiceCnt();
            user.increaseChoiceCnt();
            universityRepository.save(university);
            userRepository.save(user);

            activity=Activity
                    .builder()
                    .activityMajor(user.getMajor())
                    .activityDetailId(answer.getAnswerId())
                    .activityType(ActivityType.CHOICE_SAVE)
                    .university(university)
                    .build();
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
            user.decreaseChoiceCnt();
            universityRepository.save(university);
            userRepository.save(user);

            activity=Activity
                    .builder()
                    .activityMajor(user.getMajor())
                    .activityDetailId(answer.getAnswerId())
                    .activityType(ActivityType.CHOICE_DELETE)
                    .university(university)
                    .build();
        }


        activity=activityRepository.save(activity);

        answer.setIsChosen(type);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<QuestionListResponseDto> getQuestions(Long categoryId, String sort , Pageable pageable, String token) {
        userRepository.findById(jwtUtil.getUserIdFromToken(token))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        Page<Question> questionPage = questionRepository.findByCategoryId(categoryId, pageable, sort);

        return questionPage.map(QuestionListResponseDto::of);
    }

    @Override
    @Transactional
    public QuestionDetailResponseDto getQuestionDetail(Long questionId, String token) {
        //TODO:JWT로 교체
        Long userId = jwtUtil.getUserIdFromToken(token);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION));

        if (!question.getUser().getUserId().equals(userId)) {
            question.increseViewCount();    //조회수 증가
        }

        if (question.getUser().getUserId().equals(userId)) {
            question.updateLastViewedDate();
        }

        List<AnswerResponseDto> answers = answerRepository.findByQuestionQuestionId(questionId)
                .stream()
                .map(answer -> AnswerResponseDto.of(answer, answer.getImageList()))
                .collect(Collectors.toList());

        boolean isCurious = curiousRepository.existsByUserUserIdAndQuestionQuestionId(userId, questionId);

        return QuestionDetailResponseDto.of(question, isCurious, answers, question.getImageList());

    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> searchQuestions(String value, String sort, Pageable pageable,String token) {
        Page<Question> questionPage = questionRepository.searchByKeyword(value, sort, pageable);

        return questionPage.map(QuestionResponseDto::of);
    }
}
