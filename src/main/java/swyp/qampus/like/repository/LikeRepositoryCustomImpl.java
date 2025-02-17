package swyp.qampus.like.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import swyp.qampus.answer.domain.QAnswer;
import swyp.qampus.like.domain.Like;
import swyp.qampus.like.domain.QLike;
import swyp.qampus.user.domain.QUser;

import java.util.Optional;

import static swyp.qampus.answer.domain.QAnswer.*;
import static swyp.qampus.like.domain.QLike.*;
import static swyp.qampus.user.domain.QUser.*;

@Repository
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public LikeRepositoryCustomImpl(EntityManager em){this.queryFactory=new JPAQueryFactory(em);}


    @Override
    public Optional<Like> findLikesByAnswerAndUser(Long answer_id, String user_id) {
        return Optional.ofNullable(
                queryFactory.select(like)
                        .from(like)
                        .join(like.user,user)
                        .join(like.answer, answer)
                        .where(userAndAnswerIdEq(user_id,answer_id))
                        .fetchOne()
        );
    }

    private BooleanExpression userIdEq(String user_id){
        return user_id==null ? null : user.user_id.eq(user_id);
    }
    private BooleanExpression answerIdEq(Long answer_id){
        return answer_id == null ? null : answer.answer_id.eq(answer_id);
    }
    private BooleanExpression userAndAnswerIdEq(String user_id,Long answer_id){
        if(userIdEq(user_id)==null){
            return answerIdEq(answer_id);
        }
        if(answerIdEq(answer_id)==null){
            return userIdEq(user_id);
        }
        return answerIdEq(answer_id).and(userIdEq(user_id));
    }
}
