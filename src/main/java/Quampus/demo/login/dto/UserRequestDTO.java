package Quampus.demo.login.dto;

import lombok.Getter;

// 새로운 사용자가 회원가입할 때 요청하는
public class UserRequestDTO {


    @Getter
    public static class JoinDTO {
        private String name;
    }

    @Getter
    public static class getUserInfoDTO {
        private Long id;
    }

    public static class UserUniversityAndMajorDTO {
        private Long userId; // 사용자 ID
        private String universityName; // 학교 이름
        private String major; // 학과 이름
    }
}
