package swyp.qampus.curious.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.question.domain.Question;
import swyp.qampus.user.domain.User;

@Entity
@Getter
@NoArgsConstructor
public class Curious extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,name = "curious_id")
    private Long curiousId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Builder
    private Curious(User user,Question question){
        this.user=user;
        this.question=question;
        user.addCurious(this);
        question.addCurious(this);
    }
    public static Curious of(User user,Question question){
        return Curious.builder()
                .question(question)
                .user(user)
                .build();
    }




}
