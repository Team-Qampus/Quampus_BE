package swyp.qampus.like.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.login.entity.User;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Likes")
public class Like extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @Builder
    private Like(User user,Answer answer){
        this.answer=answer;
        this.user=user;
        user.addLike(this);
        answer.addLike(this);
    }
    public static Like of(User user,Answer answer){
        Like like=Like.builder()
                .answer(answer)
                .user(user)
                .build();
        return like;
    }
}
