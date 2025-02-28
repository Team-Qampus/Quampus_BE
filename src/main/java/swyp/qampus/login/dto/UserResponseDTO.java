package swyp.qampus.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.login.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회원 정보 응답 DTO 클래스
 * - 클라이언트에게 응답할 데이터를 담는 DTO
 */
public class UserResponseDTO {


    /**
     * 회원가입 결과 응답 DTO
     * - 회원가입 완료 후 클라이언트에 반환할 데이터
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JoinResultDTO {
        private Long userId;  // 가입한 사용자 ID
        private String name; // 이름
        private String email; // 이메일
        private String nickname;
        private String universityName; // 학교
        private String major; // 학과
        private String profileImageUrl; // 카카오 이미지
        private LocalDateTime createAt; // 계정 생성 시간
    }



    /**
     * 단일 사용자 정보 응답 DTO (미리보기 용)
     * - 특정 사용자의 미리보기 정보를 반환할 때 사용
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserPreviewDTO {
        private Long userId;
        private String name;
        private LocalDateTime updateAt;
        private LocalDateTime createAt;
    }

    /**
     * 사용자 목록 응답 DTO
     * - 여러 사용자의 미리보기 정보를 반환할 때 사용
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserPreviewListDTO {
        List<UserPreviewDTO> userPreviewDTOList;
    }
}
