package Quampus.demo.login.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Getter
@Builder
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
    @Column(name = "nickname" , nullable = false)
    private String nickname;

    // 대학교
    @Column(name = "university_name" , nullable = false)
    private String universityName;

    // 학과
    @Column(name = "major")
    private String major;

    // 프로필 이미지
    private String profileImageUrl;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime modifiedDate;

}
