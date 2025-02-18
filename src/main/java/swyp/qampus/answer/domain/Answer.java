package swyp.qampus.answer.domain;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.image.domain.Image;
import swyp.qampus.like.domain.Like;
import swyp.qampus.question.domain.Question;
import swyp.qampus.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = true)
    private Question question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false,name = "created_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(nullable = false,name = "modified_date")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Column(nullable = false)
    private int likeCnt = 0;

    //채택
    @Column(name = "is_chosen")
    private Boolean isChosen;

    @OneToMany(mappedBy = "answer",cascade = CascadeType.REMOVE)
    private List<Like> likeList=new ArrayList<>();

    @OneToMany(mappedBy = "answer",cascade = CascadeType.REMOVE)
    private List<Image> images;

    public void update(String content) {
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }

    public void decreaseLike() {
        if (this.likeCnt > 0) {
            this.likeCnt--;
        }
    }

    public void addLike(Like like){
        this.likeList.add(like);
        this.likeCnt++;
    }

    public void setIsChosen(Boolean chosen){
        this.isChosen=chosen;
    }
    @Builder
    public Answer(String content,int likeCnt,Question question,User user){
        this.likeCnt=likeCnt;
        this.content=content;
        this.question=question;
        this.user=user;
    }

}
