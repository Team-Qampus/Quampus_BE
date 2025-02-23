package swyp.qampus.answer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.domain.ChoiceRequestDto;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.category.domain.Category;
import swyp.qampus.category.repository.CategoryRepository;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;
import swyp.qampus.user.domain.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class AnswerServiceImplTest {
    @Autowired
    AnswerService answerService;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    AnswerRepository answerRepository;

    @MockitoBean
    QuestionRepository questionRepository;

    @MockitoBean
    UniversityRepository universityRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @MockitoBean
    JWTUtil jwtUtil;


    @Test
    @DisplayName("[실패케이스]-답변이 존재하지 않는 경우 예외가 발생합니다.")
    void choice_FAILED_NOT_EXIST_ANSWER() {
        //given
        Long answerId = 10L;
        Long questionId = 20L;
        Long userId = 1L;
        String token = "Bearer token";
        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();
        User user = User
                .builder()
                .email("test@naver.com")
                .major("etes")
                .name("123")
                .build();

        when(jwtUtil.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
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
        Long userId = 1L;
        String token = "Bearer token";

        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();
        User user = mock(User.class);
        Answer answer = mock(Answer.class);
        when(jwtUtil.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
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
        Long user1Id = 1L;
        Long user2Id = 2L;
        String token1 = "token1";
        String token2 = "token2";

        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();
        Answer answer = mock(Answer.class);
        Question question = mock(Question.class);
        User user1 = User
                .builder()
                .email("12312@naver.com")
                .name("asdasd")
                .major("통계")
                .build();
        User user2 = User
                .builder()
                .email("1231222@naver.com")
                .name("asdasd")
                .major("통계")
                .build();

        when(jwtUtil.getUserIdFromToken(token1)).thenReturn(user1Id);
        when(jwtUtil.getUserIdFromToken(token2)).thenReturn(user2Id);

        when(userRepository.findById(user1Id)).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2Id)).thenReturn(Optional.of(user2));

        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        when(question.getUser()).thenReturn(user2);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, token1);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-질문에 대해 채택한 답변이 존재하면 예외가 발생합니다.")
    void choice_EXISTED_CHOICE() {
        //given
        University university = createUniversityAndSaveUniversity();
        User user = createUserAndSaveUser(university);
        Question question = createQuestionAndSaveQuestion(user);
        Answer answer = createAnswerAndSaveAnswer(user, question,false);
        Long userId = 1L;

        // 실제 question의 ID 설정
        question.setQuestionId(2L);
        user.setUserId(userId);

        ChoiceRequestDto requestDto = ChoiceRequestDto
                .builder()
                .answer_id(answer.getAnswerId())
                .question_id(question.getQuestionId())
                .is_chosen(true)
                .build();

        when(jwtUtil.getUserIdFromToken("token")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(answerRepository.findById(answer.getAnswerId())).thenReturn(Optional.of(answer));
        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));
        when(universityRepository.findById(university.getUniversityId())).thenReturn(Optional.of(university));

        // question.getQuestionId()의 실제 값(2L)을 사용하여 모킹
        when(answerRepository.countChoiceOfAnswer(question.getQuestionId())).thenReturn(1);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, "token");
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이 질문에는 이미 채택된 답변이 있습니다.");
    }


    @Test
    @DisplayName("[실패케이스]-답변이 이미 채택된 상태이면 예외가 발생합니다.")
    void choice_DUPLICATED_CHOSEN() {
        //given
        University university = createUniversityAndSaveUniversity();
        User user = createUserAndSaveUser(university);
        Question question = createQuestionAndSaveQuestion(user);
        Answer answer = createAnswerAndSaveAnswer(user, question,true);
        Long userId = 1L;

        // 실제 question의 ID 설정
        question.setQuestionId(2L);
        user.setUserId(userId);

        ChoiceRequestDto requestDto = ChoiceRequestDto
                .builder()
                .answer_id(answer.getAnswerId())
                .question_id(question.getQuestionId())
                .is_chosen(true)
                .build();

        when(jwtUtil.getUserIdFromToken("token")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(answerRepository.findById(answer.getAnswerId())).thenReturn(Optional.of(answer));
        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));
        when(universityRepository.findById(university.getUniversityId())).thenReturn(Optional.of(university));
        when(answerRepository.countChoiceOfAnswer(question.getQuestionId())).thenReturn(0);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, "token");
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이미 채택된 답변입니다.");
    }

    @Test
    @DisplayName("[성공케이스]-답변을 채택하면 해당 답변의 채택상태가 TRUE로 바뀝니다.")
    void choice_SUCCESS() {
        //given
        University university = createUniversityAndSaveUniversity();
        User user = createUserAndSaveUser(university);
        Question question = createQuestionAndSaveQuestion(user);
        Answer answer = createAnswerAndSaveAnswer(user, question,false);
        Long userId = 1L;

        // 실제 question의 ID 설정
        question.setQuestionId(2L);
        user.setUserId(userId);

        ChoiceRequestDto requestDto = ChoiceRequestDto
                .builder()
                .answer_id(answer.getAnswerId())
                .question_id(question.getQuestionId())
                .is_chosen(true)
                .build();

        when(jwtUtil.getUserIdFromToken("token")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(answerRepository.findById(answer.getAnswerId())).thenReturn(Optional.of(answer));
        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));
        when(universityRepository.findById(university.getUniversityId())).thenReturn(Optional.of(university));
        when(answerRepository.countChoiceOfAnswer(question.getQuestionId())).thenReturn(0);

        //when
        answerService.choice(requestDto,"token");

        //then
        assertThat(university.getMonthlyChoiceCnt()).isEqualTo(1);
        assertThat(answer.getIsChosen()).isTrue();
    }

    @Test
    @DisplayName("[실패케이스]-이미 채택 취소된 답변에 대해 채택 취소 요청 시 예외가 발생합니다.")
    void choiceDelete_DUPLICATED_CHOSEN() {
        //given
        University university = createUniversityAndSaveUniversity();
        User user = createUserAndSaveUser(university);
        Question question = createQuestionAndSaveQuestion(user);
        Answer answer = createAnswerAndSaveAnswer(user, question,false);
        Long userId = 1L;

        // 실제 question의 ID 설정
        question.setQuestionId(2L);
        user.setUserId(userId);

        ChoiceRequestDto requestDto = ChoiceRequestDto
                .builder()
                .answer_id(answer.getAnswerId())
                .question_id(question.getQuestionId())
                .is_chosen(false)
                .build();

        when(jwtUtil.getUserIdFromToken("token")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(answerRepository.findById(answer.getAnswerId())).thenReturn(Optional.of(answer));
        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));
        when(universityRepository.findById(university.getUniversityId())).thenReturn(Optional.of(university));
        when(answerRepository.countChoiceOfAnswer(question.getQuestionId())).thenReturn(0);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            answerService.choice(requestDto, "token");
        });


        //then
        assertThat(exception.getMessage()).isEqualTo("채택이 되지 않은 답변입니다. 취소가 불가능합니다.");
    }

    @Test
    @DisplayName("[성공케이스]-답변을 채택 취소하면 해당 답변의 채택상태가 FALSE로 바뀝니다.")
    void choiceDelete_SUCCESS() {
        //given
        University university = createUniversityAndSaveUniversity();
        User user = createUserAndSaveUser(university);
        Question question = createQuestionAndSaveQuestion(user);
        Answer answer = createAnswerAndSaveAnswer(user, question,true);
        Long userId = 1L;

        // 실제 question의 ID 설정
        question.setQuestionId(2L);
        user.setUserId(userId);

        ChoiceRequestDto requestDto = ChoiceRequestDto
                .builder()
                .answer_id(answer.getAnswerId())
                .question_id(question.getQuestionId())
                .is_chosen(false)
                .build();

        when(jwtUtil.getUserIdFromToken("token")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(answerRepository.findById(answer.getAnswerId())).thenReturn(Optional.of(answer));
        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));
        when(universityRepository.findById(university.getUniversityId())).thenReturn(Optional.of(university));
        when(answerRepository.countChoiceOfAnswer(question.getQuestionId())).thenReturn(0);

        //when
        answerService.choice(requestDto,"token");

        //then
        assertThat(university.getMonthlyChoiceCnt()).isEqualTo(0);
        assertThat(answer.getIsChosen()).isFalse();
    }

    private User createUserAndSaveUser(University university) {
        User user = User
                .builder()
                .email("tkv001123@naver.com")
                .major("CS")
                .name("test1")
                .university(university)
                .build();
        university.addUser(user);
        when(userRepository.save(user)).thenReturn(user);
        return userRepository.save(user);
    }

    private University createUniversityAndSaveUniversity() {
        University university = University
                .builder()
                .universityName("testuni")
                .build();

        when(universityRepository.save(university)).thenReturn(university);
        return universityRepository.save(university);
    }

    private Question createQuestionAndSaveQuestion(User user) {
        Category category=Category
                .builder()
                .categoryName("test11")
                .build();
        category=categoryRepository.save(category);

        Question question = Question
                .builder()
                .title("test1")
                .content("test2")
                .category(category)
                .user(user)
                .build();
        when(questionRepository.save(question)).thenReturn(question);
        return questionRepository.save(question);
    }

    private Answer createAnswerAndSaveAnswer(User user, Question question,Boolean set) {
        Answer answer = Answer
                .builder()
                .content("teset")
                .user(user)
                .question(question)
                .build();
        answer.setIsChosen(set);
         when(answerRepository.save(answer)).thenReturn(answer);
        return answerRepository.save(answer);
    }
}