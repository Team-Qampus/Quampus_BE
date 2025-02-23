package swyp.qampus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.curious.domain.Curious;
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

    @Builder
    public User(String userId, String name, String email, String password, String universityName, String major){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.universityName = universityName;
        this.major = major;
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
