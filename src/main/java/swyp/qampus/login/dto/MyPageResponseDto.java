package swyp.qampus.login.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import swyp.qampus.login.entity.User;
import swyp.qampus.question.domain.MyQuestionResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private Long userId;
    private String name;
    private String nickname;
    private String major;
    private String universityName;
    private Page<MyQuestionResponseDto> questions;

    public static MyPageResponseDto of(User user, Page<MyQuestionResponseDto> questions) {
        return new MyPageResponseDto(
                user.getUserId(),
                user.getName(),
                user.getNickname(),
                user.getMajor(),
                user.getUniversity().getUniversityName(),
                questions
        );
    }
}
