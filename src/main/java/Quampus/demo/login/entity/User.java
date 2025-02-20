package Quampus.demo.login.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class User {

    // 고유 식별자
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 비밀번호
    @Column(name = "password")
    private String password;

    // 이름
    @Column(name = "name")
    private String name;

    // 이메일
    @Column(name = "email")
    private String email;

    // 닉네임
    @Column(name = "nickname")
    private String nickname;

    // 대학교
    @Column(name = "university_name")
    private String university_name;

    // 학과
    @Column(name = "major")
    private String major;

    //
    private String loginId;

    // 소셜 로그인
    private String provider;

    // 프로필 이미지
    private String profileImageUrl;

    private LocalDateTime created_date;

    private LocalDateTime modified_date;


    @Builder
    public User(String name) {
        this.name = name;
    }

    @Builder
    public User(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
