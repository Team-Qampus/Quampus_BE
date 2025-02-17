package swyp.qampus.like.service;

import net.bytebuddy.asm.MemberRemoval;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.like.domain.Like;
import swyp.qampus.like.repository.LikeRepository;
import swyp.qampus.user.domain.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@ActiveProfiles("test")
class LikeServiceImplTest {
    @Autowired
    private LikeService likeService;

    @MockitoBean
    private LikeRepository likeRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AnswerRepository answerRepository;

    private User initUser(String userId){
        User user=User.builder()
                .userId(userId)
                .email("tt")
                .major("tt")
                .name("tt")
                .universityName("tt")
                .build();
        return user;
    }
    private Answer initAnswer(Long answerId,User user){
        return Answer.builder()
                .user(user)
                .content("as")
                .build();
    }

    @Test
    @DisplayName("[실패케이스]좋아요 클릭-유저가 존재하지 않는 경우 예외가 발생합니다. ")
    void notExistUser_insert() {
        //given
        String userId="invalid";
        Long answerId=1L;

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            likeService.insert(userId, answerId);
        });

        // then
        assertEquals("사용자를 찾을 수 없습니다.",exception.getMessage());
    }
    @Test
    @DisplayName("[실패케이스]좋아요 삭제-유저가 존재하지 않는 경우 예외가 발생합니다. ")
    void notExistUser_delete() {
        //given
        String userId="invalid";
        Long answerId=1L;

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            likeService.delete(userId, answerId);
        });

        // then
        assertEquals("사용자를 찾을 수 없습니다.",exception.getMessage());
    }

    @Test
    @DisplayName("[실패케이스]좋아요 클릭-답변이 존재하지 않는 경우 예외가 발생합니다. ")
    void notExistAnswer_insert() {
        //given
        String userId="invalid";
        User user=initUser(userId);

        Long answerId=1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(answerRepository.findById(any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            likeService.insert(userId, answerId);
        });

        //then
        assertEquals("답변이 존재하지 않습니다.",exception.getMessage());
    }

    @Test
    @DisplayName("[실패케이스]좋아요 삭제-답변이 존재하지 않는 경우 예외가 발생합니다. ")
    void notExistAnswer_delete() {
        //given
        String userId="invalid";
        User user=User.builder()
                .userId(userId)
                .email("tt")
                .major("tt")
                .name("tt")
                .universityName("tt")
                .build();

        Long answerId=1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(answerRepository.findById(any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            likeService.delete(userId, answerId);
        });

        //then
        assertEquals("답변이 존재하지 않습니다.",exception.getMessage());
    }

    @Test
    @DisplayName("[실패케이스]-이미 좋아요가 눌러져 있으면 예외가 발생합니다.")
    void existedLike_insert(){
        //given
        Long answerId=1L;
        String userId="fake";
        User user=initUser(userId);
        Answer answer=initAnswer(answerId,user);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));
        Like like=Like.of(user,answer);
        when(likeRepository.findLikesByAnswerAndUser(any(),any())).thenReturn(Optional.of(like));

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            likeService.insert(userId, answerId);
        });

        //then
        assertEquals("중복된 좋아요 리소스 요청입니다.",exception.getMessage());
    }

    @Test
    @DisplayName("[실패케이스]-좋아요가 존재하지 않지만 DELETE 요청 시 예외가 발생합니다.")
    void existedLike_delete(){
        //given
        Long answerId=1L;
        String userId="fake";
        User user=initUser(userId);
        Answer answer=initAnswer(answerId,user);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));
        when(likeRepository.findLikesByAnswerAndUser(any(),any())).thenReturn(Optional.empty());

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            likeService.delete(userId, answerId);
        });

        //then
        assertEquals("중복된 좋아요 리소스 요청입니다.",exception.getMessage());
    }

    @Test
    @DisplayName("[성공케이스]-좋아요를 누르면 좋아요가 1씩 증가합니다.")
    void clickLike_insert(){
        //given
        Long answerId1=1L;
        Long answerId2=2L;
        String userId1="fake1";
        String userId2="fake2";
        User user1=initUser(userId1);
        User user2=initUser(userId2);
        //유저1이 쓴 답변
        Answer answer1=initAnswer(answerId1,user1);
        //유저2가 쓴 답변->좋아요 0개
        Answer answer2=initAnswer(answerId2,user2);

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));
        when(userRepository.saveAll(List.of(user1,user2))).thenReturn(List.of(user1,user2));

        when(answerRepository.findById(answerId1)).thenReturn(Optional.of(answer1));
        when(answerRepository.findById(answerId2)).thenReturn(Optional.of(answer2));

        //when
        likeService.insert(userId1,answerId1);
        likeService.insert(userId2,answerId1);

        //then
        Answer findAnswer1=answerRepository.findById(answerId1)
                        .orElseThrow();
        Answer findAnswer2=answerRepository.findById(answerId2)
                        .orElseThrow();

        assertThat(findAnswer1.getLikeList()).isNotEmpty();
        assertThat(findAnswer1.getLikeCnt()).isEqualTo(2);

        assertThat(findAnswer2.getLikeList()).isEmpty();
        assertThat(findAnswer2.getLikeCnt()).isEqualTo(0);
    }
}