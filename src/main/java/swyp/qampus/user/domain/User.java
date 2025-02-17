package swyp.qampus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.like.domain.Like;
import swyp.qampus.question.domain.Question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor

@Table(name = "Users")
public class User  {
    @Id
    @Column(nullable = false, name = "user_id")
    private String userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 100)
    private String universityName;

    @Column(length = 255)
    private String major;

    @Column(nullable = false)
    private LocalDateTime created_date = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime modified_date = LocalDateTime.now();

    @OneToMany
    private List<Question> questions;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Like> likeList=new ArrayList<>();

    @Builder
    public User(String userId, String name, String email, String password, String universityName, String major){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.universityName = universityName;
        this.major = major;
    }
}
