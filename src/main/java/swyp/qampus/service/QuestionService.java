package swyp.qampus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.dto.request.question.QuestionRequestDto;
import swyp.qampus.dto.request.question.QuestionUpdateRequestDto;
import swyp.qampus.dto.response.MessageResponseDto;
import swyp.qampus.dto.response.question.QuestionResponseDto;
import swyp.qampus.entity.Category;
import swyp.qampus.entity.Question;
import swyp.qampus.entity.User;
import swyp.qampus.exception.CustomException;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.repository.CategoryRepository;
import swyp.qampus.repository.QuestionRepository;
import swyp.qampus.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public QuestionResponseDto createQuestion(String user_id, QuestionRequestDto requestDto) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(requestDto.getCategory_id())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Question question = Question.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(category)
                .build();

        Question savedQuestion = questionRepository.save(question);
        return new QuestionResponseDto(savedQuestion.getQuestion_id());
    }

    @Transactional
    public MessageResponseDto updateQuestion(Long question_id, QuestionUpdateRequestDto requestDto) {
        Question question = questionRepository.findById(question_id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Category category = categoryRepository.findById(requestDto.getCategory_id())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        question.update(requestDto.getTitle(), requestDto.getContent(), category);
        return new MessageResponseDto("질문 수정 성공");
    }

    @Transactional
    public MessageResponseDto deleteQuestion(Long question_id) {
        Question question = questionRepository.findById(question_id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        question.delete();
        return new MessageResponseDto("질문 삭제 성공");
    }

}
