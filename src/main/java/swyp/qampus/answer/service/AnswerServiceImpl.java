package swyp.qampus.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerResponseDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.exception.AnswerErrorCode;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.question.domain.MessageResponseDto;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.user.domain.User;
import swyp.qampus.exception.CustomException;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    @Override
    public AnswerResponseDto createAnswer(AnswerRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUser_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.USER_NOT_FOUND));

        Question question = questionRepository.findById(requestDto.getQuestion_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        Answer answer = Answer.builder()
                .question(question)
                .content(requestDto.getContent())
                .build();

        Answer savedAnswer = answerRepository.save(answer);
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
    public void choice(Long answerId, Long questId, String token) {
        //TODO:JWT로 교체
        String userId=token;
        Answer answer=answerRepository.findById(answerId).orElseThrow(
                ()-> new RestApiException(AnswerErrorCode.NOT_EXIST_ANSWER));
        Question question=questionRepository.findById(questId).orElseThrow(
                ()->new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION)
        );

        extracted(questId, userId, question, answer);

        answer.setIsChosen(true);
        answerRepository.save(answer);
    }

    private void extracted(Long questId, String userId, Question question, Answer answer) {
        //사용자 권한 검사 -> 해당 질문을 올린 유저와 일치하는가?
        if(!userId.equals(question.getUser().getUserId())){
            throw new RestApiException(CommonErrorCode.FORBIDDEN);
        }

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

}
