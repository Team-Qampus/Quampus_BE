package swyp.qampus.answer.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.category.domain.Category;
import swyp.qampus.category.repository.CategoryRepository;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.user.domain.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
                .categoryName("111")
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
                .categoryName("111")
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
                .userId("test123")
                .email("tkv@naver.com")
                .major("컴퓨터 공학")
                .universityName("서울대")
                .password("Pwed")
                .name("123")
                .build();
        User user2 = User.builder()
                .userId("test1234")
                .email("1sw@naver.com")
                .major("컴퓨터 공학")
                .universityName("서울대")
                .password("Pwed")
                .name("11231")
                .build();
        User user3 = User.builder()
                .userId("test1235")
                .email("tk2v@naver.com")
                .major("컴퓨터 공학")
                .universityName("서울대")
                .password("Pwed")
                .name("23123")
                .build();
        userRepository.saveAll(List.of(user1,user2,user3));
        exampleUser dummyOfUser = new exampleUser(user1, user2, user3);
        return dummyOfUser;
    }

    private record exampleUser(User user1, User user2, User user3) {
    }
}