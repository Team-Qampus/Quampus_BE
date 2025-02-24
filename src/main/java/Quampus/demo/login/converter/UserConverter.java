package Quampus.demo.login.converter;

import Quampus.demo.login.dto.UserRequestDTO;
import Quampus.demo.login.dto.UserResponseDTO;
import Quampus.demo.login.entity.User;

/**
 * User 엔티티와 DTO 간의 변환을 담당하는 변환기 클래스
 * - DTO를 엔티티로 변환하여 데이터베이스에 저장할 수 있도록 함
 * - 엔티티를 DTO로 변환하여 클라이언트에 응답을 제공할 수 있도록 함
 */
public class UserConverter {

    /**
     * 회원가입 요청 DTO를 User 엔티티로 변환하는 메서드
     * @param joinDTO 클라이언트에서 받은 회원가입 요청 DTO
     * @return User 엔티티 객체
     */
    public static User toUser(UserRequestDTO.JoinDTO joinDTO) {
        return User.builder()
                .name(joinDTO.getName()) // 회원의 이름 설정
                .build();
    }

    /**
     * User 엔티티를 회원가입 결과 응답 DTO로 변환하는 메서드
     *
     * @param user 데이터베이스에서 저장된 User 엔티티
     * @return 회원가입 결과 DTO (userId, 생성일 포함)
     */
    public static UserResponseDTO.JoinResultDTO toJoinResultDTO(User user) {
        return UserResponseDTO.JoinResultDTO.builder()
                .userId(user.getId()) // 사용자 ID 설정
                .createAt(user.getCreatedDate()) // 계정 생성 날짜 설정
                .build();
    }

    public static User updateAdditionalInfo(User user, UserRequestDTO.UserUniversityAndMajorDTO dto) {
        return user.toBuilder()
                .universityName(dto.getUniversityName())
                .major(dto.getMajor())
                .build();
    }
}
