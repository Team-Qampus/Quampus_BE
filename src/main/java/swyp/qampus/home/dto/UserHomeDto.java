package swyp.qampus.home.dto;

import lombok.Builder;
import lombok.Getter;
import swyp.qampus.login.entity.User;

@Getter
@Builder
public class UserHomeDto {
    private final String name;
    private final String universityName;
    private final String major;

    public static UserHomeDto of(User user) {
        return UserHomeDto.builder()
                .name(user.getName())
                .universityName(user.getUniversity().getUniversityName())
                .major(user.getMajor())
                .build();
    }
}
