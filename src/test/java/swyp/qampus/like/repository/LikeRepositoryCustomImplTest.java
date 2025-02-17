package swyp.qampus.like.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.like.domain.Like;
import swyp.qampus.user.domain.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class LikeRepositoryCustomImplTest {
    @Autowired
    LikeRepositoryCustom likeRepository;
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
        Answer answer=Answer
                .builder()
                .content("abcd")
                .user(user)
                .build();

        //when
        Optional<Like> like=likeRepository.findLikesByAnswerAndUser(answer.getAnswerId(),user.getUserId());

        //then
        assertThat(like).isEmpty();
    }
}