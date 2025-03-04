package swyp.qampus.login.converter;

import org.springframework.security.crypto.password.PasswordEncoder;
import swyp.qampus.login.entity.User;
import swyp.qampus.university.domain.University;

// OAuth 로그인 시, 카카오 프로필 정보를 User 엔티티로 변환하는 컨버터 클래스
public class OAuthConverter {

    /**
     * 카카오 로그인 시 받은 사용자 정보를 기반으로 User 엔티티를 생성하는 메서드
     * @param email 사용자의 이메일 (카카오 계정에서 가져옴)
     * @param name 사용자의 닉네임 (카카오 프로필에서 가져옴)
     * @param password 기본 비밀번호 (소셜 로그인은 비밀번호를 직접 사용하지 않지만, 비워둘 수 없으므로 기본값 설정)
     * @param passwordEncoder 비밀번호를 해싱하는 PasswordEncoder
     * @return User 엔티티 객체
     */
    public static User toUser(String email, String name, String nickname, String password, PasswordEncoder passwordEncoder, String profileImageUrl) {
        return User.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .build();
    }

    public static User toUser(Long userId,String email, String name, String nickname, String password, PasswordEncoder passwordEncoder, String profileImageUrl) {
        return User.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .build();
    }
    public static User toUser(Long userId, String email, String name, String nickname, String password,
                              PasswordEncoder passwordEncoder, String profileImageUrl,
                              University university, String major) {
        return User.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .major(major)
                .build();
    }

}
