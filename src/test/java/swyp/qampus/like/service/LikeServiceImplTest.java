package swyp.qampus.like.service;

import net.bytebuddy.asm.MemberRemoval;
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

import java.util.Optional;

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
}