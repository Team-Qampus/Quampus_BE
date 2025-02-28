package swyp.qampus.login.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.dto.MyPageResponseDto;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.service.UserService;
import swyp.qampus.login.util.JWTUtil;
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
    private final JWTUtil jwtUtil;

    @Override
    public MyPageResponseDto getMyPageData(String token, Long categoryId, String sort, Pageable pageable) {
        User user = userRepository.findById(jwtUtil.getUserIdFromToken(token))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        List<Question> questions = questionRepository.findMyQuestions(user, categoryId, sort, pageable);

        List<MyQuestionResponseDto> questionDtos = questions.stream()
                .map(MyQuestionResponseDto::of)
                .collect(Collectors.toList());

        return MyPageResponseDto.of(user, questionDtos);
    }
}