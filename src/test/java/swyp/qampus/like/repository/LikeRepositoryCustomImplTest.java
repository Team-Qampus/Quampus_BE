package swyp.qampus.like.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.like.domain.Like;
import swyp.qampus.login.entity.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LikeRepositoryCustomImplTest {
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("좋아요를 누르지 않은 유저는 empty를 반환합니다")
    void notClickLikesReturnToEmpty(){
        //given
        User user=User.builder()
                .userId("test1")
                .email("test@naver.com")
                .name("test11")
                .password("abcd")
                .major("컴공과")
                .universityName("서울대")
                .build();
        userRepository.save(user);

        Answer answer=Answer
                .builder()
                .content("abcd")
                .user(user)
                .build();
        answerRepository.save(answer);

        userRepository.flush();
        answerRepository.flush();
        //when
        Optional<Like> like=likeRepository.findLikesByAnswerAndUser(answer.getAnswerId(),user.getUserId());

        //then
        assertThat(like).isEmpty();
    }

    @Test
    @DisplayName("좋아요를 누른 유저는 Not Empty를 반환합니다")
    void clickLikesReturnToEmpty(){
        //given
        User user1=User.builder()
                .userId("test1")
                .email("test@naver.com")
                .name("test11")
                .password("abcd")
                .major("컴공과")
                .universityName("서울대")
                .build();
        User user2=User.builder()
                .userId("test2")
                .email("test2@naver.com")
                .name("test22")
                .password("abcd2")
                .major("컴공과")
                .universityName("서울대")
                .build();
        userRepository.saveAll(List.of(user1,user2));

        Answer answer=Answer
                .builder()
                .content("abcd")
                .user(user1)
                .build();
        answerRepository.save(answer);

        Like like1=Like.of(user1,answer);
        Like like2=Like.of(user2,answer);
        likeRepository.saveAll(List.of(like1,like2));

        likeRepository.flush();

        //when
        Optional<Like> isLike1=likeRepository.findLikesByAnswerAndUser(answer.getAnswerId(),user1.getUserId());
        Optional<Like> isLike2=likeRepository.findLikesByAnswerAndUser(answer.getAnswerId(),user2.getUserId());

        //then
        assertThat(isLike1).isNotEmpty();
        assertThat(isLike2).isNotEmpty();

    }
    @Test
    @DisplayName("답변의 좋아요를 누르면 개수가 1씩 증가합니다.")
    void clickLike(){
        //given
        User user1=User.builder()
                .userId("test1")
                .email("test1@naver.com")
                .name("test11")
                .password("abcd")
                .major("컴공과")
                .universityName("서울대")
                .build();
        User user2=User.builder()
                .userId("test2")
                .email("test@naver.com")
                .name("test11")
                .password("abcd")
                .major("컴공과")
                .universityName("서울대")
                .build();
        userRepository.saveAll(List.of(user1,user2));

        Answer answer=Answer
                .builder()
                .content("abcd")
                .user(user1)
                .build();
        answerRepository.save(answer);

        //when
        Like like1=Like.of(user1,answer);
        Like like2=Like.of(user2,answer);
        likeRepository.saveAll(List.of(like1,like2));

        //then
        Answer findAnswer=answerRepository.findById(answer.getAnswerId()).orElseThrow();
        assertThat(findAnswer.getLikeCnt()).isEqualTo(2);
        assertThat(findAnswer.getLikeList()).hasSize(2)
                .extracting("user")
                .containsExactlyInAnyOrder(user1,user2);

    }
}