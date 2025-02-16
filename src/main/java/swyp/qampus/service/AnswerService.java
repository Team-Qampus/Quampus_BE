package swyp.qampus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.dto.request.answer.AnswerRequestDto;
import swyp.qampus.dto.request.answer.AnswerUpdateRequestDto;
import swyp.qampus.dto.response.MessageResponseDto;
import swyp.qampus.dto.response.answer.AnswerResponseDto;
import swyp.qampus.entity.Answer;
import swyp.qampus.entity.Question;
import swyp.qampus.entity.User;
import swyp.qampus.exception.CustomException;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.repository.AnswerRepository;
import swyp.qampus.repository.QuestionRepository;
import swyp.qampus.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public AnswerResponseDto createAnswer(AnswerRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUser_id())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Question question = questionRepository.findById(requestDto.getQuestion_id())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Answer answer = Answer.builder()
                .user(user)
                .question(question)
                .content(requestDto.getContent())
                .build();

        Answer savedAnswer = answerRepository.save(answer);
        return new AnswerResponseDto(savedAnswer.getAnswer_id(), "답변 생성 성공");
    }

    @Transactional
    public MessageResponseDto updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        answer.update(requestDto.getContent());
        return new MessageResponseDto("답변 수정 성공");
    }

    @Transactional
    public MessageResponseDto deleteAnswer(Long answer_id) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        answerRepository.delete(answer);
        return new MessageResponseDto("답변 삭제 성공");
    }

}
