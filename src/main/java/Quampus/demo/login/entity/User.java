package Quampus.demo.login.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Getter
@Setter
@Builder(toBuilder = true)
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
    @Column(name = "nickname" , nullable = true)
    private String nickname;

    // 대학교
    @Column(name = "university_name" , nullable = true)
    private String universityName;

    // 학과
    @Column(name = "major", nullable = true)
    private String major;

    // 프로필 이미지
    private String profileImageUrl;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
