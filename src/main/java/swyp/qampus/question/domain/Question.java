package swyp.qampus.question.domain;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.category.domain.Category;
import swyp.qampus.image.domain.Image;
import swyp.qampus.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private int viewCnt = 0;

    @Column(nullable = false)
    private int curiousCount = 0;

    @Column(nullable = false)
    private LocalDateTime createDate=LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime modifiedDate=LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime lastViewedDate = LocalDateTime.now(); //질문 작성한 유저가 마지막으로 질문을 조회한 시간

    @Column(nullable = false)
    private int unreadAnswerCnt = 0;

    @Column(nullable = false)
    private int answerCount = 0;

    @Builder
    public Question(User user,String title,String content,int viewCnt,int curious_count,Category category){
        this.user=user;
        this.title=title;
        this.content=content;
        this.viewCnt=viewCnt;
        this.curiousCount=curious_count;
        this.category=category;
    }

    // 조회수 증가 메서드
    public void increseViewCount() {
        this.viewCnt++;
    }

    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.modifiedDate = LocalDateTime.now();
    }

    public void delete() {
        this.isDeleted = true;
    }

    //질문을 조회할 때 lastViewedDate를 업데이트 후 미확인 답변 초기화
    public void updateLastViewedDate() {
        this.lastViewedDate = LocalDateTime.now();
        this.unreadAnswerCnt = 0;
    }

    public void incrementUnreadAnswerCount() {
        this.unreadAnswerCnt++;
    }

    public void incrementAnswerCount() {
        this.answerCount++;
    }

    public void decrementAnswerCount() {
        if (this.answerCount > 0) {
            this.answerCount--;
        }
    }

}
