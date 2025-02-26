/*
package swyp.qampus.answer.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.category.domain.Category;
import swyp.qampus.category.domain.CategoryType;
import swyp.qampus.category.repository.CategoryRepository;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.login.entity.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AnswerCustomRepositoryImplTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("답변의 채택이 되어 있으면 1이상을 반환합니다.")
    void count_1_ChoiceOfAnswer() {
        //given
        exampleUser dummyOfUser = getExampleUser();

        Category category=Category
                .builder()
                .categoryName(CategoryType.ARTS_AND_SPORTS)
                .build();
        categoryRepository.save(category);
        
        Question question = Question
                .builder()
                .title("test1")
                .content("test")
                .user(dummyOfUser.user1())
                .category(category)
                .build();
        question=questionRepository.save(question);

        exampleAnswer dummyAnswer = getExampleAnswer(question, dummyOfUser);
        dummyAnswer.answer1().setIsChosen(true);
        dummyAnswer.answer2().setIsChosen(true);
        answerRepository.saveAll(List.of(dummyAnswer.answer1(), dummyAnswer.answer2()));

        //when
        Integer cnt=answerRepository.countChoiceOfAnswer(question.getQuestionId());

        //then
        assertThat(cnt).isEqualTo(2);
    }
    @Test
    @DisplayName("답변의 채택이 안되어 있으면 0을 반환합니다.")
    void count_0_ChoiceOfAnswer() {
        //given
        exampleUser dummyOfUser = getExampleUser();

        Category category=Category
                .builder()
                .categoryName(CategoryType.HUMANITIES)
                .build();
        categoryRepository.save(category);

        Question question = Question
                .builder()
                .title("test1")
                .content("test")
                .user(dummyOfUser.user1())
                .category(category)
                .build();
        question=questionRepository.save(question);

        exampleAnswer dummyAnswer = getExampleAnswer(question, dummyOfUser);
        answerRepository.saveAll(List.of(dummyAnswer.answer1(), dummyAnswer.answer2()));

        //when
        Integer cnt=answerRepository.countChoiceOfAnswer(question.getQuestionId());

        //then
        assertThat(cnt).isEqualTo(0);
    }

    private static exampleAnswer getExampleAnswer(Question question, exampleUser dummyOfUser) {
        Answer answer1=Answer
                .builder()
                .question(question)
                .content("asdad")
                .user(dummyOfUser.user2())
                .build();
        Answer answer2=Answer
                .builder()
                .question(question)
                .content("asdasd")
                .user(dummyOfUser.user3())
                .build();
        exampleAnswer dummyAnswer = new exampleAnswer(answer1, answer2);
        return dummyAnswer;
    }

    private record exampleAnswer(Answer answer1, Answer answer2) {
    }

    private exampleUser getExampleUser() {
        User user1 = User.builder()
                .email("tkvqweqw@naver.com")
                .major("컴퓨터 공학")
                .name("123")
                .build();
        User user2 = User.builder()
                .email("1swqweqweqeq@naver.com")
                .major("컴퓨터 공학")
                .name("11231")
                .build();
        User user3 = User.builder()
                .email("tk2vqq@naver.com")
                .major("컴퓨터 공학")
                .name("23123")
                .build();
        userRepository.saveAll(List.of(user1,user2,user3));
        exampleUser dummyOfUser = new exampleUser(user1, user2, user3);
        return dummyOfUser;
    }

    private record exampleUser(User user1, User user2, User user3) {
    }
}*/
