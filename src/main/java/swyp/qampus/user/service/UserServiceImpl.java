package swyp.qampus.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.question.domain.MyQuestionResponseDto;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.user.domain.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Override
    public List<MyQuestionResponseDto> getMyQuestions(Long userId, Long categoryId, String sort, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        List<Question> questions = questionRepository.findMyQuestions(userId, categoryId, sort, page, size);

        if (questions.isEmpty()) {
            throw new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION);
        }

        return questions.stream()
                .map(MyQuestionResponseDto::new)
                .collect(Collectors.toList());
    }
}
