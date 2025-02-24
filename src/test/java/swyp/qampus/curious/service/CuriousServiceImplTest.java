package swyp.qampus.curious.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import swyp.qampus.category.domain.Category;
import swyp.qampus.category.repository.CategoryRepository;
import swyp.qampus.curious.domain.Curious;
import swyp.qampus.curious.repository.CuriousRepository;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.login.entity.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@ActiveProfiles("secret")
class CuriousServiceImplTest {
    @Autowired
    private CuriousService curiousService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private QuestionRepository questionRepository;
    @MockitoBean
    private CategoryRepository categoryRepository;
    @MockitoBean
    private CuriousRepository curiousRepository;

    @Test
    @DisplayName("[실패케이스]-유저가 존재하지 않는 경우 예외가 발생합니다. ")
    void insert_FAILED_MY_QUESTION() {
        //given
        String userId = "test";
        Long questionId = 1L;

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            curiousService.insert(userId, questionId);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-질문이 존재하지 않는 경우 예외가 발생합니다.")
    void insert_NOT_EXISTED_QUESTION() {
        //given
        String userId = "test";
        Long questionId = 1L;
        User user = User
                .builder()
                .userId(userId)
                .email("tt" + userId)
                .major("tt" + userId)
                .name("tt" + userId)
                .universityName("tt" + userId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(questionRepository.findById(any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            curiousService.insert(userId, questionId);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("질문이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-자신의 질문에 나도 궁금해요를 누르면 예외가 발생합니다.")
    void insert_CAN_NOT_CLICK_MINE() {
        //given
        String userId = "test";
        Long questionId = 1L;
        User user = User
                .builder()
                .userId(userId)
                .email("tt" + userId)
                .major("tt" + userId)
                .name("tt" + userId)
                .universityName("tt" + userId)
                .build();
        Category category = Category
                .builder()
                .categoryName("test")
                .build();
        when(categoryRepository.save(category)).thenReturn(category);
        when(userRepository.save(user)).thenReturn(user);

        Question question = Question
                .builder()
                .user(user)
                .title("test")
                .content("test")
                .category(category)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            curiousService.insert(userId, questionId);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("자신의 질문에는 '나도 궁금해요'를 클릭할 수 없습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-이미 궁금해요가 눌러져 있으면 예외가 발생합니다.")
    void insert_DUPLICATED_CURIOUS_REQUEST() {
        //given
        String userId1 = "test1";
        String userId2 = "test23";
        Long questionId = 1L;
        User user1 = User
                .builder()
                .userId(userId1)
                .email("tt" + userId1)
                .major("tt" + userId1)
                .name("tt" + userId1)
                .universityName("tt" + userId1)
                .build();
        User user2 = User
                .builder()
                .userId(userId2)
                .email("tt1" + userId1)
                .major("tt1" + userId1)
                .name("tt1" + userId1)
                .universityName("tt1" + userId1)
                .build();
        Category category = Category
                .builder()
                .categoryName("test")
                .build();

        when(categoryRepository.save(category)).thenReturn(category);

        Question question = Question
                .builder()
                .user(user1)
                .title("test")
                .content("test")
                .category(category)
                .build();

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        Curious curious = Curious.of(user2, question);

        when(curiousRepository.findCuriousByQuestionAndUser(questionId, userId2))
                .thenReturn(Optional.of(curious));

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            curiousService.insert(userId2, questionId);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("중복된 나도 궁금해요 리소스 요청입니다.");
    }

    @Test
    @DisplayName("[성공케이스]-나도 궁금해요를 누르면 궁금해요가 1씩 증가합니다.")
    void insert_SUCCESS() {
        //given
        String userId1 = "test1";
        String userId2 = "test2";
        String userId3 = "test3";
        Long questionId = 1L;
        User user1 = User
                .builder()
                .userId(userId1)
                .email("tt" + userId1)
                .major("tt" + userId1)
                .name("tt" + userId1)
                .universityName("tt" + userId1)
                .build();
        User user2 = User
                .builder()
                .userId(userId2)
                .email("tt" + userId2)
                .major("tt" + userId2)
                .name("tt" + userId2)
                .universityName("tt" + userId2)
                .build();
        User user3 = User
                .builder()
                .userId(userId3)
                .email("tt" + userId2)
                .major("tt" + userId2)
                .name("tt" + userId2)
                .universityName("tt" + userId2)
                .build();

        Category category = Category
                .builder()
                .categoryName("test")
                .build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));
        when(userRepository.findById(userId3)).thenReturn(Optional.of(user3));

        Question question = Question
                .builder()
                .user(user1)
                .title("test")
                .content("test")
                .category(category)
                .build();

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        //when
        curiousService.insert(userId2, questionId);
        curiousService.insert(userId3, questionId);

        //then
        Question question1 = questionRepository.findById(questionId)
                .orElseThrow();

        assertThat(question1.getCuriousList()).hasSize(2);
        assertThat(question1.getCuriousCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("[실패케이스]-유저가 존재하지 않는 경우 예외가 발생합니다. ")
    void delete_FAILED_MY_QUESTION() {
        //given
        String userId = "test";
        Long questionId = 1L;

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            curiousService.delete(userId, questionId);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-질문이 존재하지 않는 경우 예외가 발생합니다.")
    void delete_NOT_EXISTED_QUESTION() {
        //given
        String userId = "test";
        Long questionId = 1L;
        User user = User
                .builder()
                .userId(userId)
                .email("tt" + userId)
                .major("tt" + userId)
                .name("tt" + userId)
                .universityName("tt" + userId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(questionRepository.findById(any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            curiousService.insert(userId, questionId);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("질문이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("[실패케이스]-궁금해요가 눌러져 있지 않으면 예외가 발생합니다.")
    void delete_DUPLICATED_CURIOUS_REQUEST() {
        //given
        String userId1 = "test1";
        String userId2 = "test23";
        Long questionId = 1L;
        User user1 = User
                .builder()
                .userId(userId1)
                .email("tt" + userId1)
                .major("tt" + userId1)
                .name("tt" + userId1)
                .universityName("tt" + userId1)
                .build();
        User user2 = User
                .builder()
                .userId(userId2)
                .email("tt1" + userId1)
                .major("tt1" + userId1)
                .name("tt1" + userId1)
                .universityName("tt1" + userId1)
                .build();
        Category category = Category
                .builder()
                .categoryName("test")
                .build();

        when(categoryRepository.save(category)).thenReturn(category);

        Question question = Question
                .builder()
                .user(user1)
                .title("test")
                .content("test")
                .category(category)
                .build();

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            curiousService.delete(userId2, questionId);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("중복된 나도 궁금해요 리소스 요청입니다.");
    }

    @Test
    @DisplayName("[실패케이스]-자신의 질문에 나도 궁금해요 취소를 누르면 예외가 발생합니다.")
    void delete_CAN_NOT_CLICK_MINE() {
        //given
        String userId = "test";
        Long questionId = 1L;
        User user = User
                .builder()
                .userId(userId)
                .email("tt" + userId)
                .major("tt" + userId)
                .name("tt" + userId)
                .universityName("tt" + userId)
                .build();
        Category category = Category
                .builder()
                .categoryName("test")
                .build();
        when(categoryRepository.save(category)).thenReturn(category);
        when(userRepository.save(user)).thenReturn(user);

        Question question = Question
                .builder()
                .user(user)
                .title("test")
                .content("test")
                .category(category)
                .build();
        Curious curious = Curious.of(user, question);
        when(curiousRepository.save(curious)).thenReturn(curious);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(curiousRepository.findCuriousByQuestionAndUser(questionId, userId)).thenReturn(Optional.of(curious));
        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            curiousService.delete(userId, questionId);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("자신의 질문에는 '나도 궁금해요'를 클릭할 수 없습니다.");
    }

    @Test
    @DisplayName("[성공케이스]-나도 궁금해요 취소를 누르면 궁금해요가 1씩 감소합니다.")
    void delete_SUCCESS() {
        //given
        String userId1 = "test1";
        String userId2 = "test2";
        String userId3 = "test3";
        Long questionId = 1L;

        User user1 = User
                .builder()
                .userId(userId1)
                .email("tt" + userId1)
                .major("tt" + userId1)
                .name("tt" + userId1)
                .universityName("tt" + userId1)
                .build();
        User user2 = User
                .builder()
                .userId(userId2)
                .email("tt" + userId2)
                .major("tt" + userId2)
                .name("tt" + userId2)
                .universityName("tt" + userId2)
                .build();
        User user3 = User.builder()
                .userId(userId3)
                .email("tt" + userId3)
                .major("tt" + userId3)
                .name("tt" + userId3)
                .universityName("tt" + userId3)
                .build();

        Category category = Category.builder().categoryName("test").build();
        when(categoryRepository.save(category)).thenReturn(category);
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));
        when(userRepository.findById(userId3)).thenReturn(Optional.of(user3));

        Question question = Question
                .builder()
                .user(user1)
                .title("test")
                .content("test")
                .category(category)
                .build();
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        Curious curious1 = Curious.of(user2, question);
        Curious curious2 = Curious.of(user3, question);



        when(curiousRepository.findCuriousByQuestionAndUser(questionId, userId2))
                .thenReturn(Optional.of(curious1));

        assertThat(question.getCuriousList()).hasSize(2);
        assertThat(question.getCuriousCount()).isEqualTo(2);

        //when
        curiousService.delete(userId2, questionId);

        //then
        assertThat(question.getCuriousList()).hasSize(1);
        assertThat(question.getCuriousCount()).isEqualTo(1);
    }


}