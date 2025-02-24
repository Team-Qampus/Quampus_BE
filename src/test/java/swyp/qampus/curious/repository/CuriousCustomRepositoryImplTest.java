//package swyp.qampus.curious.repository;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import swyp.qampus.category.domain.Category;
//import swyp.qampus.category.repository.CategoryRepository;
//import swyp.qampus.curious.domain.Curious;
//import swyp.qampus.login.repository.UserRepository;
//import swyp.qampus.question.domain.Question;
//import swyp.qampus.question.repository.QuestionRepository;
//import swyp.qampus.login.entity.User;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("secret")
//@Transactional
//class CuriousCustomRepositoryImplTest {
//    @Autowired
//    CuriousRepository curiousRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    QuestionRepository questionRepository;
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Test
//    @DisplayName("궁금해요를 누르지 않은 유저는 empty를 반환합니다.")
//    void notClickCuriousReturnEmpty(){
//        //given
//        User user=User.builder()
//                .userId("test1")
//                .email("test@naver.com")
//                .name("test123")
//                .password("avsd")
//                .major("computer")
//                .universityName("서울대")
//                .build();
//        user=userRepository.save(user);
//        userRepository.flush();
//
//        Category category=Category.builder()
//                .categoryName("category")
//                .build();
//        categoryRepository.save(category);
//        categoryRepository.flush();
//
//        Question question=Question.builder()
//                .content("teet")
//                .title("tete")
//                .user(user)
//                .category(category)
//                .build();
//        question=questionRepository.save(question);
//        questionRepository.flush();
//
//
//        //when
//        Optional<Curious> curious=curiousRepository.findCuriousByQuestionAndUser(question.getQuestionId(),user.getUserId());
//
//        //then
//        assertThat(curious).isEmpty();
//    }
//
//    @Test
//    @DisplayName("궁금해요를 누른 유저는 Not empty를 반환합니다.")
//    void notClickCuriousReturnNotEmpty(){
//        //given
//        User user=User.builder()
//                .userId("test1")
//                .email("test@naver.com")
//                .name("test123")
//                .password("avsd")
//                .major("computer")
//                .universityName("서울대")
//                .build();
//        userRepository.save(user);
//        userRepository.flush();
//
//        Category category=Category.builder()
//                .categoryName("category")
//                .build();
//        categoryRepository.save(category);
//        categoryRepository.flush();
//
//        Question question=Question.builder()
//                .content("teet")
//                .title("tete")
//                .user(user)
//                .category(category)
//                .build();
//        questionRepository.save(question);
//        questionRepository.flush();
//
//        Curious curious=Curious.of(user,question);
//        curiousRepository.save(curious);
//
//        //when
//        Optional<Curious> findCurious=curiousRepository.findCuriousByQuestionAndUser(question.getQuestionId(),user.getUserId());
//
//        //then
//        assertThat(findCurious).isNotEmpty();
//    }
//    @Test
//    @DisplayName("질문의 나동 궁금해요를 누르면 개수가 1씩 증가합니다.")
//    void clickLike(){
//        //given
//        User user1=User.builder()
//                .userId("test1")
//                .email("test1@naver.com")
//                .name("test11")
//                .password("abcd")
//                .major("컴공과")
//                .universityName("서울대")
//                .build();
//        User user2=User.builder()
//                .userId("test2")
//                .email("test@naver.com")
//                .name("test11")
//                .password("abcd")
//                .major("컴공과")
//                .universityName("서울대")
//                .build();
//        userRepository.saveAll(List.of(user1,user2));
//
//        Category category=Category.builder()
//                .categoryName("category")
//                .build();
//        categoryRepository.save(category);
//        categoryRepository.flush();
//
//        Question question=Question.builder()
//                .content("teet")
//                .title("tete")
//                .user(user1)
//                .category(category)
//                .build();
//        questionRepository.save(question);
//        questionRepository.flush();
//
//        //when
//        Curious curious1=Curious.of(user1,question);
//        Curious curious2=Curious.of(user2,question);
//
//        //then
//        Question findQuestion=questionRepository.findById(question.getQuestionId()).orElseThrow();
//        assertThat(findQuestion.getCuriousCount()).isEqualTo(2);
//        assertThat(findQuestion.getCuriousList()).hasSize(2)
//                .extracting("user")
//                .containsExactlyInAnyOrder(user1,user2);
//
//    }
//
//}