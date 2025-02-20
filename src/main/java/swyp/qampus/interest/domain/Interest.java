package swyp.qampus.interest.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.question.domain.Question;
import swyp.qampus.user.domain.User;

@Entity
@Getter
@NoArgsConstructor
public class Interest extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,name = "interest_id")
    private Long interestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Builder
    private Interest(User user,Question question){
        this.user=user;
        this.question=question;

    }




}
