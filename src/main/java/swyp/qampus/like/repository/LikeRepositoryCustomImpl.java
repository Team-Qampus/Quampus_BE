package swyp.qampus.like.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import swyp.qampus.answer.domain.QAnswer;
import swyp.qampus.like.domain.Like;
import swyp.qampus.like.domain.QLike;


import java.util.Optional;

import static swyp.qampus.answer.domain.QAnswer.*;
import static swyp.qampus.like.domain.QLike.*;
import static swyp.qampus.login.entity.QUser.user;


@Repository
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public LikeRepositoryCustomImpl(EntityManager em){this.queryFactory=new JPAQueryFactory(em);}


    @Override
    public Optional<Like> findLikesByAnswerAndUser(Long answerId, Long userId) {
        return  Optional.ofNullable(
                queryFactory.select(like)
                        .from(like)
                        .join(like.user,user)
                        .join(like.answer, answer)
                        .where(userAndAnswerIdEq(userId,answerId))
                        .fetchOne()
        );
    }

    private BooleanExpression userIdEq(Long userId){
        return userId==null ? null : user.userId.eq(userId);
    }
    private BooleanExpression answerIdEq(Long answerId){
        return answerId == null ? null : answer.answerId.eq(answerId);
    }
    private BooleanExpression userAndAnswerIdEq(Long userId,Long answerId){
        if(userIdEq(userId)==null){
            return answerIdEq(answerId);
        }
        if(answerIdEq(answerId)==null){
            return userIdEq(userId);
        }
        return answerIdEq(answerId).and(userIdEq(userId));
    }
}
