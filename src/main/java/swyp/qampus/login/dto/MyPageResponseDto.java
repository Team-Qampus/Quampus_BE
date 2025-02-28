package swyp.qampus.login.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private List<MyQuestionResponseDto> questions;

    public static MyPageResponseDto of(User user, List<MyQuestionResponseDto> questions) {
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
