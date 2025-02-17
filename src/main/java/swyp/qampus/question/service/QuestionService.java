package swyp.qampus.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.question.domain.QuestionRequestDto;
import swyp.qampus.question.domain.QuestionUpdateRequestDto;
import swyp.qampus.question.domain.MessageResponseDto;
import swyp.qampus.answer.domain.QuestionResponseDto;
import swyp.qampus.category.domain.Category;
import swyp.qampus.question.domain.Question;
import swyp.qampus.user.domain.User;
import swyp.qampus.exception.CustomException;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.category.repository.CategoryRepository;
import swyp.qampus.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public QuestionResponseDto createQuestion(String user_id, QuestionRequestDto requestDto) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new CustomException(CommonErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(requestDto.getCategory_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.CATEGORY_NOT_FOUND));

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
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        Category category = categoryRepository.findById(requestDto.getCategory_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.CATEGORY_NOT_FOUND));

        question.update(requestDto.getTitle(), requestDto.getContent(), category);
        return new MessageResponseDto("질문 수정 성공");
    }

    @Transactional
    public MessageResponseDto deleteQuestion(Long question_id) {
        Question question = questionRepository.findById(question_id)
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        question.delete();
        return new MessageResponseDto("질문 삭제 성공");
    }

}
