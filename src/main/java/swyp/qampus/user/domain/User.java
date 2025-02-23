package swyp.qampus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.like.domain.Like;
import swyp.qampus.question.domain.Question;
import swyp.qampus.university.domain.University;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 255)
    private String major;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "user")
    private List<Question> questions=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Like> likeList=new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Answer> answers=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Builder
    public User(Long userId, String name, String email, String major,University university){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.major = major;
        this.university=university;
    }
    public void addQuestion(Question question){
        this.questions.add(question);
    }
    public void addLike(Like like){
        this.likeList.add(like);
    }

    public void addAnswer(Answer answer){
        this.answers.add(answer);
    }
}
