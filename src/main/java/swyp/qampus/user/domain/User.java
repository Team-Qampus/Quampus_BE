package swyp.qampus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.like.domain.Like;
import swyp.qampus.question.domain.Question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "User")
public class User {
    @Id
    @Column(nullable = false, length = 255)
    private String user_id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 100)
    private String university_name;

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

}
