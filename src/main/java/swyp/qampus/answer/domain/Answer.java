package swyp.qampus.answer.domain;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.image.domain.Image;
import swyp.qampus.like.domain.Like;
import swyp.qampus.question.domain.Question;
import swyp.qampus.login.entity.User;

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
    @Column(name = "is_chosen",nullable = false)
    private Boolean isChosen=false;

    @OneToMany(mappedBy = "answer",cascade = CascadeType.REMOVE)
    private List<Like> likeList=new ArrayList<>();

    @OneToMany(mappedBy = "answer",cascade = CascadeType.REMOVE)
    private List<Image> images;

    //이미지 리스트
    @OneToMany(mappedBy = "answer",cascade = CascadeType.REMOVE)
    private List<Image> imageList=new ArrayList<>();

    public void update(String content) {
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }

    public void decreaseLike(Like like) {
        if (this.likeCnt > 0) {
            this.likeList.remove(like);
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
    //이미지 추가
    public void addImage(Image image){
        this.imageList.add(image);
    }
}
