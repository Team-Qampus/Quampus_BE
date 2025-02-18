package swyp.qampus.answer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.domain.ChoiceRequestDto;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.user.domain.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AnswerServiceImplTest {
    @Autowired
    AnswerService answerService;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    AnswerRepository answerRepository;

    @MockitoBean
    QuestionRepository questionRepository;

    @Test
    @DisplayName("[실패케이스]-답변이 존재하지 않는 경우 예외가 발생합니다.")
    void choice_FAILED_NOT_EXIST_ANSWER() {
        //given
        Long answerId = 10L;
        Long questionId = 20L;
        String token = "Bearer token";
        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();

        when(answerRepository.findById(answerId)).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, token);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("답변이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-질문이 존재하지 않는 경우 예외가 발생합니다.")
    void choice_FAILED_NOT_EXIST_QUESTION() {
        //given
        Long answerId = 10L;
        Long questionId = 20L;
        String token = "Bearer token";
        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();
        Answer answer = mock(Answer.class);
        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, token);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("질문이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-질문을 올린 사용자와 API요청을 보낸 사용자가 일치하지 않으면 예외가 발생합니다.")
    void choice_FAILED_NOT_EQUAL_USER() {
        //given
        Long answerId = 10L;
        Long questionId = 20L;
        String user1Id = "user1";
        String user2Id = "user2";
        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();
        Answer answer = mock(Answer.class);
        Question question = mock(Question.class);
        User user = mock(User.class);

        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(question.getUser()).thenReturn(user);
        when(question.getUser().getUserId()).thenReturn(user2Id);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, user1Id);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-질문에 대해 채택한 답변이 존재하면 예외가 발생합니다.")
    void choice_EXISTED_CHOICE() {
        //given
        Long answerId = 10L;
        Long questionId = 20L;
        String user1Id = "user1";

        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();
        Answer answer = mock(Answer.class);
        Question question = mock(Question.class);
        User user = mock(User.class);

        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(question.getUser()).thenReturn(user);
        when(question.getUser().getUserId()).thenReturn(user1Id);
        when(answerRepository.countChoiceOfAnswer(questionId)).thenReturn(3);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, user1Id);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이 질문에는 이미 채택된 답변이 있습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-답변이 이미 채택된 상태이면 예외가 발생합니다.")
    void choice_DUPLICATED_CHOSEN() {
        //given
        Long answerId = 10L;
        Long questionId = 20L;
        String user1Id = "user1";
        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();
        Answer answer = mock(Answer.class);
        Question question = mock(Question.class);
        User user = mock(User.class);

        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(question.getUser()).thenReturn(user);
        when(question.getUser().getUserId()).thenReturn(user1Id);
        when(answerRepository.countChoiceOfAnswer(questionId)).thenReturn(0);
        when(answer.getIsChosen()).thenReturn(true);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, user1Id);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이미 채택된 답변입니다.");
    }

    @Test
    @DisplayName("[성공케이스]-답변을 채택하면 해당 답변의 채택상태가 TRUE로 바뀝니다.")
    void choice_SUCCESS() {
        //given
        Long answerId = 10L;
        Long questionId = 20L;
        String user1Id = "user1";
        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();
        Answer answer = mock(Answer.class);
        Question question = mock(Question.class);
        User user = mock(User.class);

        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(question.getUser()).thenReturn(user);
        when(question.getUser().getUserId()).thenReturn(user1Id);
        when(answerRepository.countChoiceOfAnswer(questionId)).thenReturn(0);
        when(answer.getIsChosen()).thenReturn(false);

        //when
        answerService.choice(requestDto, user1Id);

        //then
        verify(answer).setIsChosen(true);
        verify(answerRepository).save(answer);
    }
}