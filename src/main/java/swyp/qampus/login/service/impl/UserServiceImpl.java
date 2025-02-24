package swyp.qampus.login.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.service.UserService;
import swyp.qampus.question.domain.MyQuestionResponseDto;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.question.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Override
    public List<MyQuestionResponseDto> getMyQuestions(Long userId, Long categoryId, String sort, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        List<Question> questions = questionRepository.findMyQuestions(userId, categoryId, sort, pageable);

        if (questions.isEmpty()) {
            throw new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION);
        }

        return questions.stream()
                .map(MyQuestionResponseDto::of)
                .collect(Collectors.toList());
    }
}
