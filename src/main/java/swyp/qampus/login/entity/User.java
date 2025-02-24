package swyp.qampus.login.entity;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.curious.domain.Curious;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "user_id")
    private Long userId;

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

    // 닉네임
    @Column(name = "nickname" , nullable = true)
    private String nickname;

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

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Curious> curiousList=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Builder(toBuilder = true)
    public User(Long userId, String name, String email, String password, String universityName, String major,String nickname){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.universityName = universityName;
        this.major = major;
        this.nickname=nickname;
    }
    public void addQuestion(Question question){
        this.questions.add(question);
    }
    public void addLike(Like like){
        this.likeList.add(like);
    }
    public void decreaseLike(Like like){
        likeList.remove(like);
    }
    //나도 궁금해요 추가
    public void addCurious(Curious curious){
        this.curiousList.add(curious);
    }
    //나도 궁금해요 삭제
    public void deleteCurious(Curious curious){
        curiousList.remove(curious);
    }

    public void addAnswer(Answer answer){
        this.answers.add(answer);
    }
}
